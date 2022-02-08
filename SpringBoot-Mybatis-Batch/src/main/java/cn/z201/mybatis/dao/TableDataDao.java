package cn.z201.mybatis.dao;

import cn.z201.mybatis.entity.TableData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 测试数据表 Mapper 接口
 * </p>
 *
 * @author z201.coding@gmail.com
 * @since 2022-02-08
 */
public interface TableDataDao extends BaseMapper<TableData> {

    int batchInsert(@Param("tableDataList") List<TableData> tableDataList);

    void truncate();
}
