package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ath.adminefectivo.dto.TransaccionesContablesDTO;
import com.ath.adminefectivo.entities.TransaccionesContables;

public interface IGenerarArchivoRepository extends JpaRepository<TransaccionesContables, Long> 
 {

	/*
	 * Metodo encargado para la consulta par generar el archivo contable por banco
	 * 
	 * */
	
	@Query(value ="SELECT tc.idOperacion,tc.ID_GENERICO,tc.FECHA,tc.CONSECUTIVO_DIA,tc.TIPO_TRANSACCION,tc.BANCO_AVAL,tc.CODIGO_CENTRO,tc.NATURALEZA,tc.CUENTA_CONTABLE,"
			+ "tc.CODIGO_MONEDA,tc.VALOR,tc.tc.TIPO_PROCESO,tc.NUMERO_COMPROBANTE,tc.TIPO_IDENTIFICACION,tc.ID_TERCERO,tc.NOMBRE_TERCERO,tc.IDENTIFICADOR,"
			+ "tc.DESCRIPCION,tc.REFERENCIA1,tc.REFERENCIA2"
			+ "FROM Transacciones_Contables tc cuentas_puc cp transaccion_interna ti ON"
			+ "(tc.CUENTA_CONTABLE = cp.CUENTA_CONTABLE) AND "
			+ "(tc.naturaleza in('C','D') AND"
			+ "(cp.tipo_cuenta<>'TRAINT') AND"
			+ "(tc.id_transaccion_interna = ti.id_transaccion_interna) AND "
			+ "WHERE (tc.fecha = ?1 AND TI.tipo_proceso = ?2 AND tc.banco_aval = ?3",nativeQuery=true) 
	public List<TransaccionesContablesDTO> generarArchivo(Date fecha,String tipoContabilidad,String codBanco);
}
