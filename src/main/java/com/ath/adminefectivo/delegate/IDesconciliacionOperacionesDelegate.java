package com.ath.adminefectivo.delegate;

import java.util.List;

public interface IDesconciliacionOperacionesDelegate {

	/**
	 * Delegate encargado de persistir una operacion desconciliada
	 * @param List<Integer>
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean procesoDesconciliacion(List<Integer> operacionesADesconciliar);

}
