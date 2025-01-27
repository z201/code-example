package cn.z201.example.spring.spi.jdk;

/**
 * @author z201.coding@gmail.com
 **/
@LanguageType(language = "english")
public class SpeakEnglish implements SpeakSPIService{
    @Override
    public Object echo() {
        return "english : hello";
    }
}
