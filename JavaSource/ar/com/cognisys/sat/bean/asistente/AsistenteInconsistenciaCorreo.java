package ar.com.cognisys.sat.bean.asistente;

import ar.com.cognisys.common.exception.ExcepcionControladaAlerta;
import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.core.administrador.AdministradorUsuario;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;

public class AsistenteInconsistenciaCorreo {

	public static void asignar(String cuit, String correo) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		
		try {
			if (AdministradorUsuario.existeUsuarioPorCorreo(correo))
				throw new ExcepcionControladaAlerta("El correo ya se encuentra en uso");
			
			Usuario usuario = AdministradorUsuario.buscar(cuit);
			
			if (usuario == null)
				throw new ExcepcionControladaAlerta("El usuario no existe");
			
			AdministradorUsuario.actualizarCorreo(usuario.getIdUsuario(), correo);	
			
			AsistenteActivacion.generarCodigo(cuit);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta e) {
			throw new ExcepcionControladaAlerta(e.getMessage());
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
}