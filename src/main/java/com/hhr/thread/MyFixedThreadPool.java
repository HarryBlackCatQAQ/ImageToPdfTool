package com.hhr.thread;


import java.util.concurrent.*;

/**
 * @Author: Harry
 * @Date: 2021/10/5 21:04
 * @Version 1.0
 */
public class MyFixedThreadPool extends BaseThreadPool{
    private ExecutorService fixedThreadPool;

    /**
     * 单例模式 使用
     * @return
     */
    public MyFixedThreadPool(){
        fixedThreadPool = newCachedThreadPool(30,new MyThreadFactory("FixedThreadPool"));
    }

    public void execute(Runnable runnable){
        this.fixedThreadPool.execute(runnable);
    }


}
