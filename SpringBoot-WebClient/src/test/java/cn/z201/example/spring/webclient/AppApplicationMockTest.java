package cn.z201.example.spring.webclient;

import cn.z201.example.spring.webclient.AppController;
import cn.z201.example.spring.webclient.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ExtendWith(SpringExtension.class)
@ActiveProfiles("dev")
@WebFluxTest
public class AppApplicationMockTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AppController appController;

    @Test
    @Disabled
    public void get() {
        UserVo user = new UserVo("1", "张三");
        Mockito.when(appController.info("1")).thenReturn(Mono.just(user));
        EntityExchangeResult<UserVo> result = webTestClient.get().uri("/1").exchange().expectStatus().isOk()
                .expectBody(UserVo.class).returnResult();
        log.info("{}", result);
    }

    @Test
    @Disabled
    public void list() {
        List<UserVo> userList = new ArrayList<>();
        userList.add(new UserVo("1", "张三"));
        userList.add(new UserVo("2", "王武"));
        Mockito.when(appController.list()).thenReturn(Flux.fromIterable(userList));
        EntityExchangeResult<List<UserVo>> result = webTestClient.post().uri("/list").exchange().expectStatus().isOk()
                .expectBodyList(UserVo.class).returnResult();
        log.info("{}", result);
    }

}