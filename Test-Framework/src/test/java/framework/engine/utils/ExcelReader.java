package framework.engine.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static framework.engine.utils.Constants.DATATABLES_PATH;
import static framework.engine.utils.LoadProperties.extraerParametro;

public class ExcelReader {
    static Properties properties;

    /**
     * Retorna el sheet correspondiente a los datos pasados por parámetros.
     * @param pExcelFilePath
     * @param pSheetName
     * @return lSheet
     * @throws IOException
     */
    List<Map<String, String>> getData(String pExcelFilePath, String pSheetName)
            throws IOException {
        Sheet lSheet = getSheetByName(pExcelFilePath, pSheetName);
        return readSheet(lSheet);
    }

    private Sheet getSheetByName(String pExcelFilePath, String pSheetName) throws IOException {
        return getWorkBook(pExcelFilePath).getSheet(pSheetName);
    }

    private Workbook getWorkBook(String pExcelFilePath) throws IOException {
        return WorkbookFactory.create(new File(pExcelFilePath));
    }

    private List<Map<String, String>> readSheet(Sheet pSheet) {
        Row lRow;
        int lTotalRow = pSheet.getPhysicalNumberOfRows();
        List<Map<String, String>> lExcelRows = new ArrayList<>();
        int lHeaderRowNumber = getHeaderRowNumber(pSheet);
        if (lHeaderRowNumber != -1) {
            int lTotalColumn = pSheet.getRow(lHeaderRowNumber).getLastCellNum();
            int lSetCurrentRow = 1;
            for (int lCurrentRow = lSetCurrentRow; lCurrentRow <= lTotalRow; lCurrentRow++) {
                lRow = getRow(pSheet, pSheet.getFirstRowNum() + lCurrentRow);
                LinkedHashMap<String, String> lColumnMapData = new LinkedHashMap<>();
                for (int lCurrentColumn = 0; lCurrentColumn < lTotalColumn; lCurrentColumn++) {
                    lColumnMapData.putAll(getCellValue(pSheet, lRow, lCurrentColumn));
                }
                lExcelRows.add(lColumnMapData);
            }
        }
        return lExcelRows;
    }

