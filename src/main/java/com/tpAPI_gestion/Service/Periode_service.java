package com.tpAPI_gestion.Service;

import com.tpAPI_gestion.Entity.Periode;
import com.tpAPI_gestion.Repository.Periode_repository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Periode_service {

    @Autowired
    private Periode_repository periodeRepository;

    public String add(Periode periode){
        List<Periode> periode1 = periodeRepository.findAll();
        if(periode1.size()>=3)
        periodeRepository.save(periode);
        return "add successful";
    }

    //methode Get
    public List<Periode> read(){
        return periodeRepository.findAll();
    }

    //methode Delete
    public String delete(int id){
        periodeRepository.deleteById(id);
        return "delete successful";
    }

    //methode Put
    public Periode update(Periode periode){
        Periode periode1 = periodeRepository.save(periode);
        if (periode1 !=null){
            return periode1;
        }else throw new EntityExistsException("this rapport exist");
    }
}
