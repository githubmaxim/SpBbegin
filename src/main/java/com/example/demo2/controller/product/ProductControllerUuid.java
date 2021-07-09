package com.example.demo2.controller.product;

import com.example.demo2.entity.product.Product3;
import com.example.demo2.repository.product.ProductRepositoryUuid;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/products3")
public class ProductControllerUuid {

    @Autowired
    private ProductRepositoryUuid repo;

    @GetMapping
    public ResponseEntity<List<Product3>> list(@RequestParam(value = "param1", required = false) String name) {

        try {
            if (!Strings.isNullOrEmpty(name)) { //Strings.isNullOrEmpty() метод из Guava. Если без Guava, то "(name != null && !name.isEmpty())". Если просто "!name.isEmpty()", то будет ошибка.
                List<Product3> lineByName = repo.findLineByName(name);
                if (lineByName != null && lineByName.size() != 0) {return ResponseEntity.ok().body(lineByName);
                } else {return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            } else {
                List<Product3> products = repo.findAll();
                return new ResponseEntity<>(products, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        try {
            Product3 product1 = repo.findById(id).get(); //.get(); //эта строка проверяет на наличае такого "id" перед заполнением, и если не находит, то выходим на блок "catch"
            return ResponseEntity.status(200).body(product1); //1-й тип создания создания ответа - используется набор методов класса ResponseEntity
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Entry Not found");
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Product3 product) {
        try {
            Product3 product1 = repo.save(product);
            return ResponseEntity.status(200).body(product1);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Bad Request");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Product3 product, @PathVariable UUID id) {
        try {
            Product3 existProduct = repo.findById(id).get(); //не находим "id" - уходим в "catch"
            Product3 product1 = repo.save(product);
            return new ResponseEntity<>("Ok", HttpStatus.OK); //2-й тип создания создания ответа - используется один из конструкторов для класса ResponseEntity
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Entry Not found");
        }
    }

    @DeleteMapping("/{id}")
//    public ResponseEntity<?> delete(@PathVariable Integer id) {
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            Product3 existProduct = repo.findById(id).get(); //не находим "id" - уходим в "catch"
            Optional<Product3> p = repo.findById(id); //для возможности показать в ответе информацию, что была удалена
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

