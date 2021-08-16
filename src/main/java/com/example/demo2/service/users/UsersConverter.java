package com.example.demo2.service.users;

import com.example.demo2.dto.users.UsersDto;
import com.example.demo2.entity.users.Users;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Builder
public class UsersConverter {

    public Users fromUsersDtoToUsers(UsersDto usersDto) { //обычное заполнение, не через сеттеры
        Users users = new Users();
        users.setId(usersDto.getId());
        users.setEmail(usersDto.getEmail());
        users.setName(usersDto.getName());
        users.setLogin(usersDto.getLogin());
        users.setUniversities(usersDto.getUniversities());
        return users;
    }

    public UsersDto fromUsersToUsersDto(Users users) { //заполнение через Builder
        return UsersDto.builder()
                .id(users.getId())
                .email(users.getEmail())
                .login(users.getLogin())
                .name(users.getName())
                .universities(users.getUniversities()) //т.к. тут методы get/set созданы Lombock, то при наличаи второго
                // уровня вложения сущностей друг в друга, при создании этой переменной зачем-то создает, к уже
                // существующим в ней переменным, дополнительную переменную, в которую вкладывает полное значение всего
                // создаваемого объекта. При последующей выборке "на клиенте" необходимых параметров это не мешает,
                // просто получается отправляется лишний объем информации.
                .build();
    }
}
