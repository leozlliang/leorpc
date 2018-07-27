package com.test;


import com.zhili.api.TestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:app-ctx-client.xml")
public class ServiceTest {

    @Resource(name="TestService")
    TestService service;

    @Test
    public void testExist() {
        String resp = service.testMethod("arg1",2);
        log.info("测试回调结果:{}",resp);
        int a=1;

    }

}