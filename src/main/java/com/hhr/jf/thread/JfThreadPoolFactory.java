package com.hhr.jf.thread;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Harry
 * @Date: 2021/10/12 23:11
 * @Version 1.0
 */
public class JfThreadPoolFactory implements ThreadFactory {

    private AtomicInteger atomicInteger;
    private String threadPoolName;

    public JfThreadPoolFactory(String threadPoolName) {
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
