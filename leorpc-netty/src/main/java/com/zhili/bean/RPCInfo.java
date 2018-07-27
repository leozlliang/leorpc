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
public class RPCInfo<T> {
    private RPCHeader header;
    T payload;
}
