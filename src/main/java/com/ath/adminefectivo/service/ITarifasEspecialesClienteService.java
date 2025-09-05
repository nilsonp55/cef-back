package com.ath.adminefectivo.service;
import com.ath.adminefectivo.dto.TarifasEspecialesClienteDTO;
import com.ath.adminefectivo.dto.VTarifasEspecialesClienteDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface ITarifasEspecialesClienteService {
	
	List<TarifasEspecialesClienteDTO> consultar();
    TarifasEspecialesClienteDTO guardar(TarifasEspecialesClienteDTO dto);
    TarifasEspecialesClienteDTO actualizar(TarifasEspecialesClienteDTO dto);
    Page<VTarifasEspecialesClienteDTO> consultarPorCodigoCliente(Integer codigoCliente, String vigencia, Pageable pageable);
    void eliminar(Long idTarifaEspecial);

}
