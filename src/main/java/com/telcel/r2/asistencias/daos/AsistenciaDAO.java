/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.r2.asistencias.daos;

import com.telcel.r2.asistencias.entities.Asistencia;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author HIN4460
 */
@Dependent
@Transactional
public class AsistenciaDAO {

    @PersistenceContext(unitName = "MSSQL_SADP_PU")
    private EntityManager em;
   
   public void cargarAsistencia(Asistencia asistencia) {
       em.persist(asistencia);
       em.flush();
       em.detach(asistencia);
   }
   
}
