package com.example.demo2.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

//@Component
//@Order(2)
@Slf4j
public class FilterForSomeTemplates implements Filter {
    @Override
    public void init(FilterConfig filterconfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) throws IOException, ServletException, IOException {
//        System.out.println("filter start。。。。。");
        log.info("Filter 2 for templates - \"/users/delete\" and \"/users/findAll\". ServerName : {}", request.getServerName());
        filterchain.doFilter(request, response); //обязательная строка в конце
    }

    @Override
    public void destroy() {
    }
}
