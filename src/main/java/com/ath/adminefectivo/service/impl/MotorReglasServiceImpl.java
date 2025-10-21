package com.ath.adminefectivo.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ListaDetalleDTO;
import com.ath.adminefectivo.dto.ReglasDetalleArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.TokenMotorDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionMotorDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.factory.TipoReglaFactory;
import com.ath.adminefectivo.service.IMotorReglasService;
import com.ath.adminefectivo.service.IReglasDetalleArchivoService;
import com.ath.adminefectivo.service.ITipoReglaInterface;
import com.ath.adminefectivo.service.ITipoReglaInterfaceCostos;
import com.ath.adminefectivo.utils.UtilsString;

import lombok.EqualsAndHashCode;

@Service
@EqualsAndHashCode
public class MotorReglasServiceImpl implements IMotorReglasService {

    @Autowired
    IReglasDetalleArchivoService reglasDetalleArchivoService;

    @Autowired
    TipoReglaFactory tipoReglaFactory;

    @Autowired
    private ITipoReglaInterfaceCostos reglaCostos;

    public static final int INICIO = 0;

    private List<Integer> listaErrores;

    private List<String> listaMensajesError;

    @Override
    public ValidacionMotorDTO evaluarReglaMultiple(String regla, String valorCampo,
            ValidacionArchivoDTO validacionArchivo, int index, Map<String, ListaDetalleDTO> detalleDefinicionMap) {
        this.listaErrores = new ArrayList<>();
        this.listaMensajesError = new ArrayList<>();
        regla = regla.toUpperCase();
        Collection<TokenMotorDTO> tokenVOSet = compilarRegla(regla);
        var validacionRegla = evaluarTokens(tokenVOSet, valorCampo, validacionArchivo, index, detalleDefinicionMap);
        return ValidacionMotorDTO.builder().isValida(validacionRegla).codigosError(listaErrores)
                .mensajesError(listaMensajesError).build();
    }

    @Override
    public ValidacionMotorDTO evaluarReglaSimple(String regla, String valorCampo,
            ValidacionArchivoDTO validacionArchivo, int index, Map<String, ListaDetalleDTO> detalleDefinicionMap) {
        if (UtilsString.isNumeroEntero(regla)) {
            this.listaErrores = new ArrayList<>();
            this.listaMensajesError = new ArrayList<>();
            var validacionRegla = this.ejecutarRegla(Integer.valueOf(regla), valorCampo, validacionArchivo, index,
                    detalleDefinicionMap);
            return ValidacionMotorDTO.builder().isValida(validacionRegla).mensajesError(listaMensajesError)
                    .codigosError(listaErrores).build();
        } else {
            throw new NegocioException(ApiResponseCode.ERROR_FORMATO_REGLA_NO_VALIDA.getCode(),
                    ApiResponseCode.ERROR_FORMATO_REGLA_NO_VALIDA.getDescription(),
                    ApiResponseCode.ERROR_FORMATO_REGLA_NO_VALIDA.getHttpStatus());
        }

    }

    /**
     * Metodo encargado de realizar la evaluacion de los tokens
     * 
     * @param tokenVOSet
     * @param valorCampo
     * @return boolean
     * @author rafaelParra
     */
    private boolean evaluarTokens(Collection<TokenMotorDTO> tokenVOSet, String valorCampo,
            ValidacionArchivoDTO validacionArchivo, int index, Map<String, ListaDetalleDTO> detalleDefinicionMap) {

        boolean resultado = false;
        TokenMotorDTO tokenVO;
        TokenMotorDTO tokenAnteriorVO = null;

        for (Iterator<TokenMotorDTO> iter = tokenVOSet.iterator(); iter.hasNext();) {
            tokenVO = iter.next();

            switch (tokenVO.getTipo()) {

            case TokenMotorDTO.PARENTESIS:
                tokenVO.setResultado(evaluarTokens(tokenVO.getSubTokens(), valorCampo, validacionArchivo, index,
                        detalleDefinicionMap));
                break;

            case TokenMotorDTO.NUMERO:
                if (UtilsString.isNumeroEntero(tokenVO.getRegla())) {
                    tokenVO.setResultado(ejecutarRegla(Integer.parseInt(tokenVO.getRegla()), valorCampo,
                            validacionArchivo, index, detalleDefinicionMap));
                }

                break;

            default:
                break;
            }
            resultado = aplicarAndOr(tokenAnteriorVO, tokenVO);
            tokenVO.setResultado(resultado);
            tokenAnteriorVO = tokenVO;
        }
        return resultado;
    }

