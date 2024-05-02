package framework.engine.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

import static framework.engine.utils.Constants.*;
import static framework.engine.utils.LoadProperties.extraerParametro;

public class ConfigReport {
    private final XWPFDocument documento = new XWPFDocument();
    private int contadorPasos = 0;
    private final String colorPrincipal;
    static Properties properties;
    static String[] descripcionExcel = new String[0];

    public ConfigReport() throws IOException, InvalidFormatException {
        this.colorPrincipal = "314260";
        this.iniciarEncabezado();
        this.iniciar();
        this.iniciarFooter();
    }

    private void iniciar() {
        XWPFParagraph titulo_doc = documento.createParagraph();
        titulo_doc.setAlignment(ParagraphAlignment.CENTER);
        titulo_doc.setVerticalAlignment(TextAlignment.TOP);
        XWPFRun r1 = titulo_doc.createRun();
        r1.setBold(true);
        r1.setText("Evidencias de Pruebas Automatizadas");
        r1.setColor("6B6A67");
        r1.setFontFamily("Arial");
        r1.setFontSize(25);
        r1.setTextPosition(10);
        r1.addCarriageReturn();
    }

    private void iniciarEncabezado() throws IOException, InvalidFormatException {
        XWPFHeader encabezado = documento.createHeader(HeaderFooterType.DEFAULT);
        XWPFParagraph parrafo = encabezado.createParagraph();
        XWPFRun r1 = parrafo.createRun();

        parrafo.setAlignment(ParagraphAlignment.DISTRIBUTE);
        parrafo.setBorderBottom(Borders.CHECKED_BAR_COLOR);
        InputStream logotsoft = new FileInputStream("src/test/resources/ConfigReport/Logo_PropiaEmpresa.png");

        InputStream logoCliente = new FileInputStream("src/test/resources/ConfigReport/Logo_Cliente.png");
        r1.addPicture(logoCliente, Document.PICTURE_TYPE_PNG, "logoCliente", Units.toEMU(153), Units.toEMU(73));

        r1.setText("                      ");
        int widthLogoTsoft = 100;
        r1.addPicture(logotsoft, Document.PICTURE_TYPE_PNG, "logotsoft", Units.toEMU(widthLogoTsoft), Units.toEMU(widthLogoTsoft * 0.80));
        r1.addBreak(BreakType.COLUMN);
    }

    private void iniciarFooter() {
        XWPFFooter footer = documento.createFooter(HeaderFooterType.DEFAULT);
        XWPFParagraph parrafoFooter = footer.createParagraph();
        XWPFRun rFooter = parrafoFooter.createRun();
        rFooter.setText("Nota: La versión vigente de este documento se encuentra en medio electrónica. El documento impreso solo debe usarse como referencia.");
        rFooter.addBreak(BreakType.COLUMN);

        XWPFParagraph parrafo2Footer = footer.createParagraph();
        XWPFRun rFooter2;
        parrafo2Footer.setAlignment(ParagraphAlignment.CENTER);
        rFooter2 = parrafo2Footer.createRun();
        rFooter2.setText("Página ");
        parrafo2Footer.getCTP().addNewFldSimple().setInstr("PAGE \\* MERGEFORMAT");
        rFooter2 = parrafo2Footer.createRun();
        rFooter2.setText(" de ");
        parrafo2Footer.getCTP().addNewFldSimple().setInstr("NUMPAGES \\* MERGEFORMAT");
    }

    public void tomarCaptura() throws AWTException, IOException, InvalidFormatException {
        XWPFParagraph parrafoImagen = documento.createParagraph();
        parrafoImagen.setAlignment(ParagraphAlignment.CENTER);
        parrafoImagen.setSpacingAfter(200);
        XWPFRun r1 = parrafoImagen.createRun();

        //Screenshot
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(image, "png", os);
        InputStream pic = new ByteArrayInputStream(os.toByteArray());

        int width = 400;
        r1.addCarriageReturn();
        r1.addCarriageReturn();
        r1.addPicture(pic, Document.PICTURE_TYPE_PNG, "paso" + contadorPasos, Units.toEMU(width), Units.toEMU(width * 0.57));
    }

