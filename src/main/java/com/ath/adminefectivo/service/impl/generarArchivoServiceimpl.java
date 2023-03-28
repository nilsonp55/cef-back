package com.ath.adminefectivo.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.compuestos.RespuestaGenerarArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.impl.GenerarArchivoRepository;
import com.ath.adminefectivo.service.IFestivosNacionalesService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.ITransaccionesContablesService;
import com.ath.adminefectivo.service.IgenerarArchivoService;
import com.ath.adminefectivo.utils.s3Utils;

@Service
public class generarArchivoServiceimpl implements IgenerarArchivoService {

	@Autowired
	GenerarArchivoRepository generarArchivoRepository;

	@Autowired
	ITransaccionesContablesService transaccionesContablesService;

	@Autowired
	s3Utils s3utils;

	@Autowired
	IParametroService parametrosService;
	
	@Autowired
	IFestivosNacionalesService festivosNacionalesService;

	@Override
	public RespuestaGenerarArchivoDTO generarArchivo(Date fecha, String tipoContabilidad, int codBanco) {

		// List<RespuestaContableDTO> listaContable;

		if (codBanco == 297) {
			return generarArchivoBBOG(fecha, tipoContabilidad, codBanco);
		} else if (codBanco == 298) {
			return generarArchivoBAVV(fecha, tipoContabilidad, codBanco);
		} else {
			List<RespuestaContableDTO> listaContable = transaccionesContablesService.getCierreContable(fecha,
					tipoContabilidad, codBanco);

			// AQUI ARMAR EL ARCHIVO EXCEL
			XSSFWorkbook workbook = new XSSFWorkbook();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Sheet sheet = (Sheet) workbook.createSheet("transaccionesContables");
			Row row = sheet.createRow(0);

			RespuestaContableDTO dtoContable = new RespuestaContableDTO();
			for (int i = 0; i < listaContable.size(); i++) {

				row = sheet.createRow(i);
				dtoContable = listaContable.get(i);

				//NUEVO  ARCHIVO
				int naturalezaNum = 0;
				String naturaleza = dtoContable.getNaturalezaContable();
				if(!Objects.isNull(naturaleza) && naturaleza.equals("C")) {
					naturalezaNum = 50;
				}else {
					naturalezaNum = 40;
				}
				row.createCell(0).setCellValue(naturalezaNum);
				try {
					row.createCell(1).setCellValue( Integer.parseInt(dtoContable.getCuentaMayor()) );
				}
				catch (NumberFormatException ex) {
					row.createCell(1).setBlank();
				}
				row.createCell(2).setCellValue(dtoContable.getSubAuxiliar());
				
				int tipoIdentificacionNum = 0; 
				String tipoIdentificacion = dtoContable.getTipoIdentificacion();
				if(!Objects.isNull(tipoIdentificacion) &&  tipoIdentificacion.equals("NIT")) {
					tipoIdentificacionNum = 31;
				}
				row.createCell(3).setCellValue(tipoIdentificacionNum);
				row.createCell(4).setBlank();//tipoDeCambio Origen vs Dolar
				row.createCell(5).setBlank();//tipoDeCambio dolar vs pesos
				row.createCell(6).setCellValue(dtoContable.getValor());//valor moneda del documento
				row.createCell(7).setCellValue(dtoContable.getValor());//valor moneda local
				row.createCell(8).setBlank();//valor en moneda fuerte USD
				row.createCell(9).setBlank();//Centro de costos
				try {
					row.createCell(10).setCellValue( Integer.parseInt(dtoContable.getCentroBeneficio()) );
				}
				catch (NumberFormatException ex) {
					row.createCell(10).setBlank();
				}
				row.createCell(11).setBlank();//Orden CO
				row.createCell(12).setBlank();//Area funcional
				row.createCell(13).setCellValue(dtoContable.getIdentificador());
				row.createCell(14).setCellValue(dtoContable.getDescripcionTransaccion());
				if ( !Objects.isNull(dtoContable.getTerceroGL()) ) {
					row.createCell(15).setCellValue(dtoContable.getTerceroGL());
				}
				else {
					row.createCell(15).setBlank();
				}
				row.createCell(16).setCellValue(dtoContable.getNombreTerceroGL());
				row.createCell(17).setBlank();//Fecha conversion
				row.createCell(18).setCellValue(
						dtoContable.getClaveReferencia1() == null ? "" : dtoContable.getClaveReferencia1());
				row.createCell(19).setBlank();//Clave referencia3
							
			}

			try {
				workbook.write(stream);
				workbook.close();
			} catch (IOException e) {
				throw new NegocioException(ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getCode(),
						ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getDescription()+ " - "+ e.getMessage(),
						ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getHttpStatus());
			}
			String nombreArchivo = this.obtenerNombreArchivo(fecha, codBanco, tipoContabilidad);
			
			return RespuestaGenerarArchivoDTO.builder().nombreArchivo(nombreArchivo).archivoBytes(stream)
					.build();
		}

	}

