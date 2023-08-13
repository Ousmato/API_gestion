package com.tpAPI_gestion.Repository;

import com.tpAPI_gestion.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface Depense_repository extends JpaRepository<Depenses, Integer> {
   //Depenses findByUserAndPeriodeAndCategoryAndDate(User user, Periode periode, Category category,LocalDate date);
   Depenses findById(int id);

  // Depenses findFirstByUserIdAndCategoryIdAndPeriodeOrderByPeriodeDesc(int idUser, int categoryId, int idP);
    Depenses findFirstByUserIdAndBudgetIdAndPeriodeOrderByDateDesc(int idUser, int budgetId, Periode periode);

    //List<Depenses> findByUserIdAndCategoryId(int userId, int idCategory);
Depenses findByUserAndBudgetAndPeriodeAndDate(User userId, Budget budget, Periode periodeId, LocalDate date);

    Depenses deleteById(int id);

    Depenses findFirstByBudget(Budget budget);
    List<Depenses> findByBudgetId(int budgetId);
    Depenses findByBudget(Budget budgetId);

}
