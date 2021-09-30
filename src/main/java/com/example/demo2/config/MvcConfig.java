package com.example.demo2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Short(fast) Controllers are created immediately in the Registrar
 *
 * @author Maxim
 * @version 1.0
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * This method is used to open the "login.html" page when navigating to "/ login".
     * @param registry - A class object that helps with registering simple automated controllers pre-configured with status code and / or view.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
}