    private int getHeaderRowNumber(Sheet pSheet) {
        Row lRow;
        int lTotalRow = pSheet.getLastRowNum();
        for (int lCurrentRow = 0; lCurrentRow <= lTotalRow + 1; lCurrentRow++) {
            lRow = getRow(pSheet, lCurrentRow);
            if (lRow != null) {
                int lTotalColumn = lRow.getLastCellNum();
                for (int lCurrentColumn = 0; lCurrentColumn < lTotalColumn; lCurrentColumn++) {
                    Cell lCell;
                    lCell = lRow.getCell(lCurrentColumn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (lCell.getCellType() == CellType.STRING) {
                        return lRow.getRowNum();

                    } else if (lCell.getCellType() == CellType.NUMERIC) {
                        return lRow.getRowNum();

                    } else if (lCell.getCellType() == CellType.BOOLEAN) {
                        return lRow.getRowNum();
                    } else if (lCell.getCellType() == CellType.ERROR) {
                        return lRow.getRowNum();
                    }
                }
            }
        }
        return (-1);
    }

    private Row getRow(Sheet pSheet, int pRowNumber) {
        return pSheet.getRow(pRowNumber);
    }

    private LinkedHashMap<String, String> getCellValue(Sheet pSheet, Row pRow, int pCurrentColumn) {
        LinkedHashMap<String, String> lColumnMapData = new LinkedHashMap<>();
        Cell lCell;
        if (pRow == null) {
            if (pSheet.getRow(pSheet.getFirstRowNum()).getCell(pCurrentColumn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                    .getCellType() != CellType.BLANK) {
                String lColumnHeaderName = pSheet.getRow(pSheet.getFirstRowNum()).getCell(pCurrentColumn)
                        .getStringCellValue();
                lColumnMapData.put(lColumnHeaderName, "");
            }
        } else {
            lCell = pRow.getCell(pCurrentColumn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (lCell.getCellType() == CellType.STRING) {
                if (pSheet.getRow(pSheet.getFirstRowNum())
                        .getCell(lCell.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                        .getCellType() != CellType.BLANK) {
                    String lColumnHeaderName = pSheet.getRow(pSheet.getFirstRowNum()).getCell(lCell.getColumnIndex())
                            .getStringCellValue();
                    lColumnMapData.put(lColumnHeaderName, lCell.getStringCellValue());
                }
            } else if (lCell.getCellType() == CellType.NUMERIC) {
                if (pSheet.getRow(pSheet.getFirstRowNum())
                        .getCell(lCell.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                        .getCellType() != CellType.BLANK) {
                    String lColumnHeaderName = pSheet.getRow(pSheet.getFirstRowNum()).getCell(lCell.getColumnIndex())
                            .getStringCellValue();
                    lColumnMapData.put(lColumnHeaderName, NumberToTextConverter.toText(lCell.getNumericCellValue()));
                }
            } else if (lCell.getCellType() == CellType.BLANK) {
                if (pSheet.getRow(pSheet.getFirstRowNum())
                        .getCell(lCell.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                        .getCellType() != CellType.BLANK) {
                    String lColumnHeaderName = pSheet.getRow(pSheet.getFirstRowNum()).getCell(lCell.getColumnIndex())
                            .getStringCellValue();
                    lColumnMapData.put(lColumnHeaderName, "");
                }
            } else if (lCell.getCellType() == CellType.BOOLEAN) {
                if (pSheet.getRow(pSheet.getFirstRowNum())
                        .getCell(lCell.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                        .getCellType() != CellType.BLANK) {
                    String lColumnHeaderName = pSheet.getRow(pSheet.getFirstRowNum()).getCell(lCell.getColumnIndex())
                            .getStringCellValue();
                    lColumnMapData.put(lColumnHeaderName, Boolean.toString(lCell.getBooleanCellValue()));
                }
            } else if (lCell.getCellType() == CellType.ERROR) {
                if (pSheet.getRow(pSheet.getFirstRowNum())
                        .getCell(lCell.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                        .getCellType() != CellType.BLANK) {
                    String lColumnHeaderName = pSheet.getRow(pSheet.getFirstRowNum()).getCell(lCell.getColumnIndex())
                            .getStringCellValue();
                    lColumnMapData.put(lColumnHeaderName, Byte.toString(lCell.getErrorCellValue()));
                }
            }
        }
        return lColumnMapData;
    }

    /**
     * Escribe un String en el la celda y archivo correspondiente a los valores pasados por parámetro.
     * @param pFilePath
     * @param pSheetName
     * @param pColumna
     * @param pFila
     * @param pValor
     * @throws IOException
     */
    private void writeDataToExcel(String pFilePath, String pSheetName, int pColumna, int pFila, String pValor) throws IOException {
        FileInputStream lFis = new FileInputStream(pFilePath);
        XSSFWorkbook lWorkbook = new XSSFWorkbook (lFis);
        XSSFSheet lSheet = lWorkbook.getSheet(pSheetName);
        XSSFRow lRow1 = lSheet.getRow(pFila);
        XSSFCell lCell1 = lRow1.getCell(pColumna);
        lCell1.setCellValue(pValor);
        lFis.close();
        FileOutputStream lFos =new FileOutputStream(pFilePath);
        lWorkbook.write(lFos);
        lFos.close();
    }

    /**
     * Retorna el valor del parámetro y escenario correspondiente.
     *
     * @param pParametro
     * @param pEscenario
     * @return lValue
     * @throws IOException
     */
    public static String extractDataToExcel(String pParametro) throws IOException {
        properties = LoadProperties.loadProperties();
        String lEscenario = extraerParametro("Escenario");

        String lEscenarioFile = lEscenario.split("_")[1];
        String lFileName = properties.getProperty(lEscenarioFile);
        int lRowNum = Integer.parseInt(lEscenario.split("_")[2]);

        ExcelReader lExcelReader = new ExcelReader();
        String lFilePath = DATATABLES_PATH + lFileName + ".xlsx";
        String lDataSheet = "DataSheet";

        List<Map<String, String>> lTestData = lExcelReader.getData(lFilePath, lDataSheet);
        int lTamData = lTestData.get(lRowNum - 1).size();

        String lVariable;
        int lNumVariable;
        String lValue = null;
        for (int x = 0; x < lTamData; x++) {
            lNumVariable = x + 1;
            lVariable = "Variable" + lNumVariable;
            String lData1 = lTestData.get(lRowNum - 1).get(lVariable);
            String[] lDataValues = lData1.split("\\|\\|");
            String lKey = lDataValues[0];
            if (lKey.equals(pParametro)) {
                lValue = lDataValues[1];
                break;
            }
        }
        return lValue;
    }

    /**
     * Escribe un dato en el escenario y parámetro correspondiente.
     * @param pParametro
     * @param pDato
     * @param pEscenario
     * @throws IOException
     */
    public static void setDataToExcel(String pParametro, String pDato, String pEscenario) throws IOException {
        properties = LoadProperties.loadProperties();
        if (pEscenario.equals("")){
            pEscenario = extraerParametro("Escenario");
        }
        String lEscenarioFile = pEscenario.split("_")[1];
        String lFileName = properties.getProperty(lEscenarioFile);
        String lSetData = pParametro + "||" + pDato;
        int lRowNum = Integer.parseInt(pEscenario.split("_")[2]);

        ExcelReader lExcelReader = new ExcelReader();
        String lFilePath = DATATABLES_PATH + lFileName + ".xlsx";
        String lDataSheet = "DataSheet";

        List<Map<String, String>> lTestData = lExcelReader.getData(lFilePath, lDataSheet);
        int lTamData = lTestData.get(lRowNum - 1).size();

        String lVariable;
        int lNumVariable = 0;
        for (int x = 1; x < lTamData; x++) {
            lVariable = "Variable" + x;
            String lData1 = lTestData.get(lRowNum - 1).get(lVariable);
            String[] lDataValues = lData1.split("\\|\\|");
            String lKey = lDataValues[0];
            if (lKey.equals(pParametro) || lKey.equals("")) {
                lNumVariable = x;
                x = lTamData;
            }
        }
        lExcelReader.writeDataToExcel(lFilePath, lDataSheet,  lNumVariable,lRowNum, lSetData);
    }
}