/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.r2.asistencias.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author HIN4460
 */
@Entity
@Table(name = "asistencias")
public class Asistencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Calendar fecha;
    @Column(name = "promotor_id")
    private String promotorId;
    @Column(name = "check_in")
    @Temporal(TemporalType.TIME)
    private Calendar checkIn;
    @Column(name = "check_out")
    @Temporal(TemporalType.TIME)
    private Calendar checkOut;
    @Column(name = "latitud_in")
    private Double latIn;
    @Column(name = "longitud_in")
    private Double longIn;
    @Column(name = "latitud_out")
    private Double latOut;
    @Column(name = "longitud_out")
    private Double longOut;
    @Column(name = "valida")
    private boolean valida;

    public Asistencia() {
        this.valida = false;
    }

    public Asistencia(Calendar fecha, String promotorId, Calendar checkIn, Calendar checkOut, Double latIn, Double longIn, Double latOut, Double longOut) {
        this.fecha = fecha;
        this.promotorId = promotorId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.latIn = latIn;
        this.longIn = longIn;
        this.latOut = latOut;
        this.longOut = longOut;
        this.valida = false;
    }

    public void setAll(Calendar fecha, String promotorId, Calendar checkIn, Calendar checkOut, Double latIn, Double longIn, Double latOut, Double longOut) {
        this.fecha = fecha;
        this.promotorId = promotorId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.latIn = latIn;
        this.longIn = longIn;
        this.latOut = latOut;
        this.longOut = longOut;
        esValida();

    }

    public void esValida() {
        if (this.checkIn != null && this.checkOut != null) {
            this.valida = true;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public String getPromotorId() {
        return promotorId;
    }

    public void setPromotorId(String promotorId) {
        this.promotorId = promotorId;
    }

    public Calendar getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Calendar checkIn) {
        this.checkIn = checkIn;
    }

    public Calendar getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Calendar checkOut) {
        this.checkOut = checkOut;
    }

    public Double getLatIn() {
        return latIn;
    }

    public void setLatIn(Double latIn) {
        this.latIn = latIn;
    }

    public Double getLongIn() {
        return longIn;
    }

    public void setLongIn(Double longIn) {
        this.longIn = longIn;
    }

    public Double getLatOut() {
        return latOut;
    }

    public void setLatOut(Double latOut) {
        this.latOut = latOut;
    }

    public Double getLongOut() {
        return longOut;
    }

    public void setLongOut(Double longOut) {
        this.longOut = longOut;
    }

    public boolean isValida() {
        return valida;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String fechaStr = this.fecha == null
                ? "null"
                : df.format(this.fecha.getTime());
        String checkInStr = this.checkIn == null
                ? "null"
                : df.format(this.checkIn.getTime());
        String checkOutStr = this.checkOut == null
                ? "null"
                : df.format(this.checkOut.getTime());
        return "Asistencia{"
                + "id=" + id
                + ", fecha=" + fechaStr
                + ", promotorId=" + promotorId
                + ", checkIn=" + checkInStr
                + ", checkOut=" + checkOutStr
                + ", latIn=" + latIn
                + ", longIn=" + longIn
                + ", latOut=" + latOut
                + ", longOut=" + longOut
                + ", valida=" + valida
                + '}';
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null
                ? id.hashCode()
                : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asistencia)) {
            return false;
        }
        Asistencia other = (Asistencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
