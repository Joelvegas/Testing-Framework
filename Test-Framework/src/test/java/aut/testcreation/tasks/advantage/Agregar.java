package aut.testcreation.tasks.advantage;

import framework.engine.selenium.SeleniumWrapper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import static aut.testcreation.pages.Page_Advantage.*;
import static framework.engine.selenium.ReportFunctionalities.reporte;
import static framework.engine.utils.Constants.*;
import static framework.engine.utils.ExcelReader.*;

public class Agregar extends SeleniumWrapper{
    public Agregar(WebDriver driver) {
        super(driver);
    };

    /**
     * Agrega al carrito un ratón de la posición pasada por parámetro
     * @param pPosicion
     * @throws IOException
     * @throws InvalidFormatException
     * @throws AWTException
     */
    public static void ratonAlCarrito(String pPosicion) throws IOException, InvalidFormatException, AWTException {
        navegarURL(URL_ADVANTAGE);
        clickear(lblMice);
        reporte.reportarEvento("", true, false);
        List<WebElement> lLIstaElementos = encontarElementos(lstMice);
        WebElement lElemento = lLIstaElementos.get(Integer.parseInt(extractDataToExcel(pPosicion)));
        clickearWE(lElemento);
        reporte.reportarEvento("", true, false);
        clickear(btnAddToCart);
        reporte.reportarEvento("", true, false);
    }

}
