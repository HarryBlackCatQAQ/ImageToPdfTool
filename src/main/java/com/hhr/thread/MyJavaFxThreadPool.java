package com.hhr.thread;

import javafx.application.Platform;

import java.util.concurrent.ExecutorService;

/**
 * @Author: Harry
 * @Date: 2021/10/6 21:42
 * @Version 1.0
 */
public class MyJavaFxThreadPool extends BaseThreadPool{
    private ExecutorService javaFxThreadPool;

    /**
     * 单例模式 使用
     * @return
     */
    public MyJavaFxThreadPool(){
        javaFxThreadPool = newFixedThreadPool(10,new MyThreadFactory("javaFxThreadPool"));
    }

    public void javaFxExecute(final Runnable runnable){
        this.javaFxThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(runnable);
            }
        });
    }
}
