package com.example.demo2.users.service;

import com.example.demo2.dto.users.UsersDto;
import com.example.demo2.exception.users.ValidationException;
import com.example.demo2.repository.users.UsersRepository;
import com.example.demo2.service.users.DefaultUsersService;
import com.example.demo2.service.users.UsersConverter;
import com.example.demo2.service.users.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.example.demo2.users.prototype.UsersPrototypeForUnitTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//Заглушку тут мы будем делать только для класса "UsersRepository".
//А "userConverter" создаем только для возможности создать "userService".
public class DefaultUserServiceTest {

    private UsersRepository usersRepository;
    private UsersConverter usersConverter;
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        usersRepository = mock(UsersRepository.class);
        usersConverter = new UsersConverter();
        usersService = DefaultUsersService.builder().usersRepository(usersRepository).usersConverter(usersConverter).build();
    }

    @Test
    void findAll() {
        when(usersRepository.findAll()).thenReturn(Arrays.asList(aUser(), aUser2()));
        List<UsersDto> usersDtoList = usersService.findAll();
        assertThat(usersDtoList).isNotNull();
        assertThat(usersDtoList.get(0).getEmail()).isEqualTo("Information is not available");
        assertThat(usersDtoList.get(1).getName()).isEqualTo("test_name2");
    }

    @Test
    void findByLogin() {
        when(usersRepository.findByLogin(eq("test_login"))).thenReturn(aUser());
        UsersDto foundUser = usersService.findByLogin("test_login");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getLogin()).isEqualTo("test_login");
    }

    //Проверка, применяемого собственного исключения, для заккоментированного блока
    // кода в методе saveUser() класса "DefaultUsersService"
//    @Test
//    void saveUserThrowsValidationExceptionWhenLoginIsNull() {
//        UsersDto usersDto = aUserDTO();
//        usersDto.setLogin("");
//        assertThrows(ValidationException.class,
//                () -> usersService.saveUser(usersDto),
//                "Login is empty");
//    }


//Следующие два теста не нужны, т.к. в них применяются только встроенные шаблонные методы,
// которые проверять нет смысла
    @Test
    void deleteUser() {}
    @Test
    void saveUser() {}

}
