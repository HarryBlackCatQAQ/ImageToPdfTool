package com.hhr.thread;

import javafx.application.Platform;

import java.util.concurrent.*;

/**
 * @Author: Harry
 * @Date: 2021/10/5 21:04
 * @Version 1.0
 */
public class MyThreadPool {
    private static MyThreadPool instance;
    private ExecutorService fixedThreadPool;


    /**
     * 单例模式
     * @return
     */
    private MyThreadPool(){
        fixedThreadPool = newFixedThreadPool(30);
    }

    public static MyThreadPool getInstance() {
        if (instance == null) {
            synchronized (MyThreadPool.class) {
                if (instance == null) {
                    instance = new MyThreadPool();
                }
            }
        }
        return instance;
    }

    private ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    public ExecutorService getFixedThreadPool() {
        return fixedThreadPool;
    }

    public void execute(Runnable runnable){
        this.fixedThreadPool.execute(runnable);
    }

    public void javaFxExecute(final Runnable runnable){
        this.fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(runnable);
            }
        });
    }
}
