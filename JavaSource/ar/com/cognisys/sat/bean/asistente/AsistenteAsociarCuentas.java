package ar.com.cognisys.sat.bean.asistente;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.administrador.AdministradorPiletas;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.CuentaABL;
import ar.com.cognisys.sat.core.modelo.comun.natatorios.CuentaPileta;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;

import java.util.List;

public class AsistenteAsociarCuentas {

	public static Cuenta buscarCuenta(Usuario usuario, TiposCuentas tipoCuenta, String padron, String digitoVerificador) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {
			Cuenta cuenta = AdministradorCuenta.buscarCuenta(tipoCuenta, padron);
			
			if (tipoCuenta.sos(TiposCuentas.ABL) && !((CuentaABL) cuenta).tieneDV(digitoVerificador))
				throw new ExcepcionControladaAlerta("No se ha encontrado la cuenta");
			
			else if (usuario.tieneCuenta(cuenta))
				throw new ExcepcionControladaAlerta("La cuenta ya se encuentra asociada");

			return cuenta;
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static Cuenta buscarCuentaPileta(Usuario usuario, String tipoDoc, String nroDoc) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {
			Cuenta cuenta = AdministradorPiletas.buscarCuenta(tipoDoc, nroDoc);

			if (usuario.tieneCuenta(cuenta))
				throw new ExcepcionControladaAlerta("La cuenta ya se encuentra asociada");

			return cuenta;
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static void asociar(Usuario usuario, Cuenta cuenta, String alias) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {			
			if (!usuario.tieneCuenta(cuenta)) {				
				if (!cuenta.esPileta())
					AdministradorCuenta.asociarCuenta(usuario.getIdUsuario(), cuenta.getTipoCuenta(), cuenta.getDatoCuenta(), alias);
				else {
					CuentaPileta cuentaPileta = (CuentaPileta) cuenta;
					AdministradorCuenta.asociarCuentaPileta(usuario.getIdUsuario(), cuentaPileta.getTipoDocumento().getNombrePiletas(), cuentaPileta.getNumeroDocumento().toString(), alias);
				}
				cuenta.setAlias(alias);
				AdministradorCuenta.cargarImporte(cuenta);
				usuario.getCuentas().agregarCuenta(cuenta);
			}
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
	
	public static void asociarRS(Usuario usuario, String alias) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {			
			AdministradorCuenta.asociarCuenta(usuario.getIdUsuario(), TiposCuentas.COMERCIOS, usuario.getCuit(), alias);
			List<Cuenta> cuentas = AdministradorCuenta.recuperarPadronesComercio(usuario.getIdUsuario());

			for (Cuenta cuenta: cuentas) {
				AdministradorCuenta.cargarImporte( cuenta );
				usuario.getCuentas().agregarCuenta( cuenta );
			}
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
}