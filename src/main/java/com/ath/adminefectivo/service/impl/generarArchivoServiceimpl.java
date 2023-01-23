package com.ath.adminefectivo.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.ath.adminefectivo.dto.compuestos.RespuestaGenerarArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.TransaccionesContables;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.impl.GenerarArchivoRepository;
import com.ath.adminefectivo.service.ICierreContabilidadService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.ITransaccionesContablesService;
import com.ath.adminefectivo.service.IgenerarArchivoService;
import com.ath.adminefectivo.utils.UtilsObjects;
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
			Workbook workbook = new HSSFWorkbook();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Sheet sheet = (Sheet) workbook.createSheet("transaccionesContables");
			Row row = sheet.createRow(0);

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
				row.createCell(10)
						.setCellValue(dtoContable.getNombreTerceroGL() == null ? "" : dtoContable.getNombreTerceroGL());
				row.createCell(11).setCellValue(
						dtoContable.getClaveReferencia1() == null ? "" : dtoContable.getClaveReferencia1());
				row.createCell(12).setCellValue(
						dtoContable.getClaveReferencia2() == null ? "" : dtoContable.getClaveReferencia2());
			}

			try {
				workbook.write(stream);
				workbook.close();
			} catch (IOException e) {
				throw new NegocioException(ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getCode(),
						ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getDescription()+ " - "+ e.getMessage(),
						ApiResponseCode.ERROR_EXPORTANDO_ARCHIVO.getHttpStatus());
			}
			String nombreArchivo = this.obtenerNombreArchivo(codBanco, tipoContabilidad);
			
			return RespuestaGenerarArchivoDTO.builder().nombreArchivo(nombreArchivo).archivoBytes(stream)
					.build();
		}

	}

	@Override
	public RespuestaGenerarArchivoDTO generarArchivoBBOG(Date fecha, String tipoContabilidad, int codBanco) {
		List<String> listaContable = transaccionesContablesService.cierreContablebyBancoF1String(fecha,
				tipoContabilidad, codBanco);

		String nombreArchivo = this.obtenerNombreArchivo(codBanco, tipoContabilidad);

		BufferedWriter bw;

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
		List<String> listaContable = transaccionesContablesService.cierreContablebyBancoF1String(fecha,
				tipoContabilidad, codBanco);

		String nombreArchivo = this.obtenerNombreArchivo(codBanco, tipoContabilidad);

		BufferedWriter bw;

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
				this.obtenerNombreArchivo(297, tipoContabilidad));
		s3utils.guardarArchivoEnBytes(archivo297.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BBOG/Enviados/",
				this.obtenerNombreArchivoConFecha(297, tipoContabilidad));

		RespuestaGenerarArchivoDTO archivo298 = this.generarArchivo(fecha, tipoContabilidad, 298);
		s3utils.guardarArchivoEnBytes(archivo298.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BAVV/",
				this.obtenerNombreArchivo(298, tipoContabilidad));
		s3utils.guardarArchivoEnBytes(archivo298.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BAVV/Enviados/",
				this.obtenerNombreArchivoConFecha(298, tipoContabilidad));

		RespuestaGenerarArchivoDTO archivo299 = this.generarArchivo(fecha, tipoContabilidad, 299);
		s3utils.guardarArchivoEnBytes(archivo299.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BOCC/",
				this.obtenerNombreArchivo(299, tipoContabilidad));
		s3utils.guardarArchivoEnBytes(archivo299.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BOCC/Enviados/",
				this.obtenerNombreArchivoConFecha(299, tipoContabilidad));

		RespuestaGenerarArchivoDTO archivo300 = this.generarArchivo(fecha, tipoContabilidad, 300);
		s3utils.guardarArchivoEnBytes(archivo300.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BPOP/",
				this.obtenerNombreArchivo(300, tipoContabilidad));
		s3utils.guardarArchivoEnBytes(archivo300.getArchivoBytes(), Constantes.URL_ARCHIVOS_CONTABLES_S3 + "BPOP/Enviados/",
				this.obtenerNombreArchivoConFecha(300, tipoContabilidad));

	}

	private String obtenerNombreArchivo(int codigoBanco, String tipoContabilidad) {
		String fechaSistemaString = parametrosService.valorParametro(Constantes.FECHA_DIA_PROCESO);
		String fechaConFormato = fechaSistemaString.replace("/", "");

		if (tipoContabilidad.equals("AM")) {
			if (codigoBanco == 297) {
				return Constantes.CTB_BBOG_Manana + fechaConFormato + Constantes.EXTENSION_ARCHIVO_TXT;
			} else if (codigoBanco == 298) {
				return Constantes.CTB_BAVV_Manana + Constantes.EXTENSION_ARCHIVO_TXT;
			} else if (codigoBanco == 299) {
				return Constantes.CTB_BOCC_Manana + Constantes.EXTENSION_ARCHIVO_XLS;
			} else if (codigoBanco == 300) {
				return Constantes.CTB_BPOP_Manana + Constantes.EXTENSION_ARCHIVO_XLS;
			}
		} else {
			if (codigoBanco == 297) {
				return Constantes.CTB_BBOG_Tarde + fechaConFormato + Constantes.EXTENSION_ARCHIVO_TXT;
			} else if (codigoBanco == 298) {
				return Constantes.CTB_BAVV_Tarde + Constantes.EXTENSION_ARCHIVO_TXT;
			} else if (codigoBanco == 299) {
				return Constantes.CTB_BOCC_Tarde + Constantes.EXTENSION_ARCHIVO_XLS;
			} else if (codigoBanco == 300) {
				return Constantes.CTB_BPOP_Tarde + Constantes.EXTENSION_ARCHIVO_XLS;
			}
		}
		return null;
	}
	
	private String obtenerNombreArchivoConFecha(int codigoBanco, String tipoContabilidad) {
		String fechaSistemaString = parametrosService.valorParametro(Constantes.FECHA_DIA_PROCESO);
		String fechaConFormato = fechaSistemaString.replace("/", "");

		if (tipoContabilidad.equals("AM")) {
			if (codigoBanco == 297) {
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
