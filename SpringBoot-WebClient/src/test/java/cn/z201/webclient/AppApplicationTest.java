package cn.z201.webclient;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@AutoConfigureWebFlux
@AutoConfigureWebTestClient
public class AppApplicationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AppController appController;

    @Test
    @Disabled
    public void get() {
        UserVo user = new UserVo("1", "张三");
        Mockito.when(appController.info("1")).thenReturn(Mono.just(user));
        EntityExchangeResult<UserVo> result = webTestClient.get()
                .uri("/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserVo.class)
                .returnResult();
        log.info("{}", result);
    }

    @Test
    @Disabled
    public void ip() {
        EntityExchangeResult<String> result = webTestClient.get()
                .uri("/ip")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult();
        log.info("{}", result);
    }

    @Test
    @Disabled
    public void list() {
        List<UserVo> userList = new ArrayList<>();
        userList.add(new UserVo("1", "张三"));
        userList.add(new UserVo("2", "王武"));
        Mockito.when(appController.list()).thenReturn(Flux.fromIterable(userList));
        EntityExchangeResult<List<UserVo>> result = webTestClient.post()
                .uri("/list")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserVo.class)
                .returnResult();
        log.info("{}", result);
    }


    @Test
    @Disabled
    public void i() {
        String url = "https://myip.ipip.net/";
        WebClient webClient = WebClient.create();
        String body = webClient.get()
                .uri(url)
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // 响应的格式json
                .acceptCharset(StandardCharsets.UTF_8) // 编码集 utf-8
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        log.info("doOnSuccess");
                        return response.bodyToMono(String.class);
                    } else {
                        log.info("doOnError");
                        return response.createException().flatMap(Mono::error);
                    }
                })
                .doOnError(t -> log.error("Error: ", t)) // 异常回调
                .doFinally(s -> log.info("Finally ")) // Finally 回调
                .doOnSuccess(s-> log.info("{}",s))
                .subscribeOn(Schedulers.single())
                .block();
    }

}