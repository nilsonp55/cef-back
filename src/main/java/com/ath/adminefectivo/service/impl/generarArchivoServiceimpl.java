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

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;
import com.ath.adminefectivo.entities.TransaccionesContables;
import com.ath.adminefectivo.repositories.impl.GenerarArchivoRepository;
import com.ath.adminefectivo.service.ICierreContabilidadService;
import com.ath.adminefectivo.service.ITransaccionesContablesService;
import com.ath.adminefectivo.service.IgenerarArchivoService;
import com.ath.adminefectivo.utils.UtilsObjects;
import com.ath.adminefectivo.utils.s3Utils;

@Service
public class generarArchivoServiceimpl implements IgenerarArchivoService{

@Autowired
GenerarArchivoRepository generarArchivoRepository;

@Autowired
ITransaccionesContablesService transaccionesContablesService;

@Autowired
s3Utils s3utils;
	
@Override
public ByteArrayOutputStream generarArchivo(Date fecha, String tipoContabilidad,int codBanco) {
	
	//List<RespuestaContableDTO> listaContable;
	List<RespuestaContableDTO> listaContable = transaccionesContablesService.getCierreContable(fecha,tipoContabilidad,codBanco);
	

	System.out.println("listaContable "+listaContable.size());
	
	//AQUI ARMAR EL ARCHIVO EXCEL
	Workbook workbook = new HSSFWorkbook();
	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	Sheet sheet = (Sheet) workbook.createSheet("transaccionesContables");
	Row row = sheet.createRow(0);
	
	//TransaccionesContables  RegContable = new TransaccionesContables();
	  //TransaccionesContablesDTO lista;
	int initRow = 0;
	RespuestaContableDTO dtoContable;
	for (int i = 0; i < listaContable.size(); i++) {
		
		row = sheet.createRow(i);
		dtoContable = listaContable.get(i);
		
		
		row.createCell(0).setCellValue(dtoContable.getBancoAval());
		row.createCell(1).setCellValue(dtoContable.getNaturalezaContable());
		row.createCell(2).setCellValue(dtoContable.getCuentaMayor());
		row.createCell(3).setCellValue(dtoContable.getSubAuxiliar());
		row.createCell(4).setCellValue(dtoContable.getTipoIdentificacion());
		row.createCell(5).setCellValue(dtoContable.getValor());
		row.createCell(6).setCellValue(dtoContable.getCentroBeneficio());
		row.createCell(7).setCellValue(dtoContable.getIdentificador());
		row.createCell(8).setCellValue(dtoContable.getDescripcionTransaccion());
		row.createCell(9).setCellValue(dtoContable.getTerceroGL() == null ? 0 : dtoContable.getTerceroGL());
		row.createCell(10).setCellValue(dtoContable.getNombreTerceroGL() == null ? "" : dtoContable.getNombreTerceroGL());
		row.createCell(11).setCellValue(dtoContable.getClaveReferencia1() == null ? "" : dtoContable.getClaveReferencia1());
		row.createCell(12).setCellValue(dtoContable.getClaveReferencia2() == null ? "" : dtoContable.getClaveReferencia2());
	}
		
			
		try {
			workbook.write(stream);
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		return new ByteArrayInputStream(stream.toByteArray());
		return stream;
		//return dtoContable;
	
	
}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generarArchivosCierreContable(Date fecha, String tipoContabilidad) {
		
		ByteArrayOutputStream archivo297 = this.generarArchivo(fecha, tipoContabilidad, 297);
		s3utils.guardarArchivoEnBytes(archivo297, Constantes.URL_ARCHIVOS_CONTABLES_S3+"BBOG/", this.obtenerNombreArchivo(297, tipoContabilidad));
		
		
		ByteArrayOutputStream archivo298 = this.generarArchivo(fecha, tipoContabilidad, 298);
		s3utils.guardarArchivoEnBytes(archivo298, Constantes.URL_ARCHIVOS_CONTABLES_S3+"BAVV/", this.obtenerNombreArchivo(298, tipoContabilidad));
		
		ByteArrayOutputStream archivo299 = this.generarArchivo(fecha, tipoContabilidad, 299);
		s3utils.guardarArchivoEnBytes(archivo299, Constantes.URL_ARCHIVOS_CONTABLES_S3+"BOCC/", this.obtenerNombreArchivo(299, tipoContabilidad));
		
		ByteArrayOutputStream archivo300 = this.generarArchivo(fecha, tipoContabilidad, 300);
		s3utils.guardarArchivoEnBytes(archivo300, Constantes.URL_ARCHIVOS_CONTABLES_S3+"BPOP/", this.obtenerNombreArchivo(300, tipoContabilidad));
		
	}
	private String obtenerNombreArchivo(int codigoBanco, String tipoContabilidad) {
		if(tipoContabilidad.equals("AM")) {
			if(codigoBanco == 297) {
				return Constantes.CTB_BBOG_Manana;
			}else if(codigoBanco == 298) {
				return Constantes.CTB_BAVV_Manana;
			}else if(codigoBanco == 299) {
				return Constantes.CTB_BOCC_Manana;
			}else if(codigoBanco == 300) {
				return Constantes.CTB_BPOP_Manana;
			}
		}else {
			if(codigoBanco == 297) {
				return Constantes.CTB_BBOG_Tarde;
			}else if(codigoBanco == 298) {
				return Constantes.CTB_BAVV_Tarde;
			}else if(codigoBanco == 299) {
				return Constantes.CTB_BOCC_Tarde;
			}else if(codigoBanco == 300) {
				return Constantes.CTB_BPOP_Tarde;
			}
		}
		return null;
	}

}

