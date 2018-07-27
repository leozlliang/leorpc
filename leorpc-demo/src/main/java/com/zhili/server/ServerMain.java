package com.zhili.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2018/6/8.
 */
public class ServerMain {
    public static void main(String[] args) throws Exception {
        final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:app-ctx-server.xml");
        ctx.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                ctx.close();
            }
        });
    }
}
