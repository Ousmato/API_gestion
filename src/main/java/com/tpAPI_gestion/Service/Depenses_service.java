package com.tpAPI_gestion.Service;

import com.tpAPI_gestion.Entity.*;
import com.tpAPI_gestion.Repository.*;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class Depenses_service {

    @Autowired
    private Depense_repository depenseRepository;

    @Autowired
    private Budget_repository budgetRepository;

    @Autowired
    private Notifications_service notificationsService;

    @Autowired
    private Periode_repository periodeRepository;

    @Autowired
    private User_repository userRepository;

    @Autowired
    private Archive_repository archiveRepository;

    @Autowired
    private  Notification_repository notificationRepository;


    //methode Get
    public List<Depenses> read(){
        return depenseRepository.findAll();
    }


    //methode Delete
    public String delete(int id){
        Depenses depenses = depenseRepository.findById(id);
        if (depenses==null){
            throw new RuntimeException("depense does not "+id+"  existe");
        }
        Budget budget = depenses.getBudget();
       int newReliquat = depenses.getAmount()+budget.getReliquat();

       budget.setReliquat(newReliquat);
           budgetRepository.save(budget);

           ArchivesDepense archivesDepense = new ArchivesDepense();
           archivesDepense.setAmount(depenses.getAmount());
           archivesDepense.setLibelle(depenses.getLibelle());
           archivesDepense.setDate(LocalDate.now());
            depenses.setSupprimer(true);
            depenseRepository.saveAndFlush(depenses);
           archivesDepense.setDepenses(depenses);

           archiveRepository.save(archivesDepense);// Enregistrer l'archive dans la table archive

       // depenseRepository.deleteById(id);// Supprimez la dépense de la table principale
        return "delete successful votre budget actuel est "+newReliquat;

    }

    //methode Put
    public Depenses update(Depenses depenses){
        Depenses depenses1 = depenseRepository.findById(depenses.getId());
        if (depenses1!= null){
             return depenseRepository.save(depenses);
        }else throw new EntityExistsException("this expense already exists");

    }

    //Methode add depenses
    public  String add(Depenses  depenses){

        //Reuperer les entités depuis la base de données avec leurs id
        Periode periode = periodeRepository.findById(depenses.getPeriode().getId());
        User user = userRepository.findById(depenses.getUser().getId());
        Budget budget = budgetRepository.findById(depenses.getBudget().getId());

        //creation et initiation de l'object notification
        Notifications notifications = new Notifications();
        notifications.setBudget(budget);
        notifications.setUser(user);

        //verifier les alerte sur une depenses pour les enregistrer dans la class notification
        //methode est appeller dans la class notification
        boolean alerte = notificationsService.alerte(notifications,depenses);

        //verifier l'existence d'un user a travers son address mail
        if(user.getEmail() == null){
            throw new RuntimeException("L'utilisateur n'est pas valide, veuillez en choisir une autre");
        }

        //verifier l'existence  de la periode avant l'enregistrement de la depense
        if(periode ==null){
            throw new RuntimeException("La periode n'est pas valide, veuillez en choisir une autre");
        }

        //obtenir la date actuelle avant pour comparaison
        LocalDate currentDate = LocalDate.now();
        //verifier les dates du budget pour trouver un budget en cours
        if (budget.getEndDate().isBefore(currentDate) || budget.getStarDate().isAfter(currentDate)) {
            throw new RuntimeException("veillez choisir un budget en cours");
        }

        //trouver la premiere depenses  associer a ce budget s'il existe
        Depenses firstDepense = depenseRepository.findFirstByBudget(budget);//
        if (firstDepense==null){
            //recherche du budget precedent
            // trouve le premier en triant la liste des budget par ordre decroissant
            Budget precedentBuget = budgetRepository.findFirstByUserAndCategoryAndEndDateIsBeforeOrderByEndDateDesc
                    (budget.getUser(),budget.getCategory(),budget.getStarDate());
            if (precedentBuget!=null){
                //ajout de l'encien reliquat au montant du nouveau budget
                int lasReliquat = precedentBuget.getReliquat();
                if (lasReliquat >= 0){
                    int newbudget = lasReliquat+budget.getAmount();
                    budget.setAmount(newbudget);
                    budget.setReliquat(newbudget);
                }

                //creer une notification pour alerter l'utilisateur du transfert de son du reliquat du mois precedent
                // pour ajouter sur le montant du budget en cours
                Notifications notificationsTransfert = new Notifications();
                notificationsTransfert.setHeure(LocalTime.now());
                notificationsTransfert.setBudget(precedentBuget);
                notificationsTransfert.setUser(budget.getUser());
                notificationsTransfert.setDate(LocalDate.now());
                notificationsTransfert.setText("votre reliquat du budget precedent qui est : "+precedentBuget.getReliquat()+" a etait ajouter au montant du budget suivant : "+budget.getAmount());
                notificationRepository.save(notificationsTransfert);
            }
        }
        //enregistrement du buget apres verification
        budgetRepository.save(budget);

        //verifier la date de la depense si c'est ne pas la date actuelle
        if (depenses.getDate().isBefore(LocalDate.now()) || depenses.getDate().isAfter(LocalDate.now())){
            throw new RuntimeException(" date not valid, please chose a valid date");
        }

        // initialisation du montant et traitement des depenses en fonction des periodes
        int montant = 0;
        switch (periode.getDescription()){
            case "Journaliere" :

                //vrifier la disponibliter du budget
                if(budget.getReliquat()<depenses.getAmount()) {
                    throw new RuntimeException("Budget insuffisant "+budget.getReliquat());
                }
                montant = budget.getReliquat()-depenses.getAmount();
                budget.setReliquat(montant);

                //verifier le seuil du budget pour alerter l'utilisateur
                if (montant <= budget.getSeuil()){
                    alerte = true;
                }
                break;
            case "Mensuel" :
                //vrifier la disponibliter du budget
                if (budget.getReliquat() < depenses.getAmount()) {
                    throw new RuntimeException("Budget insuffisant ");
                }
                montant = budget.getReliquat()-depenses.getAmount();
                budget.setReliquat(montant);

                //verifier le seuil du budget pour alerter l'utilisateur
                if (montant <= budget.getSeuil()){
                    alerte = true;
                }
                break;
            case "Hebdomadaire" :
                //vrifier la disponibliter du budget
                if(budget.getReliquat()<depenses.getAmount()) {
                    throw new RuntimeException("Votre budget du moi est insuffisant "+ budget.getUser().getLastName()+ " "+ budget.getUser().getFirstName());
                }
                montant = budget.getReliquat()-depenses.getAmount();
                budget.setReliquat(montant);

                //verifier le seuil du budget pour alerter l'utilisateur
                if (montant <= budget.getSeuil()){
                    alerte = true;
                }
                break;
        }
        //enregistrement de la depense
        depenseRepository.save(depenses);
        //retou
        if (!alerte)
            return "add successful";
        else
            return "depenses enregistre avec succes, votre budget "+ budget.getCategory().getType()+"" +
                    "est en bas du seuil";
    }

}
