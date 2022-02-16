package cn.z201.spi;

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
