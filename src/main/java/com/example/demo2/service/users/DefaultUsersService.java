package com.example.demo2.service.users;

import com.example.demo2.dto.users.UsersDto;
import com.example.demo2.entity.users.Users;
import com.example.demo2.repository.users.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultUsersService implements UsersService {

    private final UsersRepository usersRepository;
    private final UsersConverter usersConverter;



    @Override
    public ResponseEntity<?> saveUser(UsersDto usersDto) {
        log.info("!!!message by DefaultUserController, method saveUser!!!");
        if (!isNull(findByLogin(usersDto.getLogin()))) { //остальная валидация в проверочных аннотациях на самих переменных в классе "UserDto"
            return ResponseEntity.status(444).body("Such login is exist");
        } else {
            Users savedUser = usersRepository.save(usersConverter.fromUsersDtoToUsers(usersDto));
            return ResponseEntity.status(200).body("OK");
        }
    }
//Ниже используем в методе saveUser() валидацию, написанную в отдельном методе
    //    @Override
//    public UsersDto saveUser(UsersDto usersDto) throws ValidationException {
//        log.info("!!!message by DefaultUserController, method saveUser!!!");
//        validateNullUserDto(usersDto); //проходим валидацию, если нет - вылетаем на собственное исключение
//        Users savedUser = usersRepository.save(usersConverter.fromUsersDtoToUsers(usersDto));
//        return usersConverter.fromUsersToUsersDto(savedUser); //в ответе конвертируем "Users" обратно в "UsersDto", чтобы у клиентов небыло доступа непосредственно к нашей сущности
//    }

    //Метод валидации для вывода разных ответов в случае неправильного ввода значения "login" или всего "UserDto"
    //А так, обычно, нужно ставить проверочные аннотации на самих переменных в классе "UserDto" и потом в методах контроллера (принимающих данные от клиента на запись) перед @RequestBody ставить аннотацию @Valid
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
        log.info("!!!message by DefaultUserController, method deleteUser!!!");
        usersRepository.deleteById(userId);
    }

    @Override
    public UsersDto findByLogin(String login) {
        log.info("!!!message by DefaultUserController, method findByLogin!!!");
        Users users = usersRepository.findByLogin(login);
        if (users != null) {
            return usersConverter.fromUsersToUsersDto(users);
        }
        return null;
    }

    @Override
    public List<UsersDto> findAll() {

        log.info("!!!message by DefaultUserController, method findAll!!!");

        //Блок заменяющий в ответе клиенту каждое значение поля Email на строку "Information is not available".
        //Если попытаться написать этот блок через Stream API, то выйдет не сильно короче, но читаемость кода для меня получается как "квадратное колесо".
        //Stream API нормально пишется и читается когда работает с информацией 1-го уровня вложенности, а не 2-го (переменными классов, которые в свою очередь находятся в коллекциях).
        //А именно с информацией 2-го уровня в 90% случаев прийдется работать (с полями сущностей).
        List<UsersDto> usersDtoList = new  ArrayList<>();
        List<Users> usersList = usersRepository.findAll();
        for(Users user: usersList) {
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
}
