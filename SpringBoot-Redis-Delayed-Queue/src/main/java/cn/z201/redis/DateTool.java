package cn.z201.redis;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author z201.coding@gmail.com
 * @date 2021/12/14
 **/
public class DateTool {

    private final static String DATE_PATTERN = "yyyy-MM-dd";
    private final static String DATE_PATTERN_S = "yyyy-MM-dd HH:mm:ss";

    public static Long currentTimeMillis() {
        return Clock.systemDefaultZone().millis();
    }

    public static LocalDateTime localDateTime() {
        Instant instant = Instant.now();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static String conversionNowFormat() {
        Instant instant = Instant.ofEpochMilli(currentTimeMillis());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(DATE_PATTERN_S));
    }

    public static String conversionFormat(Long timeMillis) {
        Instant instant = Instant.ofEpochMilli(timeMillis);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(DATE_PATTERN_S));
    }

    public static LocalDateTime conversion(Long timeMillis) {
        Instant instant = Instant.ofEpochMilli(timeMillis);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static Long conversion(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static Boolean eqDay(LocalDateTime start, LocalDateTime end) {
        String startStr = start.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
        String endStr = end.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
        if (Objects.equals(startStr, endStr)) {
            return true;
        }
        return false;
    }
}

