/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.r2.asistencias.exceptions;

/**
 *
 * @author HIN4460
 */
public class InvalidFileException extends Exception {

    private String summary;
    /**
     * Creates a new instance of <code>InvalidFileException</code> without
 summary message.
     */
    public InvalidFileException() {
    }

    /**
     * Constructs an instance of <code>InvalidFileException</code> with the
 specified summary message.
     *
     * @param msg the summary message.
     */
    public InvalidFileException(String msg) {
        super(msg);
    }
    
    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFileException(Throwable cause) {
        super(cause);
    }
    
    public InvalidFileException(String summary, String detail){
        super(detail);
        this.summary = summary;
    }

    public String getSummary() {
        return summary == null ? "" : summary;
    }        
}
