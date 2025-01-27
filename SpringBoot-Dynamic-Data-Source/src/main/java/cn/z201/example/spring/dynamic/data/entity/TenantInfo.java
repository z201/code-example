package cn.z201.example.spring.dynamic.data.entity;

import cn.z201.example.spring.dynamic.data.mybatis.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author z201.coding@gmail.com
 * @since 2022-02-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 数据源url
     */
    private String datasourceUrl;

    /**
     * 数据源用户名
     */
    private String datasourceUsername;

    /**
     * 数据源密码
     */
    private String datasourcePassword;

    /**
     * 数据源驱动
     */
    private String datasourceDriver;

    /**
     * 系统账号
     */
    private String systemAccount;

    /**
     * 账号密码
     */
    private String systemPassword;

    /**
     * 系统PROJECT
     */
    private String systemProject;

}
