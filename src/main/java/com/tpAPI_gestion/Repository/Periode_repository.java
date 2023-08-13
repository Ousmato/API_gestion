package com.tpAPI_gestion.Repository;

import com.tpAPI_gestion.Entity.Depenses;
import com.tpAPI_gestion.Entity.Periode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Periode_repository extends JpaRepository<Periode, Integer> {

    Periode findById(int id);

    //Periode findByDepensesAssocierAndDescription(Depenses depenses, String description);
}
