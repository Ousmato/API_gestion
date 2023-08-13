package com.tpAPI_gestion.Service;

import com.tpAPI_gestion.Entity.Budget;
import com.tpAPI_gestion.Entity.Category;
import com.tpAPI_gestion.Entity.Depenses;
import com.tpAPI_gestion.Repository.Category_repository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Category_service {

    @Autowired
    private Category_repository categoryRepository;

    public String add(Category category){
        Category category1 = categoryRepository.findByUserAndBudgetsAssoierAndType(category.getUser(), (Budget) category.getBudgetsAssoier(), category.getType());
        List<Category> category2 = categoryRepository.findAll();
        if(category2.size()>= 4)
            throw new RuntimeException("vous avez depasser le nombre de category");
            if(category1==null) {
            categoryRepository.save(category);
            return "add successful";
        }else
            throw new RuntimeException("category exist");
    }

    //methode Get
    public List<Category> read(){
        return categoryRepository.findAll();
    }

    //methode Delete
    public String delete(int id){
        categoryRepository.deleteById(id);
        return "delete successful";
    }

    //methode Put
    public Category update(Category category){
        Category category1 = categoryRepository.save(category);
        if (category1 !=null){
            return category1;
        }else throw new EntityExistsException("this rapport exist");
    }
}
