package com.zhili.loadbalance.impl;

import com.zhili.loadbalance.ILoadBalanceStrategy;
import com.zhili.mrpc.zk.domain.ServiceInfo;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/6/27.
 */
public class RoundRobinLB implements ILoadBalanceStrategy<ServiceInfo> {

    private static final int MAX_ATOMIC_INT = Integer.MAX_VALUE - 10000;

    private AtomicInteger pos = new AtomicInteger(0);

    @Override
    public ServiceInfo select(List<ServiceInfo> list) {

        int newPos = pos.getAndIncrement();
        //控制边界
        if(newPos > MAX_ATOMIC_INT){
            pos.set(0);
        }

        return list.get(newPos % list.size());
    }
}