	@Override
	public RespuestaGenerarArchivoDTO generarArchivoBBOG(Date fecha, String tipoContabilidad, int codBanco) {
		List<String> listaContable = transaccionesContablesService.cierreContablebyBancoF1String(fecha,
				tipoContabilidad, codBanco);

		String nombreArchivo = this.obtenerNombreArchivo(fecha, codBanco, tipoContabilidad);

		ByteArrayOutputStream x = new ByteArrayOutputStream();

		try {
			listaContable.forEach(linea -> {
				try {
					x.write(linea.getBytes());
					x.write("\r\n".getBytes());
				} catch (IOException e) {
					throw new NegocioException(ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getCode(),
							ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getDescription()+ " - "+ e.getMessage(),
							ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getHttpStatus());
				}
			});
			x.close();
		} catch (IOException e) {
			throw new NegocioException(ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getCode(),
					ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getDescription()+ " - "+ e.getMessage(),
					ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getHttpStatus());
		}

		return RespuestaGenerarArchivoDTO.builder().nombreArchivo(nombreArchivo).archivoBytes(x).build();

	}
	
	@Override
	public RespuestaGenerarArchivoDTO generarArchivoBAVV(Date fecha, String tipoContabilidad, int codBanco) {
		List<String> listaContable = transaccionesContablesService.cierreContablebyBancoF2String(fecha,
				tipoContabilidad, codBanco);

		String nombreArchivo = this.obtenerNombreArchivo(fecha, codBanco, tipoContabilidad);

		ByteArrayOutputStream x = new ByteArrayOutputStream();

		try {
			listaContable.forEach(linea -> {
				try {
					x.write(linea.getBytes());
					x.write("\r\n".getBytes());
				} catch (IOException e) {
					throw new NegocioException(ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getCode(),
							ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getDescription()+ " - "+ e.getMessage(),
							ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getHttpStatus());
				}
			});
			x.close();
		} catch (IOException e) {
			throw new NegocioException(ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getCode(),
					ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getDescription()+ " - "+ e.getMessage(),
					ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getHttpStatus());
		}

		return RespuestaGenerarArchivoDTO.builder().nombreArchivo(nombreArchivo).archivoBytes(x).build();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generarArchivosCierreContable(Date fecha, String tipoContabilidad) {

		RespuestaGenerarArchivoDTO archivo297 = this.generarArchivo(fecha, tipoContabilidad, 297);
		s3utils.guardarArchivoEnBytes(archivo297.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BBOG/",
				this.obtenerNombreArchivo(fecha, 297, tipoContabilidad));
		s3utils.guardarArchivoEnBytes(archivo297.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BBOG/Enviados/",
				this.obtenerNombreArchivoConFecha(297, tipoContabilidad));

		RespuestaGenerarArchivoDTO archivo298 = this.generarArchivo(fecha, tipoContabilidad, 298);
		s3utils.guardarArchivoEnBytes(archivo298.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BAVV/",
				this.obtenerNombreArchivo(fecha, 298, tipoContabilidad));
		s3utils.guardarArchivoEnBytes(archivo298.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BAVV/Enviados/",
				this.obtenerNombreArchivoConFecha(298, tipoContabilidad));

		RespuestaGenerarArchivoDTO archivo299 = this.generarArchivo(fecha, tipoContabilidad, 299);
		s3utils.guardarArchivoEnBytes(archivo299.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BOCC/",
				this.obtenerNombreArchivo(fecha, 299, tipoContabilidad));
		s3utils.guardarArchivoEnBytes(archivo299.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BOCC/Enviados/",
				this.obtenerNombreArchivoConFecha(299, tipoContabilidad));

		RespuestaGenerarArchivoDTO archivo300 = this.generarArchivo(fecha, tipoContabilidad, 300);
		s3utils.guardarArchivoEnBytes(archivo300.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BPOP/",
				this.obtenerNombreArchivo(fecha, 300, tipoContabilidad));
		s3utils.guardarArchivoEnBytes(archivo300.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BPOP/Enviados/",
				this.obtenerNombreArchivoConFecha(300, tipoContabilidad));

	}

	private String obtenerNombreArchivo(Date fecha, int codigoBanco, String tipoContabilidad) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
		
		if (tipoContabilidad.equals("AM")) {
			if (codigoBanco == 297) {
				
				String fechaSistemaString = formato.format(festivosNacionalesService.consultarAnteriorHabil(fecha));
				String fechaConFormato = fechaSistemaString.replace("/", "");
				return Constantes.CTB_BBOG_Manana + fechaConFormato + Constantes.EXTENSION_ARCHIVO_TXT;
			} else if (codigoBanco == 298) {
				return Constantes.CTB_BAVV_Manana + Constantes.EXTENSION_ARCHIVO_TXT;
			} else if (codigoBanco == 299) {
				return Constantes.CTB_BOCC_Manana + Constantes.EXTENSION_ARCHIVO_XLSX;
			} else if (codigoBanco == 300) {
				return Constantes.CTB_BPOP_Manana + Constantes.EXTENSION_ARCHIVO_XLSX;
			}
		} else {
			if (codigoBanco == 297) {
				String fechaSistemaString = formato.format(festivosNacionalesService.consultarAnteriorHabil(fecha));

				String fechaConFormato = fechaSistemaString.replace("/", "");
				return Constantes.CTB_BBOG_Tarde + fechaConFormato + Constantes.EXTENSION_ARCHIVO_TXT;
			} else if (codigoBanco == 298) {
				return Constantes.CTB_BAVV_Tarde + Constantes.EXTENSION_ARCHIVO_TXT;
			} else if (codigoBanco == 299) {
				return Constantes.CTB_BOCC_Tarde + Constantes.EXTENSION_ARCHIVO_XLSX;
			} else if (codigoBanco == 300) {
				return Constantes.CTB_BPOP_Tarde + Constantes.EXTENSION_ARCHIVO_XLSX;
			}
		}
		return null;
	}
	
