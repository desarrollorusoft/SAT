package ar.com.cognisys.sat.bean.privado.rs.declaracion;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionWizardBean;
import ar.com.cognisys.sat.bean.Redireccionamiento;
import ar.com.cognisys.sat.bean.asistente.AsistenteDDJJPadron;
import ar.com.cognisys.sat.core.modelo.comun.rs.PadronRS;
import ar.com.cognisys.sat.core.modelo.comun.rs.versiones.VersionPadronRS;
import ar.com.cognisys.sat.v2.vista.modelo.v2.view.rs.ConflictosDDJJRS;

@ManagedBean(name = "ResumenRS")
@ViewScoped
public class ResumenRSBean extends SeccionWizardBean {
	
	private static final long serialVersionUID = 7902435877388791350L;
	private PadronRS padron;
	private VersionPadronRS versionEnCurso;
	private ConflictosDDJJRS[] errores;
	
	@ManagedProperty( value="#{TileResumenRS}")
	private TileResumenRSBean tileResumenRsBean;
	
	@Override
	protected void inicializacion() {}
	
	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object dato) throws ExcepcionControladaError {
		this.setPadron( (PadronRS) dato );
		this.setVersionEnCurso( this.getPadron().getVersionEnCurso() );
		this.getTileResumenRsBean().cargar( this.getPadron() );
		this.refrescar();
	}

	@Override
	public void refrescar() throws ExcepcionControladaError {}
	
	@Override
	public void siguiente() throws ExcepcionControladaError {}
	
	public void aceptar() throws ExcepcionControladaError {
		this.errores = AsistenteDDJJPadron.vaidarVersion( this.getVersionEnCurso() );
		
		if (this.errores.length == 0) {
			try {
				AsistenteDDJJPadron.completarPadron( this.getPadron() );
			} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
				throw new ExcepcionControladaError(e);
			}
			redireccionar("/pages/pri/rs.xhtml");
		} else 
			mostrarModalError();
	}

	private void mostrarModalError() {
		RequestContext.getCurrentInstance().execute("PF('ModalErroresPadron').show()");
		RequestContext.getCurrentInstance().update("form");
	}

	private void redireccionar(String ruta) throws ExcepcionControladaError {
		(new Redireccionamiento(ruta)).redireccionar();
	}

	public void rechazarPadron() throws ExcepcionControladaError {
		try {
			AsistenteDDJJPadron.registrarRechazo( this.getPadron(), this.getErrores() );
			redireccionar("/pages/pri/home.xhtml");
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e);
		}
	}
	
	public PadronRS getPadron() {
		return padron;
	}

	public void setPadron(PadronRS padron) {
		this.padron = padron;
	}

	public VersionPadronRS getVersionEnCurso() {
		return versionEnCurso;
	}

	public void setVersionEnCurso(VersionPadronRS versionEnCurso) {
		this.versionEnCurso = versionEnCurso;
	}

	public TileResumenRSBean getTileResumenRsBean() {
		return tileResumenRsBean;
	}

	public void setTileResumenRsBean(TileResumenRSBean tileResumenRsBean) {
		this.tileResumenRsBean = tileResumenRsBean;
	}

	public ConflictosDDJJRS[] getErrores() {
		return errores;
	}

	public void setErrores(ConflictosDDJJRS[] errores) {
		this.errores = errores;
	}
}