package cn.z201.mdc.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
@Slf4j
public class AppApplicationController {

    @Async
    @RequestMapping(value = "")
    public Object index() {
        log.info("index");
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        return data;
    }

}
