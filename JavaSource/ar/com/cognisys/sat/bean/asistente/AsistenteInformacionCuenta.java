package ar.com.cognisys.sat.bean.asistente;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.cognisys.sat.core.administrador.AdministradorFormularioConsultas;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.CuentaABL;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.CuentaCementerio;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.CuentaComercios;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.CuentaRodados;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.CuentaVehiculos;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;

public class AsistenteInformacionCuenta {
	
	private static final String CONTRIBUYENTE = "Contribuyente";

	public static Map<String, String> generarMapaDatos(Cuenta cuenta) {
		
		switch (cuenta.getTipoCuenta()) {
			case ABL:			return generarMapaDatos((CuentaABL) cuenta);
			case VEHICULOS:		return generarMapaDatos((CuentaVehiculos) cuenta);
			case RODADOS:		return generarMapaDatos((CuentaRodados) cuenta);
			case CEMENTERIO:	return generarMapaDatos((CuentaCementerio) cuenta);
			case COMERCIOS:		return generarMapaDatos((CuentaComercios) cuenta);
			default: return null;
		}
	}
	
	public static Map<String, String> generarMapaDatos(CuentaABL cuenta) {
		
		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put(CONTRIBUYENTE, cuenta.getContribuyente().getNombreApellido());
		mapa.put("Calle", cuenta.getCalle());
		mapa.put("Altura", cuenta.getAltura());
		mapa.put("Departameno", cuenta.getDepartamento());
		mapa.put("Piso", cuenta.getPiso());
		mapa.put("Código Postal", cuenta.getCodigoPostal());
		
		return mapa;
	}
	
	public static Map<String, String> generarMapaDatos(CuentaComercios cuenta) {
		
		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put(CONTRIBUYENTE, cuenta.getContribuyente().getNombreApellido());
		mapa.put("Razón Social", cuenta.getRazonSocial());
		
		return mapa;
	}
	
	public static Map<String, String> generarMapaDatos(CuentaVehiculos cuenta) {
		
		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put(CONTRIBUYENTE, cuenta.getContribuyente().getNombreApellido());
		mapa.put("Marca", cuenta.getMarca());
		mapa.put("Modelo", cuenta.getModelo());
		mapa.put("Tipo", cuenta.getTipoVehiculo());
		mapa.put("Categoría", cuenta.getCatVehiculo());
		
		return mapa;
	}
	
	public static Map<String, String> generarMapaDatos(CuentaRodados cuenta) {
		
		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put(CONTRIBUYENTE, cuenta.getContribuyente().getNombreApellido());
		mapa.put("Marca", cuenta.getMarca());
		mapa.put("Modelo", cuenta.getModelo());
		mapa.put("Cilindrada", cuenta.getCilindrada());
		
		return mapa;
	}
	
	public static Map<String, String> generarMapaDatos(CuentaCementerio cuenta) {
		
		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put(CONTRIBUYENTE, cuenta.getContribuyente().getNombreApellido());
		mapa.put("Descripción", cuenta.getDescripcion());
		
		return mapa;
	}

	public static List<Consulta> recueprarConsultas(String cuit, Cuenta cuenta) throws ExcepcionControladaError {
		return AdministradorFormularioConsultas.buscarConsultasPorCuitCuenta( cuit, cuenta.getDatoCuenta() );
	}
}