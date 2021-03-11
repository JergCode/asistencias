/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.r2.asistencias;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.telcel.r2.asistencias.entities.Asistencia;
import com.telcel.r2.asistencias.exceptions.IncompleteRecord;
import com.telcel.r2.asistencias.exceptions.InvalidFileException;
import com.telcel.r2.asistencias.utils.excel.ExcelFormatReader;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Time;
import java.sql.Types;
import java.util.Calendar;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author emman
 */
public class Main {

    public static void main(String[] args) {

        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser("hin4460");
        ds.setPassword("Leunamme-123");
        ds.setServerName("10.27.1.8");
        ds.setPortNumber(1433);
        ds.setDatabaseName("JavaProject");
        try (Connection conn = ds.getConnection()) {
            if (conn == null) {
                System.out.println("Fail to Connect");
                return;
            }

            File attDir = new File("D:\\proyectos\\asistencias\\");

            if (!attDir.exists()) {
                System.out.println("falla al encontrar carpeta");
                return;
            }

            for (final File f : attDir.listFiles()) {
                try {
                    Sheet formSheet = ExcelFormatReader.getFormatSheet(f);
                    cargarFecha(conn, formSheet);

                } catch (InvalidFileException ex) {
                    System.out.println("Error con archivo: " + ex.getMessage());
                }
            }

        } catch (SQLException ex) {
            System.out.println("Falla de SQLExp");
            ex.printStackTrace();
        }

    }
    
    static private void updateAttendace(Connection conn, Asistencia a) throws SQLException {
        System.out.println(a);
        String sqlQuery
                = "UPDATE sdap.asistencias "
                + "SET check_out = ?, latitud_out = ?, longitud_out = ?, valida = ? "
                + "WHERE fecha = ? AND promotor_id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            Date fecha = new Date(a.getFecha().getTimeInMillis());
            if (a.getCheckOut() != null) {
                ps.setTime(1, new Time(a.getCheckOut().getTimeInMillis()));                
            } else {
                ps.setTime(1, null);
            }
            if (a.getLatOut() != null) {
                ps.setDouble(2, a.getLatOut());
            } else {
                ps.setNull(2, Types.NUMERIC);
            }
            if (a.getLongOut() != null) {
                ps.setDouble(3, a.getLongOut());
            } else {
                ps.setNull(3, Types.NUMERIC);
            }
            ps.setBoolean(4, a.isValida());

            ps.setDate(5, fecha);
            ps.setString(6, a.getPromotorId());          

            ps.executeUpdate();
        }
    }

    static private void insertAttendance(Connection conn, Asistencia a) throws SQLException {
        System.out.println(a);
        String sqlQuery
                = "INSERT INTO sdap.asistencias "
                + "(fecha, promotor_id, check_in, check_out, latitud_in, longitud_in, latitud_out, longitud_out, valida) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            Date fecha = new Date(a.getFecha().getTimeInMillis());
            ps.setDate(1, fecha);
            ps.setString(2, a.getPromotorId());
            if (a.getCheckIn() != null) {
                ps.setTime(3, new Time(a.getCheckIn().getTimeInMillis()));                
            } else {
                ps.setTime(3, null);
            }            
            if (a.getCheckOut() != null) {
                ps.setTime(4, new Time(a.getCheckOut().getTimeInMillis()));                
            } else {
                ps.setTime(4, null);
            }
            if (a.getLatIn() != null) {
                ps.setDouble(5, a.getLatIn());
            } else {
                ps.setNull(5, Types.NUMERIC);
            }
            if (a.getLongIn() != null) {
                ps.setDouble(6, a.getLongIn());
            } else {
                ps.setNull(6, Types.NUMERIC);
            }
            if (a.getLatOut() != null) {
                ps.setDouble(7, a.getLatOut());
            } else {
                ps.setNull(7, Types.NUMERIC);
            }
            if (a.getLongOut() != null) {
                ps.setDouble(8, a.getLongOut());
            } else {
                ps.setNull(8, Types.NUMERIC);
            }
            ps.setBoolean(9, a.isValida());

            ps.executeUpdate();
        }
    }

    static public void cargarFecha(Connection conn, Sheet formSheet) throws InvalidFileException, SQLException {

        int firstRow = formSheet.getFirstRowNum() + 1; //La primer fila son los titulos, por lo que iniciamos del siguinete renglon
        int lastRow = formSheet.getLastRowNum();

        for (int i = firstRow; i < lastRow; i++) {
            Row row = formSheet.getRow(i);
            Row nextRow = formSheet.getRow(i + 1);
            Asistencia asistencia = new Asistencia();

            try {
                buildAsistencia(asistencia, row, nextRow);
                i++;
            } catch (IncompleteRecord ex) {

            } finally {
                if (asistencia.getFecha() != null) {
//                    insertAttendance(conn, asistencia);
                    updateAttendace(conn, asistencia);
                }
            }

        }
    }

    static private void buildAsistencia(Asistencia asistencia, Row row, Row nextRow) throws InvalidFileException, IncompleteRecord {

        String pId;
        String pIdNextRow;

        String check;
        String checkNextRow;

        java.util.Date fechaDate;
        Calendar fecha = Calendar.getInstance();
        Calendar checkIn = null;
        Calendar checkOut = null;
        Double longIn = null;
        Double latIn = null;
        Double longOut = null;
        Double latOut = null;

        // ========== FECHA ==========        
        fechaDate = (java.util.Date) ExcelFormatReader.readCellValue(row.getCell(1), true);
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
                fechaDate = (java.util.Date) ExcelFormatReader.readCellValue(nextRow.getCell(1), true);

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
                throw new IncompleteRecord("algo");
            }
        } else {
            asistencia.setAll(fecha, pId, checkIn, checkOut, latIn, longIn, latOut, longOut);
            throw new IncompleteRecord("algo");
        }
        asistencia.setAll(fecha, pId, checkIn, checkOut, latIn, longIn, latOut, longOut);

    }
}
