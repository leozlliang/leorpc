package com.zhili.spring.namespace;

import com.zhili.spring.parser.RpcClientDefinitionParser;
import com.zhili.spring.parser.RpcRegistryDefinitionParser;
import com.zhili.spring.parser.RpcServerDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by Administrator on 2018/1/25.
 */
public class RpcNameSpaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("client",new RpcClientDefinitionParser());
        registerBeanDefinitionParser("server",new RpcServerDefinitionParser());
        registerBeanDefinitionParser("registry",new RpcRegistryDefinitionParser());
    }
}
