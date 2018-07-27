package com.zhili.spring.server;

import com.zhili.annotation.RpcService;
import com.zhili.client.SimpleNettyClient;
import com.zhili.config.ClientConfig;
import com.zhili.handler.HandlerSelector;
import com.zhili.handler.IServiceSelector;
import com.zhili.mrpc.zk.domain.ServiceInfo;
import com.zhili.mrpc.zk.registry.IServiceDiscovery;
import com.zhili.netty.server.SimpleNettyServer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import utils.NetworkUtil;
import utils.ScanPackageUtils;

import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/6/19.
 */

public class RpcRegister implements InitializingBean {
    private String scanPackage;
    private int port;
    private IServiceDiscovery serviceDiscovery;
    private IServiceSelector serviceSelector;
    private SimpleNettyServer server;

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public IServiceDiscovery getServiceDiscovery() {
        return serviceDiscovery;
    }

    public void setServiceDiscovery(IServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public SimpleNettyServer getServer() {
        return server;
    }

    public void setServer(SimpleNettyServer server) {
        this.server = server;
    }

    public IServiceSelector getServiceSelector() {
        return serviceSelector;
    }

    public void setServiceSelector(IServiceSelector serviceSelector) {
        this.serviceSelector = serviceSelector;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String ip = "127.0.0.1";
        ClientConfig config = new ClientConfig(ip,port);
        server = new SimpleNettyServer(config);
        server.setServiceSelecor(serviceSelector);
        server.start();
        registService();
    }


    private void registService() throws Exception{
        Set<String> clazzSet = ScanPackageUtils.findPackageAnnotationClass(scanPackage,RpcService.class);
        for(String clazz : clazzSet){
            ServiceInfo serviceInfo  = new ServiceInfo();
            serviceInfo.setName(clazz);
            //String address = NetworkUtil.getServerIp();
            String address = "127.0.0.1";
            serviceInfo.setAddress(address);
            serviceInfo.setPort(port);
            serviceDiscovery.registService(serviceInfo);
        }
    }


}
