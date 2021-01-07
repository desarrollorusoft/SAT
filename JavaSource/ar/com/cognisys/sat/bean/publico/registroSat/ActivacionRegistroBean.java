package ar.com.cognisys.sat.bean.publico.registroSat;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionWizardBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteRegistracion;
import ar.com.cognisys.sat.core.modelo.comun.comunicacion.EncriptacionSAT;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;

@ManagedBean(name = "ActivacionRegistro")
@ViewScoped
public class ActivacionRegistroBean extends SeccionWizardBean {
	
	private static final long serialVersionUID = 8339093289662486518L;
	private Usuario usuario;
	private String codigo;
	
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
	public void refrescar() throws ExcepcionControladaError {}
	
	@Override
	public void siguiente() throws ExcepcionControladaError {}

	public void activar() throws ExcepcionControladaError {
		
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			String mensaje = (new EncriptacionSAT()).encriptar( this.getUsuario().getCuit() );
			
			context.redirect(context.getRequestContextPath() + "/pages/pub/activacion.xhtml?ct="+mensaje);
		} catch (IOException e) {
			throw new ExcepcionControladaError("Falló el redireccionamiento", e);
		}
	}
	
	public void renviarCondigo() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		AsistenteRegistracion.enviarMailActivacion(this.getUsuario());
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}