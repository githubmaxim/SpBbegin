package com.example.demo2.users.controller;

import com.example.demo2.controller.users.UsersController;
import com.example.demo2.service.users.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.example.demo2.users.prototype.UsersPrototypeForUnitTest.aUserDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Тут мы делаем тестирование формируемых контроллером сетевых ответов для Клиентов

//Такое написание аннотаций дает возможность тестировать страницы с доступом для всех ролей
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UsersController.class)//@WebMvcTest вместо @SpringBootTest + говорим создать экземпляр только одного контроллера иначе
// потребует создать @MockBean-заглушки для всех сервисов и репозиториев остальных контроллеров. @WebMvcTest вместо
// @SpringBootTest, при желании запустить Unit-тестирование (при этом нужно будет сделать еще, кроме
// заглушки сетевого соединения, заглушку для репозитория, сервиса и т.д.)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UsersService usersService;

    @Test
    void findAllUsers() throws Exception {
        when(usersService.findAll()).thenReturn(Collections.singletonList(aUserDTO()));
        mockMvc.perform(get("/users/findAll"))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(aUserDTO()))))
                .andExpect(status().isOk());
    }

    @Test
    void findByLogin() throws Exception {
        when(usersService.findByLogin(any())).thenReturn(aUserDTO());
        mockMvc.perform(get("/users/findByLogin?param1={param}", "Nick")) //имя могу вводить любое, заглушка все равно вернет имя "test_name"
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("test_name"))
                .andExpect(jsonPath("$.login").value("test_login"))
                .andExpect(jsonPath("$.email").value("test_email@com.ua"));
    }

    @Test
    void deleteUsers() throws Exception {
        doNothing().when(usersService).deleteUser(aUserDTO().getId()); //заглушка для void метода "deleteUser()" интерфейса UsersService
        mockMvc.perform(delete("/users/delete/{id}", aUserDTO().getId()))
        .andDo(print())
        .andExpect(status().isOk());
    }

    @Test
    void saveUsersLoginIsPresent() throws Exception {
        doNothing().when(usersService).saveUser(aUserDTO()); //заглушка для void метода "saveUser()" интерфейса UsersService
        when(usersService.findByLogin(any())).thenReturn(aUserDTO());
        mockMvc.perform(post("/users/save")
                    .content(objectMapper.writeValueAsString(aUserDTO()))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(444))
                .andExpect(jsonPath("$").value("Such a login exists"));
    }

    @Test
    void saveUsersLoginIsAbsent() throws Exception {
         doNothing().when(usersService).saveUser(aUserDTO()); //заглушка для void метода
        when(usersService.findByLogin(any())).thenReturn(null);
        mockMvc.perform(post("/users/save")
                    .content(objectMapper.writeValueAsString(aUserDTO()))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").value("User created"));
    }

}
