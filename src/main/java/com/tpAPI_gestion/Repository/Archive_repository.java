package com.tpAPI_gestion.Repository;

import com.tpAPI_gestion.Entity.ArchivesDepense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Archive_repository extends JpaRepository<ArchivesDepense, Integer> {

}
