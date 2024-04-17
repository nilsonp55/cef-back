package com.ath.adminefectivo.dto;

import java.util.List;

import com.ath.adminefectivo.entities.BancoSimpleInfoEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcesarCampoDTO {
	private ArchivosLiquidacionDTO archivosLiquidacionDTO;
    private List<String> cadenaTrasportadoras;
    private List<String> cadenaEntidades;
    private String cadena;
    private String requiredFileExtension;
    private List<String> cadenaTipos;
    private List<BancoSimpleInfoEntity> bancos;
    private String[] keyValue;
}
