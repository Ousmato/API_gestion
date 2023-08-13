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

        Periode periode = periodeRepository.findById(depenses.getPeriode().getId());
        User user = userRepository.findById(depenses.getUser().getId());
        Budget budget = budgetRepository.findById(depenses.getBudget().getId());
        Notifications notifications = new Notifications();
        notifications.setBudget(budget);
        notifications.setUser(user);
        boolean alerte = notificationsService.alerte(notifications,depenses);

        if(user.getEmail() == null){
            throw new RuntimeException("L'utilisateur n'est pas valide, veuillez en choisir une autre");
        }

        if(periode ==null){
            throw new RuntimeException("La periode n'est pas valide, veuillez en choisir une autre");
        }

       /* Budget LastBudget = budgetRepository.findFirstByUserAndCategoryIdOrderByEndDateDesc(budget.getUser(), budget.getCategory().getId());
        if (LastBudget != null) {
            LocalDate lastBudgetStarDate = LastBudget.getStarDate().plusMonths(1);
            LocalDate budgetDate = budget.getStarDate();

            if (budgetDate.equals(lastBudgetStarDate) || budgetDate.isAfter(lastBudgetStarDate.plusDays(1))) {
                int LastReliquat = LastBudget.getReliquat();


                //recherhe le budget du mois suivant
                Budget budgetOfMonth = budgetRepository.findByUserAndCategoryAndStarDate(budget.getUser(),budget.getCategory(),budgetDate.plusMonths(1));
                if(budgetOfMonth!= null){
                    int montMoisSuivant = budgetOfMonth.getAmount();
                    int newBudget =  LastReliquat+montMoisSuivant;

                    budget.setAmount(newBudget);
                    budgetRepository.save(budget);
                }
            }
        }*/

                LocalDate currentDate = LocalDate.now();
        if (budget.getEndDate().isBefore(currentDate) || budget.getStarDate().isAfter(currentDate)) {
            throw new RuntimeException("veillez choisir un budget en cours");
        }
        Depenses firstDepense = depenseRepository.findFirstByBudget(budget);
        if (firstDepense==null){
            Budget precedentBuget = budgetRepository.findFirstByUserAndCategoryAndEndDateIsBeforeOrderByEndDateDesc
                    (budget.getUser(),budget.getCategory(),budget.getStarDate());
            if (precedentBuget!=null){
                int lasReliquat = precedentBuget.getReliquat();
                if (lasReliquat >= 0){
                    int newbudget = lasReliquat+budget.getAmount();
                    budget.setAmount(newbudget);
                    budget.setReliquat(newbudget);
                }
                Notifications notificationsTransfert = new Notifications();
                notificationsTransfert.setHeure(LocalTime.now());
                notificationsTransfert.setBudget(precedentBuget);
                notificationsTransfert.setUser(budget.getUser());
                notificationsTransfert.setDate(LocalDate.now());
                notificationsTransfert.setText("votre reliquat du budget precedent qui est : "+precedentBuget.getReliquat()+" a etait ajouter au montant du budget suivant : "+budget.getAmount());
                notificationRepository.save(notificationsTransfert);
            }

        }
        budgetRepository.save(budget);

        /*Depenses depenses1 = depenseRepository.findByBudget(depenses.getBudget());
        if (depenses1.getDate().isBefore(LocalDate.now()) || depenses1.getDate().isAfter(LocalDate.now())){
            throw new RuntimeException("date not valid");
        }*/
        if (depenses.getDate().isBefore(LocalDate.now()) || depenses.getDate().isAfter(LocalDate.now())){
            throw new RuntimeException(" date not valid, please chose a valid date");
        }

        int montant = 0;
        switch (periode.getDescription()){
            case "Journaliere" :
               /* Depenses depenses1 = depenseRepository.findByUserAndBudgetAndPeriodeAndDate(depenses.getUser(),depenses.getBudget(),depenses.getPeriode(),depenses.getDate());
                if( depenses1 !=null) {
                    throw new RuntimeException("depenses deja effectue");
                }*/


                if(budget.getReliquat()<depenses.getAmount()) {
                    throw new RuntimeException("Budget insuffisant "+budget.getReliquat());
                }
                montant = budget.getReliquat()-depenses.getAmount();
                budget.setReliquat(montant);

                if (montant <= budget.getSeuil()){
                    alerte = true;
                }

                break;
            case "Mensuel" :
               /*Depenses depenses2 = depenseRepository.findFirstByUserIdAndBudgetIdAndPeriodeOrderByDateDesc(depenses.getUser().getId(),
                       budget.getId(),depenses.getPeriode());
                if(depenses2 != null && depenses2.getDate().plusDays(30).isAfter(LocalDate.now())) {
                    throw new RuntimeException("vous avez deja depenser pour ce moi dans  la category " + budget.getCategory().getType());
                }*/
                if (budget.getReliquat() < depenses.getAmount()) {
                    throw new RuntimeException("Budget insuffisant ");
                }

                montant = budget.getReliquat()-depenses.getAmount();
                budget.setReliquat(montant);

                if (montant <= budget.getSeuil()){
                    alerte = true;
                }

                break;
            case "Hebdomadaire" :
               /* Depenses depenses3 = depenseRepository.findFirstByUserIdAndBudgetIdAndPeriodeOrderByDateDesc(depenses.getUser().getId(),
                        budget.getId(),depenses.getPeriode());
                if (depenses3 != null && depenses3.getDate().plusDays(7).isAfter(LocalDate.now())) {
                    throw new RuntimeException("vous avez deja un budget en cour pour cette semmaine");
                }*/
                if(budget.getReliquat()<depenses.getAmount()) {
                    throw new RuntimeException("Votre budget du moi est insuffisant "+ budget.getUser().getLastName()+ " "+ budget.getUser().getFirstName());
                }

                montant = budget.getReliquat()-depenses.getAmount();
                budget.setReliquat(montant);

                if (montant <= budget.getSeuil()){
                    alerte = true;
                }

                break;
        }
        depenseRepository.save(depenses);
        if (!alerte)
            return "add successful";
        else
            return "depenses enregistre avec succes, votre budget "+ budget.getCategory().getType()+"" +
                    "est en bas du seuil";
    }

}
