package com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.service;

import com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.config.InterceptorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Profile("wrong")
public class MDCThreadPoolWrongExample implements MDCThreadPoolExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(MDCThreadPoolWrongExample.class);

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void welcome(String expectedMdcValue) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            Assert.isTrue(expectedMdcValue.equals(MDC.get(InterceptorConfig.X_REQUEST_ID)), Thread.currentThread().getName());
            LOGGER.info("Run inside runtime create ThreadPool, Expected: {}; Actual: {}; TN: {}",
                    expectedMdcValue, MDC.get(InterceptorConfig.X_REQUEST_ID), Thread.currentThread().getName());
        });

        CompletableFuture.runAsync(() -> {
            Assert.isTrue(expectedMdcValue.equals(MDC.get(InterceptorConfig.X_REQUEST_ID)), Thread.currentThread().getName());
            LOGGER.info("Run inside ForkJoinPool, Expected: {}; Actual: {}; TN: {}",
                    expectedMdcValue, MDC.get(InterceptorConfig.X_REQUEST_ID), Thread.currentThread().getName());
        })
                .thenRunAsync(() -> LOGGER.info("Another Run inside ForkJoinPool, Expected: {}; Actual: {}; TN: {}",
                        expectedMdcValue, MDC.get(InterceptorConfig.X_REQUEST_ID), Thread.currentThread().getName()))
                .join();

        threadPoolTaskExecutor.submit(() -> {
            Assert.isTrue(expectedMdcValue.equals(MDC.get(InterceptorConfig.X_REQUEST_ID)), Thread.currentThread().getName());
            LOGGER.info("Run inside pre-configured ThreadPool, Expected: {}; Actual: {}; TN: {}",
                    expectedMdcValue, MDC.get(InterceptorConfig.X_REQUEST_ID), Thread.currentThread().getName());
        });
        try {
            //Let main Thread Sleep 1 seconds to make ThreadPool thread return to pool
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error(e.getMessage(), e);
        }
        executorService.shutdown();
    }
}
