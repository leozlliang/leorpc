package com.zhili.api;


import com.zhili.annotation.RpcService;

/**
 * Created by Administrator on 2018/6/8.
 */
@RpcService
public interface TestService {
    public String testMethod(String arg1, int arg2);
}
