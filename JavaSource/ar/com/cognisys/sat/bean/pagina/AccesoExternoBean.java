package ar.com.cognisys.sat.bean.pagina;

import java.io.IOException;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.modelo.abstracto.Bean;
import ar.com.cognisys.sat.core.administrador.AdministradorUsuario;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;

@ManagedBean
@ViewScoped
public class AccesoExternoBean extends Bean {

	private static final long serialVersionUID = 8836063013357170656L;

	private String cuit;
	private String mensaje;
	
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;
	
	@Override
	protected void inicializacion() throws ExcepcionControladaAlerta, ExcepcionControladaError  {
		
		this.setMensaje("Recuperando su usuario. Aguarde un momento por favor...");
		
		Map<String, String> request = null;
		
		try {
			request = (Map<String, String>) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		} catch (Exception e) {}
		
		this.setCuit( validarCuit(request.get("cuit")) );
		
		this.buscarUsuario();
	}
	
	private void buscarUsuario() throws ExcepcionControladaError {
		
		String redireccion = "";
		Usuario usuario = AdministradorUsuario.buscar( this.getCuit() );
		
		if (usuario == null) {
			redireccion = "/pages/inicio/usuario_no_registrado.xhtml";
		} else {
			this.getSesionBean().setUsuario( usuario );
			redireccion = "/pages/comercio/comercio.xhtml";
		}
		
		this.redireccionamiento(redireccion);
	}

	private void redireccionamiento(String pagina) throws ExcepcionControladaError {
		
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context.redirect(context.getRequestContextPath() + pagina + "?faces-redirect=true");
		} catch (IOException e) {
			throw new ExcepcionControladaError("Falló el redireccionamiento", e);
		}
    }
	
	private static String validarCuit(String cuit) throws ExcepcionControladaAlerta {
		
		if (!CUIT.validar(cuit))
			throw new ExcepcionControladaAlerta("El CUIT es incorrecto");
		
		return cuit;
	}
	
	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public SesionBean getSesionBean() {
		return sesionBean;
	}

	public void setSesionBean(SesionBean sesionBean) {
		this.sesionBean = sesionBean;
	}
}