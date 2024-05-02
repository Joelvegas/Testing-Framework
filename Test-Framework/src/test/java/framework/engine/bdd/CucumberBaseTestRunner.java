package framework.engine.bdd;

import aut.testcreation.tasks.advantage.Login;
import framework.engine.selenium.DriverFactory;

import static framework.engine.selenium.ReportFunctionalities.*;
import static framework.engine.utils.Constants.BROWSER;

import framework.engine.utils.LoadProperties;
import io.cucumber.java8.Scenario;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.qameta.allure.Allure;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.runner.RunWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm"},
        glue = {"aut.testcreation.steps","framework.engine.bdd"},
        tags = {"@fast", "@pruebas"},
        features = {"src/test/java/aut/testcreation/features"})
@CommonsLog
public class CucumberBaseTestRunner {

    public static WebDriver driver;
    private static DriverFactory driverFactory;
    public static Login login;
    static Properties properties;

    /**
     * Se ejecuta antes de cada escenario. Carga las propiedades, obtiene el nombre del escenario actual e inicializa el driver con el browser por defecto.
     * @param pEscenario
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static void setUp(Scenario pEscenario) throws IOException, InvalidFormatException {
        properties = LoadProperties.loadProperties();
        LoadProperties.getEscenario(pEscenario.getName());
        webDriverSetup();
    }

    /**
     * Inicializa el driver con el browser por defecto el cual se obtiene desde el archivo config.properties.
     * @throws IOException
     * @throws InvalidFormatException
     */
    private static void webDriverSetup() throws IOException, InvalidFormatException {
        driverFactory = new DriverFactory();
        driver = driverFactory.createWebDriver(BROWSER);
        if (driver != null) {
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(3000));
            driver.manage().window().maximize();
        }
        newReport();
        login = new Login(driver);
    }

    /**
     * Se ejecuta al final de cada escenario. Finaliza el reporte de Word y saca una screenshot si el escenario actual falla.
     * @param pEscenario
     * @throws IOException
     */
    public static void tearDown(Scenario pEscenario) throws IOException {
        finishReport();
        if (pEscenario.isFailed()) {
            try {
                File lScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);;
                InputStream lTargetStream = new FileInputStream(lScreenshot);
                System.setProperty("allure.results.directory", "build/allure-results");
                Allure.addAttachment("Screenshot on fail", "image/png", lTargetStream, "png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (driver != null) {
            driver.close();
        }
    }
}
