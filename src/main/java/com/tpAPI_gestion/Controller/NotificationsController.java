package com.tpAPI_gestion.Controller;

import com.tpAPI_gestion.Entity.Category;
import com.tpAPI_gestion.Entity.Notifications;
import com.tpAPI_gestion.Service.Notifications_service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationsController {

    @Autowired
    private Notifications_service notificationsService;


    //Endpoint pour recuperer la liste des budet
    @GetMapping("/list")
    public List<Notifications> read(){
        return notificationsService.read();
    }

    //Endpoint pour supprimer un budget
    @DeleteMapping("/supprimer/{id}")
    public String delete(@PathVariable int id){
        return  notificationsService.delete(id);

    }

    //Endpoint pour Modifier un budget specifique
    @PutMapping("/modifier")
    public Notifications update(@Valid @RequestBody Notifications notifications){
        return notificationsService.update(notifications);
    }
}
