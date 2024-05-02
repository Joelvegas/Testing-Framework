package aut.testcreation.steps;

import aut.testcreation.tasks.advantage.Agregar;
import aut.testcreation.tasks.advantage.Comprobar;
import aut.testcreation.tasks.advantage.Login;
import framework.engine.bdd.CucumberBaseTestRunner;
import io.cucumber.java8.En;

import static org.junit.jupiter.api.Assertions.*;

public class Steps_Advantage extends CucumberBaseTestRunner implements En {
    public Steps_Advantage() {
        Given("El Usuario agrega el ratón de la posición {string} al carrito", (String pPosicion) -> {
            Agregar.ratonAlCarrito(pPosicion);
        });
        Then("El Usuario comprueba que el ratón se agregó al carrito", () -> {
            assertTrue(Comprobar.cargaCarrito());
        });
        Given("El Usuario intenta crear una cuenta con el usuario {string}, el email {string} y la contraseña {string} y {string}", (String pUsername, String pEmail, String pPassword, String pConfirmPassword) -> {
            Login.crearCuenta(pUsername, pEmail, pPassword, pConfirmPassword);
        });
        Then("El Sistema muestra que el Usuario ya existe con el mensaje {string}", (String pMensajeValidacion) -> {
            assertTrue(Comprobar.mensajeValidacion(pMensajeValidacion));
        });
    }
}
