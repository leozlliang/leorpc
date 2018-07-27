package com.zhili.loadbalance;

import com.zhili.mrpc.zk.domain.ServiceInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/6/27.
 */
public interface ILoadBalanceStrategy<T> {

    public T select(List<T> list);

}
