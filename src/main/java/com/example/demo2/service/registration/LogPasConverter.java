package com.example.demo2.service.registration;

import com.example.demo2.dto.registration.LogPasDto;
import com.example.demo2.entity.registration.LogPas;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * File for converting the entity class “LogPas” to the class “LogPasDto” and vice versa.
 *
 * @author Maxim
 * @version 1.0
 */
@Component
@NoArgsConstructor
@Builder
public class LogPasConverter {

    /**
     * A method that converts the "LogPasDto" class into the "LogPas" entity class
     * @param logPasDto object of the class "LogPasDto"
     * @return object of the entity class "LogPas"
     */
    public LogPas fromLogPasDtoToLogPas(LogPasDto logPasDto){
        return LogPas.builder()
                .id(logPasDto.getId())
                .username(logPasDto.getUsername())
                .password(logPasDto.getPassword())
                .active(logPasDto.getActive())
                .roles(logPasDto.getRoles())
                .build();
    }

    /**
     *  A method that converts the "LogPas" entity class into the "LogPasDto" class
     * @param logPas object of the entity class "LogPas"
     * @return object of the class "LogPasDto"
     */
    public LogPasDto fromLogPasToLogPasDto(LogPas logPas) {
        return LogPasDto.builder()
                .id(logPas.getId())
                .username(logPas.getUsername())
                .active(logPas.getActive())
                .roles(logPas.getRoles())
                .build();
    }
}
