package com.example.demo2.entity.users;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * A subordinate entity of the “User” entity.
 *
 * @author Maxim
 * @version 1.0
 */
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

    /**
     * university name
     */
    private String name;

    /**
     * field of connection with the entity "Users"
     */
    @OneToOne(optional=false, mappedBy="universities")
//    @JsonIgnoreProperties("universities")
    private Users users;

    /**
     * field of connection with the entity "City"
     */
    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "city_id")
    @JsonIgnoreProperties("univ") //эта аннотация + такая-же аннотация в дочерней сущности (с обратной ссылкой) в связанной сущности разрывают циклическую ссылку в работе JSON
    private City cities;

    //В закомментированном блоке кода в этой сущности, а так же в "City" и "Users" показано написание других отношений (а не только @OneToOne)
//    @ManyToMany(mappedBy = "universities")
//    @JsonIgnoreProperties("universities")//эта аннотация + такая-же аннотация (с обратной ссылкой) в связанной сущности разрывают циклическую ссылку в работе JSON
//    private List<Users> usersList;
//
//    @ManyToOne(cascade = {CascadeType.PERSIST})
//    @JsonIgnoreProperties("universitySet")
//    @JoinColumn (name = "city_id")
//    private City cities;
}
