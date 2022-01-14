package cn.z201.test.mock;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AppServiceITest {

    @MockBean
    private AppServiceI appService;

    @Test
    public void testVersion() {
        String key = "mysql";
        Mockito.when(appService.version("")).thenReturn(new AppVo("", "undefined"));
        Mockito.when(appService.version("mysql")).thenReturn(new AppVo("mysql", "5.7.36"));
        Mockito.when(appService.version("redis")).thenReturn(new AppVo("redis", "6.2.6"));
        AppVo appVo = appService.version(key);
        Assertions.assertThat(appVo).isNotNull();
        log.info("{}",JsonTool.toString(appVo));
        Assertions.assertThat(appVo.getVersion()).isEqualTo("5.7.36");
        appVo = appService.version("redis");
        Assertions.assertThat(appVo).isNotNull();
        log.info("{}",JsonTool.toString(appVo));
        Assertions.assertThat(appVo.getVersion()).isEqualTo("6.2.6");
        appVo = appService.version("");
        Assertions.assertThat(appVo).isNotNull();
        log.info("{}",JsonTool.toString(appVo));
        Assertions.assertThat(appVo.getVersion()).isEqualTo("undefined");
    }

}