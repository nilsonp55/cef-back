package com.ath.adminefectivo.entities.audit;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_log_admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaLogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_log_admin")
	private Long idLogAdmin;
	
    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;
    
    @Column(name = "ip_origen")
    private String ipOrigen;
    
    @Column(name = "usuario")
    private String usuario;

    @Column(name = "opcion_menu")
    private String opcionMenu;
    
    @Column(name = "accion_http")
    private String accionHttp;
    
    @Column(name = "codigo_proceso", columnDefinition = "uuid")
    private UUID codigoProceso;
    
    @Column(name = "nombre_proceso")
    private String nombreProceso;

    @Column(name = "estado_http")
    private String estadoHttp;
    
    @Column(name = "estado_operacion")
    private String estadoOperacion;
    
    @Column(name = "mensaje_respuesta")
    private String mensajeRespuesta;
    
    @Column(name = "peticion", columnDefinition = "text")
    private String peticion;

    @Column(name = "respuesta", columnDefinition = "text")
    private String respuesta;

    @Column(name = "valor_anterior", columnDefinition = "text")
    private String valorAnterior;

    @Column(name = "valor_nuevo", columnDefinition = "text")
    private String valorNuevo;
}