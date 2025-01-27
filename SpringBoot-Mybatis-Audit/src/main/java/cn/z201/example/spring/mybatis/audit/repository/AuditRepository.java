package cn.z201.audit.repository;

import cn.z201.audit.config.aspect.annotation.MonitorAnnotation;
import cn.z201.audit.config.mdc.MdcThreadPoolTaskExecutor;
import cn.z201.audit.config.mdc.MdcTool;
import cn.z201.audit.persistence.dao.BizAuditLogDao;
import cn.z201.audit.persistence.entity.BizAuditLog;
import cn.z201.audit.utils.JsonTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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

    private void add(BizAuditLog bizAuditLog) {
        if (Objects.isNull(bizAuditLog)) {
            return;
        }
        if (Objects.isNull(bizAuditLog.getUserId())) {
            // 演示场景，应该是从其他地方插入当前用户信息。
            bizAuditLog.setUserId(1L);
        }
        log.info("{}", JsonTool.toString(bizAuditLog));
        concurrentLinkedQueue.add(bizAuditLog);
    }

    /**
     * 普通参数写入方式
     * @param type
     * @param title
     * @param descriptionExpression
     * @param args
     */
    public void add(String type,String title,String descriptionExpression, Object args) {
        //格式化描述表达式得到易读的描述
        String description = parseDescriptionExpression(new Object[]{args}, descriptionExpression);
        BizAuditLog bizAuditLog = new BizAuditLog();
        bizAuditLog.setEventType(type);
        bizAuditLog.setEventTitle(title);
        bizAuditLog.setEventDescription(description);
        bizAuditLog.setEventTime(System.currentTimeMillis());
        bizAuditLog.setOpTraceId(MdcTool.getInstance().get());
        add(bizAuditLog);
    }

    /**
     * 注解拦截方式
     * @param monitorAnnotation
     * @param args
     */
    public void add(MonitorAnnotation monitorAnnotation, Object[] args) {
        if (monitorAnnotation.audit()) {
            String type = monitorAnnotation.type();
            String title = monitorAnnotation.title();
            String descriptionExpression = monitorAnnotation.descriptionExpression();
            //格式化描述表达式得到易读的描述
            String description = parseDescriptionExpression(args, descriptionExpression);
            BizAuditLog bizAuditLog = new BizAuditLog();
            bizAuditLog.setEventType(type);
            bizAuditLog.setEventTitle(title);
            bizAuditLog.setEventDescription(description);
            bizAuditLog.setEventTime(System.currentTimeMillis());
            bizAuditLog.setOpTraceId(MdcTool.getInstance().get());
            add(bizAuditLog);
        }
    }

    @PostConstruct
    public void init() {
        mdcThreadPoolTaskExecutor.execute(() -> {
            for (; ; ) {
                try {
                    if (concurrentLinkedQueue.isEmpty()) {
                        Thread.sleep(500);
                    } else {
                        Iterator<BizAuditLog> iterator = concurrentLinkedQueue.iterator();
                        List<BizAuditLog> bizAuditLogList = new ArrayList<>();
                        while (iterator.hasNext()) {
                            if (bizAuditLogList.size() >= 10) {
                                break;
                            }
                            bizAuditLogList.add(iterator.next());
                            iterator.remove();
                        }
                        bizAuditLogDao.batchInsert(bizAuditLogList);
                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    log.error("审计日志存储失败 :" + e);
                }
            }
        });
    }

    private String parseDescriptionExpression(Object[] args, String descriptionExpression) {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(descriptionExpression, new TemplateParserContext());
        return expression.getValue(new StandardEvaluationContext(args), String.class);
    }

}
