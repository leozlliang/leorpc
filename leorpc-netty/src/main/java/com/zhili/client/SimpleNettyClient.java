package com.zhili.client;

import com.zhili.bean.RPCInfo;
import com.zhili.bean.RPCReq;
import com.zhili.bean.ReplyInfo;
import com.zhili.bean.TestBean;
import com.zhili.codec.KryoSerializer;
import com.zhili.codec.RpcDecoder;
import com.zhili.codec.RpcEncoder;
import com.zhili.codec.RpcSerializer;
import com.zhili.config.ClientConfig;
import com.zhili.handler.ClientBizHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class SimpleNettyClient {
    private ClientConfig config;
    private Bootstrap bootstrap = null;
    private Channel channel = null;
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private ClientBizHandler handler = new ClientBizHandler();
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);

    public void setEventLoopGroup(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }

    public void setHandler(ClientBizHandler handler) {
        this.handler = handler;
    }

    public SimpleNettyClient(ClientConfig config){
        this.config = config;
    }

    /**
     * 初始化Bootstrap 
     */
    private  final Bootstrap createBootstrap() {
        RpcSerializer serializable = new KryoSerializer();
        final RpcEncoder encoder = new RpcEncoder(serializable, RPCInfo.class);
        final RpcDecoder decoder = new RpcDecoder(serializable, RPCInfo.class);
        Bootstrap b = new Bootstrap();
        b.group(eventLoopGroup).channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                pipeline.addLast("decoder", decoder);
                pipeline.addLast("encoder", encoder);
                pipeline.addLast("handler", handler);
            }
        });

        b.option(ChannelOption.SO_KEEPALIVE, true);
        return b;
    }
    public Object sendMsg(final RPCReq req) throws Exception{
        return doSendMsg(req);
    }

    private Object doSendMsg(final RPCReq req) throws  Exception{
        return fixedThreadPool.submit(new Callable<Object>() {
            @Override
            public Object call() throws  Exception {
                ReplyInfo replyInfo = handler.sendMessage(req);
                replyInfo.getPromise().await();
                Object data = replyInfo.getRpcInfo().getPayload();
                return data;
            }
        }).get();
    }

    public  boolean  connect() {
        try {
            bootstrap = createBootstrap();
            this.channel = bootstrap.connect(config.getIp(), config.getPort()).addListener(new ConnectionListener(this)).sync().channel();
            log.info("connect result:{}",channel.isRegistered());
            return channel.isRegistered();
        } catch (Exception e) {
            log.error("连接Server(IP{},PORT{})失败", config.getIp(), config.getPort(), e);
            return false;
        }
    }

    public void disconnect(){
        if(eventLoopGroup!=null){
            eventLoopGroup.shutdownGracefully();
        }
        fixedThreadPool.shutdown();
    }



    public static void main(String[] args) throws Exception {
        final SimpleNettyClient client = new SimpleNettyClient(new ClientConfig("127.0.0.1",9999));
        client.connect();
        try {
            long t0 = System.nanoTime();
            for (int i = 0; i < 5; i++) {
                final int number = i;
                RPCReq req = new RPCReq();
                req.setServiceName("TestService");
                req.setMethod("testMethod");
                req.setParamTypes(new Class[]{String.class,int.class});
                req.setParams(new Object[]{"arg1",number});
                Object response = client.sendMsg(req);
                log.info("服务器返回信息：{}", response.toString());
            }

            long t1 = System.nanoTime();
            log.info("time used:{}", t1 - t0);
        } catch (Exception e) {
            log.error("main err:", e);
        }finally {
            client.disconnect();
        }
    }


}  