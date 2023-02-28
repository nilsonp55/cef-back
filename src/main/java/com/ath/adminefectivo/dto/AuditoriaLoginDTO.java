package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;
import com.ath.adminefectivo.entities.AuditoriaLogin;
import lombok.Data;

@Data
public class AuditoriaLoginDTO {

	private Long id;
	
	private String usuario;
	
	private Date fechaIngreso;
	
	
	public static final Function<AuditoriaLogin, AuditoriaLoginDTO> CONVERTER_DTO = (AuditoriaLogin t) -> {
		AuditoriaLoginDTO auditoriaLoginDTO = new AuditoriaLoginDTO();
		auditoriaLoginDTO.setId(t.getId());
		auditoriaLoginDTO.setUsuario(t.getUsuario());
		auditoriaLoginDTO.setFechaIngreso(t.getFechaIngreso());
		
		return auditoriaLoginDTO;
	};
	
	public static final Function<AuditoriaLoginDTO, AuditoriaLogin> CONVERTER_ENTITY = (AuditoriaLoginDTO t) -> {
		AuditoriaLogin auditoriaLogin = new AuditoriaLogin();
		auditoriaLogin.setId(t.getId());
		auditoriaLogin.setUsuario(t.getUsuario());
		auditoriaLogin.setFechaIngreso(t.getFechaIngreso());
		
		return auditoriaLogin;
	};
}
