package com.zhili.handler;

import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2018/6/8.
 */
@Setter
public class HandlerSelector {
    private static  ConcurrentHashMap<String,Object> HANDLER_MAP = new ConcurrentHashMap<>();

    public static void add(String serviceName,Object instance){
        HANDLER_MAP.put(serviceName,instance);
    }

    public static Object select(String serviceName){
        return HANDLER_MAP.get(serviceName);
    }

}
