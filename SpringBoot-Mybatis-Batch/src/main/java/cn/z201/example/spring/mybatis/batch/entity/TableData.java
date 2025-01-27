package cn.z201.example.spring.mybatis.batch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试数据表
 * </p>
 *
 * @author z201.coding@gmail.com
 * @since 2022-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class TableData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    private Long createTime;

}
