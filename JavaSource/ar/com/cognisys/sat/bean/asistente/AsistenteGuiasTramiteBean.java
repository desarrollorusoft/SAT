package ar.com.cognisys.sat.bean.asistente;

import java.util.List;

import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.administrador.AdministradorConfiguracionSatGuiaTramites;
import ar.com.cognisys.sat.core.modelo.comun.configuraciones.Grupo;

public class AsistenteGuiasTramiteBean {

	public static List<Grupo> recuperarGrupos() throws ExcepcionControladaError {
		
		try {
			return AdministradorConfiguracionSatGuiaTramites.recuperarPantalla().getListaGrupos();
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
}