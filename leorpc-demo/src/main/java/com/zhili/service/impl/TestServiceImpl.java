package com.zhili.service.impl;

import com.zhili.api.TestService;

/**
 * Created by Administrator on 2018/6/8.
 */
public class TestServiceImpl implements TestService {

    @Override
    public String testMethod(String arg1, int arg2) {
        return "类TestServiceImpl：arg1:"+arg1+";arg2"+arg2;
    }
}
