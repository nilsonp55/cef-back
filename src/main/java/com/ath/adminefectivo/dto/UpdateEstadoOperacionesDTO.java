package com.ath.adminefectivo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author prv_nparra
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEstadoOperacionesDTO {
	/**
	 * Id de la operacion programada para buscar y actualizar en BD
	 */
    private int idOperacion;
    
    /**
     * Valor del estado a ser actualizado en BD
     */
    private String estado;
    
    /**
     * Indica si la actualizacion del registro de la operacion programada fue exitosa.
     */
    private boolean updateExitoso;
    
    /**
     * Cuando updateExitoso es False, se establece una descripcion del error ocurrido
     */
    private String resultadoFallido;
}
