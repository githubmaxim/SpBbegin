package com.example.demo2.controller.admin;


import com.example.demo2.entity.registration.LogPas;
import com.example.demo2.service.admin.LogPasService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('ADMIN')") //это + еще в файле WebSecurityConfig.java нужно над классом дописать "@EnableGlobalMethodSecurity(prePostEnabled = true)" , для запуска механизма допуска к методам данного контроллера только с ролью "ADMIN'  при нашем "auth.jdbcAuthentication()" (вместо стандартных ".antMatchers("/admin/**").hasRole("ADMIN")" при "auth.inMemoryAuthentication()")
public class AdminController {
    private final LogPasService logPasService;

    @GetMapping("/findAll")
    public List<LogPas> findALLLogPas(){
        log.info("+++message by AdminController, method findALLLogPas+++");
        return logPasService.findAll();
    }

    @DeleteMapping("/delete/{id}")
//    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLogPas(@PathVariable Integer id){
        log.info("+++message by AdminController, method deleteLogPas+++");
        log.info("AdminController: Handling delete logpas request: " + id);
        logPasService.deleteLogPas(id);
        return ResponseEntity.status(200).build();
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> putLogPas(@PathVariable("id") Integer id, @RequestBody String role){
        log.info("+++message by AdminController, method putLogPas+++");
        log.info("AdminController: Handling put ID request: " + id);
        log.info("AdminController: Handling put ROLE request: " + role);

        return ResponseEntity.status(200).body(logPasService.putLogPas(id, role.substring(1))); //"substring()" говорит с какого символа (начиная с 0-го) нужно брать из String-ового поля
    }
}
