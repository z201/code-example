package cn.z201.example.distributed.zookeeper;

import cn.hutool.core.lang.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.LockSupport;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
public class DistributedLockZookeeperTool {

    private final Thread currentThread = Thread.currentThread();

    private static final String LOCK_ROOT = "/LOCKS"; // 锁的根路径

    private static final String LOCK_NODE_NAME = "/L_"; // 锁的名称，使用临时顺序节点

    private ZooKeeper zkClient;

    private String zNode;

    private String watcherKey;

    public DistributedLockZookeeperTool(ZooKeeper zooKeeper) {
        this.zkClient = zooKeeper;
    }

    // 判断某个元素是否被删除，如果删除了就将当前线程唤醒
    Watcher watcher = event -> {
        // 如果获取到删除节点的事件，那么就唤醒当前线程
        if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
            if (Objects.equals(watcherKey, event.getPath())) {
                LockSupport.unpark(currentThread); // 将当前线程唤醒
            }
        }
    };

    /**
     * 创建持久化顺序节点
     */
    public boolean tryLock() throws InterruptedException, KeeperException {
        String key = LOCK_ROOT.concat(LOCK_NODE_NAME);
        // 创建临时有序节点，节点为/LOCKS/path_xxxxxxxx
        this.zNode = zkClient.create(key, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        lock();
        log.info("lock  {} ", zNode);
        return true;
    }

    private void lock() throws InterruptedException, KeeperException {
        // 获取当前锁节点排序之后的下标，截取掉/LOCKS/之后的内容
        List<String> childrenNodes = zkClient.getChildren(LOCK_ROOT, false);
        Collections.sort(childrenNodes);
        // 获取当前锁节点排序之后的下标，截取掉/LOCKS/之后的内容
        final int index = childrenNodes.indexOf(zNode.substring(LOCK_ROOT.length() + 1));
        // 如果获取到的index为0，也就是第一个元素
        if (index == 0) {
            return;
        }
        // 如果不是第一个元素，获取上一个节点的路径
        final String firstNode = childrenNodes.get(index - 1);
        if (Objects.equals(LOCK_NODE_NAME, "/" + firstNode)) {
            return;
        }

        Stat stat = zkClient.exists(LOCK_ROOT + "/" + firstNode, watcher);
        if (null != stat) {
            watcherKey = LOCK_ROOT + "/" + firstNode;
            LockSupport.park();
        }
        Thread.sleep(100);
        lock();
    }

    public List<String> list() throws InterruptedException, KeeperException {
        List<String> childrenNodes = zkClient.getChildren(LOCK_ROOT, false);
        return childrenNodes;
    }

    public void close() throws Exception {
        log.info("unlock {}", zNode);
        zkClient.delete(zNode, -1);
    }

}
