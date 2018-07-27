package com.zhili.handler;

import com.google.common.collect.Maps;
import com.zhili.bean.RPCHeader;
import com.zhili.bean.RPCInfo;
import com.zhili.bean.ReplyInfo;
import io.netty.channel.*;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.Currency;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class ClientBizHandler extends SimpleChannelInboundHandler<RPCInfo> {
    private ChannelHandlerContext ctx;
    private ConcurrentHashMap<String,ReplyInfo> promiseMap = new ConcurrentHashMap();
    private AtomicLong promiseCounter = new AtomicLong(0);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    public ReplyInfo sendMessage(Object message) {
        if (ctx == null) {
            log.info("ctx为空:" + ctx);
            throw new IllegalStateException();
        }
        RPCInfo msg = new RPCInfo();
        RPCHeader header = new RPCHeader();
        header.setReqId(""+promiseCounter.addAndGet(1));
        msg.setHeader(header);
        msg.setPayload(message);
        final ChannelPromise promise = ctx.writeAndFlush(msg).channel().newPromise();
        ReplyInfo replyInfo = new ReplyInfo();
        replyInfo.setPromise(promise);
        promiseMap.put(header.getReqId(),replyInfo);

        return replyInfo;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCInfo msg) throws Exception {
        ReplyInfo replyInfo= promiseMap.get(""+msg.getHeader().getReqId());
        ChannelPromise promise = replyInfo.getPromise();
        replyInfo.setRpcInfo(msg);
        promise.setSuccess();
        promiseMap.remove(msg.getHeader().getReqId());
    }
}  