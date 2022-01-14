package cn.z201.test.mock;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@Service
public class AppServiceImpl implements AppServiceI {

    @Resource
    private RedisVersionRepository redisVersionRepository;

    @Resource
    private MysqlVersionRepository mysqlVersionRepository;

    @Override
    public AppVo version(String key) {
        String version = Strings.EMPTY;
        switch (key){
            case "mysql" :
                version = mysqlVersionRepository.version();
                break;
            case "redis" :
                version = redisVersionRepository.version();
                break;
            default : version = "undefined";
        }
        AppVo appVo = new AppVo();
        appVo.setKey(key);
        appVo.setVersion(version);
        return appVo;
    }
}
