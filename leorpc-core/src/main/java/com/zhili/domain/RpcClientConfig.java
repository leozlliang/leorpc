package com.zhili.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Administrator on 2018/6/19.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class RpcClientConfig {
    private String zkAddress;
}
