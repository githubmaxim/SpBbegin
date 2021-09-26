package com.example.demo2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

//!!!Отключил защиту "csrf" т.к. не смог ее настроить на разарешения работы для методов POST,PUT,DELETE!!!
//Можно все передавать через разрешенный метод GET, но в нем вся инфа передается в URl, а не в теле запроса

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) //это + еще в файле AdminController.java нужно над классом дописать "@PreAuthorize("hasAuthority('ADMIN')")" , для запуска механизма допуска к методам данного контроллера только с ролью "ADMIN'  при нашем "auth.jdbcAuthentication()" (вместо стандартных ".antMatchers("/admin/**").hasRole("ADMIN")" при "auth.inMemoryAuthentication()")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new  BCryptPasswordEncoder(8);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                    .antMatchers("/admin/put/**").hasRole("ADMIN")
                    .antMatchers("/", "/registration").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/index.html", true)
                    .and()
                .logout()
                    .permitAll()
                .and()
                .csrf().disable();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("select username, password, active from logpas where username=?")
                .authoritiesByUsernameQuery("select lp.username, lpr.roles from logpas lp inner join logpass_role lpr on lp.id = lpr.logpass_id where lp.username=?");
    }

//    Блок позволяющий подгружать на html-страницы, до прохождения аутентификации, файлы из этих папок
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(

                // статика
                "/css/**",
                "/js/**",
                "/fonts/**",
                "/images/**"
        );
    }
}
