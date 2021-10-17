package com.example.demo2.admin.prototype;

import com.example.demo2.dto.registration.LogPasDto;
import com.example.demo2.entity.registration.LogPas;

public class AdminPrtotypeFofUnitTest {

    //при Unit-тестировании при создании объекта задавать нужно все поля включая "id"

    public static LogPas aLogPas(){
        return LogPas.builder()
                .id(1)
                .username("test_name")
                .password("password")
                .active(true)
                .build();
    }
    public static LogPas aLogPas2(){
        return LogPas.builder()
                .id(2)
                .username("test_name2")
                .password("password2")
                .active(true)
                .build();
    }



    public static LogPasDto aLogPasDto(){
        return LogPasDto.builder()
                .id(1)
                .username("test_name")
                .password("test_password")
                .active(true)
                .build();
    }
}
