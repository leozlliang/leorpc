package com.zhili.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/6/13.
 */
public class ConnectionListener implements ChannelFutureListener {
    private SimpleNettyClient client;
    public ConnectionListener(SimpleNettyClient client) {
        this.client = client;
    }
    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (!channelFuture.isSuccess()) {
            System.out.println("Reconnect");
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    client.connect();
                }
            }, 1L, TimeUnit.SECONDS);
        }
    }
}
