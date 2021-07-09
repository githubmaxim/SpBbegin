package com.example.demo2.service;

import com.example.demo2.dto.UsersDto;
import com.example.demo2.exception.ValidationException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UsersService {

//    UsersDto saveUser(UsersDto usersDto) throws ValidationException;
    ResponseEntity<?> saveUser(UsersDto usersDto);

    void deleteUser(Integer userId);

    UsersDto findByLogin(String login);

    List<UsersDto> findAll();
}
