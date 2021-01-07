package ar.com.cognisys.sat.bean.asistente;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.natatorios.CuentaPileta;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;

public class AsistenteListaCuenta {

	public static void desvincular(Usuario usuario, Cuenta cuenta) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		
		try {
			if (!cuenta.esPileta())
				AdministradorCuenta.desasociarCuenta(usuario.getIdUsuario(), cuenta.getTipoCuenta(), cuenta.getDatoCuenta());
			else {
				CuentaPileta cuentaPileta = (CuentaPileta) cuenta;
				AdministradorCuenta.desasociarCuentaPileta(usuario.getIdUsuario(), cuentaPileta.getTipoDocumento().getNombrePiletas(), cuentaPileta.getNumeroDocumento().toString());
			}
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}	
}