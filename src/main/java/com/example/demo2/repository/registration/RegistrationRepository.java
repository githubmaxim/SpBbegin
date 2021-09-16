package com.example.demo2.repository.registration;

import com.example.demo2.entity.registration.LogPas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;



public interface RegistrationRepository extends JpaRepository <LogPas, Integer>{
    @Query("select lp from LogPas lp where lp.username = ?1")
    @Transactional
    LogPas findByUsername(String username);
}
