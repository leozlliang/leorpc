package com.zhili.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2018/6/8.
 */
public class ProxyFactory {
    public static <T>T proxy(Class<T> service, InvocationHandler handler){
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{service}, handler);
    }
}
