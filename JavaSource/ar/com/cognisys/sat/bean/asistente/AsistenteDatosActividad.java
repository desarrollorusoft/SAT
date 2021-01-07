package ar.com.cognisys.sat.bean.asistente;

import java.util.List;

import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.contenedor.ContenedorActividadComercial;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.ActividadComercial;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;

public class AsistenteDatosActividad {

	public static void validarCuentaABL(String padron) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		
		Cuenta cuenta = AdministradorCuenta.buscarCuenta(TiposCuentas.ABL, padron);
		
		if (cuenta == null)
			throw new ExcepcionControladaAlerta("No existe el padrón de ABL");
	}
}