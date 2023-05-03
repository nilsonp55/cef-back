package com.ath.adminefectivo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ath.adminefectivo.dto.compuestos.ResultadoFuncionDinamicaDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla FuncionesDinamicas
 * @author duvan.naranjo
 *
 */
@NamedNativeQuery(name = "FuncionesDinamicas.ejecutar_procedimiento", 
query = "SELECT * from ejecutar_procedimiento(:idfuncion, :parametros)", 
resultSetMapping = "Mapping.ResultadoFuncionDinamicaDTO")
@SqlResultSetMapping(name = "Mapping.ResultadoFuncionDinamicaDTO", classes = @ConstructorResult(targetClass = ResultadoFuncionDinamicaDTO.class, columns = {
		@ColumnResult(name = "id_funcion"),	@ColumnResult(name = "consecutivo"),	@ColumnResult(name = "resultado") }))
@Entity
@Table(name = "FUNCIONES_DINAMICAS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "FuncionesDinamicas.findAll", query = "SELECT t FROM FuncionesDinamicas t")
public class FuncionesDinamicas {
	
	
	@Id
	@Column(name = "ID_FUNCION")
	private Integer idFuncion;
	
	@Column(name = "NOMBRE_FUNCION")
	private String nombreFuncion;
	
	@Column(name = "DESCRIPCION_FUNCION")
	private String descripcionFuncion;	
	
	@Column(name = "SCRIPT_FUNCION")
	private String scriptFuncion;
	
	@Column(name = "CANTIDAD_PARAMETROS")
	private int cantidadParametros;
	
	@Column(name = "ESTADO")
	private int estado;
	
	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_CREACION")
	private Date fechaCreacion;

	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_MODIFICACION")
	private Date fechaModificacion;
	
	@OneToMany(mappedBy = "funcionesDinamicas", cascade = CascadeType.PERSIST)
	private List<ParametrosFuncionesDinamicas> parametrosFuncionesDinamicas;
}
