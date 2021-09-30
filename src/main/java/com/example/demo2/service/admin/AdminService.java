package com.example.demo2.service.admin;

import com.example.demo2.dto.registration.LogPasDto;

import java.util.List;

/**
 * Interface with method templates for the “DefaultAdminService” class.
 *
 * @author Maxim
 * @version 1.0
 */
public interface AdminService {
    String putLogPas(Integer id, String role);

    void deleteLogPas(Integer id);

    List<LogPasDto> findAll();
}
