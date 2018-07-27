package com.zhili.proxy;

import com.zhili.bean.RPCReq;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.Reflection;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/6/8.
 */
@Slf4j
public class ServerBizInvoker {

    public Object invoke(Object instance,RPCReq req)  throws Exception{
        Method methodImpl =getMethod(instance.getClass(),req.getMethod(),req.getParamTypes());
        return methodImpl.invoke(instance,req.getParams());
    }

    private Method getMethod(Class<?> clazz,String methodName,Class[] paramTypes) throws Exception{
        return clazz.getMethod(methodName,paramTypes);
    }
}
