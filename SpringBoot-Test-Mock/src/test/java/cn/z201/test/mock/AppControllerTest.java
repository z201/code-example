package cn.z201.test.mock;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Slf4j
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppControllerTest {

    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private AppController appController;

    @Test
    public void testVersion() throws Exception {
        // 准备一组测试数据
        Map<String,AppVo> testData = new HashMap<>();
        testData.put("undefined",new AppVo("", "undefined"));
        testData.put("mysql",new AppVo("mysql", "1.0.0"));
        testData.put("redis",new AppVo("redis", "2.0.0"));
        // 循环测试
        for (String s : testData.keySet()) {
            Mockito.when(appController.version(s)).thenReturn(testData.get(s));
            AppVo appVo = out(s,AppVo.class);
            Assertions.assertThat(appVo).isNotNull();
            Assertions.assertThat(appVo.getVersion()).isEqualTo(testData.get(s).getVersion());
        }
    }

    private <T> T out(String key,Class<T> clazz) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/"+key)
                        .contentType(APPLICATION_JSON_UTF8_VALUE)
                        // 设置返回值类型为utf-8，否则默认为ISO-8859-1
                        .accept(APPLICATION_JSON_UTF8_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        mvcResult.getResponse().setCharacterEncoding("UTF-8");
        log.info("{}",mvcResult.getResponse().getContentAsString());
        return JsonTool.toBean(mvcResult.getResponse().getContentAsString(),clazz);
    }

}