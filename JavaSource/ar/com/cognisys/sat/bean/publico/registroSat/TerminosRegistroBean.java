package ar.com.cognisys.sat.bean.publico.registroSat;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionWizardBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteRegistracion;
import ar.com.cognisys.sat.excepcion.Mensaje;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;

@ManagedBean(name = "TerminosRegistro")
@ViewScoped
public class TerminosRegistroBean extends SeccionWizardBean {
	
	private static final long serialVersionUID = -1112484797177305845L;
	private Usuario usuario;
	private boolean seleccionado;
	
	@Override
	protected void inicializacion() {}
	
	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object dato) throws ExcepcionControladaError {
		this.setUsuario((Usuario) dato);
		this.refrescar();
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {
		try {
			AsistenteRegistracion.registrar(this.getUsuario());
		} catch (ExcepcionControladaAlerta e) {
			Mensaje.addMessageWarn(e.getMessage());
		}
		this.irAlSiguiente(this.getUsuario());
	}

	@Override
	public void refrescar() throws ExcepcionControladaError {}
	
	public void aceptar() throws ExcepcionControladaError {
		AsistenteRegistracion.aceptoTyC(this.getUsuario());
		this.siguiente();
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}