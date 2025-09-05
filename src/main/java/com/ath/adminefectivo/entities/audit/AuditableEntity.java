package com.ath.adminefectivo.entities.audit;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Ancestro para entities que activan auditoria basica
 * @author prv_nparra
 * @since 2025
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AuditableEntity {
  
  @CreatedDate
  @Column(name = "fecha_creacion", updatable = false)
  private LocalDateTime fechaCreacion;
  
  @CreatedBy
  @Column(name = "usuario_creacion", updatable = false)
  private String usuarioCreacion;
  
  @LastModifiedDate
  @Column(name = "fecha_modificacion")
  private LocalDateTime fechaModificacion;
  
  @LastModifiedBy
  @Column(name = "usuario_modificacion")
  private String usuarioModificacion;

}
