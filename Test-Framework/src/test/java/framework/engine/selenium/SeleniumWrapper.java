package framework.engine.selenium;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.hamcrest.CoreMatchers;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static framework.engine.selenium.ReportFunctionalities.reporte;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static framework.engine.utils.Constants.*;

public class SeleniumWrapper extends SeleniumTestBase {

    public static final String MESSAGE_COLOR = "\u001B[30m";
    public static final String MESSAGE_BACKGROUND = "\u001B[42m";
    public static final String MESSAGE_COLOR_RESET = "\u001B[0m";
    ArrayList<String> tabs;
    private static WebDriver driver;

    //Constructor Base
    public SeleniumWrapper(WebDriver driver) {
        SeleniumWrapper.driver = driver;
    }

    public JavascriptExecutor js() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js;
    }


    //-------------------------ABRIR NAVEGADOR

    /**
     * metodo que ingresa a la pagina que le pasemos por parametro
     *
     * @param url ingresar el valor de la URL a la cual vayamos a entrar
     */
    public static void navegarURL(String url) {

        try {
            driver.get(url);
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            for (int Pestanias = 1; Pestanias < tabs.size(); Pestanias++) {
                driver.switchTo().window(tabs.get(tabs.size() - Pestanias));
                driver.close();
                driver.switchTo().window(tabs.get(tabs.size() - Pestanias - 1));
            }
        } catch (UnhandledAlertException f) {
            try {
                Alert alert = driver.switchTo().alert();
                alert.accept();
            } catch (NoAlertPresentException e) {
                driver.get(url);
            }
        }

    }


    //-------------------------PESTAÑAS DEL NAVEGADOR


    public ArrayList<String> guardarPagina() {
        tabs = new ArrayList<String>(driver.getWindowHandles());
        return tabs;
    }

    public WebDriver cambiarPestanias(int i) {
        return driver.switchTo().window(tabs.get(i));
    }

    public static void cambiarAPestaniaPrincipal() throws UnhandledAlertException {
        ArrayList<String> lstPestanias = new ArrayList<>(driver.getWindowHandles());
        int cantPestanas = lstPestanias.size();
        if (cantPestanas > 1) {
            driver.switchTo().window(lstPestanias.get(0));
        }
    }


    //-------------------------CERRAR NAVEGADOR


    /**
     * Metodo que cierra el Browser que estemos ejecutando
     */
    public static void cerrarVentanas() {
        if (driver != null) {
            driver.quit();
        }
    }


    //----------------------------ACCIONES DEL MOUSE


    /**
     * Metodo de accion del mouse, hace un hover sobre un localizador para desplegar una lista
     *
     * @param localizador ingresar el localizador que necesite la accion
     */
    public static void sobre(By localizador) throws IOException, InvalidFormatException, AWTException {
            WebElement obj = encontrarElemento(localizador);
            Actions action = new Actions(driver);
            action.moveToElement(obj).build().perform();
    }

    //<<<<<<<<<<<<<<<<<<<<<----------CLICKEAR---------->>>>>>>>>>>>>>>>>>>>

    /**
     * Metodo de accion del mouse, da click a un elemento
     *
     * @param localizador ingresar el localizador que necesite la accion
     */
    public static void clickear(By localizador) throws IOException, InvalidFormatException, AWTException {
        encontrarElemento(localizador).click();

    }

    public static void clickearWE(WebElement we) {
        we.click();
    }


    /**
     * Metodo de accion del mouse, da click a un elemento que contenga los datos pasados por parametro "Data" tras hacer una iteracion de la fila
     *
     * @param localizador ingresar el localizador que necesite la accion
     * @param Data        ingresar la cadena de datos que debe contener la fila para dar el click
     */
    public static void clickDatosEnFila(By localizador, String Data) throws IOException, InvalidFormatException, AWTException {
        WebElement objFila = null;
        List<String> argumentos = new ArrayList<>(Arrays.asList(Data.split("\\|")));
        List<WebElement> rows = encontrarElemento(localizador).findElements(By.tagName("tr"));
        for (WebElement tr : rows) {
            List<WebElement> columns = tr.findElements(By.tagName("td"));
            List<String> columnsText = new ArrayList<>();
            for (WebElement td : columns) {
                columnsText.add(td.getText());
                objFila = td;
            }
            if (columnsText.containsAll(argumentos)) {
                requireNonNull(objFila).click();
                estaDesplegado(localizador);
                break;
            }
        }
    }


    //---------------------------ENTRADAS DE DATOS

    /**
     * Metodo de accion sendKeys, introduce un valor string que le pasemos
     *
     * @param localizador ingresar el localizador que necesite la accion
     * @param texto       ingresar el valor del string que queremos
     */
    public static void escribirEnInput(By localizador, String texto) throws IOException, InvalidFormatException, AWTException {
        encontrarElemento(localizador).sendKeys(texto);
    }

    /**
     * Metodo de acción sendKeys, este metodo presiona la tecla TAB
     *
     * @param locator ingresar el localizador que necesite la accion
     */
    public static void presionarKeyTAB(By locator) throws IOException, InvalidFormatException, AWTException {
        encontrarElemento(locator).sendKeys(Keys.TAB);
    }


    /**
     * Metodo el cual va a limpiar el input que le pasemos por parametro
     *
     * @param localizador ingresar el localizador que necesite la accion
     */
    public static void limpiarInput(By localizador) throws IOException, InvalidFormatException, AWTException {
        encontrarElemento(localizador).clear();
    }


    /**
     * Metodo el cual va a insertar un archivo que este ubicado en algun archivo de nuestro ordenador
     *
     * @param localizador ingresar el localizador que necesite la accion
     * @param pathname    le pasaremos la ruta de la ubicacion donde este nuestro archivo a insertar
     */

    public static void insertarArchivo(By localizador, String pathname) throws IOException, InvalidFormatException, AWTException {
        String localDir = System.getProperty("user.dir");
        File file = new File(localDir + FILES_PATH + pathname);
        String path = file.getAbsolutePath();
        encontrarElemento(localizador).sendKeys(path);
    }


    //--------------------------------ESPERAS


    /**
     * Metodo el cual genera tiempos de esperas parametrizados en timepo
     *

     */
    public static void esperaEnSegundos() throws InterruptedException {
        TimeUnit.SECONDS.sleep(Long.parseLong(TIEMPO_ESPERA));
    }

    /**
     * Metodo el cual valida si un elemento esta desplegado con un cierto tiempo de espera
     *
     * @param locator ingresar el localizador que necesite la accion
     * @return el metodo nos retorna un boolean
     */
    public static boolean estaDesplegado(By locator) {
        try {
            WebDriverWait espera = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(ESPERA_HASTA)));
            return espera.until(visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            EXCEPTION = e.getMessage();
            return false;
        }
    }

    /**
     * Metodo el cual valida si un elemento es clickeable
     *
     * @param localizador ingresar el localizador que necesite la accion
     * @return el metodo devuelve un boolean
     */
    public static boolean estaHabilitado(By localizador) {
        try {
            WebDriverWait espera = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(ESPERA_HASTA)));
            return espera.until(visibilityOfElementLocated(localizador)).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Metodo el cual espera la presencia de un elemento el cual le pasamos el localizador por parametro
     * @param localizador  ingresar el localizador que necesite la accion
     */
    public static void esperarPresenciaElemento(By localizador) {
        WebDriverWait espera = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(ESPERA_HASTA)));
        espera.until(ExpectedConditions.presenceOfElementLocated(localizador));
    }

    /**
     * Metodo el cual valida si un elemento esta seleccionado
     *
     * @param localizador ingresar el localizador que necesite la accion
     * @return el metodo nos retorna un boolean
     */
    public static Boolean estaSeleccionado(By localizador) {
        try {
            WebDriverWait espera = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(ESPERA_HASTA)));
            return espera.until(visibilityOfElementLocated(localizador)).isSelected();
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Metodo el cual busca un elemento pasandole el localizador por parametro
     *
     * @param localizador ingresar el localizador que necesite la accion
     */
    public static WebElement encontrarElemento(By localizador) throws IOException, InvalidFormatException, AWTException {
        WebElement we;
        if(estaDesplegado(localizador)){
            we = driver.findElement(localizador);
        }else{
            System.out.println("\n" + MESSAGE_BACKGROUND + MESSAGE_COLOR + "No se encontró el elemento: " + localizador + MESSAGE_COLOR_RESET + "\n");
            reporte.reportarEvento("Elemento encontrado", false, false);
            we = null;
        }
        return we;
    }


    /**
     * Metodo el cual busca varios elementos pasandole el localizador por parametro
     *
     * @param localizador  ingresar el localizador que necesite la accion
     */
    public static List<WebElement> encontarElementos(By localizador) {
        List<WebElement> we;
        if(estaDesplegado(localizador)){
            we = driver.findElements(localizador);
        }else{
            System.out.println("No se encontraron los elementos: " + localizador);
            we = null;
        }
        return we;
    }


    //-----------------------------------SELECCIONAR

    /**
     * Metodo de acción sendKeys, el cual ingresa las flechas de direcion
     *
     * @param locator ingresar el localizador que necesite la accion
     */
    public static void seleccionarPorTeclado(By locator, int pPosicion) throws IOException, InvalidFormatException, AWTException {
        for (int i = 1; i < pPosicion; i++){
            encontrarElemento(locator).sendKeys(Keys.DOWN);
        }
        encontrarElemento(locator).sendKeys(Keys.ENTER);
    }


    /**
     * Metodo de accion sendKey, selecciona un elemento mediante el valor del string que le pasemos
     *
     * @param localizador ingresaremos el texto que contenga el elemento para seleccionarlo
     * @param texto       ingresar el localizador que necesite la accion
     */
    public static void seleccionarOpcionPorTexto(By localizador, String texto) throws IOException, InvalidFormatException, AWTException {
        Select select = new Select(encontrarElemento(localizador));
        select.selectByVisibleText(texto);
    }


    /**
     * Metodo de accion sendKey, selecciona un elemento mediante el valor del ID que contenga el elemento
     *
     * @param localizador  ingresaremos el ID que contenga el elemento para seleccionarlo
     * @param ID           ingresar el localizador que necesite la accion
     */
    public static void seleccionarOpcionPorID(By localizador, int ID) throws IOException, InvalidFormatException, AWTException {
        Select select = new Select(encontrarElemento(localizador));
        select.selectByIndex(ID);
    }


    /**
     * Metodo de accion sendKey, selecciona un elemento mediante el valor de la posicion en que esté
     *
     * @param localizador ingresaremos el valor de la posicion en la que este el elemento a seleccionar
     * @param posicion    ingresar el localizador que necesite la accion
     */
    public static void seleccionarOpcionPorIndex(By localizador, int posicion) throws IOException, InvalidFormatException, AWTException {
        Select select = new Select(encontrarElemento(localizador));
        select.selectByIndex(posicion);
    }


    //------------------------------------------OBTENER VALOR

    /**
     * Metodo que obtiene la dirección URL de una pagina
     *
     * @return el metodo devuelve la URL de la pagina
     */
    public static String obtenerUrl() {
        return driver.getCurrentUrl();
    }


    /**
     * Metodo que obtiene el titulo de una pagina
     *
     * @return el metodo devuelve el titulo de la pagina
     */
    public static String obtenerTituloUrl() {
        return driver.getTitle();
    }



    /**
     * Metodo el cual retorna el texto que contiene un elemento
     *
     * @param locator ingresar el localizador que necesite la accion
     * @return el metodo devuelve el texto
     */
    public static String obtenerTexto(By locator) throws IOException, InvalidFormatException, AWTException {
        return encontrarElemento(locator).getText();
    }



    /**
     *  Metodo el cual obtiene cualquier atributo que contenga el elemento
     * @param localizador  ingresar el localizador que necesite la accion
     * @param atributo     se ingresa el Key del atributo que se necesite para obtener el value
     * @return el metodo va a retornar el atributo obtenido
     */
    public static String obtenerAtributo(By localizador, String atributo) {
        try {
            return encontrarElemento(localizador).getAttribute(atributo);
        } catch (Exception e) {
            return "";
        }
    }


    //----------------------------------------------TABLAS-----------------------

    /**
     * Metodo que itera las filas de una tabla buscando la cadena de datos que le pasamos por parametro "Dato"
     * @param localizador  ingresar el localizador que necesite la accion
     * @param Dato         ingresar el dato que vamos a buscar dentro de la iteracion de las filas
     * @return el metodo va a retornar el valor de en que fila se encuentra el dato encontrado
     */
    public static int obtenerFilaDato(By localizador, String Dato) throws IOException, InvalidFormatException, AWTException {
        List<WebElement> Filas = encontrarElemento(localizador).findElements(By.tagName("tr"));
        int Fila = 0;
        for (int f = 0; f <= Filas.size(); f++) {
            List<WebElement> Columnas = Filas.get(f).findElements(By.tagName("td"));
            String Valor;
            for (int c = 0; c < Columnas.size(); c++) {
                Valor = Columnas.get(c).getAttribute("textContent");
                if (Valor.equals(Dato)) {
                    Fila = f;
                    c = Columnas.size();
                    f = Filas.size();
                }
            }
        }
        return Fila;
    }


    /**
     * Metodo que cuenta todos los elementos que encuentre con el localizador que le pasemos por parametro
     * @param localizador ingresar el localizador que necesite la accion
     * @return el metodo devuelve la cantidad de elementos encontrados
     */
    public static int obtenerNumeroElementos(By localizador) {
        return encontarElementos(localizador).size();
    }

    public static int obtenerNumerosColumnas(By localizador) throws IOException, InvalidFormatException, AWTException {
        WebElement body = encontrarElemento(localizador);
        WebElement fila = body.findElement(By.tagName("tr"));
        List<WebElement> columnas = fila.findElements(By.tagName("td"));
        return columnas.size();
    }


    /**
     * Metodo que cuenta la cantidad de numeros de fila de una tabla
     *
     * @param localizador ingresar el localizador que necesite la accion
     * @return el metodo retorna la cantidad de filas que tiene una tabla
     */
    public static int obtenerNumerosFilas(By localizador) throws IOException, InvalidFormatException, AWTException {
        List<WebElement> Filas = encontrarElemento(localizador).findElements(By.tagName("tr"));
        return Filas.size();
    }

    /**
     * Metodo que obtiene el valor que contiene una celda de una tabla
     * @param localizador  ingresar el localizador que necesite la accion
     * @param Fila         ingresar el valor de index de la fila
     * @param Columna      ingresar el valor de index de la columna
     * @return el metodo devuelve el textContent de la celda que pidamos por los parametros
     */
    public static String obtenerValorCelda(By localizador, int Fila, int Columna) throws IOException, InvalidFormatException, AWTException {
        List<WebElement> Filas = encontrarElemento(localizador).findElements(By.tagName("tr"));
        List<WebElement> Columnas = Filas.get(Fila).findElements(By.tagName("td"));
        return Columnas.get(Columna).getAttribute("textContent");
    }


    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public static String encontrarTxtCelda(By localizador, int rowIndex, int columnIndex) throws IOException, InvalidFormatException, AWTException {
        WebElement tabla = encontrarElemento(localizador);
        return tabla.findElement(By.xpath("./tbody/tr[" + rowIndex + "]/td[" + columnIndex + "]")).getText();
    }


    //revisar
    public static String obtenerTextoCeldas(By localizador, int indiceFila, String nombreColumna) throws IOException, InvalidFormatException, AWTException {
        // Si el valor de entrada de la columna es un número, convierta la columna en un número
        // y obtenga la celda a través del número de columna en lugar de a través del nombre.
        if (isNumeric(nombreColumna)) {
            return encontrarTxtCelda(localizador, indiceFila, Integer.parseInt(nombreColumna));
        } else {
            // obtener todos los elementos del encabezado de la tabla.
            List<WebElement> encabezadosTabla = encontarElementos(localizador);

            // encontrar el número de columna que corresponde al nombre del encabezado de la columna
            for (int indiceColumna = 0; indiceColumna < encabezadosTabla.size(); indiceColumna++) {
                if (encabezadosTabla.get(indiceColumna).getText().equals(nombreColumna)) {
                    return encontrarTxtCelda(localizador, indiceFila,indiceColumna + 1);
                }
            }
        }
        return null;
    }
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * Metodo que valida si un elemento esta seleccionado de una lista
     * @param localizador ingresar el localizador que necesite la accion
     * @return el metodo devuelve la opcion que esta seleccionada
     */
    public static String obtenerSeleccionLista(By localizador) throws IOException, InvalidFormatException, AWTException {
        Select dropdown = new Select(encontrarElemento(localizador));
        WebElement option = dropdown.getFirstSelectedOption();
        return option.getText();
    }


    /**
     * Metodo que obtiene la fecha actual
     * @param formato ingresar el formato en que queramos que el metodo nos devuelva la fecha. Ejemplo: DD/MMM/YYYY
     * @return el metodo devuelve la fecha actual
     */
    public static String obtenerFechaActual(String formato) {
        SimpleDateFormat formatter = null;
        Calendar calendario = Calendar.getInstance();
        Date Date = calendario.getTime();
        formatter = new SimpleDateFormat(formato);
        return formatter != null ? formatter.format(Date) : null;
    }


    /**
     * Metodo el cual obtiene la hora actual
     * @return el metodo devuelve la decha actual
     */
    public static String horaActual(){
        SimpleDateFormat formato;
        formato = new SimpleDateFormat("HH:mm:ss");
        GregorianCalendar calendario = (GregorianCalendar) GregorianCalendar.getInstance();
        return formato.format(calendario.getTime());
    }


