package com.example.demo2.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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
