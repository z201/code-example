package cn.z201.webclient;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author z201.coding@gmail.com
 */
@RestController
@RequestMapping("/")
@Slf4j
public class AppController {

    @Autowired
    private WebClient webClient;

    @PostMapping("/list")
    public Flux<UserVo> list() {
        List<UserVo> userList = new ArrayList<>();
        userList.add(new UserVo("1", "张三"));
        userList.add(new UserVo("2", "王五"));
        return Flux.fromIterable(userList);
    }

    @GetMapping("/{id}")
    public Mono<UserVo> info(@PathVariable(value = "id") String id) {
        return Mono.just(new UserVo(id, "某某"));
    }

    @GetMapping("/ip")
    public Mono<String> ip() {
        String url = "https://myip.ipip.net/";
        Mono<String> body = webClient.get()
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
                .doFinally(s -> log.error("Finally ")) // Finally 回调
                .subscribeOn(Schedulers.single());
        return body;
    }

    // 基于函数编程
//    @Bean
//    public RouterFunction<ServerResponse> ip() {
//        return RouterFunctions.route(RequestPredicates.GET("/ip"),
//                request -> {
//                    String url = "https://myip.ipip.net/";
//                    String body = webClient.get()
//                            .uri(url)
//                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // 响应的格式json
//                            .acceptCharset(StandardCharsets.UTF_8) // 编码集 utf-8
//                            .exchangeToMono(response -> {
//                                if (response.statusCode().equals(HttpStatus.OK)) {
//                                    return response.bodyToMono(String.class);
//                                } else {
//                                    return response.createException().flatMap(Mono::error);
//                                }
//                            })
//                            .doOnError(t -> log.error("Error: ", t)) // 异常回调
//                            .doFinally(s -> log.error("Finally ")) // Finally 回调
//                            .subscribeOn(Schedulers.single()) // 在task线程中执行
//                            .block();
//                    // 返回列表
//                    return ServerResponse.ok().bodyValue(body);
//                });
//    }


}
