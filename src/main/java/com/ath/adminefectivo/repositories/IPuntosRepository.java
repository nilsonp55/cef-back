package com.ath.adminefectivo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.service.IPuntoInterno;

/**
 * Repository encargado de manejar la logica de la entidad Puntos
 *
 * @author cesar.castano
 */
public interface IPuntosRepository
    extends JpaRepository<Puntos, Integer>, QuerydslPredicateExecutor<Puntos> {

  /**
   * Retorna el objeto Puntos para tipo de punto y codigo de punto
   * 
   * @param codigoPunto
   * @param tipoPunto
   * @return Puntos
   * @author cesar.castano
   */
  public Puntos findByCodigoPuntoAndTipoPunto(Integer codigoPunto, String tipoPunto);

  /**
   * Retorna el objeto Puntos para un codigo de punto
   * 
   * @param codigoPunto
   * @return Punto
   * @author cesar.castano
   */
  public Puntos findByCodigoPunto(Integer codigoPunto);

  /**
   * Retorna el objeto Puntos consultado por nombrePunto
   * 
   * @param nombrePunto
   * @param tipoPunto
   * @return Puntos
   * @author duvan.naranjo
   */
  public Puntos findByNombrePunto(String nombrePunto);

  /**
   * Retorna el objeto Puntos consultado por nombrePunto
   * 
   * @param nombrePunto
   * @param tipoPunto
   * @return Puntos
   * @author duvan.naranjo
   */
  public Puntos findByTipoPuntoAndCodigoCiudad(String tipoPunto, String codigoCiudad);

  /**
   * Retorna el objeto Puntos para tipo de punto y nombre de punto
   * 
   * @param nombrePunto
   * @param tipoPunto
   * @return Puntos
   * @author cesar.castano
   */
  public Puntos findByTipoPuntoAndNombrePunto(String tipoPunto, String nombrePunto);

  /**
   * Retorna el objeto Puntos
   * 
   * @param codigo_aval
   * @return Puntos
   * @author prv_ccastano
   */
  @Query("select p from Puntos p " + "inner join SitiosClientes s on p.codigoPunto = s.codigoPunto "
      + "inner join ClientesCorporativos c on c.codigoCliente = s.codigoCliente and "
      + "c.identificacion = '9999999999' "
      + "where p.tipoPunto ='CLIENTE' and c.codigoBancoAval = ?1")
  public List<Puntos> obtenerCodigoPunto(Integer codigoAval);

  /**
   * Consulta la tabla puntos por tipo de punto
   * 
   * @param tipoPunto Valor tipo punto para filtrar registros
   * @return Lista de registros de puntos por tipo
   * @author prv_nparra
   */
  public List<Puntos> findByTipoPunto(String tipoPunto);
  
      @Query(value = """
          SELECT p.codigo_punto AS codigoPunto, p.tipo_punto AS tipoPunto
          FROM controlefect.puntos_codigo_tdv pct 
          INNER JOIN bancos b ON pct.codigo_banco = b.codigo_punto
          INNER JOIN puntos p ON pct.codigo_punto = p.codigo_punto
          WHERE pct.codigo_propio_tdv = CONCAT(:códigoPuntoCargo, :nombrePuntoCargo)
          AND pct.codigo_tdv = :codigoTdv
          AND b.abreviatura = :entidad LIMIT 1
          """, nativeQuery = true)
          IPuntoInterno findPuntoInterno(
          @Param("códigoPuntoCargo") String códigoPuntoCargo,
          @Param("nombrePuntoCargo") String nombrePuntoCargo,
          @Param("codigoTdv") String codigoTdv,
          @Param("entidad") String entidad
      );
      
      @Query(value = """
    		    SELECT p.codigo_punto
    		    FROM controlefect.bancos b
    		    INNER JOIN controlefect.fondos f ON b.codigo_punto = f.banco_aval
    		    INNER JOIN controlefect.puntos p ON f.codigo_punto = p.codigo_punto
    		    WHERE b.abreviatura = :entidad
    		      AND p.tipo_punto = :tipoPunto
    		      AND p.codigo_ciudad = :codigoCiiuFondo
    		      AND f.tdv = :codigoTdv
    		      AND p.nombre_punto = CONCAT(:entidad, '-',:nomCiudadFondo,'-',:nombreTdv)
    		    LIMIT 1
    		    """, nativeQuery = true)
    		Integer findPuntoFondo(
    		    @Param("entidad") String entidad,
    		    @Param("tipoPunto") String tipoPunto,
    		    @Param("codigoCiiuFondo") String codigoCiiuFondo,
    		    @Param("codigoTdv") String codigoTdv,
    		    @Param("nombreTdv") String nombreTdv,
    		    @Param("nomCiudadFondo") String nomCiudadFondo
    		);

}
