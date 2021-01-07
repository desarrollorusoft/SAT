package ar.com.cognisys.sat.bean.pagina;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ar.com.cognisys.sat.modelo.abstracto.Bean;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;

@ManagedBean
@ViewScoped
public class LinkPagosBean extends Bean {

	private static final long serialVersionUID = -354700451906141601L;
	private String funcion;

	@Override
	protected void inicializacion() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		funcion = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("funcion");
	}
	
	public String recuperarLlamado() throws ExcepcionControladaError {
		return funcion;
	}
}