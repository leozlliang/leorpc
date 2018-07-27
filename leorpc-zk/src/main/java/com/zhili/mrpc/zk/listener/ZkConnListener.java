package com.zhili.mrpc.zk.listener;

import com.zhili.mrpc.zk.registry.ZkServiceDiscovery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/6/19.
 */
@Slf4j
@AllArgsConstructor
public class ZkConnListener implements ConnectionStateListener {
    private ZkServiceDiscovery serviceDiscovery;
    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState){
        log.info("zk lose connection,retry to re-connect");
        if(! connectionState.isConnected()){
            try {
                serviceDiscovery.getClient().blockUntilConnected(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error("zk re-connect fail",e);
            }
        }
    }
}
