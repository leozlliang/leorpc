package com.zhili.handler;

import com.zhili.bean.RPCInfo;
import com.zhili.bean.RPCReq;
import com.zhili.proxy.ServerBizInvoker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Administrator on 2018/6/6.
 */
@Slf4j
@Setter
public class ServerBizHandler extends SimpleChannelInboundHandler<RPCInfo<RPCReq>> {
    private ServerBizInvoker invoker = new ServerBizInvoker();
    private IServiceSelector serviceSelecor;



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCInfo<RPCReq> msg) throws Exception {
        log.info("SERVER接收到消息:" + msg);
        RPCInfo resp = new RPCInfo();
        resp.setHeader(msg.getHeader());
        Object service = serviceSelecor.select( msg.getPayload().getServiceName());
        Object payload = invoker.invoke(service,msg.getPayload());
        resp.setPayload(payload);
        ctx.channel().writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("exceptionCaught!", cause);
        ctx.close();
    }
}
