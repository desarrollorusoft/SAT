package ar.com.cognisys.sat.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.modelo.abstracto.Bean;
import ar.com.cognisys.sat.modelo.administrador.Navegacion;

@ManagedBean
@SessionScoped
public class SesionBean extends Bean {

	private static final long serialVersionUID = 601872483144429627L;
	private Usuario usuario;
	
	@Override
	protected void inicializacion() {}
	
	public static Usuario getUsuarioLogueado() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().evaluateExpressionGet(context, "#{sesionBean.usuario}", Usuario.class);
	}
	
	public void cerrarSesion() {
		this.setUsuario(null);
	}
	
	public String cerrarSesionContribuyente() {
		this.cerrarSesion();
		return Navegacion.inicio.toString();
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}