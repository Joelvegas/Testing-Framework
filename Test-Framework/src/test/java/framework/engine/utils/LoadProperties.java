package framework.engine.utils;

import java.io.*;
import java.util.*;

import static framework.engine.utils.Constants.RUNTIME_FILENAME;

public class LoadProperties {
    static OutputStream output;
    static Properties propRunTime = new Properties();

    /**
     * Carga las propiedades y las retorna para ser usadas.
     * @return lPropiedades
     */
    public static Properties loadProperties(){
        String lFile = "config";
        Properties lPropiedades = new Properties();
        ResourceBundle lBundle = ResourceBundle.getBundle(lFile);
        Enumeration<String> e = lBundle.getKeys();
        String lKey;
        while (e.hasMoreElements()){
            lKey = (String) e.nextElement();
            lPropiedades.put(lKey,lBundle.getObject(lKey));
        }
        return lPropiedades;
    }

    /**
     * Crea un nuevo archivo runtime.properties vacio.
     */
    public static void emptyFile(){
        try {
            output = new FileOutputStream(RUNTIME_FILENAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda el nombre del escenario actual en el archivo runtime.properties
     * @param pEscenarioName
     * @throws IOException
     */
    public static void getEscenario(String pEscenarioName) throws IOException {
        emptyFile();
        propRunTime.setProperty("Escenario", pEscenarioName);
        propRunTime.store(output, null);
    }

    /**
     * Extrae un parametro desde el archivo runtime.properties (actualmente solo usado para obtener el Escenario)
     * @param pKey
     * @return Valor del parámetro.
     */
    public static String extraerParametro(String pKey) {
        return propRunTime.getProperty(pKey);
    }
}
