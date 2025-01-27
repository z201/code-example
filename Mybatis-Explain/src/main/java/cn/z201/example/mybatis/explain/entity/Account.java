package cn.z201.example.mybatis.explain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 账号表
 * </p>
 *
 * @author z201.coding@gmail.com
 * @since 2022-02-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据是否有效 1 有效 0 无效
     */
    private Boolean isEnable;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 手机号(当前用户身份唯一标识)
     */
    private String phoneNumber;

    /**
     * 邮箱 保留字段，考虑邮箱登录。
     */
    private String email;

    /**
     * 加盐后密码
     */
    private String saltPassword;

    /**
     * 盐 扩展字段
     */
    private String salt;

    /**
     * 用户名称
     */
    private String usrName;


}
