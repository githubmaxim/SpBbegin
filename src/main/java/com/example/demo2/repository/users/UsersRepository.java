package com.example.demo2.repository.users;

import com.example.demo2.entity.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;

/**
 * The Repository for "DefaultUsersService" class
 *
 * @author Maxim
 * @version 1.0
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    /**
     * The method looks for a client with this login.
     * @param name user login
     * @return the string found in the "Users" entity
     */
    @Query("select u from Users u where u.login = ?1")
    @Transactional   //@Transactional(timeout = 10) приводит к тому, что метод становится транзакционным + запускается с таймаутом в 10 секунд
                     //и без флага readOnly. !!! При этом все стандартные методы в репозитории уже являются тразакционными!!!
    Users findByLogin(String name);
}