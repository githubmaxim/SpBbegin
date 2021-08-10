package com.example.demo2.service.users;

import com.example.demo2.dto.users.UsersDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UsersService {

//    UsersDto saveUser(UsersDto usersDto) throws ValidationException;
    void saveUser(UsersDto usersDto);

    void deleteUser(Integer userId);

    UsersDto findByLogin(String login);

    List<UsersDto> findAll();


}
