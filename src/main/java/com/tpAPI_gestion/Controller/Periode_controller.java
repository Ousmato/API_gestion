package com.tpAPI_gestion.Controller;

import com.tpAPI_gestion.Entity.Category;
import com.tpAPI_gestion.Entity.Periode;
import com.tpAPI_gestion.Service.Periode_service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/periode")
public class Periode_controller {

    @Autowired
    private Periode_service periodeService;

    @PostMapping("/add")
    public String add(@Valid @RequestBody Periode periode){
        periodeService.add(periode);
        return "add successful";
    }

    //Endpoint pour recuperer la liste des budet
    @GetMapping("/list")
    public List<Periode> read(){
        return periodeService.read();
    }

    //Endpoint pour supprimer un budget
    @DeleteMapping("/supprimer/{id}")
    public String delete(@PathVariable int id){
        periodeService.delete(id);
        return "delete successful";
    }

    //Endpoint pour Modifier un budget specifique
    @PutMapping("/modifier")
    public Periode update(@Valid  @RequestBody Periode periode){
        return periodeService.update(periode);
    }
}
