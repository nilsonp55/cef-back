package com.ath.adminefectivo.utils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene los servicios utilitarios de manera estatica
 *
 * @author CamiloBenavides
 */
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class UtilsObjects {

	/**
	 * Copia el valor que contiene los atributos de la fuente a los atributos del
	 * objeto de destino, siempre y cuando los nombres sean iguales. Si encuentra un
	 * atributo ya sea en el origen o en el destino, el proceso simplemente lo
	 * ignora.
	 * 
	 * @param origen
	 * @param dest
	 * @param permiteNull si esta opcion es true, setea las propieadades vacias como
	 *                    nulos
	 * @author CamiloBenavides
	 */
	public static void copiarPropiedades(Object origen, Object dest, boolean permiteNull) {
		BeanUtilsBean.getInstance().getConvertUtils().register(false, permiteNull, 0);

		try {
			BeanUtils.copyProperties(origen, dest);
		} catch (BeansException ex) {
			throw new AplicationException(ApiResponseCode.ERROR_COPIAR_PROPIEDADES.getCode(),
					ApiResponseCode.ERROR_COPIAR_PROPIEDADES.getDescription(),
					ApiResponseCode.ERROR_COPIAR_PROPIEDADES.getHttpStatus());

		}
	}


	/**
	 * Copia el valor que contiene los atributos de la fuente a los atributos del
	 * objeto de destino, siempre y cuando los nombres sean iguales, si encuentra un
	 * atributo ya sea en el origen o en el destino, el proceso simplemente lo
	 * ignora. si encuntra un atributo vacio lo remplaza por cero si es numerico o
	 * espacio si es string
	 * 
	 * @param origen
	 * @param dest
	 * @author CamiloBenavides
	 */
	public static void copiarPropiedades(Object origen, Object dest) {
		copiarPropiedades(origen, dest, false);
	}
	

}
