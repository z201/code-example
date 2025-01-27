package cn.z201.test.mock;

import cn.z201.example.spring.test.mock.AppApplication;
import cn.z201.example.spring.test.mock.MysqlVersionRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("dev")
public class MysqlVersionRepositoryTest {

    @MockBean
    private MysqlVersionRepository mysqlVersionRepository;

    @Test
    @Disabled
    public void testMockVersion() {
        Mockito.when(mysqlVersionRepository.version()).thenReturn("5.7.36");
        String version = mysqlVersionRepository.version();
        Assertions.assertThat(version).isNotNull();
        Assertions.assertThat(version).isEqualTo("5.7.36");
    }

}