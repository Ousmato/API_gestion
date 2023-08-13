package com.tpAPI_gestion.Repository;

import com.tpAPI_gestion.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User_repository extends JpaRepository<User, Integer> {

    User findById(int id);

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

}
