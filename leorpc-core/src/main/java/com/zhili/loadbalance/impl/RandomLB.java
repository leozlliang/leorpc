package com.zhili.loadbalance.impl;

import com.zhili.loadbalance.ILoadBalanceStrategy;
import com.zhili.mrpc.zk.domain.ServiceInfo;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/6/27.
 */
public class RandomLB implements ILoadBalanceStrategy<ServiceInfo> {

    @Override
    public ServiceInfo select(List<ServiceInfo> list) {
        int max = list.size();
        int returnVal=(int)(Math.random()*max);
        return list.get(returnVal);
    }
}
