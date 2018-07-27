package com.zhili.mrpc.zk.registry;

import com.zhili.mrpc.zk.domain.ServiceInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 */
public interface IServiceDiscovery {

    public void registService(ServiceInfo serverInfo)  throws Exception;

    public List<ServiceInfo> getServiceList(String serviceName)  throws Exception;

}
