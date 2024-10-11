package org.tyaa.training.current.server.test.system;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.tyaa.training.current.server.test.system.utils.FilePropertiesStore;
import org.tyaa.training.current.server.test.system.utils.interfaces.IPropertiesStore;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Map;

/**
 * Фабрика объектов WebDriver для тестов с доступом к браузерам
 * */
public class WebDriverFactory {

    private static final IPropertiesStore properties = new FilePropertiesStore();

    private static Map<String, String> browsers;
    private final String browser;
    private static WebDriverFactory chromeWebDriverFactoryInstance;
    private static WebDriverFactory geckoWebDriverFactoryInstance;
    private final ThreadLocal<WebDriver> webDriverThreadLocal = new ThreadLocal<>();

    static {
        WebDriverFactory.browsers = properties.getSupportedBrowsers();
    }

    private WebDriverFactory(String browser){
        this.browser = browser;
    }

    public synchronized static WebDriverFactory getInstance(String browser) {

        if (browser.equals("chrome")) {
            return (WebDriverFactory.chromeWebDriverFactoryInstance == null)
                    ? WebDriverFactory.chromeWebDriverFactoryInstance = new WebDriverFactory(browser)
                    : WebDriverFactory.chromeWebDriverFactoryInstance;
        } else {
            return (WebDriverFactory.geckoWebDriverFactoryInstance == null)
                    ? WebDriverFactory.geckoWebDriverFactoryInstance = new WebDriverFactory(browser)
                    : WebDriverFactory.geckoWebDriverFactoryInstance;
        }
    }

    public synchronized WebDriver getDriver() /*throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException*/ {

        if (!WebDriverFactory.browsers.containsKey(this.browser)) {
            System.out.println("WebDriverFactory.browsers:");
            System.out.println(WebDriverFactory.browsers);
            throw new IllegalArgumentException(String.format("Driver %s Not Found", this.browser));
        }
        // System.out.println("mode = " + System.getProperty("mode"));
        System.out.println("os = " + properties.getOs());
        // если для потока выполнения, вызвавшего этот код, ранее был создан объект WebDriver
        if (webDriverThreadLocal.get() != null) {
            // ссылка на него возвращается из данного метода, и экземпляр метода завершает свою работу
            return webDriverThreadLocal.get();
        }
        // иначе - создаём и настраиваем объект WebDriver
        /* System.setProperty(
                String.format("system.%s.driver", this.browser),
                String.format(
                        "src/main/resources/drivers/%sdriver%s",
                        this.browser,
                        properties.getOs().equals("windows") ? ".exe" : ""
                )
        ); */
        String browserClassName = WebDriverFactory.browsers.get(this.browser);
        System.out.println("browserClassName: " + browserClassName);
        /* Class<?> browserDriverClass =
                Class.forName(String.format(
                        "org.openqa.selenium.%s.%sDriver",
                        browserClassName,
                        StringUtils.capitalize(browserClassName))
                );
        WebDriver driver = (WebDriver) browserDriverClass.getConstructor().newInstance(); */
        WebDriver driver;
        if (browserClassName.equals("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browserClassName.equals("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else {
            throw new IllegalArgumentException(String.format("Driver %s Not Found", this.browser));
        }
        System.out.println("driver: " + driver);
        driver.manage()
                .timeouts()
                .implicitlyWait(properties.getImplicitlyWaitSeconds());
        webDriverThreadLocal.set(driver);
        return webDriverThreadLocal.get();
    }

    public void closeDriver() {
        try {
            webDriverThreadLocal.get().quit();
        }
        catch (Exception ex) {
            System.err.println("ERROR: Can not close WebDriver!");
        } finally {
            webDriverThreadLocal.remove();
        }
    }
}
