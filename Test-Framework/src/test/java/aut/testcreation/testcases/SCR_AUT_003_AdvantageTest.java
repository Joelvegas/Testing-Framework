package aut.testcreation.testcases;

import aut.testcreation.tasks.advantage.Agregar;
import aut.testcreation.tasks.advantage.Comprobar;
import aut.testcreation.tasks.advantage.Login;
import framework.engine.selenium.SeleniumTestBase;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SCR_AUT_003_AdvantageTest extends SeleniumTestBase {

    @Test
    void TC_003_01_agregarRaton() throws IOException, InvalidFormatException, AWTException {
        getTestName(getMethodName());
        Agregar.ratonAlCarrito("pPosicion");
        assertTrue(Comprobar.cargaCarrito());
    }

    @Test
    void TC_003_02_usuarioExistente() throws IOException, InvalidFormatException, AWTException, InterruptedException {
        getTestName(getMethodName());
        Login.crearCuenta("pUsername", "pEmail", "pPassword", "pConfirmPassword");
        assertTrue(Comprobar.mensajeValidacion("pMensajeValidacion"));
    }
}
