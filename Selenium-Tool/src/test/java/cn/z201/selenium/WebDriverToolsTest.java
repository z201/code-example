package cn.z201.selenium;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

class WebDriverToolsTest {

    @Test
    public void init(){
        WebDriver driver = WebDriverTools.chromeDriver(null,false);
        String url = "https://www.baidu.com/";
        try {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.get(url);
        }catch (Exception e){
            driver.navigate().refresh();
        }
        String html = driver.getPageSource();
        Document document = Jsoup.parseBodyFragment(html);
        System.out.println(document.toString());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.navigate().refresh();
        driver.close();
    }

}