package cn.z201.example.spring.mybatis.audit.persistence.entity;

import cn.z201.example.spring.mybatis.audit.mybatis.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据审计日志
 * </p>
 *
 * @author z201.coding@gmail.com
 * @since 2022-02-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizAuditLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 事件类型 insert update delete select 等等
     */
    private String eventType;

    /**
     * 事件标题 xxx查看了什么记录 xx修改了什么记录
     */
    private String eventTitle;

    /**
     * 事件描述
     */
    private String eventDescription;

    /**
     * 时间记录时间
     */
    private Long eventTime;

    /**
     * 操作标志
     */
    private String opTraceId;

    /**
     * 操作用户id
     */
    private Long userId;

}
