package cn.z201.audit.service.impl;

import cn.z201.audit.repository.AuditRepository;
import cn.z201.audit.service.AuditServiceI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author z201.coding@gmail.com
 **/
@Service
@Slf4j
public class AuditServiceImpl implements AuditServiceI {

    private AuditRepository auditRepository;

    @Autowired
    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public void test(String key) {
        auditRepository.add("查看","测试方法写入","#{[0]}",key);
    }
}
