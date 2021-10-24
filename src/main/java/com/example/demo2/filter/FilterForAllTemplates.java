package com.example.demo2.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
@Order(1)
@Slf4j
public class FilterForAllTemplates implements Filter {
    @Override
    public void init(FilterConfig filterconfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) throws IOException, ServletException, IOException {
        log.info("Filter 1 for req : {}", request.getRemoteAddr());
        filterchain.doFilter(request, response); //обязательная строка в конце
    }

    @Override
    public void destroy() {}
}




