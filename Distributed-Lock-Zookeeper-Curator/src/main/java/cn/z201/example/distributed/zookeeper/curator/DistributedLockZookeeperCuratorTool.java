package cn.z201.example.distributed.zookeeper.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@Component
public class DistributedLockZookeeperCuratorTool implements InitializingBean {

    private final static String ROOT_PATH_LOCK = "lock";

    @Resource(name = "curatorFramework")
    private CuratorFramework curatorFramework;

    public String createPathKey(String path) {
        return "/" + ROOT_PATH_LOCK + "/" + path;
    }

    /**
     * 释放分布式锁
     */
    public boolean releaseDistributedLock(String path) {
        try {
            if (curatorFramework.checkExists().forPath(path) != null) {
                curatorFramework.delete().forPath(path);
            }
        } catch (Exception e) {
            log.error("failed to release lock");
            return false;
        }
        return true;
    }

    /**
     * 创建 watcher 事件
     */
    private void addWatcher(String path) throws Exception {
        final PathChildrenCache cache = new PathChildrenCache(curatorFramework, path, false);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener((client, event) -> {
            String oldPath = event.getData().getPath();
            log.info("success to release lock for path:{}", oldPath);
        });
    }

    /**
     * 初始化创建永久父节点
     */
    @Override
    public void afterPropertiesSet() {
        curatorFramework = curatorFramework.usingNamespace("lock-namespace");
        String path = "/" + ROOT_PATH_LOCK;
        try {
            if (curatorFramework.checkExists().forPath(path) == null) {
                curatorFramework
                        .create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(path);
            }
            addWatcher(path);
        } catch (Exception e) {
            log.error("connect zookeeper fail，please check the log >> {}", e.getMessage(), e);
        }
    }
}
