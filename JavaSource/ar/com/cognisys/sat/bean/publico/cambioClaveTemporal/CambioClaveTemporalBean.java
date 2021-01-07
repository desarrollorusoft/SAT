package ar.com.cognisys.sat.bean.publico.cambioClaveTemporal;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaBean;
import ar.com.cognisys.sat.bean.Redireccionamiento;
import ar.com.cognisys.sat.bean.privado.perfil.CambioClaveBean;
import ar.com.cognisys.sat.core.administrador.AdministradorUsuario;
import ar.com.cognisys.sat.core.modelo.comun.comunicacion.EncriptacionSAT;
import ar.com.cognisys.sat.modelo.comun.Mensaje;

@ManagedBean(name = "CambioClaveTemporal")
@ViewScoped
public class CambioClaveTemporalBean extends PaginaBean implements ICambioClaveExterno {
	
	private static final long serialVersionUID = 8194300129804199798L;
	private String mensaje;
	
	@ManagedProperty(value = "#{CambioClave}")
	private CambioClaveBean cambioClaveBean;
	
	@Override
	protected void inicializacion() {
		this.setMensaje("Usted cuenta con una contraseña temporal. Debe cambiar para poder ingresar al sistema.");
		Map<String, String> request = null;
		
		try {
			request = (new Redireccionamiento()).recuperarDatos();
		} catch (Exception e) {}
		
		if (request != null && request.get("ct") != null) {
			String cuit = (new EncriptacionSAT()).desencriptar( request.get("ct") );	
			try {
				this.getCambioClaveBean().cargar( AdministradorUsuario.buscar(cuit), this );
			} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
				Mensaje.emitirMensajeError("No se pudieron recuperar los datos de usuario");
			} catch (ExcepcionControladaError e) {
				Mensaje.emitirMensajeError("No se pudieron recuperar los datos de usuario");
			}
		} else
			Mensaje.emitirMensajeError("No se pudo recuperar el CUIT");
	}
	
	@Override
	public void refrescar() throws ExcepcionControladaError {}

	@Override
	public void claveCambiada() {
		RequestContext.getCurrentInstance().execute( "PF('avisoCambioCorrecto').show();" );
	}
	
	public CambioClaveBean getCambioClaveBean() {
		return cambioClaveBean;
	}

	public void setCambioClaveBean(CambioClaveBean cambioClaveBean) {
		this.cambioClaveBean = cambioClaveBean;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}