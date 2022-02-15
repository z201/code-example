package cn.z201.audit.repository;

import cn.z201.audit.config.mdc.MdcThreadPoolTaskExecutor;
import cn.z201.audit.persistence.dao.BizAuditLogDao;
import cn.z201.audit.persistence.entity.BizAuditLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author z201.coding@gmail.com
 **/
@Repository
@Slf4j
public class AuditRepository {

    @Autowired
    @Qualifier("auditLogExecutor")
    private MdcThreadPoolTaskExecutor mdcThreadPoolTaskExecutor;

    @Resource
    private BizAuditLogDao bizAuditLogDao;

    private static ConcurrentLinkedQueue<BizAuditLog> concurrentLinkedQueue = new ConcurrentLinkedQueue();

    public void add(BizAuditLog bizAuditLog) {
        concurrentLinkedQueue.add(bizAuditLog);
    }

    @PostConstruct
    public void init() {
        mdcThreadPoolTaskExecutor.execute(() -> {
            for (; ; ) {
                try {
                    if (concurrentLinkedQueue.isEmpty()) {
                        Thread.sleep(1000);
                    }else{
                        if (concurrentLinkedQueue.size() > 5) {
                            Iterator<BizAuditLog> iterator = concurrentLinkedQueue.iterator();
                            List<BizAuditLog> bizAuditLogList = new ArrayList<>();
                            while (iterator.hasNext()){
                                bizAuditLogList.add(iterator.next());
                                iterator.remove();
                            }
                            bizAuditLogDao.batchInsert(bizAuditLogList);
                        }
                    }
                } catch (Exception e) {
                    log.error("消费日志:" + e);
                }
            }
        });
    }


}
