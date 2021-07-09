package com.example.demo2.service;

import com.example.demo2.dto.UsersDto;
import com.example.demo2.entity.Users;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class UsersConverter {

    public Users fromUsersDtoToUsers(UsersDto usersDto) { //обычное заполнение, не через Builder
        Users users = new Users();
        users.setId(usersDto.getId());
        users.setEmail(usersDto.getEmail());
        users.setName(usersDto.getName());
        users.setLogin(usersDto.getLogin());
        return users;
    }

    public UsersDto fromUsersToUsersDto(Users users) { //заполнение через Builder
        return UsersDto.builder()
                .id(users.getId())
                .email(users.getEmail())
                .login(users.getLogin())
                .name(users.getName())
                .build();
    }
}
