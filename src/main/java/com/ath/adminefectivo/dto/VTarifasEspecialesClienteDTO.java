package com.ath.adminefectivo.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Function;
import com.ath.adminefectivo.utils.UtilsObjects;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ath.adminefectivo.entities.VTarifasEspecialesClienteEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VTarifasEspecialesClienteDTO {

    private Long idTarifaEspecial;
    private Integer codigoBanco;
    private String abreviatura;
    private String nombreBanco;
    private String codigoTdv;
    private String nombreTransportadora;
    private Integer codigoCliente;
    private String identificacion;
    private String nombreCliente;
    private String codigoDane;
    private String nombreCiudad;
    private Integer codigoPunto;
    private String nombrePunto;
    private String tipoPunto;
    private String tipoOperacion;
    private String tipoServicio;
    private String tipoComision;
    private String unidadCobro;
    private String escala;
    private String billetes;
    private String monedas;
    private String fajado;
    private BigDecimal valorTarifa;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date fechaInicioVigencia;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date fechaFinVigencia;
    private Integer limiteComisionAplicar;
    private BigDecimal valorComisionAdicional;
    private String usuarioCreacion;
    private Date fechaCreacion;
    private String usuarioModificacion;
    private Date fechaModificacion;
    private boolean estado;
    private String reglaEdicion;

    public static final Function<VTarifasEspecialesClienteEntity, VTarifasEspecialesClienteDTO> CONVERTER_DTO = (entity) -> {
        VTarifasEspecialesClienteDTO dto = new VTarifasEspecialesClienteDTO();
        UtilsObjects.copiarPropiedades(entity, dto);
        return dto;
    };
}
