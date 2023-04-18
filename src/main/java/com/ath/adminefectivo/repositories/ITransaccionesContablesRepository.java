package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.compuestos.ConteoContabilidadDTO;
import com.ath.adminefectivo.entities.TransaccionesContables;

public interface ITransaccionesContablesRepository extends JpaRepository<TransaccionesContables, Long> {
	
	/**
	 * Retorna una lista de transacciones contables por fecha
	 * @param start
	 * @param end
	 * @return List<TransaccionesContables>
	 * @author duvan.naranjo
	 */
	List<TransaccionesContables> findByFechaBetween(Date start, Date end);
	
	/**
	 * Retorna una lista de transacciones contables por fecha
	 * @param start
	 * @param end
	 * @param tipoProceso
	 * @return List<TransaccionesContables>
	 * @author duvan.naranjo
	 */
	List<TransaccionesContables> findByFechaBetweenAndTipoProceso(Date start, Date end, String tipoProceso);

	/**
	 * Retorna una lista de transacciones contables por naturaleza, debito y credito
	 * @param start
	 * @param end
	 * @return List<TransaccionesContables>
	 * @author Miller.Caro
	 */
	List<TransaccionesContables> findByNaturaleza(String Naturaleza);
	
	
	/**
	 * Retorna una lista de transacciones contables por fecha,tipocontabilidad, y por banco
	 * @param start
	 * @param end
	 * @return List<TransaccionesContables>
	 * @author Miller.Caro
	 */
	@Query(nativeQuery = true)
	List<RespuestaContableDTO> cierreContablebyBanco(@Param("fecha")Date fecha, @Param("tipoContabilidad") String tipoContabilidad, @Param("codBanco") int codBanco, @Param("estado") int estado);
	
	/**
	 * Retorna una lista de transacciones contables por fecha,tipocontabilidad, y todos banco
	 * @param start
	 * @param end
	 * @return List<TransaccionesContables>
	 * @author Miller.Caro
	 */
	@Query(nativeQuery = true)
	List<RespuestaContableDTO> cierreContableAllBancos(Date fecha,String tipoContabilidad, int estado);
	
	/**
	 * Retorna una lista de errorescontables banco por el estado en transacciones internas
	 * @param start
	 * @param end
	 * @return List<TransaccionesContables>
	 * @author Miller.Caro
	 */
	@Query(value ="SELECT tc.id_operacion,tc.id_generico,tc.fecha, tc.consecutivo_dia, tc.tipo_transaccion, tc.banco_aval, "
			+ "	tc.codigo_centro, tc.naturaleza, tc.cuenta_contable, tc.codigo_moneda, tc.valor, tc.tipo_proceso, "
			+ "	tc.numero_comprobante, tc.tipo_identificacion, tc.id_tercero, tc.nombre_tercero, tc.identificador, "
			+ "	tc.descripcion, tc.referencia1,tc.referencia2 "
			+ "	FROM transacciones_contables tc, cuentas_puc cp, transacciones_internas ti where "
			+ " tc.CUENTA_CONTABLE = cp.CUENTA_CONTABLE AND "
			+ " tc.naturaleza in('C','D') AND "
			+ " cp.nombre_cuenta <> 'Transitoria%' AND "
			+ " ti.estado = 2 AND "
			+ " tc.id_transacciones_internas = ti.id_transacciones_internas AND "
			+ " tc.fecha = ?1 AND ti.tipo_proceso = ?2 AND tc.banco_aval = ?3 ",nativeQuery=true)
	List<RespuestaContableDTO> erroresContablesbybancoddd(Date fecha,String tipoContabilidad,int codBanco);
	
