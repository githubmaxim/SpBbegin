package com.example.demo2.users.prototype;

import com.example.demo2.dto.users.UsersDto;
import com.example.demo2.entity.users.Users;

public class UsersPrototype {
    public static Users aUser() {
        Users u = new Users();
        u.setName("test_name");
        u.setLogin("test_login");
        u.setEmail("test_email@com.ua");
        return u;
    }

    public static UsersDto aUserDTO() {
        return UsersDto.builder()
                .name("test_name")
                .login("test_login")
                .email("test_email@com.ua")
                .build();
    }
}
