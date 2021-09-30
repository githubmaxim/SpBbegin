package com.example.demo2.service.admin;

import com.example.demo2.dto.registration.LogPasDto;
import com.example.demo2.entity.registration.LogPas;
import com.example.demo2.entity.registration.Role;
import com.example.demo2.repository.admin.LogPasRepository;
import com.example.demo2.service.registration.LogPasConverter;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * The Service file that serves the functions of the "AdminController" class.
 *
 * @author Maxim
 * @version 1.0
 */
@Service
//@AllArgsConstructor
@Builder
@Slf4j
public class DefaultAdminService implements AdminService {

    private final LogPasConverter logPasConverter;
    private final LogPasRepository logPasRepository;

    /**
     * The method changes the roles of users in the "LogPas" entity
     * @param id line identification number to find on entity "LogPas"
     * @param role value to which you want to change the "role" parameter in the "logpas_role" table (created in the "LogPas" entity)
     * @return text "OK"/"id is absent"
     */
    @Override
    public String putLogPas( Integer id, String role) {
        log.info("!!!message by DefaultAdminService, method putLogPas!!!");
        log.info("message by DefaultAdminService, method putLogPas, role = "+ role);
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

    /**
     * The method removes the user credentials from the "LogPas" entity
     * @param id identifying line number to be removed from entity "LogPas"
     */
    @Override
    public void deleteLogPas(Integer id) {
        log.info("!!!message by DefaultAdminService, method deleteLogPas!!!");
        logPasRepository.deleteById(id);
    }

    /**
     * The method finds the credentials of all users in the "LogPas" entity
     * @return all data from entity "LogPas"
     */
    @Override
    public List<LogPasDto> findAll() {
        log.info("!!!message by DefaultAdminService, method findAll!!!");
                return logPasRepository.findAll()
                .stream()
                .map(x -> logPasConverter.fromLogPasToLogPasDto(x))
                .collect(Collectors.toList());
    }

}
