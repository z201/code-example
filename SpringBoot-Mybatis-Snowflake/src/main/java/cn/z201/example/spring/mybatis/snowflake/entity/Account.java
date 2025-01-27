package cn.z201.example.spring.mybatis.snowflake.entity;

import cn.z201.example.spring.mybatis.snowflake.config.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 账号表
 * </p>
 *
 * @author z201.coding@gmail.com
 * @since 2022-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
