package com.tpAPI_gestion.Service;

import com.tpAPI_gestion.Entity.*;
import com.tpAPI_gestion.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class budget_service {
    @Autowired
    private Budget_repository budgetRepository;

    @Autowired
    private Category_repository categoryRepository;

    @Autowired
    private User_repository userRepository;

    @Autowired
    private Depense_repository depenseRepository;


    //Methode pour ajouter un budget
    public  String add(Budget budget) {
        if (budget.getStarDate().isBefore(LocalDate.now())){
            throw new RuntimeException("Date not valid please chose a later date");
        }
        User userExist = userRepository.findById(budget.getUser().getId());
        if (userExist == null) {
            throw new RuntimeException("user does not exist");
            }
        Category categoryExist = categoryRepository.findById(budget.getCategory().getId());
        if (categoryExist == null) {
            throw new RuntimeException("category does not exist ensure you enter the section to create another one");
            }

        LocalDate dateDebut = budget.getStarDate();
        LocalDate dateFin = dateDebut.with(TemporalAdjusters.lastDayOfMonth());
        budget.setEndDate(dateFin);

        Budget existingBudget = budgetRepository.findByUserAndCategoryAndEndDate(budget.getUser(),budget.getCategory(),budget.getEndDate());
        if(existingBudget!=null){
            throw new RuntimeException("A budget for the same user, category, start date, and end date already exists");
        }

        LocalDate precedentBudgetDateFin = dateFin.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        Budget precedentBudget = budgetRepository.findByUserAndCategoryAndEndDate(budget.getUser(),budget.getCategory(),precedentBudgetDateFin);
        if(precedentBudget!=null){
            if (precedentBudgetDateFin.isBefore(LocalDate.now())){
                int precedentReliquat = precedentBudget.getReliquat();
                int newBudet = precedentReliquat+budget.getAmount();

                budget.setReliquat(newBudet);
                budget.setAmount(newBudet);
            }
        }
        String budgetMois = ""+dateDebut.getMonth();
        budget.setMois(budgetMois);

        budgetRepository.save(budget);
        return "Budget add successful";
    }


    //Methode pour recuperer la liste des budget
    public List<Budget> read(){
        return budgetRepository.findAll();
    }

    //Methode pour supprimer un budget
    public String delete(int id){
        Budget budgetExist =  budgetRepository.findById(id);
        if (budgetExist!=null) {
           List<Depenses> depenseExist = depenseRepository.findByBudgetId(id);
            if (depenseExist!=null){
                throw new RuntimeException("le budget ne peut pas être supprimé en raison des dépenses associées.");
            }
            budgetRepository.deleteById(id);
        }
        return "suppression successful";
    }

    //Methode pour Modifier un budget specifique
    public Budget update(Budget budget){
        Budget budget1 =  budgetRepository.findById(budget.getId());
        if (budget1==null){
            throw new RuntimeException("budget not exist");
        }else {
            List<Depenses> depenseExist = depenseRepository.findByBudgetId(budget.getId());
            if (depenseExist != null) {
                LocalDate dateDebutBudget = budget.getStarDate();
                if (!dateDebutBudget.isEqual(budget1.getStarDate())) {
                    throw new RuntimeException("la date de debut ne doit pas etre modifier");
                }
                LocalDate dateFinBudget = budget.getEndDate();
                if (!dateFinBudget.isEqual(budget1.getEndDate())) {
                    throw new RuntimeException("la date de fin ne doit pas etre modifier");
                }
            }
        }
        budgetRepository.save(budget);
        return budgetRepository.findById(budget.getId());
    }
}
