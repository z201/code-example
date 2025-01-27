package cn.z201.example.spring.webclient;

import cn.z201.example.spring.webclient.AppApplication;
import cn.z201.example.spring.webclient.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@AutoConfigureWebTestClient
public class AppApplicationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Disabled
    public void get() {
        EntityExchangeResult<UserVo> result = webTestClient.get().uri("/1").exchange().expectStatus().isOk()
                .expectBody(UserVo.class).returnResult();
        log.info("{}", result);
    }

    @Test
    @Disabled
    public void ip() {
        EntityExchangeResult<String> result = webTestClient.get().uri("/ip/info").exchange().expectStatus().isOk()
                .expectBody(String.class).returnResult();
        log.info("{}", result);
    }

    @Test
    @Disabled
    public void list() {
        EntityExchangeResult<List<UserVo>> result = webTestClient.post().uri("/list").exchange().expectStatus().isOk()
                .expectBodyList(UserVo.class).returnResult();
        log.info("{}", result);
    }

}