    /**
     * Metodo encargado de consultar y validar la regla dado un campo
     * 
     * @param regla
     * @param valorCampo
     * @return boolean
     */
    private boolean ejecutarRegla(Integer regla, String valorCampo, ValidacionArchivoDTO validacionArchivo, int index,
            Map<String, ListaDetalleDTO> detalleDefinicionMap) {
        ReglasDetalleArchivoDTO reglaVO = reglasDetalleArchivoService.buscarRegla(regla);
        var resultadoRegla = this.compilarRegla(reglaVO, valorCampo, validacionArchivo, index, detalleDefinicionMap);
        if (!resultadoRegla) {
            String mensaje = Objects.isNull(reglaVO.getMensajes()) || Objects.isNull(reglaVO.getMensajes().getMensaje())
                    ? MessageFormat.format(Constantes.MENSAJE_ERROR_VALIDACION_CAMPOS, regla)
                    : reglaVO.getMensajes().getMensaje();
            this.listaErrores.add(reglaVO.getIdRegla());
            this.listaMensajesError.add(mensaje);
        }
        return resultadoRegla;
    }

    /**
     * Metodo encargado de realizar la logica del AND y OR
     * 
     * @param tokenAnteriorVO
     * @param tokenVO
     * @return boolean
     * @author rafaelParra
     */
    private boolean aplicarAndOr(TokenMotorDTO tokenAnteriorVO, TokenMotorDTO tokenVO) {

        if (tokenAnteriorVO != null) {
            if (tokenAnteriorVO.getConector() == TokenMotorDTO.AND) {
                return tokenAnteriorVO.isResultado() && tokenVO.isResultado();
            } else {
                return tokenAnteriorVO.isResultado() || tokenVO.isResultado();
            }
        } else {
            return tokenVO.isResultado();
        }
    }

    /**
     * Metodo encargado de validar la regla y separar la regla de forma recursiva
     * 
     * @param regla
     * @return Collection<TokenMotorDTO>
     * @author rafaelParra
     */
    private Collection<TokenMotorDTO> compilarRegla(String regla) {

        Collection<TokenMotorDTO> tokenSet = new ArrayList<>();
        TokenMotorDTO tokenAnteriorVO = new TokenMotorDTO();
        TokenMotorDTO tokenVO = buscarPrimerToken(regla);
        validarEstado(INICIO, tokenVO.getTipo());
        while (tokenVO.getTipo() != TokenMotorDTO.FIN) {
            switch (tokenVO.getTipo()) {

            case TokenMotorDTO.PARENTESIS:
                tokenVO.setSubTokens(compilarRegla(tokenVO.getRegla()));
                break;

            case TokenMotorDTO.NUMERO:
                break;

            case TokenMotorDTO.AND, TokenMotorDTO.OR:
                tokenAnteriorVO.setConector(tokenVO.getTipo());
                tokenSet.add(tokenAnteriorVO);
                break;

            default:
                // levantar error, por token no v�lido
                break;
            }
            tokenAnteriorVO = tokenVO;
            tokenVO = buscarPrimerToken(tokenVO.getRestoRegla());
            validarEstado(tokenAnteriorVO.getTipo(), tokenVO.getTipo());
        }
        // si llega a este punto es porque el tipo de token es FIN
        tokenAnteriorVO.setConector(tokenVO.getTipo());
        tokenSet.add(tokenAnteriorVO);
        return tokenSet;

    }

    /**
     * Metodo encargado de realizar la busqueda del primer token
     * 
     * @param regla
     * @return TokenMotorDTO
     * @author rafaelParra
     */
    private TokenMotorDTO buscarPrimerToken(String regla) {

        TokenMotorDTO tokenVO = new TokenMotorDTO();

        if (regla == null || regla.length() == 0) {
            tokenVO.setTipo(TokenMotorDTO.FIN);
        } else {
            String caracter = regla.substring(0, 1);
            if ("(".compareTo(caracter) == 0) {
                tokenVO.setTipo(TokenMotorDTO.PARENTESIS);
                tokenVO.setRegla(buscarSubExpresion(regla).trim());
                tokenVO.setRestoRegla(regla.substring(longSubExpresion(regla) + 1).trim());
            } else {
                if (("A".compareTo(caracter) == 0) && validarAnd(regla)) {
                    tokenVO.setTipo(TokenMotorDTO.AND);
                    tokenVO.setRestoRegla(regla.substring(3).trim());
                } else {
                    if (("O".compareTo(caracter) == 0) && validarOr(regla)) {
                        tokenVO.setTipo(TokenMotorDTO.OR);
                        tokenVO.setRestoRegla(regla.substring(2).trim());
                    } else {
                        if (validarCaracter(caracter, Constantes.REGEX_NUMEROS_PERMITIDOS)) {
                            tokenVO.setTipo(TokenMotorDTO.NUMERO);
                            String idRegla = buscarNumero(regla);
                            tokenVO.setRegla(idRegla);
                            tokenVO.setRestoRegla(regla.substring(idRegla.length()).trim());
                        } else {
                            tokenVO.setTipo(TokenMotorDTO.OTRO);
                        }
                    }
                }
            }
        }
        return tokenVO;
    }

