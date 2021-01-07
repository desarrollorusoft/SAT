package ar.com.cognisys.sat.bean.asistente;

import java.util.ArrayList;
import java.util.List;

import ar.com.cognisys.sat.bean.publico.ingreso.LoginTracer;
import ar.com.cognisys.sat.core.modelo.comun.MD5;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Perfil;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Permiso;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.modelo.factory.usuario.FactoryPerfil;

public class AsistenteIngresoHsatBean {

	public static Usuario recuperarUsuario(String cuit) throws ExcepcionControladaError {
		try {
			Usuario usuario = AsistenteLogin.ingreso(cuit, MD5.getMD5("C0gn1sys4n4"), new LoginTracer());
			usuario.setListaPerfiles( generarPefilSoloLectura() );
			return usuario;
		} catch (Exception e) {
			throw new ExcepcionControladaError("No se pudo recuperar el usuario", e);
		}
	}

	private static List<Perfil> generarPefilSoloLectura() {
		List<Perfil> lista = new ArrayList<>();
		lista.add( FactoryPerfil.generarIntanciaCompleta("SOLO_LECTURA", new ArrayList<Permiso>()) );
		return lista;
	}
}