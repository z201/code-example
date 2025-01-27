package cn.z201.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AppApplicationTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    @Disabled
    public void testGeo() {
        // 1.杭州市 蒋村商务中心 120.075338,30.294845
        // 2.杭州市 中节能西溪首座 120.081806,30.294907
        // 3.杭州市 西溪蝶园 120.070452,30.294221
        // 4.杭州市 西溪财富中心 120.077854,30.296342
        // 5.杭州市 九橙 西溪创投中心 120.055468,30.284523
        Map<String, Point> points = new HashMap<>();
        points.put("1", new Point(120.075338, 30.294845));
        points.put("2", new Point(120.081806, 30.294907));
        points.put("3", new Point(120.070452, 30.294221));
        points.put("4", new Point(120.077854, 30.296342));
        points.put("5", new Point(120.055468, 30.284523));
        Long result = redisTemplate.opsForGeo().add("site", points);
        log.info("{}", result);
        Set<String> keys = redisTemplate.keys("*");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        log.info("size -> {} ", keys.size());
        List<Point> pointList = redisTemplate.opsForGeo().position("site", "1", "2");
        log.info("pointList -> {} ", gson.toJson(pointList));
        /**
         * 	METERS(6378137, "m"),
         * 	KILOMETERS(6378.137, "km"),
         * 	MILES(3963.191, "mi"),
         * 	FEET(20925646.325, "ft");
         */
        Distance distance = redisTemplate.opsForGeo().distance("site", "1", "2", RedisGeoCommands.DistanceUnit.KILOMETERS);
        log.info("distance -> {} ", gson.toJson(distance));
        Circle circle = new Circle(new Point(120.075338, 30.294845), new Distance(500, RedisGeoCommands.DistanceUnit.KILOMETERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance()
                .includeCoordinates()
                .sortAscending();
        log.info("geoLocationGeoResults -> {} ", gson.toJson(args));
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoLocationGeoResults = redisTemplate.opsForGeo().radius("site", circle, args);
        log.info("geoLocationGeoResults -> {} ", gson.toJson(geoLocationGeoResults));
        keys.forEach(item -> {
                    if (!Objects.equals("hz", item)) {
                        redisTemplate.delete(item);
                    }
                    log.info(" keys - > {} ", item);
                }
        );
    }
}