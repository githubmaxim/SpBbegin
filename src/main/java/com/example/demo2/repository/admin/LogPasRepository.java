package com.example.demo2.repository.admin;

import com.example.demo2.entity.registration.LogPas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Repository for "DefaultAdminService" class
 *
 * @author Maxim
 * @version 1.0
 */
@Repository
public interface LogPasRepository extends JpaRepository<LogPas, Integer> {
}