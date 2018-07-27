package com.zhili.mrpc.zk.listener;

import com.zhili.mrpc.zk.domain.ServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.ServiceCacheListener;

import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 */
@Slf4j
public class ZkServiceCacheListener implements ServiceCacheListener {
    private ServiceCache<ServiceInfo> serviceCache;

    public ZkServiceCacheListener(ServiceCache<ServiceInfo> serviceCache) {
        this.serviceCache = serviceCache;
    }

    @Override
    public void cacheChanged() {
        try {
            log.info("ServiceManager, cacheChanged, active or inactive service instance");
            List<ServiceInstance<ServiceInfo>> serviceInstanceList = serviceCache.getInstances();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
        log.info("Service Discovery, serviceDiscoveryCache lost connection to zookeeper");
    }
}
