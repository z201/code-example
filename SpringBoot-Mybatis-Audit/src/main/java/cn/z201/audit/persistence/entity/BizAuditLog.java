package cn.z201.audit.persistence.entity;

import cn.z201.audit.mybatis.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据审计日志
 * </p>
 *
 * @author z201.coding@gmail.com
 * @since 2022-02-15
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
     * 操作标志
     */
    private String opTraceId;

    /**
     * 操作用户id
     */
    private Long userId = 0L;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BizAuditLog{");
        sb.append("eventType='").append(eventType).append('\'');
        sb.append(", eventTitle='").append(eventTitle).append('\'');
        sb.append(", eventDescription='").append(eventDescription).append('\'');
        sb.append(", opTraceId='").append(opTraceId).append('\'');
        sb.append(", userId=").append(userId);
        sb.append('}');
        return sb.toString();
    }
}
