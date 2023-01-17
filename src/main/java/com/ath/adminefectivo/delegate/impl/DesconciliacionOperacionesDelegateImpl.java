package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IDesconciliacionOperacionesDelegate;
import com.ath.adminefectivo.service.IDesconciliacionOperacionesService;

/**
 * @author cesar.castano
 */
@Service
public class DesconciliacionOperacionesDelegateImpl implements IDesconciliacionOperacionesDelegate {

	@Autowired
	IDesconciliacionOperacionesService desconciliacionOperacionesService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean procesoDesconciliacion(List<Integer> operacionesADesconciliar) {

		return desconciliacionOperacionesService.procesoDesconciliacion(operacionesADesconciliar);
	}
}
