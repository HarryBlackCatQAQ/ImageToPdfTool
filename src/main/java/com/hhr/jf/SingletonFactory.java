package com.hhr.jf;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 　@description: 单例工厂
 *
 */
public class SingletonFactory {
    @SuppressWarnings("rawtypes")
    private static final Map<Class,Object> instance = new ConcurrentHashMap<Class, Object>();
    @SuppressWarnings("rawtypes")
    private static final Map<Class,WeakReference<Object>> weakReferenceInstance = new ConcurrentHashMap<Class, WeakReference<Object>>();
 
    /**
     * 创建可不被回收的单例模式,当没有对象引用，单例对象将被gc掉
     * @param className
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public static <E> E getInstance(Class<E> className){
        Object instace = instance.get(className);
        if(instace==null){
            synchronized (SingletonFactory.class) {
                instace = instance.get(className);
                if(instace==null){
                    try {
                        instace = className.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    instance.put(className,instace);
                }
            }
        }
        return (E)instace;
    }
 
    /**
     * 创建可回收的单例模式,当没有对象引用，单例对象将被gc掉
     *
     * @param className
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public static <E> E getWeakInstance(Class<E> className) {
        WeakReference<Object> reference = weakReferenceInstance.get(className);
        Object instace =reference==null?null:reference.get();
        if(instace==null){
            synchronized (SingletonFactory.class) {
                reference = weakReferenceInstance.get(className);
                instace =reference==null?null:reference.get();
                if(instace==null){
                    try {
                        instace = className.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    weakReferenceInstance.put(className,new WeakReference<Object>(instace));
                }
            }
        }
        return (E)instace;
    }


    public static void putInstance(Object o){
        if(!instance.containsKey(o.getClass())){
            instance.put(o.getClass(),o);
        }
    }

    public static void putWeakInstance(Object o){
        if(!weakReferenceInstance.containsKey(o.getClass())){
            weakReferenceInstance.put(o.getClass(),new WeakReference<Object>(o));
        }
    }

    public static Map<Class, Object> getInstance() {
        return instance;
    }

    public static Map<Class, WeakReference<Object>> getWeakReferenceInstance() {
        return weakReferenceInstance;
    }
}