    /**
     * Metodo encargado de realizara la busqueda de una expresión 
     * dentro de una regla multiple
     * 
     * @param regla
     * @return String
     * @author rafaelParra
     */
    private String buscarSubExpresion(String regla) {

        String lista = "";
        int cuenta = 0;
        boolean hallado = false;
        for (int i = 0; i < regla.length(); i++) {
            if (regla.substring(i, i + 1).compareTo(")") != 0) {
                lista = lista.concat(regla.substring(i, i + 1));
                cuenta++;
            } else {
                hallado = true;
                break;
            }
        }
        if (hallado && cuenta > 0) {
            return regla.substring(1, cuenta);
        } else {
            return null; // en lugar del nulo levantar excepci�n
        }
    }

    /**
     * Metodo encargado de realizar el recorrido de una subexpresion
     * 
     * @param regla
     * @return int
     * @author rafaelParra
     */
    private int longSubExpresion(String regla) {

        String lista = "";
        int cuenta = 0;
        boolean hallado = false;
        for (int i = 0; i < regla.length(); i++) {
            if (regla.substring(i, i + 1).compareTo(")") != 0) {
                lista = lista.concat(regla.substring(i, i + 1));
                cuenta++;
            } else {
                hallado = true;
                break;
            }
        }
        if (hallado && cuenta > 0) {
            return cuenta;
        } else {
            return 0; // en lugar del nulo levantar excepci�n
        }
    }

    /**
     * Metodo encargado de realizar la validación del estado de las reglas
     * 
     * @param estadoAnterior
     * @param nuevoEstado
     * @return boolean
     * @author rafaelParra
     */
    private boolean validarEstado(int estadoAnterior, int nuevoEstado) {

        switch (estadoAnterior) {

            case INICIO:
                if (nuevoEstado != TokenMotorDTO.NUMERO || nuevoEstado != TokenMotorDTO.PARENTESIS) {
                    return false; // levantar excepci�n
                }
                break;
    
            case TokenMotorDTO.PARENTESIS:
                if (nuevoEstado != TokenMotorDTO.NUMERO || nuevoEstado != TokenMotorDTO.FIN) {
                    return false; // levantar excepci�n
                }
                break;
    
            case TokenMotorDTO.AND, TokenMotorDTO.OR:
                if (nuevoEstado != TokenMotorDTO.NUMERO || nuevoEstado != TokenMotorDTO.PARENTESIS) {
                    return false; // levantar excepci�n
                }
                break;
    
            default:
                break;
        }
        return true;
    }

    /**
     * Metodo encargado de realizar la validación de un caracter
     * 
     * @param caracter
     * @param regex
     * @return boolean
     * @author rafaelParra
     */
    private boolean validarCaracter(String caracter, String regex) {

        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(caracter);
        return mat.matches();
    }

    /**
     * Metodo encargado de validar la expresión logica AND
     * 
     * @param regla
     * @return boolean
     * @author rafaelParra
     */
    private boolean validarAnd(String regla) {

        String regl = regla.substring(0, 3);
        return ("AND".compareTo(regl) == 0);
    }

    /**
     * Metodo encargado de validar la expresión logica OR
     * 
     * @param regla
     * @return boolean
     * @author rafaelParra
     */
    private boolean validarOr(String regla) {

        return "OR".compareTo(regla.substring(0, 2)) == 0;
    }

    /**
     * Metodo encargado de buscar el ID de la regla de una expresión de regla
     * 
     * @param regla
     * @return String
     * @author rafaelParra
     */
    private String buscarNumero(String regla) {

        String lista = "";
        for (int i = 0; i < regla.length(); i++) {
            if (Character.isDigit(regla.charAt(i))) {
                lista = lista.concat(regla.substring(i, i + 1));
            } else {
                break;
            }
        }
        return lista;
    }

    /**
     * 
     * @param reglaVO
     * @param valorCampo
     * @return rafaelParra
     */
    public boolean compilarRegla(ReglasDetalleArchivoDTO reglaVO, String valorCampo,
            ValidacionArchivoDTO validacionArchivo, int index, Map<String, ListaDetalleDTO> detalleDefinicionMap) {

        if ( valorCampo != null && !"".equals(valorCampo) ) {
            ITipoReglaInterface tipoReglaInterface = tipoReglaFactory.getInstance(reglaVO.getTipoRegla());

            if (!Objects.isNull(validacionArchivo) &&
            		validacionArchivo.getMaestroDefinicion().getAgrupador().equals(Constantes.LIQUIDACION_AGRUPADOR) 
            		&& reglaVO.getTipoRegla().equals(Dominios.TIPO_REGLA_CONSULTA_SQL_MULTIPARAMETRO)) {
                return reglaCostos.ejecutarRegla(reglaVO, valorCampo, validacionArchivo, index, detalleDefinicionMap);
            } else {
                return tipoReglaInterface.ejecutarRegla(reglaVO, valorCampo);
            }
        }
        else {
            return true;
        }
    }

}