package com.tpAPI_gestion.Repository;

import com.tpAPI_gestion.Entity.Budget;
import com.tpAPI_gestion.Entity.Category;
import com.tpAPI_gestion.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Category_repository extends JpaRepository<Category, Integer> {

    Category findById(int id);

    Category findByUserAndBudgetsAssoierAndType(User user, Budget budget,String type);
}
