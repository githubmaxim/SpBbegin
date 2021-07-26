package com.example.demo2.repository.users;

import com.example.demo2.entity.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    @Query("select u from Users u where u.login = ?1")
    @Transactional   //@Transactional(timeout = 10) //это приводит к тому, что метод становится транзакционным + запускается с таймаутом в 10 секунд
                     //и без флага readOnly. !!! При этом все стандартные методы в репозитории уже являются тразакционными!!!
    @Lock(LockModeType.OPTIMISTIC) //для работы этой аннотации обязательно наличие аннотации @Transactional
    Users findByLogin(String name);
}