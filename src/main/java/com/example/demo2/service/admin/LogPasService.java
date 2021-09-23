package com.example.demo2.service.admin;

import com.example.demo2.entity.registration.LogPas;

import java.util.List;

public interface LogPasService {
    String putLogPas(Integer id, String role);

    void deleteLogPas(Integer id);

    List<LogPas> findAll();
}
