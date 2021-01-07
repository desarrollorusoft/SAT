package ar.com.cognisys.sat.bean.asistente;

import java.util.List;

import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.administrador.AdministradorConfiguracionSatPreguntasFrecuentes;
import ar.com.cognisys.sat.core.modelo.comun.configuraciones.Grupo;

public class AsistentePreguntasFrecuentesBean {

	public static List<Grupo> recuperarListaGrupos() throws ExcepcionControladaError {
		
		try {
			return AdministradorConfiguracionSatPreguntasFrecuentes.recuperarGrupoDisp().getListaHijos();
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}	
}