package com.ath.adminefectivo.delegate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IGestionRetencionArchivosDelegate;
import com.ath.adminefectivo.service.impl.GestionRetencionArchivosServiceImpl;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GestionRetencionArchivosDelegateImpl implements IGestionRetencionArchivosDelegate {
	
	@Autowired
	GestionRetencionArchivosServiceImpl gestionRetencionArchivos;
	
	@Override
	@Scheduled(cron = "0 0 18 * * *")
	public boolean eliminarArchivosPorRetencion(){	
		return gestionRetencionArchivos.eliminarArchivosPorRetencion();		
	}
	
	
}
