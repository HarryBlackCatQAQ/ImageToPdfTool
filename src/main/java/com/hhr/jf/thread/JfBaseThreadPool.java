package com.hhr.jf.thread;

import java.util.concurrent.*;

/**
 * @Author: Harry
 * @Date: 2021/10/6 21:43
 * @Version 1.0
 */
public abstract class JfBaseThreadPool {
    protected static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        Executors.newCachedThreadPool();
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),threadFactory);
    }

    protected static ExecutorService newCachedThreadPool(long keepAliveTime,ThreadFactory threadFactory){
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                keepAliveTime, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),threadFactory);
    }
}
