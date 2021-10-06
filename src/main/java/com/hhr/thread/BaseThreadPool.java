package com.hhr.thread;

import java.util.concurrent.*;

/**
 * @Author: Harry
 * @Date: 2021/10/6 21:43
 * @Version 1.0
 */
public abstract class BaseThreadPool {
    protected ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),threadFactory);
    }
}
