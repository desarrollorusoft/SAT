package ar.com.cognisys.sat.bean.asistente;

import java.util.Date;
import java.util.List;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.core.administrador.AdministradorComercio;
import ar.com.cognisys.sat.core.administrador.AdministradorContribuyente;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.administrador.AdministradorPiletas;
import ar.com.cognisys.sat.core.administrador.AdministradorRegimenSimplificado;
import ar.com.cognisys.sat.core.administrador.AdministradorUsuario;
import ar.com.cognisys.sat.core.contenedor.ContenedorCaracterSAT;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.MD5;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.CuentaABL;
import ar.com.cognisys.sat.core.modelo.comun.rs.Comercio;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.CaracterSAT;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.InvalidaDatoCuentaException;
import ar.com.cognisys.sat.core.modelo.factory.cuenta.FactoryCuentasUsuario;
import ar.com.cognisys.sat.core.modelo.factory.cuenta.FactoryUsuario;
import ar.com.cognisys.sat.core.modelo.factory.rs.FactorySolicitanteRS;

public class AsistenteRegistracion {

	public static void aceptoTyC(Usuario usuario) {
		usuario.setAceptoTyC(true);
	}
	
	public static void existeCuit(String cuit) throws ExcepcionControladaAlerta {
		try {
			if ( AdministradorUsuario.existeUsuario(cuit) )
				throw new ExcepcionControladaAlerta("El CUIL/CUIT ingresado ya se encuentra registrado");
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {}
	}

	public static Cuenta buscarCuenta(TiposCuentas tipoCuenta, String padron, String digitoVerificador) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		try {
			Cuenta cuenta = AdministradorCuenta.buscarCuenta(tipoCuenta, padron);

			if (cuenta.getTipoCuenta().sos(TiposCuentas.ABL) && !((CuentaABL) cuenta).tieneDV(digitoVerificador) )
				throw new ExcepcionControladaAlerta("No se ha encontrado una cuenta con esos datos");

			return cuenta;
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
	
	public static Cuenta buscarCuenta(TiposCuentas tipoCuenta, String padron) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		try {
			return AdministradorCuenta.buscarCuenta(tipoCuenta, padron);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static Cuenta buscarCuentaPileta(String tipoDocumento, String nroDocumento) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		try {
			return AdministradorPiletas.buscarCuenta(tipoDocumento, nroDocumento);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static Usuario generarUsuario(String cuit, String correo, String clave, String claveRepetida) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		try {
			Usuario usuario = FactoryUsuario.generar(null, cuit, correo, "", "", true, false, new Date());
			usuario.setClave( MD5.getMD5(clave) );
			usuario.setCuentas( FactoryCuentasUsuario.generar( AdministradorContribuyente.buscarCuentasPorCuit( cuit ) ) );
			try {
				usuario.setComercio( AdministradorComercio.recuperarComercioBase( cuit ) );
			} catch (InvalidaDatoCuentaException a) { /*No pasa nada si no aparece*/ }
			return usuario;
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e);
		}
	}
	
	public static void enviarMailActivacion(Usuario usuario) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {
			AdministradorContribuyente.solicitarCodigoActivacion(usuario.getCuit());
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static void activar(Usuario usuario, String codigo) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		try {
			AdministradorContribuyente.activacionMobile(usuario.getCuit(), codigo);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static void registrar(Usuario usuario) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {
			if (usuario.tieneRSDatos())
				AdministradorComercio.registrarRS( usuario );
			else
				AdministradorUsuario.registrar( usuario.getCuit(), 
												usuario.getCorreo(), 
												usuario.getClave(),
												usuario.getCuentas(),
												usuario.getComercio() );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static Usuario generarUsuarioRS(String cuit, String correoAct, String telefonoAct, String celularAct, String clave, 
										   String claveRepetida, String correoSol, String telefonoSol, String celularSol, 
										   String nombre, String apellido, String caracterSeleccionado, String cuitSol) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		
		if ( clave == null || claveRepetida == null || !clave.equals(claveRepetida) )
			throw new ExcepcionControladaAlerta("Las contraseñas ingresadas son incorrectas");
		
		try {
			boolean aplicaRS = AdministradorRegimenSimplificado.permitoRegimen( cuit );

			if (!aplicaRS)
				throw new ExcepcionControladaAlerta("Usted no aplica al Régimen Simplificado.\n\nPara completar su declaración jurada por favor dirijase al sitio de Clave Fiscal.", null);
			
			Comercio comercio = AdministradorComercio.recuperarSimple( cuit );
			CaracterSAT caracter = ContenedorCaracterSAT.getInstancia().buscar(caracterSeleccionado);
			comercio.setSolicitanteRS( FactorySolicitanteRS.generar(cuitSol, nombre, apellido, caracter, correoSol, telefonoSol, celularSol) );
			
			Usuario usuario = FactoryUsuario.generar(null, cuit, correoAct, telefonoAct, celularAct, true, false, new Date());
			usuario.setClave( MD5.getMD5(clave) );
			usuario.setComercio( comercio );
			
			return usuario;
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
}