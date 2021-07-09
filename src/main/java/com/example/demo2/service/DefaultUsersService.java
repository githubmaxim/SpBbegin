package com.example.demo2.service;

import com.example.demo2.dto.UsersDto;
import com.example.demo2.entity.Users;
import com.example.demo2.exception.ValidationException;
import com.example.demo2.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    private ResponseEntity<?> validateExistLoginUserDto(UsersDto usersDto) throws ValidationException {
        String login = usersDto.getLogin();
        if (!isNull(findByLogin(usersDto.getLogin()))) {
            return ResponseEntity.status(404).body("Such login is exist");
        }
        return ResponseEntity.status(200).body("OK");
    }

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
        return usersRepository.findAll()
                .stream()
//                .map(usersConverter::fromUsersToUsersDto)
                .map(x -> usersConverter.fromUsersToUsersDto(x))
                .collect(Collectors.toList());
    }
}
