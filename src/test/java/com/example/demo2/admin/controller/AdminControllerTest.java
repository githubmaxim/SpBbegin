package com.example.demo2.admin.controller;

import com.example.demo2.controller.admin.AdminController;
import com.example.demo2.service.admin.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.example.demo2.admin.prototype.AdminPrtotypeFofUnitTest.aLogPasDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//“ут мы делаем тестирование формируемых контроллером сетевых ответов дл€  лиентов

//!!!—ервер понимает запрос, но не дает авторизоватьс€ на странице дл€ јдмина (status 403).
//!!!ƒл€ возможности пройти Spring Security авторизацию, нужно:
// 1. в фале контроллера отключить аннотацию дл€ регистрации доступа только дл€ роли ADMIN,
// 2. раскомментировать тестовые методы,
// 3. после тестировани€ оп€ть включить аннотацию и закомментировать тестовые методы.
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@WebMvcTest(AdminController.class)//@WebMvcTest вместо @SpringBootTest + говорим создать экземпл€р только одного контроллера иначе
// потребует создать @MockBean-заглушки дл€ всех сервисов и репозиториев остальных контроллеров. @WebMvcTest вместо
// @SpringBootTest, при желании запустить Unit-тестирование (при этом нужно будет сделать еще, кроме
// заглушки сетевого соединени€, заглушку дл€ репозитори€, сервиса и т.д.)
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminService adminService;


//    @Test
//    //@WithMockUser(username = "user1", password = "pwd", roles = {"ADMIN"})
//    void findAllLogPas() throws Exception {
//        when(adminService.findAll()).thenReturn(Collections.singletonList(aLogPasDto()));
//        mockMvc.perform(get("/admin/findAll"))
//                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(aLogPasDto()))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
////    @WithMockUser(username = "a", password = "a", roles = {"ADMIN"})
//    void deleteUsers() throws Exception {
//        doNothing().when(adminService).deleteLogPas(aLogPasDto().getId());
//        mockMvc.perform(delete("/admin/delete/{id}", aLogPasDto().getId()))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void putLogPasTest() throws Exception {
//        when(adminService.putLogPas(anyInt(), any())).thenReturn("OK");
//        mockMvc.perform(put("/admin/put/{id}", aLogPasDto().getId())
//                .content(objectMapper.writeValueAsString(aLogPasDto().getRoles()))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }

    @Test
    void f() throws Exception {    }
}
