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
public class ServiceInfo {
    private String address;
    private int port;
    private String name;
    private double ver;
    private String description;
    private String zoneId;
}
