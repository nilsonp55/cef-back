package com.ath.adminefectivo.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CajerosATM;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.Oficinas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.PuntosCodigoTDV;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de la tabla de Puntos
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuntosDTO {

	private Integer codigoPunto;
	
	private String tipoPunto;
	
	private String nombrePunto;
	
	private String codigoCiudad;
	
	private List<OficinasDTO> oficinas;

	private List<SitiosClientesDTO> sitiosClientes;

	private List<PuntosCodigoTdvDTO> puntosCodigoTDV;

	private List<FondosDTO> fondos;

	private List<CajerosATMDTO> cajeroATM;

	private List<BancosDTO> bancos;

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<Puntos, PuntosDTO> CONVERTER_DTO = (Puntos t) -> {
		var puntosDTO = new PuntosDTO();
		UtilsObjects.copiarPropiedades(t, puntosDTO);
		
		List<OficinasDTO> oficinasDTO = new ArrayList();
		if(!t.getOficinas().isEmpty()) {
			t.getOficinas().forEach(oficina ->{
				oficinasDTO.add(OficinasDTO.CONVERTER_DTO.apply(oficina));
			});
		}
		puntosDTO.setOficinas(oficinasDTO);
		
		List<SitiosClientesDTO> sitiosClienteDTO = new ArrayList();
		if(!t.getSitiosClientes().isEmpty()) {
			t.getSitiosClientes().forEach(sitiosCliente ->{
				sitiosClienteDTO.add(SitiosClientesDTO.CONVERTER_DTO.apply(sitiosCliente));
			});
		}
		puntosDTO.setSitiosClientes(sitiosClienteDTO);
		
		List<PuntosCodigoTdvDTO> puntosCodigpoTdvDTO = new ArrayList();
//		if(!t.getPuntosCodigoTDV().isEmpty()) {
//			t.getPuntosCodigoTDV().forEach(puntoCodigo ->{
//				puntosCodigpoTdvDTO.add(PuntosCodigoTdvDTO.CONVERTER_DTO.apply(puntoCodigo));
//			});
//		}
		puntosDTO.setPuntosCodigoTDV(puntosCodigpoTdvDTO);
		
		List<FondosDTO> fondosDTO = new ArrayList();
		if(!t.getFondos().isEmpty()) {
			t.getFondos().forEach(fondo ->{
				fondosDTO.add(FondosDTO.CONVERTER_DTO.apply(fondo));
			});
		}
		puntosDTO.setFondos(fondosDTO);
		
		List<CajerosATMDTO> cajerosDTO = new ArrayList();
		if(!t.getCajeroATM().isEmpty()) {
			t.getCajeroATM().forEach(cajero ->{
				cajerosDTO.add(CajerosATMDTO.CONVERTER_DTO.apply(cajero));
			});
		}
		puntosDTO.setCajeroATM(cajerosDTO);
		
		List<BancosDTO> bancosDTO = new ArrayList();
		if(!t.getBancos().isEmpty()) {
			t.getBancos().forEach(banco ->{
				bancosDTO.add(BancosDTO.CONVERTER_DTO.apply(banco));
			});
		}
		puntosDTO.setBancos(bancosDTO);
		
		
		return puntosDTO;
	};
	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<PuntosDTO, Puntos> CONVERTER_ENTITY = (PuntosDTO t) -> {
		var puntos = new Puntos();
		UtilsObjects.copiarPropiedades(t, puntos);
		
		List<Oficinas> oficinas = new ArrayList();
		if(!t.getOficinas().isEmpty()) {
			t.getOficinas().forEach(oficina ->{
				oficinas.add(OficinasDTO.CONVERTER_ENTITY.apply(oficina));
			});
		}
		puntos.setOficinas(oficinas);
		
		List<SitiosClientes> sitiosCliente = new ArrayList();
		if(!t.getSitiosClientes().isEmpty()) {
			t.getSitiosClientes().forEach(sitioCliente ->{
				sitiosCliente.add(SitiosClientesDTO.CONVERTER_ENTITY.apply(sitioCliente));
			});
		}
		puntos.setSitiosClientes(sitiosCliente);
		
		List<PuntosCodigoTDV> puntosCodigpoTdv = new ArrayList();
//		if(!t.getPuntosCodigoTDV().isEmpty()) {
//			t.getPuntosCodigoTDV().forEach(puntoCodigo ->{
//				puntosCodigpoTdv.add(PuntosCodigoTdvDTO.CONVERTER_ENTITY.apply(puntoCodigo));
//			});
//		}
		puntos.setPuntosCodigoTDV(puntosCodigpoTdv);
		
		List<Fondos> fondos = new ArrayList();
		if(!t.getFondos().isEmpty()) {
			t.getFondos().forEach(fondo ->{
				fondos.add(FondosDTO.CONVERTER_ENTITY.apply(fondo));
			});
		}
		puntos.setFondos(fondos);
		
		List<CajerosATM> cajeros = new ArrayList();
		if(!t.getCajeroATM().isEmpty()) {
			t.getCajeroATM().forEach(cajero ->{
				cajeros.add(CajerosATMDTO.CONVERTER_ENTITY.apply(cajero));
			});
		}
		puntos.setCajeroATM(cajeros);
		
		List<Bancos> bancos = new ArrayList();
		if(!t.getBancos().isEmpty()) {
			t.getBancos().forEach(banco ->{
				bancos.add(BancosDTO.CONVERTER_ENTITY.apply(banco));
			});
		}
		puntos.setBancos(bancos);
		
		return puntos;
	};
}
