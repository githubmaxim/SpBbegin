package com.example.demo2.config;

import com.example.demo2.filter.FilterForSomeTemplates;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class RegistrationFilter {

    @Bean
    public FilterRegistrationBean<FilterForSomeTemplates> myFilter() {
        FilterRegistrationBean<FilterForSomeTemplates> filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new FilterForSomeTemplates());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/users/delete","/users/findAll"));
        return filterRegistrationBean;
    }
}
