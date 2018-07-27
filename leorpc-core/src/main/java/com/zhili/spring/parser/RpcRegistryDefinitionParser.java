package com.zhili.spring.parser;

import com.zhili.annotation.RpcService;
import com.zhili.mrpc.zk.registry.ZkServiceDiscovery;
import com.zhili.proxy.RpcClientProxy;
import com.zhili.spring.client.RpcClientFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import utils.ScanPackageUtils;

import java.util.Set;

public class RpcRegistryDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String address = element.getAttribute("address");
        String port = element.getAttribute("port");
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        try {
            BeanDefinitionBuilder clientBeanBuilder = BeanDefinitionBuilder.genericBeanDefinition(ZkServiceDiscovery.class);
            clientBeanBuilder.addPropertyValue("address", address);
            clientBeanBuilder.addPropertyValue("port", Integer.valueOf(port));
            registry.registerBeanDefinition("serviceDiscovery", clientBeanBuilder.getBeanDefinition());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}