//----------------------------------------VERIFICACIONES

    /**
     * Metodo de accion assertThat que valida el textContent de un elemento junto al que pasemos por parametro "Valor"
     * @param localizador  ingresar el localizador que necesite la accion
     * @param valor        ingresar el valor que vamos a comparar con el textContent del elemento que usemos
     */
    public static void verificarTexto(By localizador, String valor) {
        String obtieneAtributo = obtenerAtributo(localizador, "textContent");
        assertThat(obtieneAtributo, CoreMatchers.containsString(valor));
    }


    /**
     * Metodo que valida la existencia de un dato dentro de la tabla
     * @param localizador    ingresar el localizador que necesite la accion
     * @param UltimaFila
     * @param Fila
     * @param Data
     */
    public static void verificarDatoEnTabla(By localizador, boolean UltimaFila, int Fila, String Data) throws IOException, InvalidFormatException, AWTException {
        String[] Datos = Data.split("\\|");
        List<WebElement> Filas = encontrarElemento(localizador).findElements(By.tagName("tr"));
        if (UltimaFila) {
            Fila = Filas.size() - 2;
        }
        List<WebElement> Columnas = Filas.get(Fila).findElements(By.tagName("td"));
        int numDatos = Datos.length;
        for (int i = 0; i < numDatos; i++) {
            String strDato = Columnas.get(i).getAttribute("textContent");
            if (!Datos[i].equals("*")) {
                assertThat(strDato, containsString(Datos[i]));
            }
        }
    }


    /**
     * Metodo que valida la existencia de datos mediante una iteración de las columnas de una tabla
     * @param localizador   ingresar el localizador que necesite la accion
     * @param pNombre       ingresar el dato que necesitemos validar su existencia en las filas
     * @return  el metodo devuelve el index de la columna donde se encuentra el dato buscado
     */
    public static boolean verificarDatoEnColumna(By localizador, String pNombre) throws IOException, InvalidFormatException, AWTException {
        boolean contieneArgs = false;
        //esperarPresenciaElemento(localizador);
        List<String> argumentos = new ArrayList<>(Arrays.asList(pNombre.split("\\|")));
        List<WebElement> filas = encontrarElemento(localizador).findElements(By.tagName("th"));
        List<String> textoColumnas = new ArrayList<>();
        for (WebElement th : filas){
            textoColumnas.add(th.getText());
        }

        if (textoColumnas.containsAll(argumentos)) {
            contieneArgs = true;
        }
        return contieneArgs;
    }


    /**
     * Metodo que valida la existencia de datos mediante una iteración de las filas de una tabla
     * @param localizador  ingresar el localizador que necesite la accion
     * @param Data         ingresar el dato que necesitemos validar su existencia en las filas
     * @return el metodo devuelve el index de la fila donde se encuentra el dato buscado
     */
    public static boolean verificarDatoEnFila(By localizador, String Data) throws IOException, InvalidFormatException, AWTException {
        boolean containsArgs = false;
        //esperarPresenciaElemento(localizador);
        List<String> argumentos = new ArrayList<>(Arrays.asList(Data.split("\\|")));
        List<WebElement> filas = encontrarElemento(localizador).findElements(By.tagName("tr"));
        for (WebElement tr : filas){
            List<WebElement> columnas = tr.findElements(By.tagName("td"));
            List<String> textoColumnas = new ArrayList<>();
            for (WebElement td : columnas) {
                textoColumnas.add(td.getText());
            }
            if (textoColumnas.containsAll(argumentos)){
                containsArgs = true;
                break;
            }
        }
        return containsArgs;
    }




