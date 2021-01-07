package ar.com.cognisys.sat.bean.asistente;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.core.administrador.AdministradorContribuyente;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;

public class AsistenteActivacion {

	public static void activar(String cuit, String codigo) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {
			AdministradorContribuyente.activacionMobile(cuit, codigo);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static void generarCodigo(String cuit) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		try {
			AdministradorContribuyente.solicitarCodigoActivacion(cuit);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
}