	/**
	 * Retorna una lista de errorescontables por bancos por el estado en transacciones internas
	 * @param start
	 * @param end
	 * @return List<TransaccionesContables>
	 * @author Miller.Caro
	 * */
	@Query(value ="SELECT tc.id_operacion,tc.id_generico,tc.fecha, tc.consecutivo_dia, tc.tipo_transaccion, tc.banco_aval, "
			+ "tc.codigo_centro, tc.naturaleza, tc.cuenta_contable, tc.codigo_moneda, tc.valor, tc.tipo_proceso, "
			+ "tc.numero_comprobante, tc.tipo_identificacion, tc.id_tercero, tc.nombre_tercero, tc.identificador, "
			+ "tc.descripcion, tc.referencia1,tc.referencia2 "
			+ "FROM Transacciones_contables tc, cuentas_puc cp, transacciones_internas ti WHERE "
			+ " tc.CUENTA_CONTABLE = cp.CUENTA_CONTABLE AND "
			+ " tc.naturaleza in('C','D') AND "
			+ " cp.nombre_cuenta <> 'Transitoria%' AND "
			+ " ti.estado = 3 AND "
			+ " tc.id_transacciones_internas = ti.id_transacciones_internas) AND "
			+ " tc.fecha = ?1 AND ti.tipo_proceso = ?2)",nativeQuery=true)
	List<RespuestaContableDTO> erroresContablesAllbanco(Date fecha,String tipoContabilidad);
	
	/**
	 * Retorna proceso contable AM o PM
	 * @param start
	 * @param end
	 * @return string String tipoPoceso 
	 * @author Miller.Caro
	 * */
	String findBytipoProceso(String str);

	@Query(nativeQuery = true)
	ConteoContabilidadDTO conteoContabilidad(Date fechaInicio, Date fechaFin, String tipoProceso);

	/**
	 * Ejecuta el procedimiento de la base de datos generarcomprobantecontable
	 * 
	 * @param fecha
	 * @param tipoContabilidad
	 * @return String
	 * @author rparra
	 */
	@Procedure(name = "generarcomprobantecontable")
	String generarcomprobantecontable(@Param("pfechaproceso") Date fecha, @Param("ptipoproceso") String tipoContabilidad);
	
	/**
	 * Retorna una lista de strings con el resultado concatenado por comas
	 * @param start
	 * @param end
	 * @return List<TransaccionesContables>
	 * @author duvan.naranjo
	 * */
	@Query(value ="SELECT "
			+ "'' || CASE WHEN tc.naturaleza = 'C' THEN '50' ELSE '40' END ||','|| "
			+ "			    TRIM(tc.cuenta_contable) ||','|| "
			+ "			    CASE WHEN tc.cuenta_auxiliar IS NULL THEN '' ELSE TRIM(tc.cuenta_auxiliar) END ||','|| "
			+ "			    CASE WHEN tc.tipo_identificacion IS NULL THEN '' WHEN tc.tipo_identificacion = 'NIT' THEN '31' ELSE  TRIM(tc.tipo_identificacion) END ||','|| "
			+ "			    '' ||','|| "
			+ "			    '' ||','|| "
			+ "			    CASE WHEN tc.valor IS NULL THEN '' ELSE TRIM(REPLACE(to_char(tc.valor,'999G999G999G999G999'),',','.')) end ||','|| "
			+ "				CASE WHEN tc.valor IS NULL THEN '' ELSE TRIM(REPLACE(to_char(tc.valor,'999G999G999G999G999'),',','.')) end ||','|| "
			+ "			    '' ||','|| "
			+ "			    '' ||','|| "
			+ "			    CASE WHEN tc.codigo_centro IS NULL THEN '' ELSE TRIM(tc.codigo_centro) END||','|| "
			+ "			    '' ||','|| "
			+ "			    '' ||','|| "
			+ "			    CASE WHEN tc.identificador IS NULL THEN '' ELSE TRIM(tc.identificador) END||','|| "
			+ "				CASE WHEN tc.descripcion IS NULL THEN '' ELSE TRIM(tc.descripcion) END||','|| "
			+ "				CASE WHEN tc.id_tercero IS NULL THEN '' WHEN tc.id_tercero = 860035827 THEN CAST(tc.id_tercero as varchar)||'5' "
			+ "						ELSE CAST(tc.id_tercero as varchar) END||','|| "
			+ "				CASE WHEN tc.nombre_tercero IS NULL THEN '' ELSE TRIM(tc.nombre_tercero) END||','|| "
			+ "			    '' ||','|| "
			+ "			    '' ||','|| "
			+ "				CASE WHEN tc.referencia2 IS NULL THEN '' ELSE TRIM(tc.referencia2) END AS texto "
			+ " FROM "
			+ "	transacciones_contables tc, "
			+ "	cuentas_puc cp, "
			+ "	transacciones_internas ti, "
			+ "	bancos b "
			+ "WHERE "
			+ "	tc.cuenta_contable = cp.CUENTA_CONTABLE AND "
			+ "	tc.banco_aval = cp.banco_aval AND "
			+ "	cp.nombre_cuenta NOT LIKE 'Transitoria%' AND "
			+ "	tc.id_transacciones_internas = ti.id_transacciones_internas AND "
			+ "	ti.estado = ?4 AND "
			+ "	tc.fecha = ?1 AND "
			+ "	ti.tipo_proceso = ?2 AND "
			+ "	tc.banco_aval = ?3 AND "
			+ "	tc.banco_aval = b.codigo_punto "
			+ "order by tc.id_operacion, tc.id_transacciones_internas, tc.id_transacciones_contables",nativeQuery=true)
	List<String> cierreContablebyBancoF1String(Date fecha, String tipoContabilidad, int codBanco, int estado);
	
