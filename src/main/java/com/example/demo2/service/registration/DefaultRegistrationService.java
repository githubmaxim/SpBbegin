package com.example.demo2.service.registration;

import com.example.demo2.dto.registration.LogPasDto;
import com.example.demo2.entity.registration.LogPas;
import com.example.demo2.entity.registration.Role;
import com.example.demo2.repository.registration.RegistrationRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * The Service file that serves the functions of the "RegistrationController" class.
 *
 * @author Maxim
 * @version 1.0
 */
@Service
@Builder
@Slf4j
public class DefaultRegistrationService implements RegistrationService {
    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LogPasConverter logPasConverter;

    /**
     * This method saving new login and password in entity "LogPas" or sends an error message
     * @param logPasDto new login and password client
     * @param model service accompanying information
     * @return If successful, it goes to the “login.html” page, otherwise it goes to the “registration.html” page and displays a warning text
     */
    @Override
    public String addUser(LogPasDto logPasDto, Map<String, Object> model) {
        if (logPasDto.getUsername().isEmpty() | logPasDto.getPassword().isEmpty()) {
            model.put("message", "Login or Pass unfilled!");
            return "registration";
        }
        else if (logPasDto.getUsername().contains(" ") | logPasDto.getPassword().contains(" ")) {
            model.put("message", "Login and Pass should not contain spaces!");
            return "registration";
        }
        else if (Character.isDigit(logPasDto.getUsername().charAt(0))) {
            model.put("message", "First symbol \"User name\" field should be a letter!");
            return "registration";
        }
        else if (logPasDto.getUsername().length()>20 | logPasDto.getPassword().length()>20) {
            model.put("message", "The length of the login and password must not exceed 20 characters!");
            return "registration";
        }

        LogPas logPasFromDb = registrationRepository.findByUsername(logPasDto.getUsername());

        if (logPasFromDb != null) {
            model.put("message", "This Login Exist!");
            return "registration";
        }

        logPasDto.setActive(true);
        logPasDto.setRoles(Collections.singleton(Role.USER));
        logPasDto.setPassword(passwordEncoder.encode(logPasDto.getPassword())); //добавляем кодирование паролей
        LogPas logPas = logPasConverter.fromLogPasDtoToLogPas(logPasDto);
        registrationRepository.save(logPas);

        return "redirect:/login";
    }
}
