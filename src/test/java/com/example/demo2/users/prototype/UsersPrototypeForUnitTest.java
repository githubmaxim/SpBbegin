package com.example.demo2.users.prototype;

import com.example.demo2.dto.users.UsersDto;
import com.example.demo2.entity.users.Users;

public class UsersPrototypeForUnitTest {

    //при Unit-тестировании при создании объекта задавать нужно все поля включая "id"

    public static Users aUser() {
        Users u = new Users();
        u.setId(1);
        u.setName("test_name");
        u.setLogin("test_login");
        u.setEmail("test_email@com.ua");
        return u;
    }

    public static Users aUser2() {
        Users u = new Users();
        u.setId(2);
        u.setName("test_name2");
        u.setLogin("test_login2");
        u.setEmail("test_email2@com.ua");
        return u;
    }

    public static UsersDto aUserDTO() {
        return UsersDto.builder()
                .id(2)
                .name("test_name")
                .login("test_login")
                .email("test_email@com.ua")
                .build();
    }
}
