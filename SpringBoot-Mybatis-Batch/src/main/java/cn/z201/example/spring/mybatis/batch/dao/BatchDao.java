package cn.z201.example.spring.mybatis.batch.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * @author z201.coding@gmail.com
 **/
@Component
@Slf4j
public class BatchDao {

    private final static int MAX_SIZE = 500;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public <T, M> boolean batchSave(Collection<T> entityList, Class<M> mapper, BiConsumer<M, Collection<T>> fuc) {
        SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        M entityMapper = sqlSession.getMapper(mapper);
        try {
            fuc.accept(entityMapper, entityList);
            sqlSession.flushStatements();
            sqlSession.clearCache();
            return true;
        }
        catch (Exception e) {
            log.error("{}", e.getMessage());
            sqlSession.rollback();
        }
        finally {
            sqlSession.close();
        }
        return false;
    }

}
