package com.example.demo2.controller.admin;


import com.example.demo2.dto.registration.LogPasDto;
import com.example.demo2.service.admin.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The Controller file that serves the functions of the "admin.js" file used on the "admin.html" page.
 *
 * @author Maxim
 * @version 1.0
 */
@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('ADMIN')") //это + еще в файле WebSecurityConfig.java нужно над классом дописать "@EnableGlobalMethodSecurity(prePostEnabled = true)" , для запуска механизма допуска к методам данного контроллера только с ролью "ADMIN'  при нашем "auth.jdbcAuthentication()" (вместо стандартных ".antMatchers("/admin/**").hasRole("ADMIN")" при "auth.inMemoryAuthentication()").
                                       // Эта аннотация также может ставиться не для всего класса, а только над нужными методами.
public class AdminController {

    private final AdminService adminService;

    /**
     * This method is used to run the "findAll()" class "DefaultAdminService"(interface "AdminService") method
     * @return all data from entity "LogPas"
     */
    @GetMapping("/findAll")
    public List<LogPasDto> findALLLogPas(){
        log.info("+++message by AdminController, method findALLLogPas+++");
        return adminService.findAll();
    }

    /**
     * This method is used to run the "deleteLogPas()" class "DefaultAdminService"(interface "AdminService") method
     * @param id identifying line number to be removed from entity "LogPas"
     * @return status "200"
     */
    @DeleteMapping("/delete/{id}")
//    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLogPas(@PathVariable Integer id){
        log.info("+++message by AdminController, method deleteLogPas+++");
        log.info("AdminController: Handling delete logpas request: " + id);
        adminService.deleteLogPas(id);
        return ResponseEntity.status(200).build();
    }

    /**
     * This method is used to run the "putLogPas()" class "DefaultAdminService"(interface "AdminService") method
     * @param id line identification number to find on entity "LogPas"
     * @param role value to which you want to change the "role" parameter in the "logpas_role" table (created in the "LogPas" entity)
     * @return status "200" and "OK"|"id is absent"
     */
    @PutMapping("/put/{id}")
    public ResponseEntity<?> putLogPas(@PathVariable("id") Integer id, @RequestBody String role){
        log.info("+++message by AdminController, method putLogPas+++");
        log.info("AdminController: Handling put ID request: " + id);
        log.info("AdminController: Handling put ROLE request: " + role);

        return ResponseEntity.status(200).body(adminService.putLogPas(id, role.substring(1))); //"substring()" говорит с какого символа (начиная с 0-го) нужно брать из String-ового поля
    }
}
