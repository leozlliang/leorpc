package com.zhili.bean;

import io.netty.channel.ChannelPromise;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Administrator on 2018/6/7.
 */
@Setter
@Getter
@ToString
public class ReplyInfo {
    ChannelPromise promise;
    RPCInfo rpcInfo;
}
