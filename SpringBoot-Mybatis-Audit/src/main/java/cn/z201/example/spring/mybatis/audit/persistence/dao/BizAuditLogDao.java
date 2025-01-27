package cn.z201.example.spring.mybatis.audit.persistence.dao;

import cn.z201.example.spring.mybatis.audit.persistence.entity.BizAuditLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据审计日志 Mapper 接口
 * </p>
 *
 * @author z201.coding@gmail.com
 * @since 2022-02-16
 */
public interface BizAuditLogDao extends BaseMapper<BizAuditLog> {

    int batchInsert(@Param("bizAuditLogList") List<BizAuditLog> bizAuditLogList);

}
