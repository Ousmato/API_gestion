package com.tpAPI_gestion.Controller;

import com.tpAPI_gestion.Entity.ArchivesDepense;
import com.tpAPI_gestion.Repository.Archive_repository;
import com.tpAPI_gestion.Service.Archive_service;
import com.tpAPI_gestion.Service.Depenses_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/archive")
public class Archive_controller {

    @Autowired
    private Depenses_service depensesService;

    @Autowired
    private Archive_service archiveService;

    @GetMapping("/")
    public List<ArchivesDepense> archivesDepenseList(){
        return archiveService.archivesList();
    }
}
