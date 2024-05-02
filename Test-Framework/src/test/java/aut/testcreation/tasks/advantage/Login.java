package aut.testcreation.tasks.advantage;

import framework.engine.selenium.SeleniumWrapper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.io.IOException;

import static aut.testcreation.pages.Page_Advantage.*;
import static framework.engine.selenium.ReportFunctionalities.reporte;
import static framework.engine.utils.Constants.URL_ADVANTAGE;
import static framework.engine.utils.ExcelReader.*;

public class Login extends SeleniumWrapper {
    public Login(WebDriver driver) {
        super(driver);
    }

    /**
     * Realiza todo los pasos para crear una cuenta desde ingresar a la página principal hasta presionar el botón para registrarse. Utiliza los datos pasados por parámetros.
     * @param pUsername
     * @param pEmail
     * @param pPassword
     * @param pConfirmPassword
     * @throws IOException
     * @throws InvalidFormatException
     * @throws AWTException
     * @throws InterruptedException
     */
    public static void crearCuenta(String pUsername, String pEmail, String pPassword, String pConfirmPassword) throws IOException, InvalidFormatException, AWTException, InterruptedException {
        navegarURL(URL_ADVANTAGE);
        clickear(btnUser);
        reporte.reportarEvento("", true, false);
        esperaEnSegundos();
        esperaEnSegundos();
        clickear(lnkCreateNewAccount);
        reporte.reportarEvento("", true, false);
        escribirEnInput(fldUsername, extractDataToExcel(pUsername));
        escribirEnInput(fldEmail, extractDataToExcel(pEmail));
        escribirEnInput(fldPassword, extractDataToExcel(pPassword));
        escribirEnInput(fldConfirmPassword, extractDataToExcel(pConfirmPassword));
        reporte.reportarEvento("", true, false);
        esperaEnSegundos();
        clickear(cbxIAgree);
        clickear(btnRegister);
        reporte.reportarEvento("", true, false);
        esperaEnSegundos();
    }
}
