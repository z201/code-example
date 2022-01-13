package cn.z201.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

/**
 * @author z201.coding@gmail.com
 **/
@Configuration
@Slf4j
public class ZookeeperConfig {

    private static final String LOCK_ROOT = "/LOCKS";   //锁的根路径
    private static final String LOCK_NODE_NAME = "/L_";  //锁的名称，使用临时顺序节点

    @Value("${zookeeper.address}")
    private String connectString;

    @Value("${zookeeper.timeout}")
    private int timeout;

    @Bean(name = "zkClient")
    public ZooKeeper zkClient() {
        ZooKeeper zooKeeper = null;
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            //连接成功后，会回调watcher监听，此连接操作是异步的，执行完new语句后，直接调用后续代码
            //  可指定多台服务地址 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
            zooKeeper = new ZooKeeper(connectString, timeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (Event.KeeperState.SyncConnected == event.getState()) {
                        //如果收到了服务端的响应事件,连接成功
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            init(zooKeeper);
            log.info("init zookeeper success {}", zooKeeper.getState());
        } catch (Exception e) {
            throw new IllegalArgumentException("init zookeeper error " + e.getMessage());
        }
        return zooKeeper;
    }

    public void init(ZooKeeper zooKeeper) throws InterruptedException, KeeperException {
        Stat existsNode = zooKeeper.exists(LOCK_ROOT, false);
        if (existsNode == null) {
            zooKeeper.create(LOCK_ROOT, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        String key = LOCK_ROOT.concat(LOCK_NODE_NAME);
        existsNode = zooKeeper.exists(key, false);
        if (existsNode == null) {
            zooKeeper.create(key, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }


}
