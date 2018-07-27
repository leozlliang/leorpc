package com.zhili.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Administrator on 2018/6/7.
 */
@Setter
@Getter
@ToString
public class RPCHeader {
    private String reqId;
    private String reqIp;
    private String traceId;
}
