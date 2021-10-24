package com.example.demo2.config;

import com.example.demo2.filter.FilterForSomeTemplates;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


/**
 * A class for registering filters that will only be applied to specific templates.
 *
 * @author Maxim
 * @version 1.0
 */
@Configuration
public class RegistrationFilter {

    /**
     * The method that registers the filter “FilterForAllTemplates.java” for the templates “/ users / delete” and “/ users / findAll”.
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<FilterForSomeTemplates> myFilter() {
        FilterRegistrationBean<FilterForSomeTemplates> filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new FilterForSomeTemplates());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/users/delete","/users/findAll"));
        return filterRegistrationBean;
    }
}
