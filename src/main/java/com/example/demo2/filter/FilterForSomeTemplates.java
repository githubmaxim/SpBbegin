package com.example.demo2.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


/**
 * A class that creates a filter that will be triggered when clients only access specific URL patterns.
 *
 * @author Maxim
 * @version 1.0
 */
@Slf4j
public class FilterForSomeTemplates implements Filter {
    @Override
    public void init(FilterConfig filterconfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) throws IOException, ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

//        System.out.println("filter start。。。。。");
        log.info("Filter 2 for templates - /users/delete and /users/findAll. URI : {}, User Name : {}", req.getRequestURI(), req.getRemoteUser());
        filterchain.doFilter(request, response); //обязательная строка в конце
    }

    @Override
    public void destroy() {
    }
}
