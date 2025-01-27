package cn.z201.redis;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author z201.coding@gmail.com
 **/
@Getter
@Setter
@Builder
public class OrderBo {

    private Long id;

    private Long createTime;

    private Long orderDeadlineTime;

}
