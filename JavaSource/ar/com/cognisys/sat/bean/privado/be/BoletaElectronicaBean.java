package ar.com.cognisys.sat.bean.privado.be;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaBean;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteBoletaElectronica;

@ManagedBean(name = "BoletaElectronica")
@ViewScoped
public class BoletaElectronicaBean extends PaginaBean {
	
	private static final long serialVersionUID = 531083491059282043L;
	private boolean habilitado;
	
	@Override
	protected void inicializacion() {
		this.setHabilitado( AsistenteBoletaElectronica.estaHabilitado( SesionBean.getUsuarioLogueado() ) );
	}
	
	@Override
	public void refrescar() throws ExcepcionControladaError {}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}
}