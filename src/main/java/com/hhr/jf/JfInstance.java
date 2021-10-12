package com.hhr.jf;

import com.hhr.jf.annotation.JfAutowired;
import com.hhr.jf.annotation.JfComponent;
import com.hhr.jf.annotation.JfController;
import com.hhr.jf.annotation.JfService;
import com.hhr.jf.thread.JfThreadPool;
import javafx.fxml.FXMLLoader;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @Author: Harry
 * @Date: 2021/10/12 7:27
 * @Version 1.0
 */
@Slf4j
public class JfInstance {
    private static String packagePath;

    /**
     * 对JfController注解的类 进行注入给单例工厂管理
     */
    public static void parseJfControllerAnnotation(FXMLLoader fxmlLoader,String packagePath){
       if(packagePath != null){
           JfInstance.packagePath = packagePath;
       }

        JfController jfController = fxmlLoader.getController().getClass().getAnnotation(JfController.class);
        if(jfController != null){
            //注入 JfController
            SingletonFactory.putInstance(fxmlLoader.getController());
            log.info(fxmlLoader.<Integer>getController() + " 注入");
            //注入 JfController 中的 JfAutowired
            parseJfAutowiredAnnotation(fxmlLoader);

//            parseJfControllerAnnotationInSomeJfComponentAnnotation(fxmlLoader);
            parseJfControllerAnnotationInSomeJfComponentAnnotationAndJfServiceAnnotation(fxmlLoader);
        }
    }

    private static void parseJfControllerAnnotationInSomeJfComponentAnnotationAndJfServiceAnnotation(final FXMLLoader fxmlLoader){
        List<String> classes = JfScan.getClassesList(JfInstance.packagePath);
        for(String classString : classes){
            try {
                final Class<?> clazz = Class.forName(classString);

                JfComponent jfComponent = clazz.getAnnotation(JfComponent.class);
                JfService jfService = clazz.getAnnotation(JfService.class);

                if(jfComponent != null || jfService != null){
                    Field[] fields = getClassFields(clazz);
                    Class<?> fxmlControllerClass = fxmlLoader.getController().getClass();
                    for(final Field field : fields){
                        JfAutowired jfAutowired = field.getAnnotation(JfAutowired.class);
                        if(jfAutowired != null && field.getType().equals(fxmlControllerClass)){
                            field.setAccessible(true);

                            final boolean isWeak;
                            if(jfService != null){
                                isWeak = false;
                            }
                            else{
                                isWeak = jfComponent.weakReference();
                            }

                            JfThreadPool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    Object instance;
                                    if(isWeak){
                                       while (!SingletonFactory.getWeakReferenceInstance().containsKey(clazz)){}
                                       instance = getInstanceByIsWeak(clazz,isWeak);
                                    }
                                    else{
                                        while (!SingletonFactory.getInstance().containsKey(clazz)){}
                                        instance = getInstanceByIsWeak(clazz,isWeak);
                                    }

                                    try {
                                        field.set(instance,fxmlLoader.getController());
                                        log.info(instance + " 多线程注入 " + fxmlLoader.getController() + " " + Thread.currentThread().getName());
                                    } catch (IllegalAccessException e) {
                                        log.error("JfAutowired 注入失败:" + fxmlLoader.getController().getClass().getName() + "下的" + field.getType()
                                                + ",注入类型为" + JfController.class + " error in:parseJfControllerAnnotationInSomeJfComponentAnnotationAndJfServiceAnnotation\n"
                                                + "  error:" + e.getMessage());
                                    }
                                }
                            });
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
//                System.err.println(classString + "不是类!");
            }
        }
    }

    /**
     * 对JfController 中的 JfAutowired注解进行注入
     */
    private static void parseJfAutowiredAnnotation(FXMLLoader fxmlLoader){
        Field[] fields = getClassFieldsByObject(fxmlLoader.getController());

        for(Field field : fields){
            JfAutowired jfAutowired = field.getAnnotation(JfAutowired.class);

            if(jfAutowired != null){
                Class<?> fieldClass = field.getType();
                Annotation[] annotations = fieldClass.getAnnotations();
                for(Annotation annotation : annotations){
                    //对 属性中的class 带有JfComponent 或者 JfService 才注入 否注 JfAutowired注解失效
                    if(annotation.annotationType().equals(JfComponent.class) || annotation.annotationType().equals(JfService.class)){

                        field.setAccessible(true);

                        Object instance;
                        if(annotation.annotationType().equals(JfComponent.class)){
                            JfComponent jfComponent = (JfComponent) annotation;
                            instance = getInstanceByIsWeak(field.getType(),jfComponent.weakReference());
                        }
                        else{
                            instance = SingletonFactory.getInstance(field.getType());
                        }


                        try {
                            //JfAutowired 注入
                            field.set(fxmlLoader.getController(),instance);
                            log.info(fxmlLoader.getController() + " 注入 " + instance);
                        } catch (IllegalAccessException e) {
                            log.error("JfAutowired 注入失败:" + fxmlLoader.getController().getClass().getName() + "下的" + field.getType()
                                    + ",注入类型为" + annotation.annotationType() + " error in:parseJfAutowiredAnnotation\n"
                                    + "  error:" + e.getMessage());
                        }

                        //对JfComponent 和 JfService 中有 对含有该JfController注解所带类的属性也进行注入 JfAutowired注入
                        parseJfAutowiredAnnotationInJfServiceAnnotationAndJfComponentAnnotation(fxmlLoader,instance);
                    }
                }
            }
        }
    }

    /**
     * 对JfComponent 和 JfService 中含有已经load的JfController 进行注入
     */
    private static void parseJfAutowiredAnnotationInJfServiceAnnotationAndJfComponentAnnotation(FXMLLoader fxmlLoader,Object instance){
        Field[] fields = getClassFieldsByObject(instance);
        Class<?> fxmlControllerClass = fxmlLoader.getController().getClass();

        for(Field field : fields){
            field.setAccessible(true);
            JfAutowired jfAutowired = field.getAnnotation(JfAutowired.class);
            if(jfAutowired != null && field.getType().equals(fxmlControllerClass)){
                try {
                    field.set(instance,fxmlLoader.getController());
                    log.info(instance + " 注入 " + fxmlLoader.getController());
                } catch (IllegalAccessException e) {
                    log.error("JfAutowired 注入失败:" + fxmlLoader.getController().getClass().getName() + "下的" + field.getType()
                            + ",注入类型为" + JfController.class + " error in:parseJfAutowiredAnnotationInJfServiceAnnotationAndJfComponentAnnotation\n"
                            + "  error:" + e.getMessage());
                }
            }
        }
    }


    /**
     * 获取该类中的属性
     * @param o 该类的实体类
     * @return 属性列表
     */
    private static Field[] getClassFieldsByObject(Object o){
        return getClassFields(o.getClass());
    }

    /**
     * 获取该类中的属性
     * @param clazz 该类的class
     * @return 属性列表
     */
    private static Field[] getClassFields(Class<?> clazz){
        return clazz.getDeclaredFields();
    }

    /**
     * 根据是否weak获取单例工厂中的实例
     */
    private static Object getInstanceByIsWeak(Class<?> clazz,boolean isWeak){
        if(isWeak){
            return SingletonFactory.getWeakInstance(clazz);
        }
        else {
            return SingletonFactory.getInstance(clazz);
        }
    }

}