    private void crearMatrizPruebasAutomatizadas() {
        XWPFParagraph titulo = documento.createParagraph();
        XWPFRun runTitulo = titulo.createRun();
        XWPFTable tabla = documento.createTable(4, 4);

        runTitulo.setText("Matriz de Pruebas Automatizadas");
        runTitulo.setColor("1f497d");
        runTitulo.setFontSize(16);

        tabla.setWidth("100%");
        XWPFTableRow primeraFila = tabla.getRow(0);
        for (int i = 0; i < 4; i++) {
            XWPFTableCell celda = primeraFila.getCell(i);
            celda.setColor(colorPrincipal);
            celda.setWidth("25%");
        }
        tabla.getRow(1).getCell(0).setText("Nombre");
        tabla.getRow(1).getCell(1).setText("Demo");
        tabla.getRow(2).getCell(0).setText("ID Matriz");
        tabla.getRow(2).getCell(1).setText("1001");
        tabla.getRow(2).getCell(2).setText("Tipo:");
        tabla.getRow(2).getCell(3).setText("Regresión");
        tabla.getRow(3).getCell(0).setText("Fabrica Autor:");
        tabla.getRow(3).getCell(1).setText("Automatizada");
        tabla.getRow(3).getCell(2).setText("Fecha de Ejecución");
        tabla.getRow(3).getCell(3).setText("20/2/22");
    }

    private void crearTablaDatosEjecucion() {
        XWPFParagraph titulo = documento.createParagraph();
        XWPFRun runTitulo = titulo.createRun();
        XWPFTable tabla = documento.createTable(2, 4);

        runTitulo.addCarriageReturn();
        runTitulo.addCarriageReturn();
        runTitulo.setText("Datos de la Ejecución: Automatizados");
        runTitulo.setColor("1f497d");
        runTitulo.setFontSize(16);

        tabla.setWidth("100%");
        XWPFTableRow primeraFila = tabla.getRow(0);
        String[] titulos = {"Run ID", "Run Name", "Instancia de Prueba (Escenario)", "Caso de Prueba"};
        for (int i = 0; i < 4; i++) {
            XWPFTableCell celda = primeraFila.getCell(i);
            celda.setWidth("25%");
            celda.setColor(this.colorPrincipal);
            celda.removeParagraph(0);
            XWPFParagraph parrafoCelda = celda.addParagraph();
            parrafoCelda.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun runCelda = parrafoCelda.createRun();
            runCelda.setText(titulos[i]);
            runCelda.setColor("FFFFFF");

        }
        tabla.getRow(1).getCell(0).setText("1113");
        tabla.getRow(1).getCell(1).setText("Run_8-21_10-20-15");
        tabla.getRow(1).getCell(2).setText("Automatizados");
        tabla.getRow(1).getCell(3).setText("Demo");
    }

    private void crearTablaDetallesEvidencias() {
        XWPFParagraph titulo = documento.createParagraph();
        XWPFRun runTitulo = titulo.createRun();
        XWPFTable tabla = documento.createTable(2, 4);

        runTitulo.addCarriageReturn();
        runTitulo.addCarriageReturn();
        runTitulo.setText("Detalle y Evidencias:");
        runTitulo.setColor("1f497d");
        runTitulo.setFontSize(16);

        XWPFTableRow filaTitulos = tabla.getRow(0);
        tabla.setWidth("100%");
        String[] titulos;
        if(EXCEPTION == null){
            titulos = new String[]{"Paso", "Descripción del Paso", "Resultado Esperado", "Estado"};
        } else {
            titulos = new String[]{"Paso", "Descripción de la excepción", "Resultado Esperado", "Estado"};
        }
        String[] largos = {"15%", "35%", "35%", "15%"};
        for (int i = 0; i < 4; i++) {
            XWPFTableCell celda = filaTitulos.getCell(i);
            celda.setWidth(largos[i]);
            celda.setColor(this.colorPrincipal);
            celda.removeParagraph(0);
            XWPFParagraph parrafoCelda = celda.addParagraph();
            parrafoCelda.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun runCelda = parrafoCelda.createRun();
            runCelda.setText(titulos[i]);
            runCelda.setColor("FFFFFF");

        }

        tabla.getRow(1).getCell(0).setText("Paso " + contadorPasos);
        tabla.getRow(1).getCell(1).setText("<<Descripción>>");
        tabla.getRow(1).getCell(2).setText("<<Resultado>>");
        tabla.getRow(1).getCell(3).setText("<<Estado>>");
    }

