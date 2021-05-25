package com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.controller;

import com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.config.InterceptorConfig;
import com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.service.MDCThreadPoolExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WelcomeController
{
    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

    @Autowired
    private MDCThreadPoolExample mdcThreadPoolExample;

    @GetMapping("/welcome")
    public String welcomeMessage(HttpServletRequest request)
    {
        logger.info("inside the welcomeMessage");
        mdcThreadPoolExample.welcome(request.getHeader(InterceptorConfig.X_REQUEST_ID));
        return "welcome";
    }
}
