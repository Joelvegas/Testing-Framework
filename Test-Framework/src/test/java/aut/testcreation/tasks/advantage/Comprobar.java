package aut.testcreation.tasks.advantage;

import framework.engine.selenium.SeleniumWrapper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static aut.testcreation.pages.Page_Advantage.*;
import static framework.engine.selenium.ReportFunctionalities.reporte;
import static framework.engine.utils.ExcelReader.*;

public class Comprobar extends SeleniumWrapper {
    public Comprobar(WebDriver driver) {
        super(driver);
    }

    /**
     * Comprueba que el carrito tenga uno o más elementos.
     * @return boolean: lEstado
     * @throws IOException
     * @throws InvalidFormatException
     * @throws AWTException
     */
    public static boolean cargaCarrito() throws IOException, InvalidFormatException, AWTException {
        int lCartCount = Integer.parseInt(obtenerTexto(lblShoppingCartCount));
        boolean lEstado = lCartCount != 0;
        reporte.reportarEvento("", lEstado, false);
        return lEstado;
    }

    /**
     * Comprueba que el mensaje de error mostrado sea el correspondiente
     * @param pMensajeValidacion
     * @return boolean: lEstado
     * @throws IOException
     * @throws InvalidFormatException
     * @throws AWTException
     */
    public static boolean mensajeValidacion(String pMensajeValidacion) throws IOException, InvalidFormatException, AWTException {
        String lTextoRegistro = obtenerTexto(lblMessageRegister);
        boolean lEstado = Objects.equals(lTextoRegistro, extractDataToExcel(pMensajeValidacion));
        reporte.reportarEvento("", lEstado, false);
        return lEstado;
    }
}
