package cn.z201.example.spring.scheduling.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author z201.coding@gmail.com
 **/
@Component
@Lazy(value = false)
@Slf4j
public class ScheduleTasks {

    private final static String DATE_PATTERN_S = "yyyy-MM-dd HH:mm:ss";

    private String conversionNowFormat() {
        Instant instant = Instant.ofEpochMilli(currentTimeMillis());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern(DATE_PATTERN_S));
    }

    private Long currentTimeMillis() {
        return Clock.systemDefaultZone().millis();
    }

    @Scheduled(fixedDelay = 60000) // fixedDelay = 60000 表示当前方法执行完成 60000ms(1分钟) 后，Spring
                                   // scheduling会再次执行方法
    public void testFixDelay() {
        log.info("fixedDelay: 时间 :{}", conversionNowFormat());
    }

    @Scheduled(fixedRate = 60000) // fixedRate = 60000 表示当前方法开始执行 60000ms(1分钟) 后，Spring
                                  // scheduling会再次执行方法
    public void testFixedRate() {
        log.info("fixedRate: 时间 :{}", conversionNowFormat());
    }

    @Scheduled(initialDelay = 180000, fixedRate = 5000)
    // initialDelay = 180000 表示延迟 180000 (3秒) 执行第一次方法, 然後每 5000ms(5 秒) 再次执行方法
    public void testInitialDelay() {
        log.info("initialDelay: 时间 :{}", conversionNowFormat());
    }

    @Scheduled(cron = "0 0/1 * * * ?") // cron接受cron表示式，根据cron表示式规则执行
    public void testCron() {
        log.info("cron: 时间:{}", conversionNowFormat());
    }

}
