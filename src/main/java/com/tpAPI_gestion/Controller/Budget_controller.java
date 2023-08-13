package com.tpAPI_gestion.Controller;

import com.tpAPI_gestion.Entity.Budget;
import com.tpAPI_gestion.Service.budget_service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budget")
public class Budget_controller {
    @Autowired
    private budget_service budgetService;

    //Endpoint pour ajouter un budget
    @PostMapping("/add")
    public String add(@Valid @RequestBody Budget budget){
       return budgetService.add(budget);

    }

    //Endpoint pour recuperer la liste des budet
    @GetMapping("/list")
    public List<Budget> read(){
        return budgetService.read();
    }

    //Endpoint pour supprimer un budget
    @DeleteMapping("/supprimer/{id}")
    public String delete(@PathVariable int id){
        budgetService.delete(id);
        return "delete successful";
    }

    //Endpoint pour Modifier un budget specifique
    @PutMapping("/modifier")
    public Budget update(@Valid @RequestBody Budget budget){
        return budgetService.update(budget);
    }
}
