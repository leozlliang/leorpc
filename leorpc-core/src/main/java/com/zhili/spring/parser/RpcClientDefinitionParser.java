package com.zhili.spring.parser;

import com.zhili.annotation.RpcService;
import com.zhili.domain.RpcClientConfig;
import com.zhili.proxy.RpcClientProxy;
import com.zhili.spring.client.RpcClientFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import utils.ScanPackageUtils;

import java.lang.reflect.InvocationHandler;
import java.util.Set;

public class RpcClientDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        BeanDefinitionRegistry registry = parserContext.getRegistry();
        //注册clientProxy
        BeanDefinitionBuilder rpcClientProxyBeanBuilder = BeanDefinitionBuilder.genericBeanDefinition(RpcClientProxy.class);
        rpcClientProxyBeanBuilder.addPropertyReference("serviceDiscovery","serviceDiscovery");
        String scanPackage=element.getAttribute("scanPackage");
        registry.registerBeanDefinition("rpcClientProxy",rpcClientProxyBeanBuilder.getBeanDefinition());
        //为每个interface绑定代理
        Set<String> clazzSet = ScanPackageUtils.findPackageAnnotationClass(scanPackage,RpcService.class);
        for(String clazz : clazzSet){
            try {
                BeanDefinitionBuilder clientBeanBuilder = BeanDefinitionBuilder.genericBeanDefinition(RpcClientFactoryBean.class);
                Class<?> proxyInterfaceClazz = Class.forName(clazz);
                clientBeanBuilder.addPropertyValue("proxyInterface",proxyInterfaceClazz);
                clientBeanBuilder.addPropertyReference("invocationHandler","rpcClientProxy");
                String shortClassName = ClassUtils.getShortName(clazz);
                registry.registerBeanDefinition(shortClassName,clientBeanBuilder.getBeanDefinition());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}