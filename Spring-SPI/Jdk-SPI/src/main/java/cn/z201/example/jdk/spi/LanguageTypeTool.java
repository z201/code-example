package cn.z201.example.jdk.spi;

import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * @author z201.coding@gmail.com
 **/
public class LanguageTypeTool {

    public static SpeakSPIService language(String language) {
        ServiceLoader<SpeakSPIService> serviceLoader = ServiceLoader.load(SpeakSPIService.class,
                Thread.currentThread().getContextClassLoader());
        Iterator<SpeakSPIService> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            SpeakSPIService speakSPIService = iterator.next();
            Class clazz = speakSPIService.getClass();
            LanguageType languageType = (LanguageType) clazz.getDeclaredAnnotation(LanguageType.class);
            if (null != languageType) {
                if (Objects.equals(language, languageType.language())) {
                    return speakSPIService;
                }
            }
        }
        return null;
    }

}
