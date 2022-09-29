package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.FuncionesDinamicasDTO;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a las funciones dinamicas
 * @author duvan.naranjo
 */
public interface ITarifasOperacionDelegate {

	TarifasOperacionDTO eliminarTarifasOperacion(Integer idTarifaOperacion);

	TarifasOperacionDTO actualizarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO);

	TarifasOperacionDTO getTarifasOperacionById(Integer idTarifaOperacion);

	TarifasOperacionDTO guardarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO);

	List<TarifasOperacionDTO> getTarifasOperacion(Predicate predicate);



}
