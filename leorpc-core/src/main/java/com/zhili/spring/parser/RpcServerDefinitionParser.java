package com.zhili.spring.parser;

import com.zhili.annotation.RpcService;
import com.zhili.mrpc.zk.registry.ZkServiceDiscovery;
import com.zhili.proxy.RpcClientProxy;
import com.zhili.spring.client.RpcClientFactoryBean;
import com.zhili.spring.server.RpcRegister;
import com.zhili.spring.server.ServiceSelector;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import utils.ScanPackageUtils;

import java.util.Set;

public class RpcServerDefinitionParser implements BeanDefinitionParser {


    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        //
        BeanDefinitionBuilder serviceSelectorBuilder = BeanDefinitionBuilder.genericBeanDefinition(ServiceSelector.class);
        registry.registerBeanDefinition("serviceSelector", serviceSelectorBuilder.getBeanDefinition());
        //
        String scanPackage = element.getAttribute("scanPackage");
        String port = element.getAttribute("port");
        BeanDefinitionBuilder clientBeanBuilder = BeanDefinitionBuilder.genericBeanDefinition(RpcRegister.class);
        clientBeanBuilder.addPropertyValue("scanPackage", scanPackage);
        clientBeanBuilder.addPropertyValue("port", Integer.valueOf(port));
        clientBeanBuilder.addPropertyReference("serviceDiscovery","serviceDiscovery");
        clientBeanBuilder.addPropertyReference("serviceSelector","serviceSelector");
        registry.registerBeanDefinition("rpcRegister", clientBeanBuilder.getBeanDefinition());

        return null;
    }
}