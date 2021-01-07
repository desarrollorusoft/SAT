package ar.com.cognisys.sat.bean.publico.inconsistenciaCorreo;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import ar.com.cognisys.common.exception.ExcepcionControladaAlerta;
import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaBean;
import ar.com.cognisys.generico.modelo.comun.AsistenteObjeto;
import ar.com.cognisys.sat.bean.Redireccionamiento;
import ar.com.cognisys.sat.bean.asistente.AsistenteInconsistenciaCorreo;
import ar.com.cognisys.sat.core.modelo.comun.comunicacion.EncriptacionSAT;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;

@ManagedBean(name = "InconsistenciaCorreo")
@ViewScoped
public class InconsistenciaCorreoBean extends PaginaBean {
	
	private static final long serialVersionUID = -6671753371160972978L;
	private String cuit;
	private String correo;
	private String correoReingreso;
	
	@Override
	protected void inicializacion() {
		this.recuperarDatos();
		this.refrescar();
	}
	
	@Override
	public void refrescar() {}
	
	public void cargar() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		validar();
		AsistenteInconsistenciaCorreo.asignar(this.getCuit(), this.getCorreo());
		RequestContext.getCurrentInstance().execute("PF('modalFin').show();");
	}

	private void validar() throws ExcepcionControladaAlerta {
		if( !AsistenteObjeto.tieneContenido(this.getCorreo()) || !AsistenteObjeto.tieneContenido(this.getCorreoReingreso()) ) 
			throw new ExcepcionControladaAlerta("Los datos se encuentran incompletos");
		if( !this.getCorreo().equals(this.getCorreoReingreso()) )
			throw new ExcepcionControladaAlerta("Los correos no coinciden");
		if( !AsistenteObjeto.esCorreo( this.getCorreo() ) )
			throw new ExcepcionControladaAlerta("El correo ingresado es incorrecto");
	}
	
	private void recuperarDatos() {
		Map<String, String> request = null;
		
		try {
			request = (new Redireccionamiento()).recuperarDatos();
		} catch (Exception e) {}
		
		if (request != null && request.get("ct") != null)
			this.setCuit( (new EncriptacionSAT()).desencriptar( request.get("ct") ) );	
	}

	public void redireccionar() throws ExcepcionControladaError {
		Redireccionamiento r = (new Redireccionamiento("/pages/pub/activacion.xhtml"));
		r.agregarParametro("ct", (new EncriptacionSAT()).encriptar( CUIT.quitarMascara(cuit) ));
		try {
			r.redireccionar();
		} catch (ar.com.cognisys.common.exception.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
	
	public boolean tieneCuit() {
		return AsistenteObjeto.tieneContenido( this.getCuit() );
	}
	
	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		if (correo != null)
			correo = correo.toLowerCase();
		this.correo = correo;
	}

	public String getCorreoReingreso() {
		return correoReingreso;
	}

	public void setCorreoReingreso(String correoReingreso) {
		if (correoReingreso != null)
			correoReingreso = correoReingreso.toLowerCase();
		this.correoReingreso = correoReingreso;
	}
}