package cn.z201.mybatis;

import cn.z201.mybatis.dao.AccountDao;
import cn.z201.mybatis.entity.Account;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 * @date 2022/1/7
 **/
@RestController
public class AppApplicationController {

    @Resource
    AccountDao accountDao;

    @Autowired
    AccountToolService accountToolService;

    @RequestMapping(value = "")
    public Object index() {
        accountDao.insert(accountToolService.create());
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.last("ORDER BY id DESC  LIMIT 0 , 5");
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", accountDao.selectList(accountQueryWrapper));
        return data;
    }
}
