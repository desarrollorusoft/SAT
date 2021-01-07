package ar.com.cognisys.sat.bean.privado.rs.declaracion;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaAlerta;
import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionWizardBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteDDJJPadron;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.DDJJSV;
import ar.com.cognisys.sat.core.modelo.comun.rs.PadronRS;
import ar.com.cognisys.sat.core.modelo.comun.rs.versiones.VersionPadronRS;
import ar.com.cognisys.sat.excepcion.Mensaje;

@ManagedBean(name = "ServiciosVarios")
@ViewScoped
public class ServiciosVariosBean extends SeccionWizardBean {
	
	private static final long serialVersionUID = -8320913849340911566L;
	private PadronRS padron;
	private VersionPadronRS versionEnCurso;
	private DDJJSV serviciosVarios;
	private boolean poseeSV;
	
	@Override
	public void refrescar() throws ExcepcionControladaError {}

	@Override
	protected void inicializacion() {
		this.poseeSV = true;
	}
	
	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object padron) throws ExcepcionControladaError {
		this.setPadron( (PadronRS) padron );
		this.setVersionEnCurso( this.getPadron().getVersionEnCurso() );
		this.setServiciosVarios( AsistenteDDJJPadron.obtenerServiciosVarios( this.getVersionEnCurso() ) );
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {
		try {
			validar();
			AsistenteDDJJPadron.actualizarSV( this.getVersionEnCurso(), this.getServiciosVarios() );
			this.irAlSiguiente( this.getPadron() );
		} catch (ExcepcionControladaAlerta e) {
			Mensaje.addMessageWarn(e.getMessage());
		}
	}
	
	private void validar() throws ExcepcionControladaAlerta {
		try {
			AsistenteDDJJPadron.validarSV( this.getVersionEnCurso(), this.poseeSV );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta e) {
			throw new ExcepcionControladaAlerta(e.getMessage());
		}
	}
	
	public void actualizarPanel() {
		if ( !poseeSV ) {
			this.getServiciosVarios().setCalderas(0);
			this.getServiciosVarios().setMotores(0);
		}
	}

	public PadronRS getPadron() {
		return padron;
	}

	public void setPadron(PadronRS padron) {
		this.padron = padron;
	}

	public boolean isPoseeSV() {
		return poseeSV;
	}

	public void setPoseeSV(boolean poseeSV) {
		this.poseeSV = poseeSV;
	}

	public VersionPadronRS getVersionEnCurso() {
		return versionEnCurso;
	}

	public void setVersionEnCurso(VersionPadronRS versionEnCurso) {
		this.versionEnCurso = versionEnCurso;
	}

	public DDJJSV getServiciosVarios() {
		return serviciosVarios;
	}

	public void setServiciosVarios(DDJJSV serviciosVarios) {
		this.serviciosVarios = serviciosVarios;
	}
}