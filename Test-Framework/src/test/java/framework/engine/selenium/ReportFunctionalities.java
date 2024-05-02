package framework.engine.selenium;

import framework.engine.utils.ConfigReport;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

import static framework.engine.utils.LoadProperties.extraerParametro;

public class ReportFunctionalities {
    public static ConfigReport reporte;
    public static void newReport() throws IOException, InvalidFormatException {
        reporte = new ConfigReport();
    }

    public static void finishReport() throws IOException {
        reporte.finalizar(extraerParametro("Escenario"));
    }
}


