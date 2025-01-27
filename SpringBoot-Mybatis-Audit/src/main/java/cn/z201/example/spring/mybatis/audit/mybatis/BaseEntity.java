package cn.z201.example.spring.mybatis.audit.mybatis;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author z201.coding@gmail.com
 **/
@Data
public class BaseEntity {

    /**
     * Entity基类字段,这里是数据库的字段
     */
    public static final String[] BASE_ENTITY = { "id", "is_enable", "create_time", "update_time" };

    /**
     * 主键
     */
    private Long id;

    /**
     * 数据是否有效 1 有效 0 无效
     */
    @TableField("is_enable")
    private Boolean isEnable;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Long updateTime;

}
