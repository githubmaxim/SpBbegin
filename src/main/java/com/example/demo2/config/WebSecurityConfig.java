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
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

//!!!Отключил защиту "csrf" т.к. не смог ее настроить на разарешения работы для методов POST,PUT,DELETE!!!
//Можно все передавать через разрешенный метод GET, но в нем вся инфа передается в URl, а не в теле запроса

/**
 * Spring security configuration class.
 *
 * @author Maxim
 * @version 1.0
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) //это + еще в файле AdminController.java нужно над классом дописать "@PreAuthorize("hasAuthority('ADMIN')")" , для запуска механизма допуска к методам данного контроллера только с ролью "ADMIN'  при нашем "auth.jdbcAuthentication()" (вместо стандартных ".antMatchers("/admin/**").hasRole("ADMIN")" при "auth.inMemoryAuthentication()")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    //Добавляем кодирование паролей
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new  BCryptPasswordEncoder(8);
    }
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This method is used for authorization.
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/registration").permitAll()
                    .anyRequest().authenticated()
                    .and()
//                .requiresChannel()
//                    .antMatchers("/**").requiresSecure()
//                    .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/maiin.html", true)
//                    .defaultSuccessUrl("/index.html", true)
                    .and()
                .logout()
                    .permitAll()
                .and()
                .csrf().disable();
    }

    /**
     * This method is used for authentication.
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("select username, password, active from logpas where username=?")
                .authoritiesByUsernameQuery("select lp.username, lpr.roles from logpas lp inner join logpass_role lpr on lp.id = lpr.logpass_id where lp.username=?");
    }

//    Метод позволяющий подгружать на html-страницы файлы из перечисленных папок, до прохождения аутентификации
    /**
     * This method allows you to upload files from the listed folders to html pages before passing authentication.
     * @param web
     */
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
