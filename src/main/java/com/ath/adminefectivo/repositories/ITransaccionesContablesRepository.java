package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

	/**
	 * Retorna validacion del cierre contable
	 * @param estado
	 * @return String estado 
	 * @author Miller.Caro
	 * */
	@Query(value ="SELECT distinct case when  ti.estado = null then 0 else ti.estado end as estado"
			+ " FROM transacciones_contables tc, cuentas_puc cp, transacciones_internas ti where "
			+ " tc.CUENTA_CONTABLE = cp.CUENTA_CONTABLE AND "
			+ " tc.naturaleza in('C','D') AND "
			+ " cp.nombre_cuenta <> 'Transitoria%' AND "
			+ " tc.id_transacciones_internas = ti.id_transacciones_internas AND "
			+ " ti.estado = ?1 ",nativeQuery=true)
	Integer estadovalidacionContable(int estado);

	@Query(nativeQuery = true)
	ConteoContabilidadDTO conteoContabilidad(Date fechaInicio, Date fechaFin, String tipoProceso);
}
