package com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.concurrent;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public final class ThreadMDCUtils {

    private ThreadMDCUtils() {
    }

    public static <T> Callable<T> wrapWithMdcContext(Callable<T> task) {
        //save the current MDC context
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                setMDCContext(contextMap);
                return task.call();
            } finally {
                // once the task is complete, clear MDC
                MDC.clear();
            }
        };
    }

    public static Runnable wrapWithMdcContext(Runnable task) {
        //save the current MDC context
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                setMDCContext(contextMap);
                task.run();
            } finally {
                // once the task is complete, clear MDC
                MDC.clear();
            }
        };
    }

    public static <U> Supplier<U> wrapWithMdcContext(Supplier<U> supplier) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                setMDCContext(contextMap);
                return supplier.get();
            } finally {
                MDC.clear();
            }
        };
    }

    public static void setMDCContext(Map<String, String> contextMap) {
        MDC.clear();
        if (contextMap != null) {
            MDC.setContextMap(contextMap);
        }
    }
}
