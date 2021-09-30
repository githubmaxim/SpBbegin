package com.example.demo2.service.users;

import com.example.demo2.dto.users.UsersDto;
import com.example.demo2.entity.users.Users;
import com.example.demo2.repository.users.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

/**
 * The Service file that serves the functions of the "UsersController" class.
 *
 * @author Maxim
 * @version 1.0
 */
@Service
@Builder
@Slf4j
public class DefaultUsersService implements UsersService {

    private final UsersRepository usersRepository;
    private final UsersConverter usersConverter;

    @Autowired
    private ServletContext servletContext;

    /**
     * Method displays all information from the "Users" entity
     * @return all data from entity "Users"
     */
    @Override
    public List<UsersDto> findAll() {

        log.info("!!!message by DefaultUserService, method findAll!!!");

        //Блок заменяющий в ответе клиенту каждое значение поля Email на строку "Information is not available".
        //Если попытаться написать этот блок через Stream API, то выйдет не сильно короче, но читаемость кода для меня при этом становится как "квадратное колесо".
        //Stream API нормально пишется и читается, когда работает с информацией 1-го уровня вложенности, а не 2-го/3/4 и т.д. (переменными классов, которые в свою очередь находятся в коллекциях).
        //А именно с информацией 2-го и т.д. уровнями в 90% случаев прийдется работать (с полями сущностей).
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

    /**
     * The method, according to the “login” field, finds and displays information about the user from the “Users” entity.
     * @param login user login
     * @return all user data from the "Users" entity
     */
    @Override
    public UsersDto findByLogin(String login) {
        log.info("!!!message by DefaultUserService, method findByLogin!!!");
        Users users = usersRepository.findByLogin(login);
        if (users != null) {
            log.info("!!!message by DefaultUserService, method findByLogin!!! users - " + users);
            UsersDto usersDto = usersConverter.fromUsersToUsersDto(users);
            log.info("!!!message by DefaultUserService, method findByLogin!!! usersDTO - " + usersDto);
            return usersDto;
        }
        log.info("!!!message by DefaultUserService, method findByLogin!!! - NULL");
        return null;
    }

    /**
     * Method removes information about user from the “Users” entity
     * @param userId identifying line number to be removed from entity "Users"
     */
    @Override
    public void deleteUser(Integer userId) {
        log.info("!!!message by DefaultUserService, method deleteUser!!!");
        usersRepository.deleteById(userId);
    }

    /**
     * Method saves information about user in the “Users” entity
     * @param usersDto DTO file object for the Users entity
     */
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







}