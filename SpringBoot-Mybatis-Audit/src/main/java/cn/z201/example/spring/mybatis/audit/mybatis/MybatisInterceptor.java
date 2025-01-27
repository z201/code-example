package cn.z201.example.spring.mybatis.audit.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.time.Clock;
import java.util.*;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }), })
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Long startTime = Clock.systemDefaultZone().millis();
        try {
            Object[] args = invocation.getArgs();
            MappedStatement mappedStatement = (MappedStatement) args[0];
            Object entity = args[1];
            if (SqlCommandType.INSERT.name().equalsIgnoreCase(mappedStatement.getSqlCommandType().name())) {
                // 获取实体集合
                List<Object> entitySet = getEntitySet(entity);
                // 批量设置id
                for (Object object : entitySet) {
                    insertProcess(object);
                }
            }
            else if (SqlCommandType.UPDATE.name().equalsIgnoreCase(mappedStatement.getSqlCommandType().name())) {
                // 获取实体集合
                List<Object> entitySet = getEntitySet(entity);
                for (Object object : entitySet) {
                    updateProcess(object);
                }
            }
            return invocation.proceed();
        }
        finally {
            Long timeConsuming = Clock.systemDefaultZone().millis() - startTime;
            log.info("SQL RunTime {} ms", timeConsuming);
        }
    }

    /**
     * object是需要插入的实体数据,它可能是对象,也可能是批量插入的对象。 如果是单个对象,那么object就是当前对象
     * 如果是批量插入对象，那么object就是一个map集合,key值为"list",value为ArrayList集合对象
     */
    private List<Object> getEntitySet(Object object) {
        List<Object> set = new ArrayList<>();
        if (object instanceof Map) {
            Iterator entries = ((Map<?, ?>) object).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                Object value = entry.getValue();
                if (value instanceof Collection) {
                    set.addAll((Collection<?>) value);
                }
                else {
                    set.add(value);
                }
            }
        }
        else {
            // 单个插入对象
            set.add(object);
        }
        return set;
    }

    private void insertProcess(Object object) {
        Long time = Clock.systemDefaultZone().millis();
        if (object instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) object;
            baseEntity.setId(SnowflakeTool.getInstance().nextId());
            if (Objects.isNull(baseEntity.getIsEnable())) {
                baseEntity.setIsEnable(true);
            }
            if (Objects.isNull(baseEntity.getCreateTime())) {
                baseEntity.setCreateTime(time);
            }
            if (Objects.isNull(baseEntity.getUpdateTime())) {
                baseEntity.setUpdateTime(time);
            }
        }
    }

    private void updateProcess(Object object) {
        Long time = Clock.systemDefaultZone().millis();
        if (object instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) object;
            if (Objects.isNull(baseEntity.getUpdateTime())) {
                baseEntity.setUpdateTime(time);
            }
        }
    }

}
