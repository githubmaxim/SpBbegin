package com.example.demo2.admin.controller;

//��� �� ������ ������������ ����������� ������������ ������� ������� ��� ��������

import com.example.demo2.controller.users.UsersController;
import com.example.demo2.service.admin.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.example.demo2.users.prototype.UsersPrototypeForUnitTest.aUserDTO;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)//@WebMvcTest ������ @SpringBootTest + ������� ������� ��������� ������ ������ ����������� �����
// ��������� ������� @MockBean-�������� ��� ���� �������� � ������������ ��������� ������������. @WebMvcTest ������
// @SpringBootTest, ��� ������� ��������� Unit-������������ (��� ���� ����� ����� ������� ���, �����
// �������� �������� ����������, �������� ��� �����������, ������� � �.�.)
@AutoConfigureMockMvc
public class AdminControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminService adminService;

    @Test
    void findAllLogPas() throws Exception {
//        when(adminService.findAll()).thenReturn(Collections.singletonList(aUserDTO()));
//        mockMvc.perform(get("/users/findAll"))
//                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(aUserDTO()))))
//                .andExpect(status().isOk());
    }
}
