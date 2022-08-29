package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.entities.TransaccionesContables;
import com.ath.adminefectivo.entities.TransaccionesInternas;

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
	@Query(value ="SELECT tc.idOperacion,tc.ID_GENERICO,tc.FECHA,tc.CONSECUTIVO_DIA,tc.TIPO_TRANSACCION,tc.BANCO_AVAL,tc.CODIGO_CENTRO,tc.NATURALEZA,tc.CUENTA_CONTABLE,"
			+ "tc.CODIGO_MONEDA,tc.VALOR,tc.tc.TIPO_PROCESO,tc.NUMERO_COMPROBANTE,tc.TIPO_IDENTIFICACION,tc.ID_TERCERO,tc.NOMBRE_TERCERO,tc.IDENTIFICADOR,"
			+ "tc.DESCRIPCION,tc.REFERENCIA1,tc.REFERENCIA2"
			+ "FROM Transacciones_contables tc cuentas_puc cp transacciones_internas ti ON"
			+ "(tc.CUENTA_CONTABLE = cp.CUENTA_CONTABLE) AND "
			+ "(tc.naturaleza in('C','D')) AND"
			+ "(cp.tipo_cuenta<>'TRAINT') AND"
			+ "(tc.id_transaccion_interna = ti.id_transaccion_interna) AND "
			+ "WHERE (tc.fecha = ?1 AND TI.tipo_proceso = ?2 AND tc.banco_aval = ?3)",nativeQuery=true)
	List<RespuestaContableDTO> cierreContablebyBanco(Date fecha,String tipoContabilidad,String codBanco);
	
	/**
	 * Retorna una lista de transacciones contables por fecha,tipocontabilidad, y todos banco
	 * @param start
	 * @param end
	 * @return List<TransaccionesContables>
	 * @author Miller.Caro
	 */
	@Query(value ="SELECT tc.idOperacion,tc.ID_GENERICO,tc.FECHA,tc.CONSECUTIVO_DIA,tc.TIPO_TRANSACCION,tc.BANCO_AVAL,tc.CODIGO_CENTRO,tc.NATURALEZA,tc.CUENTA_CONTABLE,"
			+ "tc.CODIGO_MONEDA,tc.VALOR,tc.tc.TIPO_PROCESO,tc.NUMERO_COMPROBANTE,tc.TIPO_IDENTIFICACION,tc.ID_TERCERO,tc.NOMBRE_TERCERO,tc.IDENTIFICADOR,"
			+ "tc.DESCRIPCION,tc.REFERENCIA1,tc.REFERENCIA2"
			+ "FROM Transacciones_contables tc cuentas_puc cp transacciones_internas ti ON"
			+ "(tc.CUENTA_CONTABLE = cp.CUENTA_CONTABLE) AND "
			+ "(tc.id_transaccion_interna = ti.id_transaccion_interna) AND "
			+ "(cp.tipo_cuenta<>'TRAINT') AND"
			+ "(tc.naturaleza in('C','D')) AND"
			+ "WHERE (tc.fecha = ?1 AND TI.tipo_proceso = ?2) ",nativeQuery=true)
	List<RespuestaContableDTO> cierreContableAllBancos(Date fecha,String tipoContabilidad);
	
	/**
	 * Retorna una lista de errorescontables banco por el estado en transacciones internas
	 * @param start
	 * @param end
	 * @return List<TransaccionesContables>
	 * @author Miller.Caro
	 */
	@Query(value ="SELECT tc.idOperacion,tc.ID_GENERICO,tc.FECHA,tc.CONSECUTIVO_DIA,tc.TIPO_TRANSACCION,tc.BANCO_AVAL,tc.CODIGO_CENTRO,tc.NATURALEZA,tc.CUENTA_CONTABLE,"
			+ "tc.CODIGO_MONEDA,tc.VALOR,tc.tc.TIPO_PROCESO,tc.NUMERO_COMPROBANTE,tc.TIPO_IDENTIFICACION,tc.ID_TERCERO,tc.NOMBRE_TERCERO,tc.IDENTIFICADOR,"
			+ "tc.DESCRIPCION,tc.REFERENCIA1,tc.REFERENCIA2"
			+ "FROM Transacciones_contables tc cuentas_puc cp transacciones_internas ti ON"
			+ "(tc.CUENTA_CONTABLE = cp.CUENTA_CONTABLE) AND "
			+ "(tc.naturaleza in('C','D')) AND"
			+ "(cp.tipo_cuenta<>'TRAINT') AND"
			+ "(ti.estado = 'ERROR') AND"
			+ "(tc.id_transaccion_interna = ti.id_transaccion_interna) AND "
			+ "WHERE (tc.fecha = ?1 AND TI.tipo_proceso = ?2 AND tc.banco_aval = ?3)",nativeQuery=true)
	List<RespuestaContableDTO> erroresContablesbybanco(Date fecha,String tipoContabilidad,String codBanco);
	
	/**
	 * Retorna una lista de errorescontables por bancos por el estado en transacciones internas
	 * @param start
	 * @param end
	 * @return List<TransaccionesContables>
	 * @author Miller.Caro
	 * */
	@Query(value ="SELECT tc.idOperacion,tc.ID_GENERICO,tc.FECHA,tc.CONSECUTIVO_DIA,tc.TIPO_TRANSACCION,tc.BANCO_AVAL,tc.CODIGO_CENTRO,tc.NATURALEZA,tc.CUENTA_CONTABLE,"
			+ "tc.CODIGO_MONEDA,tc.VALOR,tc.tc.TIPO_PROCESO,tc.NUMERO_COMPROBANTE,tc.TIPO_IDENTIFICACION,tc.ID_TERCERO,tc.NOMBRE_TERCERO,tc.IDENTIFICADOR,"
			+ "tc.DESCRIPCION,tc.REFERENCIA1,tc.REFERENCIA2"
			+ "FROM Transacciones_contables tc cuentas_puc cp transacciones_internas ti ON"
			+ "(tc.CUENTA_CONTABLE = cp.CUENTA_CONTABLE) AND "
			+ "(tc.naturaleza in('C','D')) AND"
			+ "(cp.tipo_cuenta<>'TRAINT') AND"
			+ "(ti.estado = 'ERROR') AND"
			+ "(tc.id_transaccion_interna = ti.id_transaccion_interna) AND "
			+ "WHERE (tc.fecha = ?1 AND TI.tipo_proceso = ?2)",nativeQuery=true)
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
	@Query(value ="SELECT ti.estado"
			+ "FROM Transacciones_contables tc cuentas_puc cp transacciones_internas ti ON"
			+ "(tc.CUENTA_CONTABLE = cp.CUENTA_CONTABLE) AND "
			+ "(tc.naturaleza in('C','D')) AND"
			+ "(cp.tipo_cuenta<>'TRAINT') AND"
			+ "(ti.estado = 'ERROR') AND"
			+ "(tc.id_transaccion_interna = ti.id_transaccion_interna) AND "
			+ "WHERE (ti.estado = ?1)",nativeQuery=true)
	String estadovalidacionContable(String estado);
}
