package com.example.demo2.controller.users;

import com.example.demo2.dto.users.UsersDto;
import com.example.demo2.exception.users.ValidationException;
import com.example.demo2.service.users.UsersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    //Тут два варианта написания для получения параметра: через @RequestParam или @PathVariable
    @GetMapping("/download")
//    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@RequestParam(value = "param1", required = false, defaultValue = "forDownload.doc") String fileName) throws IOException {
//    public ResponseEntity<?> downloadFile(@PathVariable Optional<String> fileName) throws IOException {
        log.info("+++message by UserController, method download+++");
        log.info("UserController: Handling find by login request: " + fileName);
        if (fileName.equals("empty")) {
//        if (fileName.get().equals("empty")) {
            return usersService.downloadFile("forDownload.doc");
        } else {
            return usersService.downloadFile(fileName);
//            return usersService.downloadFile(fileName.get());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUsers(@PathVariable Integer id) {
        log.info("+++message by UserController, method deleteUsers+++");
        log.info("UserController: Handling delete user request: " + id);
        usersService.deleteUser(id);
        return ResponseEntity.ok().build();
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
