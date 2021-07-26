package com.example.demo2.entity.users;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "city")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "city"}) //будет неправильно работать при автоматическом присвоении БД-ых значения полю "id", а при UUID все ОК!
@ToString(of = {"id", "city"})

public class City {
    @Id
    @GeneratedValue
    private Integer id;

    private String city;

    @OneToMany (mappedBy = "cities")
    @JsonIgnoreProperties("cities")
    private Set<University> universitySet;
}
