/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.r2.asistencias.bean;

import com.telcel.r2.asistencias.daos.AsistenciaDAO;
import com.telcel.r2.asistencias.entities.Asistencia;
import com.telcel.r2.asistencias.exceptions.InvalidFileException;
import com.telcel.r2.asistencias.utils.excel.ExcelFormatReader;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author emman
 */
@Startup
public class AsistenciaLoader {

    @Inject
    private AsistenciaDAO asisDao;

    @PostConstruct
    private void init() {
        System.out.println("Loading");
        String dirPath = "D:\\proyectos\\asistencias\\";
        File asisDirectory = new File(dirPath);

        if (!asisDirectory.exists()) {
            System.out.println("falla al encontrar carpeta");
            return;
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar hoy = Calendar.getInstance();

        for (final File f : asisDirectory.listFiles()) {
            try {
                Sheet formSheet = ExcelFormatReader.getFormatSheet(f);
                cargarFecha(formSheet);

            } catch (InvalidFileException ex) {
                System.out.println("Error con archivo: " + ex.getMessage());
            }

        }
    }

    public void cargarFecha(Sheet formSheet) throws InvalidFileException {

        int firstRow = formSheet.getFirstRowNum() + 1; //La primer fila son los titulos, por lo que iniciamos del siguinete renglon
        int lastRow = formSheet.getLastRowNum();

        for (int i = firstRow; i < lastRow; i++) {
            Row row = formSheet.getRow(i);
            Row nextRow = formSheet.getRow(i + 1);
            Asistencia asistencia = new Asistencia();

            try {
                buildAsistencia(asistencia, row, nextRow);
                i++;

            } finally {
                if (asistencia.getFecha() != null) {
                    asisDao.cargarAsistencia(asistencia);
                }
            }

        }
    }

    private void buildAsistencia(Asistencia asistencia, Row row, Row nextRow) throws InvalidFileException {

        String pId;
        String pIdNextRow;

        String check;
        String checkNextRow;

        Date fechaDate;
        Calendar fecha = Calendar.getInstance();
        Calendar checkIn = null;
        Calendar checkOut = null;
        Double longIn = null;
        Double latIn = null;
        Double longOut = null;
        Double latOut = null;

        // ========== FECHA ==========        
        fechaDate = (Date) ExcelFormatReader.readCellValue(row.getCell(1), true);
        fecha.setTime(fechaDate);

        // ========== CHECK ==========
        check = ExcelFormatReader.readCellValueAsString(row.getCell(2)).toLowerCase().replaceAll("\\s", "");

        // ========== PROMOTOR_ID ==========
        pId = ExcelFormatReader.readCellValueAsString(row.getCell(5));

        // ========== HORA ==========
        if (fechaDate != null) {
            if (check.equals("checkin")) {
                checkIn = Calendar.getInstance();
                checkIn.setTime(fechaDate);
            } else if (check.equals("checkout")) {
                checkOut = Calendar.getInstance();
                checkOut.setTime(fechaDate);
            }
        }

        // ========== LATITUD ==========
        if (check.equals("checkin")) {
            latIn = Double.parseDouble(ExcelFormatReader.readCellValueAsString(row.getCell(8)));
        } else if (check.equals("checkout")) {
            latOut = Double.parseDouble(ExcelFormatReader.readCellValueAsString(row.getCell(8)));
        }

        // ========== LONGITUD ==========
        if (check.equals("checkin")) {
            longIn = Double.parseDouble(ExcelFormatReader.readCellValueAsString(row.getCell(9)));
        } else if (check.equals("checkout")) {
            longOut = Double.parseDouble(ExcelFormatReader.readCellValueAsString(row.getCell(9)));
        }

        // ========== VALIDAR SI EL SIGUIENTE DATO ES DEL MISMO PROMOTOR ==========
        // ========== PROMOTOR_ID SIGUIENTE RENGLON ==========
        pIdNextRow = ExcelFormatReader.readCellValueAsString(nextRow.getCell(5));

        // ========== CHECK DEL SIGUIENTE RENGLON ==========
        checkNextRow = ExcelFormatReader.readCellValueAsString(nextRow.getCell(2), true).toLowerCase().replaceAll("\\s", "");

//        REVISAR SI EL CHECK-OUT ES FUERA DE TIEMPO        
//        Date fechaDateNext = (Date) ExcelFormatReader.readCellValue(nextRow.getCell(1), true);
//        Calendar fechaNext = Calendar.getInstance();
//        fechaNext.setTime(fechaDateNext);
//        System.out.println(pId + ": " + fecha.get(Calendar.DATE));
//        System.out.println(pIdNextRow + ": " + fechaNext.get(Calendar.DATE));
        if (pId.equals(pIdNextRow)) {
            // Eso indica que el siguiente renglon es parte del mismo registro
            if (!check.equals(checkNextRow)) {
                //Esto indica que el registro no esta duplicado: 2 Check in o 2 Check out del mismo promotor
                // ========== FECHA ==========
                fechaDate = (Date) ExcelFormatReader.readCellValue(nextRow.getCell(1), true);

                // ========== HORA ==========
                if (fechaDate != null) {
                    if (checkNextRow.equals("checkin")) {
                        checkIn = Calendar.getInstance();
                        checkIn.setTime(fechaDate);
                    } else if (checkNextRow.equals("checkout")) {
                        checkOut = Calendar.getInstance();
                        checkOut.setTime(fechaDate);
                    }
                }

                // ========== LATITUD ==========
                if (checkNextRow.equals("checkin")) {
                    latIn = Double.parseDouble(ExcelFormatReader.readCellValueAsString(row.getCell(8)));
                } else if (checkNextRow.equals("checkout")) {
                    latOut = Double.parseDouble(ExcelFormatReader.readCellValueAsString(row.getCell(8)));
                }

                // ========== LONGITUD ==========
                if (checkNextRow.equals("checkin")) {
                    longIn = Double.parseDouble(ExcelFormatReader.readCellValueAsString(nextRow.getCell(9)));
                } else if (checkNextRow.equals("checkout")) {
                    longOut = Double.parseDouble(ExcelFormatReader.readCellValueAsString(nextRow.getCell(9)));
                }

            } else {
                return;
            }            
        }
        asistencia.setAll(fecha, pId, checkIn, checkOut, latIn, longIn, latOut, longOut);

    }

}
