package com.example.demo2.dto.registration;

import com.example.demo2.entity.registration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * File DTO for entity "LogPas".
 *
 * @author Maxim
 * @version 1.0
 */
//следующие две аннотации требует в этом классе, но не требует в сущности "LogPas".
@AllArgsConstructor
@NoArgsConstructor
@Data//тут эту аннотацию (которая содержит в себе 4 аннотации) можно применять т.к. нет поля “id”, которое бы автоматически назначала БД.
//Иначе ломается логика, встроенной в эту аннотацию, внутренней аннотации @EqualsAndHashCode.
@Builder
public class LogPasDto {
    private Integer id;
    private String username;
    private String password;
    private boolean active;
    private Set<Role> roles;

    public boolean getActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
}
