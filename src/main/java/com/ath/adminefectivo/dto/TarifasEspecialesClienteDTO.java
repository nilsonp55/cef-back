package com.ath.adminefectivo.dto;

import com.ath.adminefectivo.entities.TarifasEspecialesCliente;
import com.ath.adminefectivo.utils.UtilsObjects;
import com.ath.adminefectivo.validation.ValidarTarifasEspecialesCliente;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Function;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidarTarifasEspecialesCliente
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TarifasEspecialesClienteDTO {
	
    private Long idTarifaEspecial;
    private Integer idArchivoCargado;
	private Integer idRegistro;

    private Integer codigoBanco;
    private String codigoTdv;
    private Integer codigoCliente;
    private String codigoDane;
    private Integer codigoPunto;

    private String tipoOperacion;
    private String tipoServicio;
    private String tipoComision;
    private String unidadCobro;
    private String escala;
    private String billetes;
    private String monedas;
    private String fajado;

    private BigDecimal valorTarifa;
    
    private Date fechaInicioVigencia;
    private Date fechaFinVigencia;
    private Integer limiteComisionAplicar;
    private BigDecimal valorComisionAdicional;

    private String usuarioCreacion;
    private Date fechaCreacion;
    private String usuarioModificacion;
    private Date fechaModificacion;
    private boolean estado;
    
    /**
     * Convierte un DTO a entidad
     */
    public static final Function<TarifasEspecialesClienteDTO, TarifasEspecialesCliente> CONVERTER_ENTITY = (TarifasEspecialesClienteDTO dto) -> {
        TarifasEspecialesCliente entity = new TarifasEspecialesCliente();
        UtilsObjects.copiarPropiedades(dto, entity);
        return entity;
    };

    /**
     * Convierte una entidad a DTO
     */
    public static final Function<TarifasEspecialesCliente, TarifasEspecialesClienteDTO> CONVERTER_DTO = (TarifasEspecialesCliente entity) -> {
        TarifasEspecialesClienteDTO dto = new TarifasEspecialesClienteDTO();
        UtilsObjects.copiarPropiedades(entity, dto);
        return dto;
    };
}
