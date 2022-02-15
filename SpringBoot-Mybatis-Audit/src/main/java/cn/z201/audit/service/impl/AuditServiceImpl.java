package cn.z201.audit.service.impl;

import cn.z201.audit.config.aspect.annotation.MonitorAnnotation;
import cn.z201.audit.service.AuditServiceI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author z201.coding@gmail.com
 **/
@Service
@Slf4j
public class AuditServiceImpl implements AuditServiceI {

    @Override
    @MonitorAnnotation(audit = true, type = "查看", title = "首页", descriptionExpression = "#{[0]}")
    public void test(String key) {

    }
}
