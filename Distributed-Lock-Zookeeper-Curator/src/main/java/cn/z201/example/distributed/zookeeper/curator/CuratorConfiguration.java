package cn.z201.example.distributed.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author z201.coding@gmail.com
 **/
@Configuration
public class CuratorConfiguration {

    @Value("${zookeeper.curator.retryCount}")
    private int retryCount;

    @Value("${zookeeper.curator.elapsedTimeMs}")
    private int elapsedTimeMs;

    @Value("${zookeeper.curator.connectString}")
    private String connectString;

    @Value("${zookeeper.curator.sessionTimeoutMs}")
    private int sessionTimeoutMs;

    @Value("${zookeeper.curator.connectionTimeoutMs}")
    private int connectionTimeoutMs;

    @Bean(name = "curatorFramework", initMethod = "start")
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs,
                new RetryNTimes(retryCount, elapsedTimeMs));
    }

}
