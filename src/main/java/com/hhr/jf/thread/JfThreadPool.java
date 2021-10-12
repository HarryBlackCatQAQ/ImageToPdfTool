package com.hhr.jf.thread;

import java.util.concurrent.ExecutorService;

/**
 * @Author: Harry
 * @Date: 2021/10/12 23:10
 * @Version 1.0
 */
public class JfThreadPool extends JfBaseThreadPool{
    private final static ExecutorService jfThreadPool = newCachedThreadPool(20,new JfThreadPoolFactory("JfThreadPool"));

    public static void execute(Runnable runnable){
        jfThreadPool.execute(runnable);
    }
}
