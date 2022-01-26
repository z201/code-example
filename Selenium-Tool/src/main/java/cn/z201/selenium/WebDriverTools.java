package cn.z201.selenium;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
    public static WebDriver chromeDriver() {
        return chromeDriver(null, true);
    }

    /**
     * 创建Chrome浏览器驱动
     * 需要系统已经安装好了chrome浏览器和chromedriver已加入到系统PATH环境变量中
     * @return
     */
    public static WebDriver chromeDriver(Object proxyServer) {
        return chromeDriver(proxyServer, true);
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

    public static void killChromeDriver() throws IOException {
        int exitValue = -1;
        BufferedReader bufferedReader = null;
        try {
            String content = "ps -ef|grep 'chrome' | grep -v grep | awk '{print $2}' | xargs kill -9";
            List<String> shell = new ArrayList<>();
            shell.add("sh");
            shell.add("-c");
            shell.add(content);
            ProcessBuilder processBuilder = new ProcessBuilder(shell);
            Process process = processBuilder.start();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));
            // command log
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
            exitValue = process.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        if (exitValue == 0) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAIL");
        }
    }
}
