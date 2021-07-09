package com.example.demo2.controller.product;

import com.example.demo2.entity.product.Product;
import com.example.demo2.repository.product.ProductRepository;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository repo;

    @GetMapping
    public ResponseEntity<List<Product>> getAll(@RequestParam(value = "param1", required = false) String name) {

        try {
            if (!Strings.isNullOrEmpty(name)) { //Strings.isNullOrEmpty() метод из Guava. Если без Guava, то "(name != null && !name.isEmpty())". Если просто "!name.isEmpty()", то будет ошибка.
                List<Product> lineByName = repo.findLineByName(name);
                if (lineByName != null && lineByName.size() != 0) {return ResponseEntity.ok().body(lineByName);
                } else {return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            } else {
                List<Product> products = repo.findAll();
                return new ResponseEntity<>(products, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        try {
            Product product1 = repo.findById(id).get(); //эта строка проверяет на наличае такого "id" перед заполнением, и если не находит, то выходим на блок "catch"
            return ResponseEntity.status(200).body(product1); //1-й тип создания создания ответа - используется набор методов класса ResponseEntity
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Entry Not found");
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Product product) {
        try {
            Product product1 = repo.save(product);
            return ResponseEntity.status(200).body(product1);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Bad Request");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Product product, @PathVariable Integer id) {
        try {
            Product existProduct = repo.findById(id).get(); //не находим "id" - уходим в "catch"
            Product product1 = repo.save(product);
            return new ResponseEntity<>(product1, HttpStatus.OK); //2-й тип создания создания ответа - используется один из конструкторов для класса ResponseEntity
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Entry Not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            Product existProduct = repo.findById(id).get(); //не находим "id" - уходим в "catch"
            Optional<Product> p = repo.findById(id); //для возможности показать в ответе информацию, что была удалена
            repo.deleteById(id);
            return ResponseEntity.ok().body(p.get());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(500).body("Entry Not found");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestParam(value = "param1", required = false) String name) {
        try {
            if (name.equals("all"))
            repo.deleteAll();
            return ResponseEntity.ok().body("You Deleted All !!!");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Entry Not found");
        }
    }


}
