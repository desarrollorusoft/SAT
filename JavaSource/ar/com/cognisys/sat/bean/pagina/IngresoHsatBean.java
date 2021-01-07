package ar.com.cognisys.sat.bean.pagina;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.net.util.Base64;

import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteIngresoHsatBean;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.modelo.abstracto.Bean;

@ManagedBean
@ViewScoped
public class IngresoHsatBean extends Bean {

	private static final long serialVersionUID = 8836063013357170656L;
	private String mensaje;
	
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;
	
	@Override
	protected void inicializacion() throws ExcepcionControladaAlerta, ExcepcionControladaError  {
		
		this.setMensaje("Recuperando su usuario. Aguarde un momento por favor...");
		
		Usuario usuario = this.recuperarUsuario();
		usuario.setFechaUltimoIngreso(new Date());
		
		this.getSesionBean().setUsuario(usuario);
		this.redireccionamiento(this.redirigir(usuario));
	}

	private Usuario recuperarUsuario() throws ExcepcionControladaError {
		
		Map<String, String> request = null;
		
		try {
			request = (Map<String, String>) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		} catch (Exception e) {
		}
		
		return AsistenteIngresoHsatBean.recuperarUsuario(new String( Base64.decodeBase64(request.get("u").getBytes() ) ) );
	}
	
	public String redirigir(Usuario usuario) {	
		return "/pages/pri/home.xhtml";
	}
	
	private void redireccionamiento(String pagina) throws ExcepcionControladaError {
		
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context.redirect(context.getRequestContextPath() + pagina + "?faces-redirect=true");
		} catch (IOException e) {
			throw new ExcepcionControladaError("No se pudo redireccionar", e);
		}
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