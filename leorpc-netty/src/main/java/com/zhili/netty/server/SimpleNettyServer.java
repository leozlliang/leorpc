package com.zhili.netty.server;

import com.zhili.bean.RPCInfo;
import com.zhili.codec.KryoSerializer;
import com.zhili.codec.RpcDecoder;
import com.zhili.codec.RpcEncoder;
import com.zhili.codec.RpcSerializer;
import com.zhili.config.ClientConfig;
import com.zhili.handler.IServiceSelector;
import com.zhili.handler.ServerBizHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class SimpleNettyServer {
    private  String ip = "127.0.0.1";
    private  int port = 9999;
    private IServiceSelector serviceSelecor;

    /** 用于分配处理业务线程的线程组个数 */
    protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2; // 默认  
    /** 业务出现线程大小 */
    protected static final int BIZTHREADSIZE = 4;
    /* 
     * NioEventLoopGroup实际上就是个线程池, 
     * NioEventLoopGroup在后台启动了n个NioEventLoop来处理Channel事件, 
     * 每一个NioEventLoop负责处理m个Channel, 
     * NioEventLoopGroup从NioEventLoop数组里挨个取出NioEventLoop来处理Channel 
     */
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);

    public SimpleNettyServer(ClientConfig config) {
        this.ip = config.getIp();
        this.port = config.getPort();
    }

    public void setServiceSelecor(IServiceSelector serviceSelecor) {
        this.serviceSelecor = serviceSelecor;
    }

    public  void start() throws Exception {
        RpcSerializer serializable = new KryoSerializer();
        final RpcEncoder encoder = new RpcEncoder(serializable, RPCInfo.class);
        final RpcDecoder decoder = new RpcDecoder(serializable, RPCInfo.class);
        final ServerBizHandler bizHandler = new ServerBizHandler();
        bizHandler.setServiceSelecor(serviceSelecor);
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                pipeline.addLast("decoder", decoder);
                pipeline.addLast("encoder", encoder);
                pipeline.addLast(bizHandler);
            }
        });

        b.bind(ip, 9999).sync();
    }

    protected static void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        ClientConfig config = new ClientConfig("127.0.0.1",9999);
        new SimpleNettyServer(config).start();
        // TcpServer.shutdown();  
    }
}  