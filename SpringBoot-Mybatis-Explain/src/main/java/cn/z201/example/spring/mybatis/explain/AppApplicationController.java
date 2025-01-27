package cn.z201.example.mybatis.explain;

import cn.z201.example.mybatis.explain.dao.AccountDao;
import cn.z201.example.mybatis.explain.entity.Account;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
public class AppApplicationController {

    @Resource
    AccountDao accountDao;

    @RequestMapping(value = "")
    public Object index() {
        Account account = TestTool.create();
        accountDao.insert(account);
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", account);
        return data;
    }

    @RequestMapping(value = "list")
    public Object list() {
        List<Account> accountList = accountDao.selectList(null);
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", accountList);
        return data;
    }

}
