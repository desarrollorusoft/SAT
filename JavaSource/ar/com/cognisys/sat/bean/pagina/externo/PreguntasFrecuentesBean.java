package ar.com.cognisys.sat.bean.pagina.externo;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.sat.bean.asistente.AsistentePreguntasFrecuentesBean;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.modelo.abstracto.Bean;
import ar.com.cognisys.sat.core.modelo.comun.configuraciones.Grupo;

@ManagedBean
@ViewScoped
public class PreguntasFrecuentesBean extends Bean {
	
	private static final long serialVersionUID = -5550674518358384724L;
	private List<Grupo> listaGrupos;
	
	@Override
	protected void inicializacion() throws ExcepcionControladaError {
		this.setListaGrupos(AsistentePreguntasFrecuentesBean.recuperarListaGrupos());
	}

	public List<Grupo> getListaGrupos() {
		return listaGrupos;
	}

	public void setListaGrupos(List<Grupo> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}
}