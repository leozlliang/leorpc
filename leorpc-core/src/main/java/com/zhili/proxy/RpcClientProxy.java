package com.zhili.proxy;

import com.zhili.bean.RPCReq;
import com.zhili.client.SimpleNettyClient;
import com.zhili.config.ClientConfig;
import com.zhili.loadbalance.ILoadBalanceStrategy;
import com.zhili.loadbalance.impl.RandomLB;
import com.zhili.mrpc.zk.domain.ServiceInfo;
import com.zhili.mrpc.zk.registry.IServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Administrator on 2018/6/8.
 */

public class RpcClientProxy implements InvocationHandler {
    private String appName;
    private IServiceDiscovery serviceDiscovery;
    private ILoadBalanceStrategy loadBalanceStrategy = new RandomLB();

    public IServiceDiscovery getServiceDiscovery() {
        return serviceDiscovery;
    }

    public void setServiceDiscovery(IServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    //private
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ServiceInfo serviceInfo = getServinceInfo(appName);
        SimpleNettyClient client = new SimpleNettyClient(new ClientConfig(serviceInfo.getAddress(),serviceInfo.getPort()));
        client.connect();
        RPCReq req = new RPCReq();
        req.setServiceName( method.getDeclaringClass().getName());
        req.setMethod(method.getName());
        req.setParamTypes(method.getParameterTypes());
        req.setParams(args);
        return client.sendMsg(req);
    }

    //稍后抽象成serviceProvider
    private ServiceInfo getServinceInfo(String serviceName) throws Exception{
        List<ServiceInfo> list = serviceDiscovery.getServiceList(serviceName);
        ServiceInfo serviceInfo  = (ServiceInfo)loadBalanceStrategy.select(list);
        return serviceInfo;
    }
}
