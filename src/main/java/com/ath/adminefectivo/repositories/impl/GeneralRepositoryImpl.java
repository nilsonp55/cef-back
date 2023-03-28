package com.ath.adminefectivo.repositories.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.ath.adminefectivo.repositories.IGeneralRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Repository encargado de manejar la logica de la entidad ArchivosCargados
 *
 * @author CamiloBenavides
 */
@Service
@Log4j2
public class GeneralRepositoryImpl implements IGeneralRepository{
	
    @PersistenceContext
    private EntityManager entityManager;
	
    @Override
	public boolean ejecutarQueryNativa(String consulta) {
		
		Query query =  entityManager.createNativeQuery(consulta);
		
		var result = query.getResultList();

		
		return !result.isEmpty();
	}
    
    
    @Override
	public boolean ejecutarQueryNativa(String consulta, String parametro) {
		
		Query query =  entityManager.createNativeQuery(consulta);
		log.debug("consulta "+ consulta);
		log.debug("PARAMETRO "+ parametro);
		query.setParameter("parametro", parametro);
		
		return (boolean)  query.getSingleResult();
	}
}
