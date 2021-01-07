package ar.com.cognisys.sat.bean.asistente;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.core.administrador.AdministradorContribuyente;
import ar.com.cognisys.sat.core.administrador.AdministradorUsuario;
import ar.com.cognisys.sat.core.modelo.comun.MD5;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;

public class AsistentePerfil {

	public static void cambiarClave(Usuario usuario, String claveActual, String claveNueva) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		try {
			if (usuario.tieneClave(MD5.getMD5(claveActual)))
				AdministradorContribuyente.cambiarClave(usuario.getCuit(), MD5.getMD5(claveNueva));
			else
				throw new ExcepcionControladaAlerta("La contraseña actual ingresada es incorrecta");
			
			usuario.setClave( MD5.getMD5(claveNueva) );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static void cambiarDatos(String nombre, String apellido, Usuario usuario, String correo, String telefono1, String telefono2) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {
			if (!usuario.getCorreo().equals(correo) && AdministradorUsuario.existeUsuarioPorCorreo(correo))	
				throw new ExcepcionControladaAlerta("El correo cargado ya se encuentra en uso");
			
			AdministradorContribuyente.actualizarDatosUsuario(usuario.getIdUsuario(), correo, telefono1, telefono2, nombre, apellido, 1);
			usuario.setCorreo(correo);
			usuario.setNombre(nombre);
			usuario.setApellido(apellido);
			usuario.setTelefono1(telefono1);
			usuario.setTelefono2(telefono2);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
}