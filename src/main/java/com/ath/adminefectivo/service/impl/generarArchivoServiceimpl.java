package com.ath.adminefectivo.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;
import com.ath.adminefectivo.entities.TransaccionesContables;
import com.ath.adminefectivo.repositories.impl.GenerarArchivoRepository;
import com.ath.adminefectivo.service.IgenerarArchivoService;

@Service
public class generarArchivoServiceimpl implements IgenerarArchivoService{

@Autowired
GenerarArchivoRepository generarArchivoRepository;
	
@Override
public ByteArrayInputStream generarArchivo(Date fecha, String tipoContabilidad,String codBanco) {
	
	//List<RespuestaContableDTO> listaContable;
	List<TransaccionesContablesDTO> listaContable;
	listaContable = generarArchivoRepository.generarArchivo(fecha, tipoContabilidad, codBanco);
	
	
	//AQUI ARMAR EL ARCHIVO EXCEL
	Workbook workbook = new HSSFWorkbook();
	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	Sheet sheet = (Sheet) workbook.createSheet("transaccionesContables");
	Row row = sheet.createRow(0);
	
	//TransaccionesContables  RegContable = new TransaccionesContables();
	  //TransaccionesContablesDTO lista;
	int initRow = 1;
		
		RespuestaContableDTO dtoContable = new RespuestaContableDTO();
		
		listaContable.forEach(item-> {
	    	dtoContable.setBancoAval(item.getBancoAval().getAbreviatura());
	    	dtoContable.setNaturalezaContable(item.getNaturaleza()=="D" ? 40: 50);
	    	dtoContable.setCuentaMayor(item.getCuentaContable());
	    	dtoContable.setSubAuxiliar(item.getCuentaAuxiliar());
	    	dtoContable.setTipoIdentificacion(item.getTipoIdentificacion());
	    	dtoContable.setValorMoneda(item.getValor());
	    	dtoContable.setValorPesos(item.getValor());
	    	//TODO revisar logica de cuando va este valor dtoContable.setCentroCosto(item.getCodigoCentro());
	    	dtoContable.setCentroBeneficio(item.getCodigoCentro());
	    	dtoContable.setIdentificador(item.getIdentificador());
	    	dtoContable.setDescripcionTransaccion(item.getDescripcion());
	    	dtoContable.setTerceroGL(item.getIdTercero());
	    	dtoContable.setNombreTerceroGL(item.getNombreTercero());
	    	dtoContable.setClaveReferencia1(item.getReferencia1());
	    	dtoContable.setClaveReferencia2(item.getReferencia2());	    		
	});
		
		row = sheet.createRow(initRow);
		row.createCell(0).setCellValue(dtoContable.getBancoAval());
		row.createCell(1).setCellValue(dtoContable.getNaturalezaContable());
		row.createCell(2).setCellValue(dtoContable.getCuentaMayor());
		row.createCell(3).setCellValue(dtoContable.getSubAuxiliar());
		row.createCell(4).setCellValue(dtoContable.getTipoIdentificacion());
		row.createCell(5).setCellValue(dtoContable.getValorMoneda());
		row.createCell(6).setCellValue(dtoContable.getValorPesos());
		row.createCell(7).setCellValue(dtoContable.getCentroBeneficio());
		row.createCell(8).setCellValue(dtoContable.getIdentificador());
		row.createCell(9).setCellValue(dtoContable.getDescripcionTransaccion());
		row.createCell(10).setCellValue(dtoContable.getTerceroGL());
		row.createCell(11).setCellValue(dtoContable.getNombreTerceroGL());
		row.createCell(11).setCellValue(dtoContable.getClaveReferencia1());
		row.createCell(11).setCellValue(dtoContable.getClaveReferencia2());
		initRow++;
			
		try {
			workbook.write(stream);
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ByteArrayInputStream(stream.toByteArray());
		//return dtoContable;
	
	
}
}

