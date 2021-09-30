package com.example.demo2.controller.users;

import com.example.demo2.dto.users.UsersDto;
import com.example.demo2.exception.users.ValidationException;
import com.example.demo2.service.users.UsersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * The Controller file that serves the functions of the "workingWithFields.js" file used on the "workingWithFields.html" page.
 *
 * @author Maxim
 * @version 1.0
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
//еще есть @Log4j/@Log и т.д. + у них в скобках "()" можно дописывать разные ключи конфигурации. Позволяют выводить логи без создания строки с логером
public class UsersController {

//    static final Logger log = LoggerFactory.getLogger(UsersController.class);

    private final UsersService usersService;

//!!!Исключения в методах и тем более проброс через "throws" я не применяю, т.к. работаю через механизм проверки полей через if-ы и пишу код сам для себя!!!

    /**
     * This method is used to run the "findAll()" class "DefaultUsersService"(interface "UsersService") method
     * @return all data from entity "Users"
     */
    @GetMapping("/findAll")
    public List<UsersDto> findAllUsers() {
        log.info("+++message by UserController, method findAllUsers+++");
        log.info("UserController: Handling find all users request");
        return usersService.findAll();
    }

    /**
     * This method is used to run the "findByLogin()" class "DefaultUsersService"(interface "UsersService") method
     * @param login the field by which you need to find a record in the "Users" entity
     * @return the requested data from the "Users" entity or null
     */
    @GetMapping("/findByLogin")
    public UsersDto findByLogin(@RequestParam(value = "param1", required = false) String login) {
        log.info("+++message by UserController, method findByLogin+++");
        log.info("UserController: Handling find by login: " + login);
        return usersService.findByLogin(login);
    }

    /**
     * This method is used to run the "deleteUser()" class "DefaultUsersService"(interface "UsersService") method
     * @param id the identifying line number to be found in the "Users" entity on the server.
     * @return status "200"
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUsers(@PathVariable Integer id) {
        log.info("+++message by UserController, method deleteUsers+++");
        log.info("UserController: Handling delete user request: " + id);
        usersService.deleteUser(id);
        return ResponseEntity.status(200).build();
    }


    // Для другого написания метода saveUsers() в классе DefaultUserService
    //    @PostMapping("/save")
//    public UsersDto saveUsers(@Valid @RequestBody UsersDto usersDto) {
//        log.info("+++message by UserController, method saveUsers+++");
//        log.info("UserController: Handling save users: " + usersDto);
//        return usersService.saveUser(usersDto);
//    }

    /**
     * This method is used to run the "saveUser()" class "DefaultUsersService"(interface "UsersService") method
     * @param usersDto data uploading to the server
     * @return status "200"|"444" and "User created"|"Such a login exists"
     */
    @PostMapping("/save")
    //логика прописана тут, а не в сервисе т.к. на выходе необходимо получать объект ResponseEntity-класса, что делать в сервисе не рекомендуется
    public ResponseEntity<?> saveUsers(@Valid @RequestBody UsersDto usersDto) {
        log.info("+++message by UserController, method saveUsers+++");
        log.info("UserController: Handling save users: " + usersDto);
        if (!isNull(usersService.findByLogin(usersDto.getLogin()))) { //основная валидация находится в файле "workingWithFields.js", кроме того валидация есть в проверочных аннотациях на самих переменных в классе "UserDto"
            return ResponseEntity.status(444).body("Such a login exists");
        } else {
            usersService.saveUser(usersDto);
            return ResponseEntity.status(200).body("User created");
        }
    }

    /**
     * This method is used to show how the mechanism for receiving information from a client using the form method
     * @param form all data from form
     * @return text
     */
    @PostMapping(value = "/form", produces = {"application/json", "application/xml"}, consumes = {"application/x-www-form-urlencoded"})
    public @ResponseBody String form(ForForm form) { //форма отправляет тип  "application/x-www-form-urlencoded" , но Spring не понимает его как RequestBody. Поэтому мы должны удалить аннотацию @RequestBody(из скобок) и добавить @ResponseBody после "public".
//    public @ResponseBody  ResponseEntity<?> form(ForForm form) { //форма отправляет тип  "application/x-www-form-urlencoded" , но Spring не понимает его как RequestBody. Поэтому мы должны удалить аннотацию @RequestBody(из скобок) и добавить @ResponseBody после "public".
        log.info("+++message by UserController, method form+++");
        log.info("number=" + form.getNumber() + ", word=" + form.getWord());
        return "You sended by form-method: number=" + form.getNumber() + ", word=" + form.getWord();
    }
}

//Создание пула из 4-х фиксированных потоков на процессоре
//    ExecutorService executorService = Executors.newFixedThreadPool(4);
//    CompletionService executorCompletionService = new ExecutorCompletionService<>(executorService );
//    List<Future> futures = new ArrayList<Future<Integer>>();
//        futures.add(executorCompletionService.submit(new ....));
//        futures.add(executorCompletionService.submit(() -> { for (int r = 0; r < 100; r++) {...}  }    );
//        futures.add(executorCompletionService.submit(...));
//        futures.add(executorCompletionService.submit(...));
//
//        for (int i=0; i<futures.size(); i++) {
//        Integer result = executorCompletionService.take().get();
