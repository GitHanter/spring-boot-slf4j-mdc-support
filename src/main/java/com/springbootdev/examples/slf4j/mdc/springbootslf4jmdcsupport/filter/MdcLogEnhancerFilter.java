package com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.filter;

import com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.config.InterceptorConfig;
import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@Component
public class MdcLogEnhancerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        MDC.put(InterceptorConfig.X_REQUEST_ID,
                ((HttpServletRequest) servletRequest).getHeader(InterceptorConfig.X_REQUEST_ID));
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
