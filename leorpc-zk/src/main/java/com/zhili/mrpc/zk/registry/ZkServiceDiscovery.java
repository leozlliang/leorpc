package com.zhili.mrpc.zk.registry;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.zhili.mrpc.zk.domain.ServiceInfo;
import com.zhili.mrpc.zk.listener.ZkConnListener;
import com.zhili.mrpc.zk.listener.ZkServiceCacheListener;
import lombok.Getter;
import lombok.Setter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.*;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2018/6/19.
 */
@Getter
@Setter
public class ZkServiceDiscovery implements IServiceDiscovery,InitializingBean {

    private CuratorFramework client;
    private ServiceDiscovery<ServiceInfo> serviceDiscovery;
    private ConcurrentHashMap<String,ServiceCache<ServiceInfo>> sreviceCacheMap = new ConcurrentHashMap();
    private String address;
    private int port;
    private String basePath = "com.zhili.mrpc.root.dev";

    public void start() throws Exception{
        String zkHost = address+":"+port;
        client = CuratorFrameworkFactory.newClient(zkHost, new ExponentialBackoffRetry(1000, 3));
        client.start();
        client.blockUntilConnected();
        client.getConnectionStateListenable().addListener(new ZkConnListener(this));
        serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                .client(client)
                .basePath(basePath)
                .build();
        serviceDiscovery.start();
    }

    public void registService(ServiceInfo serverInfo) throws Exception{
        /**
         * 指定服务的 地址，端口，名称
         */
        ServiceInstanceBuilder<ServiceInfo> sib = ServiceInstance.builder();
        sib.address(serverInfo.getAddress());
        sib.port(serverInfo.getPort());
        sib.name(serverInfo.getName());
        sib.payload(serverInfo);
        ServiceInstance<ServiceInfo> instance = sib.build();
        //服务注册
        serviceDiscovery.registerService(instance);
        serviceDiscovery.start();
    }

    public List<ServiceInfo> getServiceList(String serviceName)  throws Exception{
        ServiceCache<ServiceInfo> serviceCache = sreviceCacheMap.get(serviceName);
        if(serviceCache==null){
            serviceCache = serviceDiscovery.serviceCacheBuilder().name(serviceName).build();
            serviceCache.addListener(new ZkServiceCacheListener(serviceCache));
            serviceCache.start();
            sreviceCacheMap.put(serviceName,serviceCache);
        }
        return Lists.transform(serviceCache.getInstances(), new Function<ServiceInstance<ServiceInfo>, ServiceInfo>() {
            @Override
            public ServiceInfo apply(ServiceInstance<ServiceInfo> serviceInfoServiceInstance) {
                return serviceInfoServiceInstance.getPayload();
            }
        });

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
    }
}
