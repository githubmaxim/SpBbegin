package com.example.demo2.service.users;

import com.example.demo2.dto.users.UsersDto;

import java.util.List;

public interface UsersService {

//    UsersDto saveUser(UsersDto usersDto) throws ValidationException;
    void saveUser(UsersDto usersDto);

    void deleteUser(Integer userId);

    UsersDto findByLogin(String login);

    List<UsersDto> findAll();
}
