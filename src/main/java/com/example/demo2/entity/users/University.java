package com.example.demo2.entity.users;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "university")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "name", "cities"}) //будет неправильно работать при автоматическом присвоении БД-ых значения полю "id", а при UUID все ОК!
@ToString(of = {"id", "name", "cities"})
public class University {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "universities")
    @JsonIgnoreProperties("universities")//эта аннотация + такая-же аннотация (с обратной ссылкой) в связанной сущности разрывают циклическую ссылку в работе JSON
    private List<Users> usersList;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JsonIgnoreProperties("universitySet")
    @JoinColumn (name = "city_id")
    private City cities;
}