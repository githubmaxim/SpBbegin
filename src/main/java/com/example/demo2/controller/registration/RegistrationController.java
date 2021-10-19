package com.example.demo2.controller.registration;

import com.example.demo2.dto.registration.LogPasDto;
import com.example.demo2.service.registration.DefaultRegistrationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * Controller file responsible for the page navigation mechanism when registering clients.
 *
 * @author Maxim
 * @version 1.0
 */
@Controller //т.к. я буду выводить в ответе не RestTemplate, а ссылку на html-файл, то писать нужно
// именно @Controller(а не @RestController, который будет выводить вместо страницы название страницы)
@AllArgsConstructor
@Slf4j
public class RegistrationController {

    @Autowired
    private DefaultRegistrationService defaultRegistrationService;

    /**
     *This method is used to open the "main.html" page when navigating to "/".
     * @return link to the "main.html" page
     */
    @GetMapping("/")
    public String indexHtml() {
        log.info("+++message by RegistrationController, method indexHtml+++");
        return "index";
//        return "maiin";
    }

//    @GetMapping("/login") - не пишется т.к. в соответствии с настройками Spring Security сам предоставляет фильтр, который перехватывает этот запрос и аутентифицирует пользователя

    /**
     *This method is used to open the "registration.html" page when navigating to "/registration".
     * @return link to the "registration.html" page
     */
    @GetMapping("/registration")
    public String registration(){
        log.info("+++message by RegistrationController, method registration+++");
        return "registration";
    }

    /**
     * This method is used to run the "addUser()" class "DefaultRegistrationService"(interface "RegistrationService") method
     * @param logPasDto new login and password client
     * @param model service accompanying information
     * @return If successful, it goes to the “login.html” page, otherwise it goes to the “registration.html” page and displays a warning text
     */
    @PostMapping("/registration")
    public String addUser(LogPasDto logPasDto, Map<String, Object> model) {
        log.info("+++message by RegistrationController, method addUser+++");
        return defaultRegistrationService.addUser(logPasDto, model);
    }
}
