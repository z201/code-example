package cn.z201.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * @author z201.coding@gmail.com
 * @date 2020-08-30
 **/
public class WebDriverTools {


    /**
     * 创建Chrome浏览器驱动
     * 需要系统已经安装好了chrome浏览器和chromedriver已加入到系统PATH环境变量中
     * @return
     */
    public static WebDriver chromeDriver(Object proxyServer) {
        return chromeDriver(proxyServer, false);
    }

    /**
     *
     * @param proxyServer 代理地址
     * @param view 是否打开浏览器
     * @return
     */
    public static WebDriver chromeDriver(Object proxyServer, Boolean view) {
        String osName = System.getProperties().getProperty("os.name");
        String path = null;
        if (osName.equals("Linux")) {
            path = "/usr/bin/chromedriver";
        }
        if (osName.equals("Mac OS X")) {
            path = "/usr/local/bin/chromedriver";
        }
        System.setProperty("webdriver.chrome.chromedriver", path);
//        ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(new File(path)).usingAnyFreePort().build();
        ChromeOptions chromeOptions = new ChromeOptions();
        if (null != proxyServer) {
            chromeOptions.addArguments("--proxy-server=http://" + proxyServer);
        }
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--no-sandbox"); //Bypass OS security model
        chromeOptions.addArguments("--disable-web-security");
        chromeOptions.addArguments("--ignore-urlfetcher-cert-requests");
        chromeOptions.addArguments("--disable-renderer-backgrounding");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--log-level=3"); // set log level
        chromeOptions.addArguments("--silent");
        chromeOptions.addArguments("lang=zh_CN.UTF-8");
        chromeOptions.addArguments("user-agent=\"" + SpiderProxyTools.randomUserAgent() + "\"");
        // 创建无界面浏览器对象
        chromeOptions.setHeadless(view);
        return new ChromeDriver(chromeOptions);
    }
}
