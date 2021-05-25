package com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.concurrent;

import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @see org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration
 */
public class MDCTaskSchedulerCustomizer implements TaskSchedulerCustomizer {

    @Override
    public void customize(ThreadPoolTaskScheduler taskScheduler) {
        taskScheduler.setThreadFactory(runnable ->
                taskScheduler.newThread(ThreadMDCUtils.wrapWithMdcContext(runnable)));
    }

}