//----------------------------------------CALENDARIOS

    public static String mesNumAMesPalabra(String nummes) {
        String messtr = "";
        switch (nummes) {
            case "01":
                messtr = "ENERO";
                break;
            case "02":
                messtr = "FEBRERO";
                break;
            case "03":
                messtr = "MARZO";
                break;
            case "04":
                messtr = "ABRIL";
                break;
            case "05":
                messtr = "MAYO";
                break;
            case "06":
                messtr = "JUNIO";
                break;
            case "07":
                messtr = "JULIO";
                break;
            case "08":
                messtr = "AGOSTO";
                break;
            case "09":
                messtr = "SEPTIEMBRE";
                break;
            case "10":
                messtr = "OCTUBRE";
                break;
            case "11":
                messtr = "NOVIEMBRE";
                break;
            case "12":
                messtr = "DICIEMBRE";
                break;
        }
        return messtr;
    }


    public static String mesNumAMesCorto(String nummes) {
        String messtr;
        switch (nummes) {
            case "01":
                messtr = "ene";
                break;
            case "02":
                messtr = "feb";
                break;
            case "03":
                messtr = "mar";
                break;
            case "04":
                messtr = "abr";
                break;
            case "05":
                messtr = "may";
                break;
            case "06":
                messtr = "jun";
                break;
            case "07":
                messtr = "jul";
                break;
            case "08":
                messtr = "ago";
                break;
            case "09":
                messtr = "sep";
                break;
            case "10":
                messtr = "oct";
                break;
            case "11":
                messtr = "nov";
                break;
            case "12":
                messtr = "dic";
                break;
            default:
                messtr = nummes;
        }
        return messtr;
    }



    public static String mesPalabraAMesNum(String MesStr) {
        MesStr = MesStr.toLowerCase(Locale.ROOT);

        if ((MesStr.equals("ene")) || (MesStr.equals("january")) || (MesStr.equals("enero")) || (MesStr.equals("jan")))
            return  "01";
        if (MesStr.equals("feb") || MesStr.equals("february") || MesStr.equals("febrero"))
            return  "02";
        if (MesStr.equals("mar") || MesStr.equals("march") || MesStr.equals("marzo"))
            return  "03";
        if ((MesStr.equals("abr")) || (MesStr.equals("april")) || (MesStr.equals("abril")) || (MesStr.equals("apr")))
            return  "04";
        if (MesStr.equals("may") || MesStr.equals("mayo"))
            return  "05";
        if (MesStr.equals("jun") || MesStr.equals("june") || MesStr.equals("junio"))
            return  "06";
        if (MesStr.equals("jul") || MesStr.equals("july") || MesStr.equals("julio"))
            return  "07";
        if ((MesStr.equals("ago")) || (MesStr.equals("august")) || (MesStr.equals("agosto")) || (MesStr.equals("aug")))
            return  "08";
        if (MesStr.equals("sep") || MesStr.equals("september") || MesStr.equals("septiembre"))
            return  "09";
        if (MesStr.equals("oct") || MesStr.equals("october") || MesStr.equals("octubre"))
            return  "10";
        if (MesStr.equals("nov") || MesStr.equals("november") || MesStr.equals("noviembre"))
            return  "11";
        if ((MesStr.equals("dic")) || (MesStr.equals("december")) || (MesStr.equals("diciembre")) || (MesStr.equals("dec")))
            return  "12";

        return MesStr;
    }


    /**
     * Metodo que cambia el formato de presentacion de las fechas Ejemplo: 10/ene/2022  -->  10/01/2022
     * @param Fecha  se ingresa la fecha que necesitemos cambiarle el formato de presentación
     * @return  el metodo devuelve la fecha con el cambio de formato
     */
    public static String convertirfecha(String Fecha) {
        String [] arrFecha = Fecha.split("-");
        String dia = arrFecha[0];
        String mes = arrFecha[1];
        String year = arrFecha[2];
        if (mes.matches("[+-]?\\d*(\\.\\d+)?")) {
            mes = SeleniumWrapper.mesNumAMesCorto(mes);
        }
        else{
            mes = mesPalabraAMesNum(mes);
        }
        Fecha = dia + "-" + mes + "-" + year;
        return Fecha;
    }



    //-------------------------------DIVISAS



    /**
     * Metodo que parsea valores de formato moneda a formato Double
     * @param localizador   ingresar el localizador que necesite la accion
     * @param atributo      se ingresa el Key del atributo que contenga el valor, para obtener el value
     * @return el metodo devuelve el valor del elemento en formato Double
     */
    public static double monedaANumero(By localizador, String atributo){
        String valor = obtenerAtributo(localizador, atributo);
        String.valueOf(driver.findElements(localizador));
        valor = valor.replace("$","");
        valor = valor.replace(",","");
        valor = valor.replace(".","");
        if(valor.contains("US$ ")){
            valor = valor.replace("US$ ", "").trim();
        }
        else if (valor.contains("CH$ ")){
            valor = valor.replace("CH$ ", "").trim();
        }
        else if (valor.contains("MX$ ")){
            valor = valor.replace("MX$ ", "").trim();
        }
        else {
            valor = valor.replace("$ ", "").trim();
        }
        return Double.parseDouble(valor);
    }

    public static void cambiarAIframe(By localizador) throws IOException, InvalidFormatException, AWTException {
        driver.switchTo().frame(encontrarElemento(localizador));
    }
}
