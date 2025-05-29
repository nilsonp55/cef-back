package com.ath.adminefectivo.service;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface IDetalleLiquidacionProcesamiento {

	String getConsecutivoRegistro();
	Integer getIdArchivoCargado();
	String getIdRegistro();
	String getIdsLiquidacionApp();
	String getIdsLiquidacionTdv();
	BigInteger getTipoTransaccion();
	String getEntidad();
	java.util.Date getFechaServicioTransporte();
	String getIdentificacionCliente();
	String getRazonSocial();
	String getCodigoPuntoCargo();
	String getNombrePuntoCargo();
	String getCiudadFondo();
	String getNombreTipoServicio();
	String getTipoOperacion();
	String getMonedaDivisa();
	Integer getAplicativo();
	Integer getTdv();
	BigDecimal getValorProcesadoBillete();
	BigDecimal getValorProcesadoBilleteTdv();
	BigDecimal getValorProcesadoMoneda();
	BigDecimal getValorProcesadoMonedaTdv();
	BigDecimal getValorTotalProcesado();
	BigDecimal getValorTotalProcesadoTdv();
	BigDecimal getSubtotal();
	BigDecimal getSubtotalTdv();
	String getIva();
	Double getValorTotal();
	Double getClasificacionFajado();
	Double getClasificacionFajadoTdv();
	Double getClasificacionNoFajado();
	Double getClasificacionNoFajadoTdv();
	Double getCostoPaqueteo();
	Double getCostoPaqueteoTdv();
	Double getMonedaResiduo();
	Double getMonedaResiduoTdv();
	Double getBilleteResiduo();
	Double getBilleteResiduoTdv();
	BigDecimal getValorAlmacenamientoBillete();
	BigDecimal getValorAlmacenamientoBilleteTdv();
	BigDecimal getValorAlmacenamientoMoneda();
	BigDecimal getValorAlmacenamientoMonedaTdv();
	String getEstado();
	String getModulo();
	BigInteger getIdLlavesMaestroTdv();
	BigInteger getIdLlavesMaestroApp();

}
