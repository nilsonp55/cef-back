package com.ath.adminefectivo.repositories;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.entities.MaestroLlavesCostosEntity;

@Repository
public interface MaestroLlavesCostosRepository extends JpaRepository<MaestroLlavesCostosEntity, Long> {
    boolean existsByIdMaestroLlave(BigInteger idMaestroLlave);
    
	@Modifying
	@Transactional
	@Query(value = """
			    INSERT INTO controlefect.maestro_llaves_costos (id_maestro_llave)
			    SELECT DISTINCT COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv)
			    FROM (
			        SELECT id_llaves_maestro_app, id_llaves_maestro_tdv
			        FROM controlefect.v_detalle_liquidacion_transporte
			        UNION ALL
			        SELECT id_llaves_maestro_app, id_llaves_maestro_tdv
			        FROM controlefect.v_detalle_liquidacion_procesamiento
			    ) v
			    WHERE (v.id_llaves_maestro_app IS NOT NULL OR v.id_llaves_maestro_tdv IS NOT NULL)
			      AND NOT EXISTS (
			        SELECT 1
			        FROM controlefect.maestro_llaves_costos mlc
			        WHERE mlc.id_maestro_llave = COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv)
			    )
			""", nativeQuery = true)
	int insertarMaestroLlaves();
    
    
	@Modifying
	@Transactional
	@Query(value = """
			INSERT INTO controlefect.maestro_llaves_costos_archivos (id_llave, nombre_archivo, id_maestro_definicion_archivo)
			SELECT DISTINCT
			    mlc.id_llave,
			    ac.nombre_archivo,
			    CASE
			        WHEN ac.nombre_archivo ILIKE '%LIQ_TRANSPORTE%' THEN 'LIQTP'
			        WHEN ac.nombre_archivo ILIKE '%LIQ_PROCESAMIENTO%' THEN 'LIQPR'
			        ELSE NULL
			    END AS id_maestro_definicion_archivo
			FROM (
			    SELECT id_llaves_maestro_app, id_llaves_maestro_tdv, id_archivo_cargado
			    FROM controlefect.v_detalle_liquidacion_transporte
			    UNION ALL
			    SELECT id_llaves_maestro_app, id_llaves_maestro_tdv, id_archivo_cargado
			    FROM controlefect.v_detalle_liquidacion_procesamiento
			) v
			JOIN controlefect.archivos_cargados ac
			  ON ac.id_archivo = v.id_archivo_cargado
			JOIN controlefect.maestro_llaves_costos mlc
			  ON mlc.id_maestro_llave = COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv)
			LEFT JOIN controlefect.maestro_llaves_costos_archivos mlca
			  ON mlca.id_llave = mlc.id_llave
			  AND mlca.nombre_archivo = ac.nombre_archivo
			  AND mlca.id_maestro_definicion_archivo = CASE
			      WHEN ac.nombre_archivo ILIKE '%LIQ_TRANSPORTE%' THEN 'LIQTP'
			      WHEN ac.nombre_archivo ILIKE '%LIQ_PROCESAMIENTO%' THEN 'LIQPR'
			      ELSE NULL
			  END
			WHERE (ac.nombre_archivo ILIKE '%LIQ_TRANSPORTE%' OR ac.nombre_archivo ILIKE '%LIQ_PROCESAMIENTO%')
			  AND mlca.id_llave IS NULL;
						""", nativeQuery = true)
	int insertarArchivosLlaves();
	
	@Modifying
	@Transactional
	@Query("UPDATE MaestroLlavesCostosEntity m " +
	       "SET m.estado = :estado, m.observacionesAth = :observacionesAth " +
	       "WHERE m.idMaestroLlave IN :llaves")
	int actualizarEstadoAndObservacionesPorLlaves(@Param("llaves") List<BigInteger> llaves,
	                                            @Param("estado") String estado,
	                                            @Param("observacionesAth") String observacionesAth);


}
