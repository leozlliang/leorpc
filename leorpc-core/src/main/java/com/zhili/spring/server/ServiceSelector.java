package com.zhili.spring.server;

import com.zhili.handler.IServiceSelector;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/27.
 */
public class ServiceSelector implements IServiceSelector,ApplicationContextAware {
    private ApplicationContext ctx;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    @Override
    public Object select(String serviceName) {
        try {
            Map<String, ?> beanMap =  ctx.getBeansOfType(Class.forName(serviceName));
            if(!CollectionUtils.isEmpty(beanMap)){
                return beanMap.values().toArray()[0];
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
