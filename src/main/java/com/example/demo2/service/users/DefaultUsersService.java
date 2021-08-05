package com.example.demo2.service.users;

import com.example.demo2.dto.users.UsersDto;
import com.example.demo2.entity.users.Users;
import com.example.demo2.repository.users.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultUsersService implements UsersService {

    private final UsersRepository usersRepository;
    private final UsersConverter usersConverter;

    @Autowired
    private ServletContext servletContext;

    @Override
    public void saveUser(UsersDto usersDto) { //не нужно чтобы сервис на выходе выдавал объект ResponseEntity-класса
        log.info("!!!message by DefaultUserService, method saveUser!!!");
        usersRepository.save(usersConverter.fromUsersDtoToUsers(usersDto));
    }


//Ниже используем в методе saveUser() валидацию, написанную в отдельном методе
//        @Override
//    public UsersDto saveUser(UsersDto usersDto) throws ValidationException {
//        log.info("!!!message by DefaultUserController, method saveUser!!!");
//        validateNullUserDto(usersDto); //проходим валидацию, если нет - вылетаем на собственное исключение
//        Users savedUser = usersRepository.save(usersConverter.fromUsersDtoToUsers(usersDto));
//        return usersConverter.fromUsersToUsersDto(savedUser); //в ответе конвертируем "Users" обратно в "UsersDto", чтобы у клиентов небыло доступа непосредственно к нашей сущности
//    }
//
//    //Метод валидации для вывода разных ответов в случае неправильного ввода значения "login" или всего "UserDto"
//    //А так, обычно, нужно ставить проверочные аннотации на самих переменных в классе "UserDto" и потом в методах контроллера (принимающих данные от клиента на запись) перед @RequestBody ставить аннотацию @Valid
//    private void validateNullUserDto(UsersDto usersDto) throws ValidationException {
//        if (isNull(usersDto)) {
//            throw new ValidationException("Object user is null");
//        }
//        if (isNull(usersDto.getLogin()) || usersDto.getLogin().isEmpty()) {
//            throw new ValidationException("Login is empty");
//        }
//    }

    @Override
    public void deleteUser(Integer userId) {
        log.info("!!!message by DefaultUserService, method deleteUser!!!");
        usersRepository.deleteById(userId);
    }

    @Override
    public UsersDto findByLogin(String login) {
        log.info("!!!message by DefaultUserService, method findByLogin!!!");
        Users users = usersRepository.findByLogin(login);
        if (users != null) {
            log.info("!!!message by DefaultUserService, method findByLogin!!! users - " + users);
            return usersConverter.fromUsersToUsersDto(users);
        }
        log.info("!!!message by DefaultUserService, method findByLogin!!! - NULL");
        return null;
    }

    @Override
    public List<UsersDto> findAll() {

        log.info("!!!message by DefaultUserService, method findAll!!!");

        //Блок заменяющий в ответе клиенту каждое значение поля Email на строку "Information is not available".
        //Если попытаться написать этот блок через Stream API, то выйдет не сильно короче, но читаемость кода для меня получается как "квадратное колесо".
        //Stream API нормально пишется и читается когда работает с информацией 1-го уровня вложенности, а не 2-го (переменными классов, которые в свою очередь находятся в коллекциях).
        //А именно с информацией 2-го уровня в 90% случаев прийдется работать (с полями сущностей).
        List<UsersDto> usersDtoList = new ArrayList<>();
        List<Users> usersList = usersRepository.findAll();
        for (Users user : usersList) {
            UsersDto usersDto = usersConverter.fromUsersToUsersDto(user);
            usersDto.setEmail("Information is not available");
            usersDtoList.add(usersDto);
        }
        return usersDtoList;

        //Блок отправляющий клиенту всю информацию без изменений
//        return usersRepository.findAll()
//                .stream()
//                .map(x -> usersConverter.fromUsersToUsersDto(x))
//                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> downloadFile(String fileName) throws IOException {

        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName); // создается типа файла, который будет вставляться в тело ответа

//   В следующем блоке кода используется механизмы "NIO 2" + "IO". Тут файл перед передачей разрывается на байты.
        Path path = Paths.get("D:/Java" + "/" + fileName);

        if (Files.exists(path)) {
            byte[] data = Files.readAllBytes(path);
            ByteArrayResource resource = new ByteArrayResource(data);


            HttpHeaders headers = new HttpHeaders(); //создаем объект для дальнейшей записи в него всех необходимых заголовков (это в запросе/ответе инфа идущая после первой “стартовой” строки и до превой пустой строки, после которой идет тело сообщения)
            headers.add("Content-Disposition", String.format("attachment; filename=" + path.getFileName().toString()));
            //следующие три строки устанавливают запрет кеширования броузерами данной отправляемой информации
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(mediaType)
                    .contentLength(data.length)
                    .body(resource);
        } else {
            log.info("!!!message by DefaultUserService, method downloadFile, block else");
            return ResponseEntity.status(404).body("Entry Not found");
        }

    }


    //   !!! В следующем блоке кода используется механизм чистого "IO". Тут файл перед передачей разрывается на байты. !!!
//
//        File file = new File("D:/Java" + "/" + fileName);
//
//       // ” InputStreamResource”, используется только для открытых файлов, лучше не использовать вообще. Для чтения файла лучше использовать FileSystemResource использующий
//       //  (начиная с Spring Framework 5.0) NIO.2 или ByteArrayResource(для загрузки содержимого из любого заданного массива байтов и потом возможности многоразового чтения потока).
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//        HttpHeaders headers = new HttpHeaders(); //создаем объект для дальнейшей записи в него всех необходимых заголовков (это в запросе/ответе инфа идущая после первой “стартовой” строки и до превой пустой строки, после которой идет тело сообщения)
//        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");
//
//        ResponseEntity<Object> responseEntity = ResponseEntity
//                .ok() //создаем в ответе в стартовой строке типа “HTTP 1.* 200 OK”
//                .headers(headers) //заполняем ответ необходимыми заголовками
//                .contentLength(file.length()) // это число байт данных в теле запроса или ответа. При ответе пользователь тогда сможет увидеть процент скаченных данных
//            //  .contentType(MediaType.parseMediaType("application/txt")) //еще один вариант описание данных, содержащихся в теле запроса или ответа. Данная информация помогает браузеру узнать с помощью какого приложения можно открыть данное содержание, и подсказывает пользователю открыть с помощью существующего приложения на их компьютере при завершении скачивания.
//                .contentType(mediaType)
//                .body(resource); //заполняем ответ телом
//
//        return responseEntity;


}