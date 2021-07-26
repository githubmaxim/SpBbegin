package com.example.demo2.controller.users;

import com.example.demo2.dto.users.UsersDto;
import com.example.demo2.exception.users.ValidationException;
import com.example.demo2.service.users.UsersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.Objects.isNull;

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
    @PostMapping("/save") //логика прописана тут, а не в сервисе т.к. на выходе необходимо получать объект ResponseEntity-класса, что делать не рекомендуется
    public ResponseEntity<?> saveUsers(@Valid @RequestBody UsersDto usersDto) throws ValidationException {
        log.info("+++message by UserController, method saveUsers+++");
        log.info("UserController: Handling save users: " + usersDto);
        if (!isNull(usersService.findByLogin(usersDto.getLogin()))) { //остальная валидация в проверочных аннотациях на самих переменных в классе "UserDto"
            return ResponseEntity.status(444).body("Such login is exist");
        } else {
            usersService.saveUser(usersDto);
            return ResponseEntity.status(200).body("OK");
        }
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
