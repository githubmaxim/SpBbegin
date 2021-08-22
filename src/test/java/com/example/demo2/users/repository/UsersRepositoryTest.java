package com.example.demo2.users.repository;

import com.example.demo2.entity.users.Users;
import com.example.demo2.repository.users.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static com.example.demo2.users.prototype.UsersPrototypeForUnitTest.aUser;

@DataJpaTest
@TestPropertySource(locations = "classpath:myTestApplication.properties") //подгружает не стандартный "application.properties",
// а необходимый "myTestApplication.properties". В "myTestApplication.properties" для тестирования без обращения к реальной
// (MySQL) БД создаем подключение к внутренней "H2" БД. Для того, чтобы это сработало необходимо раскомментировать или
// создать (если нет) в "pom.xml" блок подключающий БД "H2".

class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    void findByLogin() {
        usersRepository.save(aUser());
        Users foundUser = usersRepository.findByLogin(aUser().getLogin());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getName()).isEqualTo(aUser().getName());
    }
}
