package com.hhr.thread;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Harry
 * @Date: 2021/10/6 17:37
 * @Version 1.0
 */
public class MyThreadFactory implements ThreadFactory {

    private AtomicInteger atomicInteger;
    private String threadPoolName;

    public MyThreadFactory(String threadPoolName) {
        this.threadPoolName = threadPoolName;
        this.atomicInteger = new AtomicInteger(0);
    }

    @Override
    public Thread newThread(@NotNull Runnable r) {
        int counter = this.atomicInteger.incrementAndGet();
        StringBuilder threadName = new StringBuilder(threadPoolName);
        threadName.append("-thread-").append(counter);
        return new Thread(r,threadName.toString());
    }
}
