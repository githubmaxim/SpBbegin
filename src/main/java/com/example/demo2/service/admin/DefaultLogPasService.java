package com.example.demo2.service.admin;

import com.example.demo2.entity.product.Product;
import com.example.demo2.entity.registration.LogPas;
import com.example.demo2.entity.registration.Role;
import com.example.demo2.repository.admin.LogPasRepository;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
//@AllArgsConstructor
@Builder
@Slf4j
public class DefaultLogPasService implements LogPasService {

    private final LogPasConverter logPasConverter;
    private final LogPasRepository logPasRepository;

    @Override
    public String putLogPas( Integer id, String role) {
        log.info("!!!message by DefaultLogPasService, method putLogPas!!!");
        log.info("message by DefaultLogPasService, method putLogPas, role = "+ role);
        try {
            LogPas existLogPas = logPasRepository.findById(id).get(); //не находим "id" - уходим в "catch"

            if (role.equals("ADMIN")){
                existLogPas.setRoles(new HashSet<>(Arrays.asList(Role.ADMIN)));
            } else { existLogPas.setRoles(new HashSet<>(Arrays.asList(Role.USER)));}

            log.info("!!ogPas - ", existLogPas);
            System.out.println(existLogPas.getRoles() + "," + existLogPas.getUsername() + "," + existLogPas.getPassword() + "," + existLogPas.getActive());
            LogPas logPas1 = logPasRepository.save(existLogPas);
            return "OK";
        } catch (NoSuchElementException e) {
            return "id is absent";
        }
    }

    @Override
    public void deleteLogPas(Integer id) {
        log.info("!!!message by DefaultLogPasService, method deleteLogPas!!!");
        logPasRepository.deleteById(id);
    }

    @Override
    public List<LogPas> findAll() {
        log.info("!!!message by DefaultLogPasService, method findAll!!!");
                return logPasRepository.findAll();
//                .stream()
//                .map(x -> logPasConverter.fromLogPasToLogPasDto(x))
//                .collect(Collectors.toList());
    }

}
