package cn.z201.example.spring.mybatis.explain.config;

import cn.hutool.db.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Clock;
import java.util.*;
import java.util.regex.Matcher;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "query",
                args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }) })
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Long startTime = Clock.systemDefaultZone().millis();
        List<ExplainResultDto> explainResultList = new ArrayList<>();
        try {
            final Object[] args = invocation.getArgs();
            final MappedStatement mappedStatement = (MappedStatement) args[0];
            final Executor executor = (Executor) invocation.getTarget();
            Object entity = args[1];
            if (SqlCommandType.SELECT.name().equalsIgnoreCase(mappedStatement.getSqlCommandType().name())) {
                BoundSql boundSql;
                if (args.length == 4) {
                    boundSql = mappedStatement.getBoundSql(entity);
                }
                else {
                    // 几乎不可能走进这里面,除非使用Executor的代理对象调用query[args[5]]
                    boundSql = (BoundSql) args[5];
                }
                String sql = getSql(boundSql, mappedStatement);
                log.info("EXPLAIN  \n {}", SqlUtil.formatSql(sql));
                Statement stmt = executor.getTransaction().getConnection().createStatement();
                stmt.execute("EXPLAIN " + sql + " ;");
                ResultSet rs = stmt.getResultSet();
                ExplainResultDto explainResultVo = null;
                while (rs.next()) {
                    explainResultVo = new ExplainResultDto();
                    explainResultVo.setId(rs.getString("id"));
                    explainResultVo.setSelectType(rs.getString("select_type"));
                    explainResultVo.setTable(rs.getString("table"));
                    explainResultVo.setPartitions(rs.getString("partitions"));
                    explainResultVo.setType(rs.getString("type"));
                    explainResultVo.setPossibleKeys(rs.getString("possible_keys"));
                    explainResultVo.setKey(rs.getString("key"));
                    explainResultVo.setKeyLen(rs.getString("key_len"));
                    explainResultVo.setRef(rs.getString("ref"));
                    explainResultVo.setRows(rs.getString("rows"));
                    explainResultVo.setFiltered(rs.getString("filtered"));
                    explainResultVo.setExtra(rs.getString("Extra"));
                    explainResultList.add(explainResultVo);
                }
            }
            return invocation.proceed();
        }
        finally {
            Long timeConsuming = Clock.systemDefaultZone().millis() - startTime;
            if (!CollectionUtils.isEmpty(explainResultList)) {
                log.info("{}", JsonTool.toString(explainResultList));
            }
            log.info("SQL RunTime {} ms", timeConsuming);
        }
    }

    /**
     * 生成要执行的SQL命令
     * @param boundSql
     * @param ms
     * @return
     */
    private String getSql(BoundSql boundSql, MappedStatement ms) {
        String sql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (!CollectionUtils.isEmpty(parameterMappings) && parameterObject != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    // 参数值
                    Object value;
                    // 获取参数名称
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 获取参数值
                        value = boundSql.getAdditionalParameter(propertyName);
                    }
                    else if (ms.getConfiguration().getTypeHandlerRegistry()
                            .hasTypeHandler(parameterObject.getClass())) {
                        // 如果是单个值则直接赋值
                        value = parameterObject;
                    }
                    else {
                        MetaObject metaObject = ms.getConfiguration().newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameter(value)));
                }
            }

        }
        return sql;
    }

    public String getParameter(Object parameter) {
        if (parameter instanceof String) {
            return "'" + parameter + "'";
        }
        return parameter.toString();
    }

}
