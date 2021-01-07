package ar.com.cognisys.sat.bean.asistente;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.administrador.AdministradorPiletas;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.CuentaComercios;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.DDJJ;
import ar.com.cognisys.sat.core.modelo.comun.natatorios.CuentaPileta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;

public class AsistenteDetalleCuenta {

	public static Cuenta cargarCuenta(Cuenta cuenta) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		try {
			if (cuenta.getTipoCuenta().sos( "PILETAS" )) {
				CuentaPileta cuentaPileta = (CuentaPileta) cuenta;
				return AdministradorPiletas.buscarCuenta(cuentaPileta.getTipoDocumento().getNombrePiletas(), cuentaPileta.getNumeroDocumento().toString());
			}
			return AdministradorCuenta.buscarCuenta(cuenta.getTipoCuenta(), cuenta.getDatoCuenta());
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e);
		}
	}

	public static DDJJ obtenerOtraDeclaracion(DDJJ declaracionActual, CuentaComercios cuenta) {
		
		if (cuenta == null || cuenta.getListaDeclaraciones() == null || 
			cuenta.getListaDeclaraciones().isEmpty() || cuenta.getListaDeclaraciones().size() == 1)
			return declaracionActual;
		else {
			if (cuenta.getDeclaracionOriginal().equals( declaracionActual ))
				return cuenta.getDeclaracionRectificativa(); 
			else
				return cuenta.getDeclaracionOriginal();
		}
	}
	
	public static boolean esDDJJOriginal(DDJJ declaracion) {
		return (declaracion != null && declaracion.getVez() == 0);
	}
}