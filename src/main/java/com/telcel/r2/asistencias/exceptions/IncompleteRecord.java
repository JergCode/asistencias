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
public class IncompleteRecord extends Exception {

    /**
     * Creates a new instance of <code>IncompleteRegister</code> without detail
     * message.
     */
    public IncompleteRecord() {
    }

    /**
     * Constructs an instance of <code>IncompleteRegister</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public IncompleteRecord(String msg) {
        super(msg);
    }
    
}
