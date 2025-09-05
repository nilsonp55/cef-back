package com.ath.adminefectivo.repositories;

import com.ath.adminefectivo.entities.VTarifasEspecialesClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface IVTarifasEspecialesClienteRepository extends JpaRepository<VTarifasEspecialesClienteEntity, Long> {
    
	Page<VTarifasEspecialesClienteEntity> findByCodigoCliente(Integer codigoCliente, Pageable pageable);
    
    Page<VTarifasEspecialesClienteEntity> findByCodigoClienteIn(List<Integer> codigosCliente, Pageable pageable);
    
    Page<VTarifasEspecialesClienteEntity> findByCodigoClienteAndFechaFinVigenciaBefore(Integer codigoCliente, Date fecha, Pageable pageable);

    @Query("SELECT v FROM VTarifasEspecialesClienteEntity v WHERE v.codigoCliente = :codigoCliente AND v.fechaFinVigencia >= :fecha")
    Page<VTarifasEspecialesClienteEntity> findByCodigoClienteAndFechaFinVigenciaAfterOrEquals(@Param("codigoCliente") Integer codigoCliente, @Param("fecha") Date fecha, Pageable pageable);
    
    Optional<VTarifasEspecialesClienteEntity> findByIdTarifaEspecial(Long idTarifaEspecial);
    
    @Query("""
    	    SELECT v FROM VTarifasEspecialesClienteEntity v
    	    WHERE v.codigoBanco = :codigoBanco
    	      AND v.codigoCliente = :codigoCliente
    	      AND v.codigoTdv = :codigoTdv
    	      AND v.codigoPunto = :codigoPunto
    	      AND v.codigoDane = :codigoDane
    	      AND v.tipoComision = :tipoComision
    	      AND v.tipoOperacion = :tipoOperacion
    	      AND v.tipoServicio = :tipoServicio
    	      AND v.escala = :escala
    	      AND (:idTarifaEspecial IS NULL OR v.idTarifaEspecial <> :idTarifaEspecial)
    	      AND (
    	            (:fechaInicio BETWEEN v.fechaInicioVigencia AND v.fechaFinVigencia)
    	         OR (:fechaFin BETWEEN v.fechaInicioVigencia AND v.fechaFinVigencia)
    	         OR (v.fechaInicioVigencia BETWEEN :fechaInicio AND :fechaFin)
    	         OR (v.fechaFinVigencia BETWEEN :fechaInicio AND :fechaFin)
    	      )
    	""")
    	List<VTarifasEspecialesClienteEntity> findCrucesDeVigencia(
    			@Param("codigoBanco") Integer codigoBanco,
    	        @Param("codigoCliente") Integer codigoCliente,
    	        @Param("codigoTdv") String codigoTdv,
    	        @Param("codigoPunto") Integer codigoPunto,
    	        @Param("codigoDane") String codigoDane,
    	        @Param("tipoComision") String tipoComision,
    	        @Param("tipoOperacion") String tipoOperacion,
    	        @Param("tipoServicio") String tipoServicio,
    	        @Param("escala") String escala,
    	        @Param("fechaInicio") Date fechaInicio,
    	        @Param("fechaFin") Date fechaFin,
    	        @Param("idTarifaEspecial") Long idTarifaEspecial // puede ser null
    	);



}