	/**
	 * Retorna una lista de strings con el resultado concatenado por comas
	 * @param start
	 * @param end
	 * @return List<TransaccionesContables>
	 * @author duvan.naranjo
	 * */
	@Query(value ="SELECT"
			+ "     '' || CASE WHEN tc.naturaleza = 'C' THEN '50' ELSE '40' END ||','|| "
			+ "    trim(tc.cuenta_contable) ||','|| "
			+ "    CASE WHEN tc.cuenta_auxiliar IS NULL THEN '' ELSE trim(tc.cuenta_auxiliar) END ||','|| "
			+ "    CASE WHEN tc.tipo_identificacion IS NULL THEN '' WHEN tc.tipo_identificacion = 'NIT' THEN '31' ELSE  trim(tc.tipo_identificacion) END ||','|| "
			+ "    '' ||','|| "
			+ "    '' ||','|| "
			+ "		CASE WHEN tc.valor IS NULL THEN '' ELSE CAST(tc.valor AS VARCHAR) end||','|| "
			+ "		CASE WHEN tc.valor IS NULL THEN '' ELSE CAST(tc.valor AS VARCHAR) end||','|| "
			+ "    '' ||','|| "
			+ "    CASE WHEN tc.codigo_centro  IS NOT NULL AND (trim(tc.cuenta_contable) like '4%' or trim(tc.cuenta_contable) like '5%') THEN trim(tc.codigo_centro) ELSE '' END||','|| "
			+ "    CASE WHEN tc.codigo_centro  IS NOT NULL AND (trim(tc.cuenta_contable) like '4%' or trim(tc.cuenta_contable) like '5%') THEN '' ELSE trim(tc.codigo_centro) END||','|| "
			+ "    '' ||','|| "
			+ "    '' ||','|| "
			+ "    '' ||','|| "
			+ "    CASE WHEN tc.descripcion IS NULL THEN '' ELSE trim(tc.descripcion) END||','|| "
			+ "		CASE WHEN tc.id_tercero IS NULL THEN '' ELSE CAST(tc.id_tercero AS VARCHAR) END||','||  "
			+ "		CASE WHEN tc.nombre_tercero IS NULL THEN '' ELSE trim(tc.nombre_tercero) END AS texto	"
			+ "FROM "
			+ "	transacciones_contables tc, "
			+ "	cuentas_puc cp, "
			+ "	transacciones_internas ti, "
			+ "	bancos b "
			+ "WHERE"
			+ "	tc.cuenta_contable = cp.CUENTA_CONTABLE AND "
			+ "	tc.banco_aval = cp.banco_aval AND "
			+ "	cp.nombre_cuenta NOT LIKE 'Transitoria%' AND "
			+ "	tc.id_transacciones_internas = ti.id_transacciones_internas AND  "
			+ "	ti.estado = ?4 AND "
			+ "	tc.fecha = ?1 AND "
			+ "	ti.tipo_proceso = ?2 AND "
			+ "	tc.banco_aval = ?3 AND "
			+ "	tc.banco_aval = b.codigo_punto "
			+ "order by tc.id_operacion, tc.id_transacciones_internas, tc.id_transacciones_contables",nativeQuery=true)
	List<String> cierreContablebyBancoF2String(Date fecha, String tipoContabilidad, int codBanco, int estado);
}
