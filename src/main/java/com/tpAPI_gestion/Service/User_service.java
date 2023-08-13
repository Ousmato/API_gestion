package com.tpAPI_gestion.Service;

import com.tpAPI_gestion.Entity.User;
import com.tpAPI_gestion.Repository.User_repository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class User_service {


    @Autowired
    private User_repository userRepository;

    //Methode pour ajouter un utilisateur
    public String add(User user){
        User user1 = userRepository.findByEmail(user.getEmail());
        if(user1==null){
            userRepository.save(user);
        }else
            throw new RuntimeException("user exist");
        return "add success";
    }

    //Methode pour recuperer la liste des utilisateur
    public List<User> read(){
        return userRepository.findAll();
    }

    //Methode pour supprimer un utilisateur
    public String delete(int id){
        userRepository.deleteById(id);
        return "suppression successful";
    }

    //Methode pour Modifier les informations d'un utilisateur
    public User update(User user){
        User userExist = userRepository.findById(user.getId());
        if (userExist==null) {
            userRepository.save(user);
            return userRepository.findById(user.getId());
        }else
            throw new RuntimeException("user exist");
    }

    //Methode connexion pour verifier si l'utilisateur existe
    public User connexion(String email, String password){
        User user = userRepository.findByEmailAndPassword(email, password);
        if(user != null){
            return user;
        }else throw new EntityExistsException("user doesn't exist");
    }

}
