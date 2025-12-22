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
  public List<Puntos> findByNombrePunto(String nombrePunto);

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

      /**
       * 
       * @param nombrePunto
       * @param codigoCiudad
       * @param codigoBancoAval
       * @param nombreFondo
       * @return List<Puntos>
       * @author prv_nparra
       */
      @Query(value = "SELECT p FROM Puntos p JOIN Fondos f ON f.codigoPunto = p.codigoPunto "
          + " WHERE p.tipoPunto = 'FONDO' "
          + " and p.nombrePunto = :nombrePunto "
          + " and p.codigoCiudad = :codigoCiudad "
          + " and f.bancoAVAL = :codigoBancoAval "
          + " and f.nombreFondo = :nombreFondo")
      public List<Puntos> findFondoUnique(
          @Param(value = "nombrePunto") String nombrePunto, 
          @Param(value = "codigoCiudad") String codigoCiudad, 
          @Param(value = "codigoBancoAval") Integer codigoBancoAval, 
          @Param(value = "nombreFondo") String nombreFondo );
      
      /**
       * 
       * @param nombrePunto
       * @param codigoCiudad
       * @param numeroNit
       * @param abreviatura
       * @param nombreBanco
       * @return
       * @author prv_nparra
       */
      @Query(value = "SELECT p FROM Puntos p JOIN Bancos b ON b.codigoPunto = p.codigoPunto "
          + " WHERE p.tipoPunto = 'BANCO' "
          + " and p.nombrePunto = :nombrePunto "
          + " and p.codigoCiudad = :codigoCiudad "
          + " and b.numeroNit = :numeroNit "
          + " and b.abreviatura = :abreviatura "
          + " and b.nombreBanco = :nombreBanco")
      public List<Puntos> findBancoUnique(
          @Param(value = "nombrePunto") String nombrePunto,
          @Param(value = "codigoCiudad") String codigoCiudad,
          @Param(value = "numeroNit") String numeroNit,
          @Param(value = "abreviatura") String abreviatura,
          @Param(value = "nombreBanco") String nombreBanco );
      
      /**
       * 
       * @param nombrePunto
       * @param codigoCiudad
       * @param bancoAval
       * @param codigoATM
       * @return
       * @author prv_nparra
       */
      @Query(value = "SELECT p from Puntos p JOIN CajerosATM c ON c.codigoPunto = p.codigoPunto "
          + " WHERE p.tipoPunto = 'CAJERO' "
          + " and p.nombrePunto = :nombrePunto "
          + " and p.codigoCiudad = :codigoCiudad "
          + " and c.bancoAval = :bancoAval "
          + " and c.codigoATM = :codigoATM ")
      public List<Puntos> findCajeroUnique(
          @Param(value = "nombrePunto") String nombrePunto,
          @Param(value = "codigoCiudad") String codigoCiudad,
          @Param(value = "bancoAval") Integer bancoAval,
          @Param(value = "codigoATM") String codigoATM);
      
      /**
       * 
       * @param nombrePunto
       * @param codigoCiudad
       * @param bancoAval
       * @param codigoOficina
       * @return
       * @author prv_nparra
       */
      @Query(value = "SELECT p from Puntos p JOIN Oficinas o ON o.codigoPunto = p.codigoPunto "
          + " WHERE p.tipoPunto = 'OFICINA' "
          + " and p.nombrePunto = :nombrePunto "
          + " and p.codigoCiudad = :codigoCiudad "
          + " and o.bancoAVAL = :bancoAval "
          + " and o.codigoOficina = :codigoOficina ")
      public List<Puntos> findOficinaUnique(
          @Param(value = "nombrePunto") String nombrePunto,
          @Param(value = "codigoCiudad") String codigoCiudad,
          @Param(value = "bancoAval") Integer bancoAval,
          @Param(value = "codigoOficina") Integer codigoOficina);
      
      /**
       * 
       * @param nombrePunto
       * @param codigoCiudad
       * @return
       * @author prv_nparra
       */
      @Query(value = "SELECT p from Puntos p "
          + " WHERE p.tipoPunto = 'BAN_REP' "
          + " and p.nombrePunto = :nombrePunto "
          + " and p.codigoCiudad = :codigoCiudad ")
      public List<Puntos> findBanrepUnique(
          @Param(value = "nombrePunto") String nombrePunto,
          @Param(value = "codigoCiudad") String codigoCiudad);
      
      /**
       * 
       * @param nombrePunto
       * @param codigoCiudad
       * @param identificadorCliente
       * @param codigoCliente
       * @param codigoBancoAval
       * @return
       * @author prv_nparra
       */
      @Query(nativeQuery = true, 
          value = "select p.codigo_punto, p.codigo_ciudad, p.estado, p.nombre_punto, p.tipo_punto, "
              + " p.fecha_creacion, p.fecha_modificacion, p.usuario_creacion, p.usuario_modificacion " 
          + " from puntos p join sitios_cliente sc on sc.codigo_punto = p.codigo_punto "
          + " where p.tipo_punto = 'CLIENTE' "
          + " and p.nombre_punto = :nombrePunto "
          + " and p.codigo_ciudad = :codigoCiudad "
          + " and sc.identificador_cliente = :identificadorCliente "
          + " and sc.codigo_cliente = :codigoCliente ")
      public List<Puntos> findSitioClienteUnique(
          @Param(value = "nombrePunto") String nombrePunto,
          @Param(value = "codigoCiudad") String codigoCiudad,
          @Param(value = "identificadorCliente") String identificadorCliente,
          @Param(value = "codigoCliente") Integer codigoCliente);
      
      public Puntos findByNombrePuntoAndTipoPunto(String nombrePunto, String tipoPunto);
}
