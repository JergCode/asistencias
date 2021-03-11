/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.r2.asistencias.utils.excel;

import com.telcel.r2.asistencias.exceptions.InvalidFileException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author HIN4460
 */
public class ExcelFormatReader {
//
//    static public Sheet getFormatSheet(File file) throws InvalidFileException {
//        return ExcelFormatReader.getFormatSheet(file, 0);
//    }
//
//    static public Sheet getFormatSheet(File file, int sheetId) throws InvalidFileException {
//        try (Workbook wb = WorkbookFactory.create(file)) {
//            return wb.getSheetAt(sheetId);
//        } catch (IOException | EncryptedDocumentException ex) {
//            throw new InvalidFileException("Archivo corrupto o invalido", "Revise que el archivo no este corrupto y sea valido,"
//                    + " si el problema persiste comuniquese con sistemas");
//        }
//    }

    static public Sheet getFormatSheet(File file) throws InvalidFileException {
        return ExcelFormatReader.getFormatSheet(file, 0);
    }

    static public Sheet getFormatSheet(File file, int sheetId) throws InvalidFileException {
        try (Workbook wb = WorkbookFactory.create(file)) {
            return wb.getSheetAt(sheetId);
        } catch (IOException | EncryptedDocumentException ex) {
            throw new InvalidFileException("Archivo corrupto o invalido", "Revise que el archivo no este corrupto y sea valido,"
                    + " si el problema persiste comuniquese con sistemas");
        }
    }

    static public Object readCellValue(Cell cell, boolean isDate) throws InvalidFileException {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType().name()) {
            case "STRING":
                return cell.getStringCellValue();
            case "NUMERIC":
                return isDate
                        ? cell.getDateCellValue()
                        : cell.getNumericCellValue();
            case "BOOLEAN":
                return cell.getBooleanCellValue();
            case "BLANK":
                throw new InvalidFileException("Formato Incompleto",
                        "Los campos del formato no estan completos, favor de revisar...");
            default:
                return null;
        }
    }    

    static public String readCellValueAsString(Cell cell, boolean mustHaveValue) throws InvalidFileException {
        if (cell == null) {
            return null;
        }
        
        DataFormatter df = new DataFormatter();
        switch (cell.getCellType().name()) {
            case "STRING":
                return cell.getStringCellValue();
            case "NUMERIC":
            case "BOOLEAN":
                return df.formatCellValue(cell);
            case "BLANK":
                if (mustHaveValue) {
                    throw new InvalidFileException("Formato Incompleto",
                            "Los campos del formato no estan completos, favor de revisar...");
                } else {
                    return "";
                }
            default:
                return null;
        }
    }

    static public String readCellValueAsString(Cell cell) throws InvalidFileException {
        return ExcelFormatReader.readCellValueAsString(cell, true);
    }

    static public int getLastNSSRow(Sheet sheet, int firstRow) throws InvalidFileException {

        int lastRow = sheet.getLastRowNum();
        if (lastRow > firstRow) {
            Iterator<Row> i = sheet.iterator();
            while (i.hasNext()) {
                Row row = i.next();
                if (row.getRowNum() < firstRow) {
                    continue;
                }
                Cell cell;
                cell = row.getCell(2);
                if (cell.getCellType() == CellType.BLANK) {
                    if (row.getRowNum() == firstRow) {
                        throw new InvalidFileException("Formato Incompleto", "El formato no tiene datos cargados, favor de revisar...");
                    }
                    return row.getRowNum() - 1;
                }
            }
            return lastRow;
        }
        throw new InvalidFileException("Formato modificado o corrupto", "Baje el formato establecido e intente de nuevo");

    }
}
