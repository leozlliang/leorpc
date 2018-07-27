# leorpc
<br />
core: RPC的核心部分，对服务注册，序列化（kryo），netty传输等整合，服务选择(service loadbalance) <br />
netty: Netty小封装，作为RPC的通讯组件 <br />
zk: 基于zk,apache cuator做服务注册及服务发现 <br />
demo: 可运行demo <br />


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

