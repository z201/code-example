package cn.z201.example.spring.test.mock;

import cn.z201.example.spring.test.mock.AppApplication;
import cn.z201.example.spring.test.mock.AppController;
import cn.z201.example.spring.test.mock.AppVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebFlux
@ActiveProfiles("dev")
public class AppControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AppController appController;

    @Test
    public void testVersion() throws Exception {
        // 准备一组测试数据
        Map<String, AppVo> testData = new HashMap<>();
        testData.put("undefined", new AppVo("", "undefined"));
        testData.put("mysql", new AppVo("mysql", "1.0.0"));
        testData.put("redis", new AppVo("redis", "2.0.0"));
        // 循环测试
        for (String s : testData.keySet()) {
            Mockito.when(appController.version(s)).thenReturn(testData.get(s));
            EntityExchangeResult<String> result = webTestClient.post().uri("/" + s)
                    .contentType(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectHeader()
                    .contentType(MediaType.APPLICATION_JSON).expectBody(String.class).returnResult();
            log.info("{}", result);
        }

    }

}