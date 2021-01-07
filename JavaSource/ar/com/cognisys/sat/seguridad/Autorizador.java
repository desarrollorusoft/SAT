package ar.com.cognisys.sat.seguridad;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.modelo.parametrizacion.Parametrizacion;
import ar.com.cognisys.sat.modelo.parametrizacion.ParametrizacionArbol;
import ar.com.cognisys.sat.modelo.parametrizacion.ParametrizacionPagina;
import ar.com.cognisys.sat.modelo.parametrizacion.ParametrizacionPerfil;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Perfil;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Permiso;

@ManagedBean
@SessionScoped
public class Autorizador implements Serializable {

	private static final long serialVersionUID = 1738909587760422416L;
	
	public static boolean tienePermiso(String permiso) {
		
		boolean respuesta = false;
		
		if (hayUsuarioLoguedo()) {
			return false;
		} else {
			for (Perfil perfil : SesionBean.getUsuarioLogueado().getListaPerfiles()) {
				for (Permiso permisoActual : perfil.getListaPermisos()) {
					if (permisoActual.getNombre().equals(permiso)) {
						respuesta = true;
					}
				}
			}
		}
		
		return respuesta;
	}
	
	public static boolean tienePerfil(String perfil) {
		
		boolean respuesta = false;
		
		if (hayUsuarioLoguedo()) {
			return false;
		} else {
			for (Perfil p : SesionBean.getUsuarioLogueado().getListaPerfiles()) {
				if (p.getNombre().equals(perfil)) {
					return true;
				}
			}
		}

		return respuesta;
	}
	
	public static boolean tieneAcceso(String paginaDeAcceso) {
		
		return tieneAlgunPerfil( buscarPerfilesParaLaPagina(paginaDeAcceso, (new Parametrizacion()).getArbol()) );
	}
	
	private static List<ParametrizacionPerfil> buscarPerfilesParaLaPagina(String paginaDeAcceso, ParametrizacionArbol arbol) {

		for (ParametrizacionPagina pagina : arbol.getListaPaginas()) {
			
			if (pagina.getNombre().equals(paginaDeAcceso)) {
				
				return pagina.getListaPerfiles();
			}
		}
		
		return null;
	}

	private static boolean tieneAlgunPerfil(List<ParametrizacionPerfil> listaPerfiles) {
		
		for (ParametrizacionPerfil pp : listaPerfiles) {
			
			if (tienePerfil(pp.getNombre())) {
				return true;
			}
		}
		
		return false;
	}

	private static boolean hayUsuarioLoguedo() {
		return (SesionBean.getUsuarioLogueado() == null);
	}
}