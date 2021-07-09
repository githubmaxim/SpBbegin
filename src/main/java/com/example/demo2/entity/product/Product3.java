package com.example.demo2.entity.product;


import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "product3")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@Builder
@EqualsAndHashCode(of = {"id", "name", "price"}) //будет неправильно работать при автоматическом присвоении БД-ых значения полю "id", а при UUID все ОК!
@ToString(of = {"id", "name", "price"})
public class Product3 {

   ////Не проходят тесты с проверкой или нахождением по "id" типа "uuid" ни по закомментированным строкам и ни по не закомментированным строкам !!!
//    @GeneratedValue(generator="system-uuid2")
//    @GenericGenerator(name="system-uuid2", strategy = "uuid2") //@GenericGenerator аннотация для подключения самостоятельно созданного генераторов
//    @GeneratedValue(strategy = GenerationType.AUTO)  //нет толку
//    @Column(name="id", insertable = false, updatable = false, nullable = false) //нет толку
//    @Column(name = "id", columnDefinition = "VARCHAR(255)") //вообще не создается таблица
//    @Column(name = "id", columnDefinition = "BINARY(16)") //нет толку
//    @Type(type="org.hibernate.type.UUIDCharType") //нет толку


    @Id
    @GeneratedValue
    private UUID id;

//    @NotEmpty(message = "name can not be empty.")
//    @Size(max = 100, message = "name can not be more than 100 characters.")
    private String name;

//    @NotEmpty(message = "price can not be empty.")
//    @Size(max = 100, message = "price can not be more than 100 characters.")
    private float price;

    public Product3(String name, float price) {
        this.name = name;
        this.price = price;
    }
}
