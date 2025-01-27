package cn.z201.example.jdk.spi;

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