    public void reportarEvento(String pResultadoEsperado, boolean pEstado, boolean pPrecondicion) throws IOException, InvalidFormatException, AWTException {
        XWPFParagraph parrafoBreak = documento.createParagraph();
        XWPFRun rBreak = parrafoBreak.createRun();
        rBreak.addBreak(BreakType.PAGE);

        XWPFTable tabla = documento.createTable(2, 4);
        XWPFTableRow filaTitulos = tabla.getRow(0);
        tabla.setWidth("100%");
        String[] titulos = {};
        titulos = new String[]{"Paso", "Descripción del Paso", "Resultado Esperado", "Estado"};
        String[] largos = {"15%", "35%", "35%", "15%"};
        for (int i = 0; i < 4; i++) {
            XWPFTableCell celda = filaTitulos.getCell(i);
            celda.setWidth(largos[i]);
            celda.setColor(this.colorPrincipal);
            celda.removeParagraph(0);
            XWPFParagraph parrafoCelda = celda.addParagraph();
            parrafoCelda.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun runCelda = parrafoCelda.createRun();
            runCelda.setText(titulos[i]);
            runCelda.setColor("FFFFFF");
        }

        XWPFTableCell celda10 = tabla.getRow(1).getCell(0);
        XWPFTableCell celda11 = tabla.getRow(1).getCell(1);
        XWPFTableCell celda12 = tabla.getRow(1).getCell(2);
        XWPFTableCell celda13 = tabla.getRow(1).getCell(3);

        descripcionExcel = obtenerDescripcion(pPrecondicion);
        String esperado = getResultadoEsperado(pResultadoEsperado);

        if (pPrecondicion){
            celda10.setText("Precondición");
        } else {
            if(EXCEPTION != null){
                if (contadorPasos == 0) {
                    celda10.setText("Precondición");
                } else {
                    this.contadorPasos++;
                    celda10.setText("Paso " + contadorPasos);
                }
                EXCEPTION = null;
            }else {
                this.contadorPasos++;
                celda10.setText("Paso " + contadorPasos);
            }
        }


        celda11.setText(descripcionExcel[0]);
        celda12.setText(esperado);
        celda13.setText(pEstado ? "Exitoso" : "Fallido");

        this.tomarCaptura();
    }

    public void finalizar(String tituloDocumento) throws IOException {
        String pattern = "dd-MM-yyyy_HH-mm-ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        String fileName;
        if (IS_DEBUG.equals("true")){
            fileName = "Debug.docx";
        } else {
            fileName = tituloDocumento + "_" + todayAsString + ".docx";
        }
        FileOutputStream word = new FileOutputStream("src/test/resources/Evidencias/" + fileName);

        documento.write(word);
        word.close();
    }

    private String[] obtenerDescripcion(boolean precondicion) throws IOException {
        //String lValue = null;
        String[] lDataValues = new String[2];

        if(EXCEPTION == null){
            properties = LoadProperties.loadProperties();
            String lEscenario = extraerParametro("Escenario");

            String lEscenarioFile = lEscenario.split("_")[1];
            String lFileName = properties.getProperty(lEscenarioFile);
            lFileName = lFileName.replace("DT", "DC");
            int lRowNum = Integer.parseInt(lEscenario.split("_")[2]);

            ExcelReader lExcelReader = new ExcelReader();
            String lFilePath = DESCRIPTIONS_PATH + lFileName + ".xlsx";
            String lDataSheet = "DataSheet";

            List<Map<String, String>> lTestData = lExcelReader.getData(lFilePath, lDataSheet);

            String lData1;
            String lPaso;
            lPaso = "Paso" + (contadorPasos + 1);
            if (precondicion){
                lData1 = lTestData.get(lRowNum - 1).get("precondicion");
            }else {
                lData1 = lTestData.get(lRowNum - 1).get(lPaso);
            }
            lDataValues = lData1.split("\\|\\|");
        } else{
            lDataValues[0] = EXCEPTION;
            //EXCEPTION = null;
        }

        return lDataValues;
    }

    private String getResultadoEsperado(String resultadoEsperado) {
        String esperado;
        if(Objects.equals(resultadoEsperado, "")){
            try{
                esperado = descripcionExcel[1];
            }catch (Exception e){
                esperado = "";
            }
        } else {
            esperado = resultadoEsperado;
        }
        return esperado;
    }

}
