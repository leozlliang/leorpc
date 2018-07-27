package com.zhili.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Administrator on 2018/6/8.
 */
@Setter
@Getter
@ToString
public class RPCReq {
    private String serviceName;
    private String method;
    private Class<?>[] paramTypes;
    private Object[] params;

}
