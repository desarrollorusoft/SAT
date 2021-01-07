package ar.com.cognisys.sat.bean.pagina.externo;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.StreamedContent;

import ar.com.cognisys.sat.bean.asistente.AsistenteGuiasTramiteBean;
import ar.com.cognisys.sat.modelo.abstracto.Bean;
import ar.com.cognisys.sat.core.modelo.comun.configuraciones.Grupo;
import ar.com.cognisys.sat.core.modelo.comun.configuraciones.Item;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;

@ManagedBean
@ViewScoped
public class GuiasTramiteBean extends Bean {
	
	private static final long serialVersionUID = -1281467418380825940L;
	private List<Grupo> listaGrupos; 
	
	@Override
	protected void inicializacion() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		this.setListaGrupos( AsistenteGuiasTramiteBean.recuperarGrupos() );
	}

	public StreamedContent descargarArchivoItem(Item item) {
		// TODO IMPLEMENTAR!
		return null;
	}
	
	public StreamedContent descargarArchivoGrupo(Grupo grupo) {
		// TODO IMPLEMENTAR!
		return null;
	}
	
	public List<Grupo> getListaGrupos() {
		return listaGrupos;
	}

	public void setListaGrupos(List<Grupo> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}
}