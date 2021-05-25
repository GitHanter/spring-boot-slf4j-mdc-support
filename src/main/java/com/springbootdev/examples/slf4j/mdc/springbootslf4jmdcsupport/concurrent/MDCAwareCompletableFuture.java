package com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.concurrent;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * https://medium.com/asyncparadigm/logging-in-a-multithreaded-environment-and-with-completablefuture-construct-using-mdc-1c34c691cef0
 */
public class MDCAwareCompletableFuture<T> extends CompletableFuture<T> {

    public static final ExecutorService MDC_AWARE_ASYNC_POOL = new MDCAwareForkJoinPool();

    @Override
    public <U> CompletableFuture<U> newIncompleteFuture() {
        return new MDCAwareCompletableFuture<U>();
    }


    @Override
    public Executor defaultExecutor() {
        return MDC_AWARE_ASYNC_POOL;
    }

    public static <T> CompletionStage<T> getMDCAwareCompletionStage(CompletableFuture<T> future) {
        return new MDCAwareCompletableFuture<>()
                .completeAsync(() -> null)
                .thenCombineAsync(future, (aVoid, value) -> value);
    }

    public static <T> CompletionStage<T> getMDCHandledCompletionStage(CompletableFuture<T> future,
                                                                      Function<Throwable, T> throwableFunction) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return getMDCAwareCompletionStage(future)
                .handle((value, throwable) -> {
                    ThreadMDCUtils.setMDCContext(contextMap);
                    if (throwable != null) {
                        return throwableFunction.apply(throwable);
                    }
                    return value;
                });
    }
}
