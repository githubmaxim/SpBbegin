package com.example.demo2.service.admin;

import com.example.demo2.dto.registration.LogPasDto;
import com.example.demo2.entity.registration.LogPas;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Builder
public class LogPasConverter {
    public LogPas fromLogPasDtoToLogPas(LogPasDto logPasDto){
        return LogPas.builder()
                .id(logPasDto.getId())
                .username(logPasDto.getUsername())
                .password(logPasDto.getPassword())
                .active(logPasDto.getActive())
                .roles(logPasDto.getRoles())
                .build();
    }

    public LogPasDto fromLogPasToLogPasDto(LogPas logPas) {
        return LogPasDto.builder()
                .id(logPas.getId())
                .username(logPas.getUsername())
                .active(logPas.getActive())
                .roles(logPas.getRoles())
                .build();
    }
}
