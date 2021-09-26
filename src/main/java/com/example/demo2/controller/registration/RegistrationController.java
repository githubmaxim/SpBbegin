package com.example.demo2.controller.registration;

import com.example.demo2.entity.registration.LogPas;
import com.example.demo2.entity.registration.Role;
import com.example.demo2.repository.registration.RegistrationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;


@Controller //т.к. я буду выводить в ответе не RestTemplate, а ссылку на html-файл, то писать нужно
// именно @Controller(а не @RestController, который будет выводить вместо страницы название страницы)
@AllArgsConstructor
@Slf4j
public class RegistrationController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String indexHtml() {
        log.info("+++message by RegistrationController, method indexHtml+++");
        return "maiin";
    }

//    @GetMapping("/login") - не пишется т.к. в соответствии с настройками Spring Security сам предоставляет фильтр, который перехватывает этот запрос и аутентифицирует пользователя


    @GetMapping("/registration")
    public String registration(){
        log.info("+++message by RegistrationController, method registration+++");
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(LogPas logPas, Map<String, Object> model) {
        log.info("+++message by RegistrationController, method addUser+++");

        if (logPas.getUsername().isEmpty() | logPas.getPassword().isEmpty()) {
            model.put("message", "Login or Pass unfilled!");
            return "registration";
        }

        LogPas logPasFromDb = registrationRepository.findByUsername(logPas.getUsername());

        if (logPasFromDb != null) {
            model.put("message", "This Login+Pass Exist!");
            return "registration";
        }

        logPas.setActive(true);
        logPas.setRoles(Collections.singleton(Role.USER));
        logPas.setPassword(passwordEncoder.encode(logPas.getPassword()));
        registrationRepository.save(logPas);

        return "redirect:/login";
    }
}
