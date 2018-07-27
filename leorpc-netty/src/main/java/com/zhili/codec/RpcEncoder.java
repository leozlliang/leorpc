package com.zhili.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Administrator on 2018/6/7.
 */
public class RpcEncoder extends MessageToByteEncoder {

    private RpcSerializer rpcSerialize;
    private Class<?> genericClass;

    public RpcEncoder(RpcSerializer rpcSerialize, Class<?> genericClass) {
        this.rpcSerialize = rpcSerialize;
        this.genericClass = genericClass;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = rpcSerialize.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}