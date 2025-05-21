package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ErroresCamposDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionLineasDTO;
import com.ath.adminefectivo.entities.CostosProcesamiento;
import com.ath.adminefectivo.entities.CostosTransporte;
import com.ath.adminefectivo.repositories.MaestroLlavesCostosRepository;
import com.ath.adminefectivo.service.IArchivosLiquidacionService;
import com.ath.adminefectivo.service.ICostosProcesamientoService;
import com.ath.adminefectivo.service.ICostosTransporteService;
import com.ath.adminefectivo.utils.UtilsString;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ArchivosLiquidacionServiceImpl implements IArchivosLiquidacionService {
	
	
	@Autowired 
	ICostosTransporteService costosTransporteService;
	
	@Autowired 
	ICostosProcesamientoService costosProcesamientoService;

	@Override
	public Long persistirCostos(ValidacionArchivoDTO validacionArchivo, ArchivosLiquidacionDTO archivoProcesar) {
		
		String idMaestroArchivo = validacionArchivo.getMaestroDefinicion().getIdMaestroDefinicionArchivo();
		
		switch (idMaestroArchivo) 
		{
			case "LIQTP":
				this.persistirCostoTransporte(validacionArchivo, archivoProcesar);
				break;
			case "LIQPR":
				this.persistirCostoProcesamiento(validacionArchivo, archivoProcesar);
				break;
		
			default:
				break;
		}
		
		return null;
	}
	
	
	public Long persistirCostoTransporte(ValidacionArchivoDTO validacionArchivo, ArchivosLiquidacionDTO archivoProcesar)
	{
		validacionArchivo.setDescripcion(archivoProcesar.getTdv());
		return costosTransporteService.persistir(validacionArchivo);
	}
	
	
	
	public Long persistirCostoProcesamiento(ValidacionArchivoDTO validacionArchivo, ArchivosLiquidacionDTO archivoProcesar)
	{
		validacionArchivo.setDescripcion(archivoProcesar.getTdv());
		return costosProcesamientoService.persistir(validacionArchivo);
	}
	
	
	/**
	 * Metodo encargado de realizar las validaciones correspondientes a las
	 * validaciones de registros ya ceptados y conciliados
	 *
	 * @param maestroDefinicion
	 * @param validacionArchivo
	 * @return
	 */
     public ValidacionArchivoDTO validarCostoConciliado(MaestrosDefinicionArchivoDTO maestroDefinicion,
    		 											ValidacionArchivoDTO validacionArchivo) {

			for (var i = 0; i < validacionArchivo.getValidacionLineas().size(); i++) {
				ValidacionLineasDTO lineaDTO = validacionArchivo.getValidacionLineas().get(i);
				List<ErroresCamposDTO> erroresCampos = this.validarCostoAceptado(lineaDTO,
															maestroDefinicion.getIdMaestroDefinicionArchivo());
				if (!erroresCampos.isEmpty()) {
					lineaDTO.setCampos(erroresCampos);
					lineaDTO.setEstado(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO);
					validacionArchivo.setNumeroErrores(validacionArchivo.getNumeroErrores() + (erroresCampos.size()));
					validacionArchivo.setEstadoValidacion(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO);
				} else {
					lineaDTO.setEstado(Dominios.ESTADO_VALIDACION_CORRECTO);
				}
			}
		return validacionArchivo;
	}
	
	/**
	 * Metodo encargado de realizar la validacion de los registros ya fueron aceptados en 
	 * una conciliacion de otro archivo
	 * 
	 * @param lineaDTO
	 * @param idMaestroDefinicionArchivo
	 * @return List<ErroresCamposDTO>
	 * @author hector.mercado
	 */
	private List<ErroresCamposDTO> validarCostoAceptado(ValidacionLineasDTO lineaDTO, String idMaestroDefinicionArchivo) {
		List<ErroresCamposDTO> erroresCamposDTO = new ArrayList<>();
		switch (idMaestroDefinicionArchivo) 
		{
			case "LIQTP":
				erroresCamposDTO = this.validarCostoTransporteAceptado(lineaDTO);
				break;
			case "LIQPR":
				erroresCamposDTO = this.validarCostoProcesamientoAceptado(lineaDTO);
				break;
		
			default:
				break;
				
			
		}
		
		return erroresCamposDTO;

	}

	/**
	 * Metodo encargado de realizar la validacion de los registros ya fueron aceptados en 
	 * una conciliacion en la tabla de costos_transporte
	 * 
	 * @param lineaDTO
	 * @param idMaestroDefinicionArchivo
	 * @return List<ErroresCamposDTO>
	 * @author hector.mercado
	 */	
	private List<ErroresCamposDTO> validarCostoTransporteAceptado(ValidacionLineasDTO lineaDTO)
	{
	
		List<String> listaFechaDominio = new ArrayList<>();
		listaFechaDominio.add(Constantes.FECHA_PATTERN_DD_MM_YYYY_WITH_SLASH);
		
		List<ErroresCamposDTO> erroresCamposDTO = new ArrayList<>();
		List<String> contenidoLinea = lineaDTO.getContenido();	
		
		List<String> mensajes = new ArrayList<>();
		mensajes.add("Este servicio y/o proceso ya se encuentra conciliado en un archivo diferente");
		
		//Transporte
		String entidadTransporte = contenidoLinea.get(0);
        var fechaServicioTransporte = UtilsString.toDate(contenidoLinea.get(3),listaFechaDominio);
        String codigoPuntoCargoTransporte = contenidoLinea.get(6);
        String nombrePuntoCargoTransporte = contenidoLinea.get(7);
  	    String ciudadFondoTransporte = contenidoLinea.get(11);
  	    String nombreTipoServicioTransporte = contenidoLinea.get(12);
  	    
  	    var valorCadenaTransporte = String.format("%s, %s, %s, %s, %s, %s", entidadTransporte,
  	    								contenidoLinea.get(3),
  	    							  codigoPuntoCargoTransporte,
  	    							  nombrePuntoCargoTransporte,
  	    							  ciudadFondoTransporte,
  	    							  nombreTipoServicioTransporte);
		
  	    
  	    List<CostosTransporte> registrosTransporte = costosTransporteService.findAceptados(entidadTransporte, 
  	    									fechaServicioTransporte,
  	    									codigoPuntoCargoTransporte,
  	    									nombrePuntoCargoTransporte,
  	    									ciudadFondoTransporte,
  	    									nombreTipoServicioTransporte);
  	   
  	   
  	   if (Objects.nonNull(registrosTransporte) && !registrosTransporte.isEmpty()) {
			var mensajeErrores = mensajes;
			var mensajeErroresTxt = String.join(Constantes.SEPARADOR_PUNTO_Y_COMA, mensajeErrores);
			erroresCamposDTO.add(ErroresCamposDTO.builder().numeroCampo(1)
					.estado(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO).contenido(valorCadenaTransporte)
					.mensajeErrorTxt(mensajeErroresTxt).mensajeError(mensajeErrores).build());
		}
  	   
  	    
  	    return erroresCamposDTO;
  	   
  	   
	}
	
	/**
	 * Metodo encargado de realizar la validacion de los registros ya fueron aceptados en 
	 * una conciliacion en la tabla de costos_procesamiento
	 * 
	 * @param lineaDTO
	 * @param idMaestroDefinicionArchivo
	 * @return List<ErroresCamposDTO>
	 * @author hector.mercado
	 */	
	private List<ErroresCamposDTO> validarCostoProcesamientoAceptado(ValidacionLineasDTO lineaDTO)
	{
	
		List<String> listaDominioFecha = new ArrayList<>();
		listaDominioFecha.add(Constantes.FECHA_PATTERN_DD_MM_YYYY_WITH_SLASH);
		
		List<ErroresCamposDTO> erroresCamposDTO = new ArrayList<>();
		List<String> contenido = lineaDTO.getContenido();	
		
		List<String> mensajes = new ArrayList<>();
		mensajes.add("Este servicio y/o proceso ya se encuentra conciliado en un archivo diferente");
		
		//Procesamiento
		String entidad = contenido.get(0);
        var fechaServicioTransporte = UtilsString.toDate(contenido.get(3),listaDominioFecha);
        String codigoPuntoCargo = contenido.get(7);
        String nombrePuntoCargo = contenido.get(8);
  	    String ciudadFondo = contenido.get(10);
  	    String nombreTipoServicio = contenido.get(11);
  	    
  	    var valor = String.format("%s, %s, %s, %s, %s, %s", entidad,
  	    								contenido.get(3),
  	    							  codigoPuntoCargo,
  	    							  nombrePuntoCargo,
  	    							  ciudadFondo,
  	    							  nombreTipoServicio);
		
  	    
  	    List<CostosProcesamiento> registros = costosProcesamientoService.findAceptados(entidad, 
  	    									fechaServicioTransporte,
  	    									codigoPuntoCargo,
  	    									nombrePuntoCargo,
  	    									ciudadFondo,
  	    									nombreTipoServicio);
  	   
  	   
  	   if (Objects.nonNull(registros) && !registros.isEmpty()) {
			var mensajeErrores = mensajes;
			var mensajeErroresTxt = String.join(Constantes.SEPARADOR_PUNTO_Y_COMA, mensajeErrores);
			erroresCamposDTO.add(ErroresCamposDTO.builder().numeroCampo(1)
					.estado(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO).contenido(valor)
					.mensajeErrorTxt(mensajeErroresTxt).mensajeError(mensajeErrores).build());
		}
  	   
  	    
  	    return erroresCamposDTO;
  	   
  	   
	}

	/**
	 * Metodo encargado de calcular si es una ENTRADA o SALIDA para costos TDV
	 * 
	 * @param tipoServicio
	 * @param idMaestroDefinicionArchivo
	 * @author jchaparro
	 */	
	public String getEntradaSalida(String tipoServicio, String idMaestroDefinicion) {
		
		if (tipoServicio == null) {
		    return "UNDIFINED";
		}
		
		tipoServicio = tipoServicio.toUpperCase();

        switch (tipoServicio) {
            case Dominios.TIPO_OPERA_RECOLECCION:
            case Dominios.TIPO_OPERA_RETIRO:
                return Constantes.NOMBRE_ENTRADA;

            case Dominios.TIPO_OPERA_PROVISION:
            case Dominios.TIPO_OPERA_CONSIGNACION:
                return Constantes.NOMBRE_SALIDA;

            case Dominios.TIPO_OPERA_TRASLADO:
                if (Constantes.MAESTRO_ARCHIVO_PROCESAMIENTO.equals(idMaestroDefinicion)) {
                    return Constantes.NOMBRE_ENTRADA;
                } else if (Constantes.MAESTRO_ARCHIVO_TRANSPORTE.equals(idMaestroDefinicion)) {
                    return Constantes.NOMBRE_SALIDA;
                }
                break;

            case Dominios.TIPO_OPERA_VENTA:

                return Constantes.NOMBRE_ENTRADA;
        }

        return null;
	}
}