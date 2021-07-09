package com.example.demo2.repository;

import com.example.demo2.entity.Product;
import com.example.demo2.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    @Query("select u from Users u where u.login = ?1")
    Users findByLogin(String name);
}