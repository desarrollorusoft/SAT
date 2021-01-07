package ar.com.cognisys.sat.bean.privado.rs.declaracion;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionGeneralBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteRegimenSimplificado;
import ar.com.cognisys.sat.core.modelo.comun.rs.PadronRS;
import ar.com.cognisys.sat.modelo.comun.Mensaje;

@ManagedBean(name = "TileResumenRS")
@ViewScoped
public class TileResumenRSBean extends SeccionGeneralBean {
	
	private static final long serialVersionUID = 7902435877388791350L;
	private List<ResumenView> listaResumenes;
	private PadronRS padron;
	private boolean visible;
	
	@Override
	protected void inicializacion() {
		try {
			this.refrescar();
		} catch (ExcepcionControladaError e) {
			Mensaje.emitirMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object padron) throws ExcepcionControladaError {
		this.setPadron((PadronRS) padron);
		this.refrescar();
		this.getListaResumenes().addAll( AsistenteRegimenSimplificado.generarResumenes( this.getPadron() ) );
		
		this.setVisible(true);
	}

	@Override
	public void refrescar() throws ExcepcionControladaError {
		this.setListaResumenes(new ArrayList<ResumenView>());
		this.setVisible(false);
	}
	
	@Override
	public void siguiente() throws ExcepcionControladaError {}

	public List<ResumenView> getListaResumenes() {
		return listaResumenes;
	}

	public void setListaResumenes(List<ResumenView> listaResumenes) {
		this.listaResumenes = listaResumenes;
	}

	public PadronRS getPadron() {
		return padron;
	}

	public void setPadron(PadronRS padron) {
		this.padron = padron;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}