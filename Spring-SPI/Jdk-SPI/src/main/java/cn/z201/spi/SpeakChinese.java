package cn.z201.spi;

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
