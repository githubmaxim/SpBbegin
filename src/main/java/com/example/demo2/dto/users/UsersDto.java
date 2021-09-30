package com.example.demo2.dto.users;

import com.example.demo2.entity.users.University;
import lombok.Data;
import lombok.Builder;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * File DTO for entity "Users".
 *
 * @author Maxim
 * @version 1.0
 */
@Data //тут эту аннотацию (которая содержит в себе 4 аннотации) можно применять т.к. нет поля “id”, которое бы автоматически назначала БД.
      //Иначе ломается логика, встроенной в эту аннотацию, внутренней аннотации @EqualsAndHashCode.
@Builder
public class UsersDto {
    private Integer id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Name cannot be empty")
    private String login;

    @Email(message = "Email should be valid")
    private String email;

    private University universities;

}