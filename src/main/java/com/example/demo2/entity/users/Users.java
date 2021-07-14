package com.example.demo2.entity.users;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
//Тут аннотацию @Data (которая содержит в себе 4 аннотации) применять нельзя !!! т.к. есть поле “id”, которое автоматически назначает БД.
//При этом ломается логика, встроенной в эту аннотацию, внутренней аннотации @EqualsAndHashCode.

//Аннотацию @Builder (создающую конструкторы) тут, я так понял, применять не нужно, для того чтобы доступ к данным сущности велся только через геттеры/сеттеры
@EqualsAndHashCode(of = {"id", "name", "login", "email"}) //будет неправильно работать при автоматическом присвоении БД-ых значения полю "id", а при UUID все ОК!
@ToString(of = {"id", "name", "login", "email"})

public class Users {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String login;
    private String email;
}
