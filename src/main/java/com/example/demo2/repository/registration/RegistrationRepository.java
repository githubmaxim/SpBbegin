package com.example.demo2.repository.registration;

import com.example.demo2.entity.registration.LogPas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Repository for "RegistrationController" class
 *
 * @author Maxim
 * @version 1.0
 */
@Repository
public interface RegistrationRepository extends JpaRepository <LogPas, Integer>{

    /**
     * The method looks for a client with this login.
     * @param username client login
     * @return the string found in the "LogPas" entity
     */
    @Query("select lp from LogPas lp where lp.username = ?1")
    @Transactional
    LogPas findByUsername(String username);
}
