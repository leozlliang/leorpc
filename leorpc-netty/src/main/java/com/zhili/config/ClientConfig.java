package com.zhili.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Administrator on 2018/6/8.
 */
@Setter
@Getter
@ToString
public class ClientConfig {
    private String ip;
    private int port;
    private int soTimeout;

    public ClientConfig(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
