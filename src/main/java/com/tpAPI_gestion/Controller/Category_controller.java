package com.tpAPI_gestion.Controller;

import com.tpAPI_gestion.Entity.Budget;
import com.tpAPI_gestion.Entity.Category;
import com.tpAPI_gestion.Service.Category_service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class Category_controller {

    @Autowired
    private Category_service categoryService;

    @PostMapping("/add")
    public String add(@Valid  @RequestBody Category category){
        return categoryService.add(category);

    }

    //Endpoint pour recuperer la liste des budet
    @GetMapping("/list")
    public List<Category> read(){
        return categoryService.read();
    }

    //Endpoint pour supprimer un budget
    @DeleteMapping("/supprimer/{id}")
    public String delete(@PathVariable int id){
        categoryService.delete(id);
        return "delete successful";
    }

    //Endpoint pour Modifier un budget specifique
    @PutMapping("/modifier")
    public Category update(@Valid @RequestBody Category category){
        return categoryService.update(category);
    }
}
