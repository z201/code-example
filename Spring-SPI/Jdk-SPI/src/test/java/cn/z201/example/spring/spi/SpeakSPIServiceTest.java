package cn.z201.example.jdk.spi;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.ServiceLoader;


class SpeakSPIServiceTest {

    @Test
    public void setUp() {
        ServiceLoader<SpeakSPIService> serviceLoader = ServiceLoader.load(SpeakSPIService.class, Thread.currentThread().getContextClassLoader());
        Iterator<SpeakSPIService> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            SpeakSPIService speakSPIService = iterator.next();
            System.out.println(speakSPIService.echo());
        }
        SpeakSPIService speakSPIService = LanguageTypeTool.language("chinese");
        if (null != speakSPIService) {
            System.out.println(speakSPIService.echo());
        }
        speakSPIService = LanguageTypeTool.language("english");
        if (null != speakSPIService) {
            System.out.println(speakSPIService.echo());
        }
    }

}