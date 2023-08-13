package com.tpAPI_gestion.Repository;

import com.tpAPI_gestion.Entity.Budget;
import com.tpAPI_gestion.Entity.Category;
import com.tpAPI_gestion.Entity.Depenses;
import com.tpAPI_gestion.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface Budget_repository extends JpaRepository<Budget, Integer> {

    Budget findById(int id);

    Budget findByUserAndCategoryAndEndDate(User user, Category category,LocalDate date);
    Budget findFirstByUserAndCategoryIdOrderByEndDateDesc(User user,int categoryId);
    Budget findByCategory(Category category);

    //Budget findByUserAndCategoryAndEndDate(User user, Category category, LocalDate enDate);
    Budget findFirstByUserAndCategoryAndEndDateIsBeforeOrderByEndDateDesc(User user,Category category,LocalDate enDate);

}
