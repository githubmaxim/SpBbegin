package com.example.demo2.repository;

import com.example.demo2.entity.Product3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepositoryUuid extends JpaRepository<Product3, UUID> {
    @Query("select p from Product3 p where p.name = ?1")
    List<Product3> findLineByName(String name);

//    @Query("drop from Product p where p.name = ?1")
//    void deleteLineByName(String name);
}

