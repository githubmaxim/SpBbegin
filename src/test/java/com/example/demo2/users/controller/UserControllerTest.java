package com.example.demo2.users.controller;

import com.example.demo2.controller.users.UsersController;
import com.example.demo2.dto.users.UsersDto;
import com.example.demo2.service.users.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static com.example.demo2.users.prototype.UsersPrototype.aUserDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    UsersService usersService;

    @BeforeEach
    void setUp() {
        usersService = mock(UsersService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new UsersController(usersService)).build();
        objectMapper = new ObjectMapper();
    }

//    @Test
//    void saveUsers() throws Exception {
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//
//        ResponseEntity<?> responseEntity = new ResponseEntity<>(
//                "some response body",
//                header,
//                HttpStatus.OK
//        );
//        when(usersService.saveUser(any())).thenReturn(responseEntity);
//        mockMvc.perform(post("/users/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(aUserDTO())))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(objectMapper.writeValueAsString(aUserDTO())));
//    }

    @Test
    void findAllUsers() throws Exception {
        when(usersService.findAll()).thenReturn(Collections.singletonList(aUserDTO()));
        mockMvc.perform(get("/users/findAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(aUserDTO()))))
                .andExpect(status().isOk());
    }

    @Test
    void findByLogin() {
    }

    @Test
    void deleteUsers() {
    }
}
