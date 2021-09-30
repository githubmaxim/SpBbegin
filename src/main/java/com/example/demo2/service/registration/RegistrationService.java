package com.example.demo2.service.registration;

import com.example.demo2.dto.registration.LogPasDto;

import java.util.Map;

/**
 * Interface with method templates for the “DefaultRegistrationService” class.
 *
 * @author Maxim
 * @version 1.0
 */
public interface RegistrationService {
    String addUser(LogPasDto logPasDto, Map<String, Object> model);
}
