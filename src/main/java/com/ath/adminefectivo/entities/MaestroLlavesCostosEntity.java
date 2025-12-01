package com.ath.adminefectivo.entities;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "maestro_llaves_costos", uniqueConstraints = {
    @UniqueConstraint(columnNames = "id_maestro_llave")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaestroLlavesCostosEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_llave")
    private Long id;

    @Column(name = "id_maestro_llave", nullable = false, unique = true)
    private BigInteger idMaestroLlave;

    @Column(name = "estado", length = 20)
    private String estado;
    
    @Column(name = "observaciones_ath", length = 255, nullable = true)
    private String observacionesAth;
    
}
