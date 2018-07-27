package com.zhili.mrpc.zk.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Administrator on 2018/6/15.
 */
@Setter
@Getter
@ToString
public class ZkConfig {
    private String hosts;
    private String basePath;
    private int port;
    private int soTimeout;
    private int retryTime;

}
