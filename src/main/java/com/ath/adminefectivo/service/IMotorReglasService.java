package com.ath.adminefectivo.service;

import com.ath.adminefectivo.dto.ReglasDetalleArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionMotorDTO;

/**
 * Interfaz de los servicios referentes al motor de reglas 
 *
 * @author rparra
 */
public interface IMotorReglasService {

    /**
     * 
     * @param regla
     * @param valorCampo
     * @return boolean
     * @author rparra
     */
    ValidacionMotorDTO evaluarReglaSimple(String regla, String valorCampo);

    /**
     * Metodo encargado de realizar la ejecución de una regla
     * 
     * @param reglaVO
     * @param valorCampo
     * @return booolean
     * @author rparra
     */
    boolean compilarRegla(ReglasDetalleArchivoDTO reglaVO, String valorCampo);

    /**
     * 
     * @param regla
     * @param valorCampo
     * @return
     * @return boolean
     * @author CamiloBenavides
     */
    ValidacionMotorDTO evaluarReglaMultiple(String regla, String valorCampo);

    


}