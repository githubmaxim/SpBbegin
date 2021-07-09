package com.example.demo2.controller;

import com.example.demo2.dto.UsersDto;
import com.example.demo2.exception.ValidationException;
import com.example.demo2.service.UsersService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
//еще есть @Log4j/@Log и т.д. + у них в скобках "()" можно дописывать разные ключи конфигурации. Позволяют выводить логи без создания строки с логером
public class UsersController {

//    static final Logger log = LoggerFactory.getLogger(UsersController.class);

    private final UsersService usersService;


// Для другого написания метода saveUsers() в классе DefaultUserService
    //    @PostMapping("/save")
//    public UsersDto saveUsers(@Valid @RequestBody UsersDto usersDto) throws ValidationException {
//        log.info("+++message by UserController, method saveUsers+++");
//        log.info("UserController: Handling save users: " + usersDto);
//        return usersService.saveUser(usersDto);
//    }
    @PostMapping("/save")
    public ResponseEntity<?> saveUsers(@Valid @RequestBody UsersDto usersDto) throws ValidationException {
        log.info("+++message by UserController, method saveUsers+++");
        log.info("UserController: Handling save users: " + usersDto);
        return usersService.saveUser(usersDto);
    }

    @GetMapping("/findAll")
    public List<UsersDto> findAllUsers() {
        log.info("+++message by UserController, method findAllUsers+++");
        log.info("UserController: Handling find all users request");
        return usersService.findAll();
    }

    @GetMapping("/findByLogin")
    public UsersDto findByLogin(@RequestParam(value = "param1", required = false) String login) {
        log.info("+++message by UserController, method findByLogin+++");
        log.info("UserController: Handling find by login request: " + login);
        return usersService.findByLogin(login);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUsers(@PathVariable Integer id) {
        log.info("+++message by UserController, method deleteUsers+++");
        log.info("UserController: Handling delete user request: " + id);
        usersService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
