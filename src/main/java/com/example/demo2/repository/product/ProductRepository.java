package com.example.demo2.repository.product;

import com.example.demo2.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("select p from Product p where p.name = ?1")
    List<Product> findLineByName(String name);

//    @Query("drop from Product p where p.name = ?1")
//    void deleteLineByName(String name);
}
