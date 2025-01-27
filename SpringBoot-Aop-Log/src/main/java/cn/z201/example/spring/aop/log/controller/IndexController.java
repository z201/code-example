package cn.z201.example.spring.aop.log.controller;

import cn.z201.example.spring.aop.log.config.aspect.annotation.MonitorAnnotation;
import cn.z201.example.spring.aop.log.config.core.AjaxResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
public class IndexController {

    @RequestMapping
    @MonitorAnnotation(title = "index")
    public Object index(@RequestBody(required = false) Object object, HttpServletRequest request,
            HttpServletResponse response) {
        return AjaxResult.success();
    }

}
