# leorpc 简单的微服务核心源码demo，供学习用
<br />
core: RPC的核心部分，对服务注册，序列化（kryo），netty传输，服务选择(service loadbalance) 等整合<br />
netty: Netty小封装，作为RPC的通讯组件 <br />
zk: 基于zk,apache cuator做服务注册及服务发现 <br />
demo: 可运行demo <br />
<br/>
待完善：<br />
1. 限流(计划用Hytrix或Guva RateLimiter完成)<br />
2. Netty连接池化<br />


<pre><code>
客户端配置：
    &ltbeans&gt
        &ltleorpc:registry address="127.0.0.1" port="2181" /&gt
        &ltleorpc:client scanPackage="com.zhili.api" /&gt
    &lt/beans&gt
具体接口：
@RpcService
public interface TestService {
    public String testMethod(String arg1, int arg2);
}
    
</code></pre>

<pre><code>
服务端配置：
    &ltbeans&gt
        &ltleorpc:registry address="127.0.0.1" port="2181" /&gt
        &ltleorpc:server port="9999" scanPackage="com.zhili.api"  &gt&lt/leorpc:server&gt
        &ltbean id="testService" class="com.zhili.service.impl.TestServiceImpl" /&gt
    &lt/beans&gt
</code></pre>

