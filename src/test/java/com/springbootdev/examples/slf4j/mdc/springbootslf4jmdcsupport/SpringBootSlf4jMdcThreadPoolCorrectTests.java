package com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport;

import com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.config.InterceptorConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("correct")
class SpringBootSlf4jMdcThreadPoolCorrectTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @RepeatedTest(10)
    void contextLoads() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture.allOf(IntStream.rangeClosed(1, 10).mapToObj(index -> {
            final HttpHeaders headers = new HttpHeaders();
            headers.set(InterceptorConfig.X_REQUEST_ID, InterceptorConfig.X_REQUEST_ID + index);
            final HttpEntity<Void> entity = new HttpEntity<>(headers);
            return CompletableFuture.supplyAsync(() -> restTemplate.exchange("http://localhost:8080/welcome",
                    HttpMethod.GET, entity, Void.class), executorService);
        }).toArray(CompletableFuture[]::new)).join();
        Assertions.assertNull(MDC.get(InterceptorConfig.X_REQUEST_ID));
        executorService.shutdown();
    }


}