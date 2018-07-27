package com.zhili.spring.client;

import com.zhili.domain.RpcClientConfig;
import com.zhili.proxy.RpcClientProxy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import utils.ProxyFactory;

import java.lang.reflect.InvocationHandler;

/**
 * Created by Administrator on 2018/6/8.
 */


public class RpcClientFactoryBean<T>  implements FactoryBean<T> {
    private InvocationHandler invocationHandler;
    private Class<T> proxyInterface;


    public InvocationHandler getInvocationHandler() {
        return invocationHandler;
    }

    public void setInvocationHandler(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    public Class<T> getProxyInterface() {
        return proxyInterface;
    }

    public void setProxyInterface(Class<T> proxyInterface) {
        this.proxyInterface = proxyInterface;
    }

    @Override
    public T getObject() throws Exception {
        return ProxyFactory.proxy(proxyInterface,invocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
