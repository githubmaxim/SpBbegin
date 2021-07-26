package com.example.demo2.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//Тут аннотацию @Data (которая содержит в себе 4 аннотации) применять нельзя !!! т.к. есть поле “id”, которое автоматически назначает БД.
//При этом ломается логика, встроенной в эту аннотацию, внутренней аннотации @EqualsAndHashCode.

//Аннотацию @Builder (создающую конструкторы) тут, я так понял, применять не нужно, для того чтобы доступ к данным сущности велся только через геттеры/сеттеры
@EqualsAndHashCode(of = {"id", "name", "login", "email", "universities"}) //будет неправильно работать при автоматическом присвоении БД-ых значения полю "id", а при UUID все ОК!
@ToString(of = {"id", "name", "login", "email", "universities"})

public class Users {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String login;
    private String email;

    @ManyToMany(cascade = {CascadeType.PERSIST}) //или CascadeType.MERGE - они аналог "=CascadeType.ALL", только для связи @ManyToMany и @ManyToOne
    @JoinTable(name = "Users_University", joinColumns = @JoinColumn(name = "users_id"), inverseJoinColumns = @JoinColumn(name = "university_id"))
    @JsonIgnoreProperties("usersList") //эта аннотация + такая-же аннотация в дочерней сущности (с обратной ссылкой) в связанной сущности разрывают циклическую ссылку в работе JSON
    private List<University> universities;

}
