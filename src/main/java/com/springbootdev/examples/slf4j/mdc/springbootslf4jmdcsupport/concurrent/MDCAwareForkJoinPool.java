package com.springbootdev.examples.slf4j.mdc.springbootslf4jmdcsupport.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class MDCAwareForkJoinPool extends ForkJoinPool {

    @Override
    public <T> ForkJoinTask<T> submit(Callable<T> task) {
        return super.submit(ThreadMDCUtils.wrapWithMdcContext(task));
    }

    @Override
    public <T> ForkJoinTask<T> submit(Runnable task, T result) {
        return super.submit(ThreadMDCUtils.wrapWithMdcContext(task), result);
    }

    @Override
    public ForkJoinTask<?> submit(Runnable task) {
        return super.submit(ThreadMDCUtils.wrapWithMdcContext(task));
    }

    @Override
    public void execute(Runnable task) {
        super.execute(ThreadMDCUtils.wrapWithMdcContext(task));
    }
}
