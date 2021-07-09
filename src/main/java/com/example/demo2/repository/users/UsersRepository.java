package com.example.demo2.repository.users;

import com.example.demo2.entity.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    @Query("select u from Users u where u.login = ?1")
    Users findByLogin(String name);
}