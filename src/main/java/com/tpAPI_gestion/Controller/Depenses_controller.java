package com.tpAPI_gestion.Controller;

import com.tpAPI_gestion.Entity.Category;
import com.tpAPI_gestion.Entity.Depenses;
import com.tpAPI_gestion.Entity.User;
import com.tpAPI_gestion.Service.Depenses_service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depenses")
public class Depenses_controller {

    @Autowired
    private Depenses_service depensesService;


    @PostMapping("/add")
    public String add( @Valid @RequestBody Depenses depenses){
      return  depensesService.add(depenses);

    }
    //Endpoint pour recuperer la liste des budet
    @GetMapping("/list")
    public List<Depenses> read(){
        return depensesService.read();
    }

    //Endpoint pour supprimer un budget
    @DeleteMapping("/supprimer/{id}")
    public String delete(@PathVariable int id){
      return   depensesService.delete(id);
    }

    //Endpoint pour Modifier un budget specifique
    @PutMapping("/modifier")
    public Depenses update(@Valid @RequestBody Depenses depenses){
        return depensesService.update(depenses);
    }

}
