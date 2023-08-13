package com.tpAPI_gestion.Service;

import com.tpAPI_gestion.Entity.ArchivesDepense;
import com.tpAPI_gestion.Repository.Archive_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Archive_service {
    @Autowired
    private Archive_repository archiveRepository;

    public List<ArchivesDepense> archivesList(){
        return archiveRepository.findAll();
    }

}
