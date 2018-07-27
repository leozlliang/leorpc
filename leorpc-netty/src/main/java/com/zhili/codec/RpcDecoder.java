package com.zhili.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
public class RpcDecoder extends ByteToMessageDecoder {

    private RpcSerializer rpcSerialize;
    private Class<?> genericClass;

    public RpcDecoder(RpcSerializer rpcSerialize, Class<?> genericClass) {
        this.rpcSerialize = rpcSerialize;
        this.genericClass = genericClass;
    }

    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        Object obj = rpcSerialize.deserialize(data, genericClass);
        out.add(obj);
    }

}
