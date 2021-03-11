package com.telcel.r2.asistencias.entities;

import java.util.Calendar;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-04-29T18:59:58")
@StaticMetamodel(Asistencia.class)
public class Asistencia_ { 

    public static volatile SingularAttribute<Asistencia, Boolean> valida;
    public static volatile SingularAttribute<Asistencia, Calendar> fecha;
    public static volatile SingularAttribute<Asistencia, Calendar> checkIn;
    public static volatile SingularAttribute<Asistencia, String> promotorId;
    public static volatile SingularAttribute<Asistencia, Double> latOut;
    public static volatile SingularAttribute<Asistencia, Double> longIn;
    public static volatile SingularAttribute<Asistencia, Long> id;
    public static volatile SingularAttribute<Asistencia, Double> latIn;
    public static volatile SingularAttribute<Asistencia, Double> longOut;
    public static volatile SingularAttribute<Asistencia, Calendar> checkOut;

}