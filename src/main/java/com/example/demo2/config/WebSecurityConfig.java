package com.example.demo2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/registration").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/index.html", true)
                    .and()
                .logout()
                    .permitAll();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
//                .passwordEncoder(bcryptPasswordEndcoder())
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
