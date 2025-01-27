package cn.z201.example.jdk.spi;

/**
 * @author z201.coding@gmail.com
 **/
@LanguageType(language = "german")
public class SpeakGerman implements SpeakSPIService {

    @Override
    public Object echo() {
        return "german : hallo";
    }

}
