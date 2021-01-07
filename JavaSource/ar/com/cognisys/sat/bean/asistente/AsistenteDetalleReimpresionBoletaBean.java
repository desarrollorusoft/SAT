package ar.com.cognisys.sat.bean.asistente;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.administrador.AdministradorGeneracionRecibos;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.abstracto.Deuda;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.CuentaABL;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.CuentaCementerio;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.CuentaRodados;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.CuentaVehiculos;
import ar.com.cognisys.sat.core.modelo.comun.deudas.Cuota;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;

public class AsistenteDetalleReimpresionBoletaBean {

	public static Deuda recalcularDeuda(Cuenta cuenta, Date fecha) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		
		Deuda deuda = null;
		
		if (cuenta instanceof CuentaABL)
			deuda = recalcularDeuda((CuentaABL) cuenta, fecha);
		
		else if (cuenta instanceof CuentaVehiculos)
			deuda = recalcularDeuda((CuentaVehiculos) cuenta, fecha);
		
		else if (cuenta instanceof CuentaRodados)
			deuda = recalcularDeuda((CuentaRodados) cuenta, fecha);
		
		else if (cuenta instanceof CuentaCementerio)
			deuda = recalcularDeuda((CuentaCementerio) cuenta, fecha);
	
		return deuda;
	}
	
	public static void recalcularDeudaFecha(Cuenta cuenta, Date fecha) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		
		// Se setea todo para recalcular
		for (Cuota cuota : cuenta.getDeudas().getListaCuotas()) 
			cuota.setPagar(true);
		
		Deuda deuda = null;
		
		if (cuenta instanceof CuentaABL)
			deuda = recalcularDeuda((CuentaABL) cuenta, fecha);
		
		else if (cuenta instanceof CuentaVehiculos)
			deuda = recalcularDeuda((CuentaVehiculos) cuenta, fecha);
		
		else if (cuenta instanceof CuentaRodados)
			deuda = recalcularDeuda((CuentaRodados) cuenta, fecha);
		
		else if (cuenta instanceof CuentaCementerio)
			deuda = recalcularDeuda((CuentaCementerio) cuenta, fecha);
		
		cuenta.setDeudas(deuda);
	}


	public static Deuda recalcularDeuda(CuentaABL cuenta, Date fecha) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		
		return AdministradorCuenta.recalcularDeuda(cuenta.getNumero().toString(), 
												   TiposCuentas.ABL, 
												   fecha, 
												   recuperarListadoAPagar(cuenta.getDeudas()),
												   esContado(cuenta.getDeudas()),
												   cuenta.isDeudaLegales());
	}

	public static Deuda recalcularDeuda(CuentaVehiculos cuenta, Date fecha) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		
		return AdministradorCuenta.recalcularDeuda(cuenta.getDominio(), 
												   null,
												   TiposCuentas.VEHICULOS, 
												   fecha, 
												   recuperarListadoAPagar(cuenta.getDeudas()),
												   esContado(cuenta.getDeudas()),
												   cuenta.isDeudaLegales());
	}
	
	public static Deuda recalcularDeuda(CuentaRodados cuenta, Date fecha) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		
		return AdministradorCuenta.recalcularDeuda(cuenta.getDominio(), 
												   null,
												   TiposCuentas.RODADOS, 
												   fecha, 
												   recuperarListadoAPagar(cuenta.getDeudas()),
												   esContado(cuenta.getDeudas()),
												   cuenta.isDeudaLegales());
	}
	
	public static Deuda recalcularDeuda(CuentaCementerio cuenta, Date fecha) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		
		return AdministradorCuenta.recalcularDeuda(cuenta.getNumero().toString(), 
												   cuenta.getNumeroNomenclador(),
												   TiposCuentas.CEMENTERIO, 
												   fecha, 
												   recuperarListadoAPagar(cuenta.getDeudas()),
												   esContado(cuenta.getDeudas()),
												   cuenta.isDeudaLegales());
	}
	
	private static List<Cuota> recuperarListadoAPagar(Deuda deudas) {
		
		List<Cuota> listado = new ArrayList<Cuota>();
		
		for (Cuota cuota : deudas.getListaCuotas())
			if (cuota.isPagar())
				listado.add(cuota);
		
		return listado;
	}
	
	private static boolean esContado(Deuda deudas) {
		
		Integer cant = 0, max = deudas.getListaCuotasVencidas().size();
		
		for (Cuota cuota : deudas.getListaCuotasVencidas())
			if (cuota.isPagar())
				cant++;
		
		return (cant == max);
	}
	
	public static String generarRecibo(Cuenta cuenta, Date fecha, Deuda deuda) throws ExcepcionControladaError {
		
		String correo = "reimpresion@vicentelopez.gov.ar";
		normalizarListado(deuda.getListaCuotas());
		byte[] data = generarDataRecibos(cuenta, fecha, correo, deuda);
		
		try {
			String fileName = Integer.toString(new Date().hashCode());
	        
	        File file = File.createTempFile(fileName, ".pdf");
	        
	        FileOutputStream stream = new FileOutputStream(file);
	        stream.write(data);
	        stream.flush();
	        stream.close();
	        
	        return file.getName();
		} catch (Exception e) {
			throw new ExcepcionControladaError("Fallo la generacion de la boleta", e);
		}
	}
	
	private static void normalizarListado(List<Cuota> listaCuotas) {
		
		for (Cuota cuota : listaCuotas)
			if (cuota.getFechaVencimiento() == null)
				cuota.setFechaVencimiento( generarFecha(cuota.getPeriodo()) );
	}

	private static byte[] generarDataRecibos(Cuenta cuenta, Date fecha, String correo, Deuda deuda) throws ExcepcionControladaError {
		
		byte[] data = null;
		
		if (cuenta instanceof CuentaABL)
			data = AdministradorGeneracionRecibos.generarReciboABL_Datos((CuentaABL) cuenta, 
																		 correo, 
																		 fecha, 
																		 deuda.getTotalCoutasGeneral().getTotal(), 
																		 deuda.getListaCuotas(), 
																		 deuda.getNumeroComprobante());	
		else if (cuenta instanceof CuentaVehiculos)
			data = AdministradorGeneracionRecibos.generarReciboVehiculo_Datos((CuentaVehiculos) cuenta, 
																			  correo, 
																			  fecha, 
																			  deuda.getTotalCoutasGeneral().getTotal(), 
																			  deuda.getListaCuotas(), 
																			  deuda.getNumeroComprobante());	
		else if (cuenta instanceof CuentaRodados)
			data = AdministradorGeneracionRecibos.generarReciboRodado_Datos((CuentaRodados) cuenta, 
																		    correo, 
																		    fecha, 
																		    deuda.getTotalCoutasGeneral().getTotal(), 
																		    deuda.getListaCuotas(), 
																		    deuda.getNumeroComprobante());	
		else if (cuenta instanceof CuentaCementerio)
			data = AdministradorGeneracionRecibos.generarReciboCementerio_Datos((CuentaCementerio) cuenta, 
																				correo, 
																				fecha, 
																				deuda.getTotalCoutasGeneral().getTotal(), 
																				deuda.getListaCuotas(), 
																				deuda.getNumeroComprobante());	
		
		return data;
	}
	
	private static Date generarFecha(String periodo) {
		try {
			if (!periodo.isEmpty()) {
				String[] partes = periodo.split("-");
				
				if (partes.length >= 2) {
					periodo = partes[0]+"-"+partes[1]+"-01";
				}
			}
			
			return (new SimpleDateFormat("yyyy-MM-dd")).parse(periodo);
		} catch (ParseException e) {
			return (new Date());
		}
	}
}