package com.hhr.jf.thread;

import com.hhr.jf.SingletonFactory;
import com.hhr.jf.annotation.JfController;
import com.hhr.jf.util.LogUtil;
import javafx.fxml.FXMLLoader;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @Author: Harry
 * @Date: 2021/10/13 18:11
 * @Version 1.0
 */
@Slf4j
public class JfBeanInjectThread implements Runnable{
    private boolean isWeak;
    private Class<?> clazz;
    private FXMLLoader fxmlLoader;
    private Field field;

    public JfBeanInjectThread(boolean isWeak, Class<?> clazz, FXMLLoader fxmlLoader, Field field) {
        this.isWeak = isWeak;
        this.clazz = clazz;
        this.fxmlLoader = fxmlLoader;
        this.field = field;
    }

    @Override
    public void run() {
        Object instance;
        if(isWeak){
            while (!SingletonFactory.getWeakReferenceInstance().containsKey(clazz)){}
            instance = SingletonFactory.getInstanceByIsWeak(clazz,isWeak);
        }
        else{
            while (!SingletonFactory.getInstance().containsKey(clazz)){}
            instance = SingletonFactory.getInstanceByIsWeak(clazz,isWeak);
        }

        try {
            field.set(instance,fxmlLoader.getController());
            log.info("多线程注入 " + LogUtil.getClassSimpleName(instance) + " 中的 " + LogUtil.getClassSimpleName(fxmlLoader.getController()));
        } catch (IllegalAccessException e) {
            log.error("JfAutowired 注入失败:" + fxmlLoader.getController().getClass().getName() + "下的" + field.getType()
                    + ",注入类型为" + JfController.class + " error in:parseJfControllerAnnotationInSomeJfComponentAnnotationAndJfServiceAnnotation\n"
                    + "  error:" + e.getMessage());
        }
    }
}
