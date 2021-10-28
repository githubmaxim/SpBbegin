package com.example.demo2.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * A class that creates a filter that is triggered when clients access any URLs.
 *
 * @author Maxim
 * @version 1.0
 */
@Component
@Order(1) //если будет несколько фильтров, то этот будет выполнен первым
@Slf4j
public class FilterForAllTemplates implements Filter {
    @Override
    public void init(FilterConfig filterconfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) throws IOException, ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;

        log.info("Filter 1 for all templates URL : {}", req.getRequestURL());
        filterchain.doFilter(request, response); //обязательная строка в конце
    }

    @Override
    public void destroy() {}
}




