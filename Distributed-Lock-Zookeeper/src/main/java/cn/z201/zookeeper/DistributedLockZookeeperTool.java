package cn.z201.zookeeper;

import cn.hutool.core.lang.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.LockSupport;


/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
public class DistributedLockZookeeperTool implements Watcher, AutoCloseable {

    private ZooKeeper zkClient;
    private String path;
    private String zNode;

    private final Thread currentThread = Thread.currentThread();

    public DistributedLockZookeeperTool(ZooKeeper zooKeeper, String path) {
        this.zkClient = zooKeeper;
        this.path = path;
    }

    /**
     * 创建持久化顺序节点
     *
     * @param path
     */
    public boolean lock(String path) throws InterruptedException, KeeperException {
        Stat existsNode = zkClient.exists("/" + path, false);
        if (existsNode == null) {
            zkClient.create("/" + path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        this.zNode = zkClient.create("/" + path + "/" + path + "_", new byte[0],
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        this.zNode = this.zNode.substring(zNode.lastIndexOf("/") + 1);
        List<String> childrenNodes = zkClient.getChildren("/" + path, false);
        Collections.sort(childrenNodes);
        String firstNode = childrenNodes.get(0);
        if (!firstNode.equals(zNode)) {
            String lastNode = firstNode;
            for (String node : childrenNodes) {
                if (!zNode.equals(node)) {
                    lastNode = node;
                } else {
                    Stat stat =  zkClient.exists("/" + path + "/" + lastNode, this);
                    if (null != stat) {
                        LockSupport.park();
                    }
                    break;
                }
            }
        }
        log.info("zNode {}", zNode);
        return true;
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            log.info("释放锁-将当前线程唤醒");
            LockSupport.unpark(currentThread);  //将当前线程唤醒
        }
    }


    @Override
    public void close() throws Exception {
        if (Validator.isNotEmpty(zNode)) {
            log.info("释放锁");
            zkClient.delete("/" + path + "/" + zNode, -1);
        }
    }

}
