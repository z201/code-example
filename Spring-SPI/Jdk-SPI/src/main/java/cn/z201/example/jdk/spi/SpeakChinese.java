package cn.z201.example.spring.spi.jdk;

/**
 * @author z201.coding@gmail.com
 **/
@LanguageType(language = "chinese")
public class SpeakChinese implements SpeakSPIService {

    @Override
    public Object echo() {
        return "chinese : 你好";
    }
}