	private String obtenerNombreArchivoConFecha(int codigoBanco, String tipoContabilidad) {
		String fechaSistemaString = parametrosService.valorParametro(Constantes.FECHA_DIA_PROCESO);
		String fechaConFormato = fechaSistemaString.replace("/", "");
		SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");

		if (tipoContabilidad.equals("AM")) {
			if (codigoBanco == 297) {
				Date fecha = parametrosService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
				fecha = festivosNacionalesService.consultarAnteriorHabil(fecha);
				fechaSistemaString = formato.format(fecha);

				fechaConFormato = fechaSistemaString.replace("/", "");
				return Constantes.CTB_BBOG_Manana + fechaConFormato + Constantes.EXTENSION_ARCHIVO_TXT;
			} else if (codigoBanco == 298) {
				return Constantes.CTB_BAVV_Manana + fechaConFormato + Constantes.EXTENSION_ARCHIVO_TXT;
			} else if (codigoBanco == 299) {
				return Constantes.CTB_BOCC_Manana + fechaConFormato + Constantes.EXTENSION_ARCHIVO_XLS;
			} else if (codigoBanco == 300) {
				return Constantes.CTB_BPOP_Manana + fechaConFormato + Constantes.EXTENSION_ARCHIVO_XLS;
			}
		} else {
			if (codigoBanco == 297) {
				Date fecha = parametrosService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
				fecha = festivosNacionalesService.consultarAnteriorHabil(fecha);
				fechaSistemaString = formato.format(fecha);

				fechaConFormato = fechaSistemaString.replace("/", "");
				return Constantes.CTB_BBOG_Tarde + fechaConFormato + Constantes.EXTENSION_ARCHIVO_TXT;
			} else if (codigoBanco == 298) {
				return Constantes.CTB_BAVV_Tarde + fechaConFormato + Constantes.EXTENSION_ARCHIVO_TXT;
			} else if (codigoBanco == 299) {
				return Constantes.CTB_BOCC_Tarde + fechaConFormato + Constantes.EXTENSION_ARCHIVO_XLS;
			} else if (codigoBanco == 300) {
				return Constantes.CTB_BPOP_Tarde + fechaConFormato + Constantes.EXTENSION_ARCHIVO_XLS;
			}
		}
		return null;
	}

}
