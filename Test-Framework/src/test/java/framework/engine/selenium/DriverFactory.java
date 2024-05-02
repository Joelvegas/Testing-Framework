package framework.engine.selenium;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class DriverFactory {

    public static ThreadLocal<WebDriver> hiloLocal = new ThreadLocal<>();

    /**
     * inicializa el WebDriver segun la seleccion del browser
     * @param pBrowser: chrome | firefox | edge
     * @return WebDriver
     */
    public WebDriver inicializarDriver(String pBrowser){

        System.out.println("browser value is: "+pBrowser);
        switch (pBrowser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                hiloLocal.set(new FirefoxDriver());
                getDriver().manage().deleteAllCookies();
                getDriver().manage().window().maximize();
                getDriver().manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
                return getDriver();
            case "edge":
                WebDriverManager.edgedriver().setup();
                hiloLocal.set(new EdgeDriver());
                getDriver().manage().deleteAllCookies();
                getDriver().manage().window().maximize();
                getDriver().manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
                return getDriver();
            case "chrome":
                WebDriverManager.chromedriver().setup();
                hiloLocal.set(new ChromeDriver());
                getDriver().manage().deleteAllCookies();
                getDriver().manage().window().maximize();
                getDriver().manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
                return getDriver();
            default:
                throw new RuntimeException("Navegador no configurado: " + pBrowser);
        }
    }

    /**
     * Configura el driver para el navegador elegido y retorna el driver.
     * @param pWebDriver
     * @return WebDriver
     */
    public WebDriver createWebDriver(String pWebDriver) {
        switch (pWebDriver) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();
            case "edge":
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver();
            case "chrome":
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();
            default:
                throw new RuntimeException("Error en el webdriver: " + pWebDriver);
        }
    }

    /**
     * retorna el WebDriver desde el ThreadLocal
     * @return WebDriver
     */
    public static synchronized WebDriver getDriver(){
        return hiloLocal.get();
    }
}