package ar.com.cognisys.sat.bean.privado.subsidio;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteSubsidio;
import ar.com.cognisys.sat.core.modelo.comun.tramiteSubsidio.TramiteSubsidio;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "SubsidioCompleto")
@ViewScoped
public class SubsidioCompletoBean extends SeccionBean {
	
	private static final long serialVersionUID = 5392608503631110572L;
	private TramiteSubsidio tramite;

	@Override
	protected void inicializacion() {}

	@Override
	public void refrescar() throws ExcepcionControladaError {}

	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object o) throws ExcepcionControladaError {
		this.setTramite((TramiteSubsidio) o);
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {}

	public void descargar() {
		// TODO!
	}

	public TramiteSubsidio getTramite() {
		return tramite;
	}

	public void setTramite(TramiteSubsidio tramite) {
		this.tramite = tramite;
	}
}