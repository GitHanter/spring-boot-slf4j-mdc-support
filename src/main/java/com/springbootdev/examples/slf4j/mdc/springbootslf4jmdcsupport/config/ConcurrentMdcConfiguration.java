package com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.config;

import com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.concurrent.MDCTaskSchedulerCustomizer;
import com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.concurrent.ThreadMDCUtils;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskDecorator;

@Configuration
@Profile("correct")
@AutoConfigureBefore({TaskSchedulingAutoConfiguration.class, TaskExecutionAutoConfiguration.class})
public class ConcurrentMdcConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TaskDecorator mdcTaskDecorator() {
        return ThreadMDCUtils::wrapWithMdcContext;
    }

    @Bean
    @ConditionalOnMissingBean
    public MDCTaskSchedulerCustomizer mdcTaskSchedulerCustomizer() {
        return new MDCTaskSchedulerCustomizer();
    }
}
