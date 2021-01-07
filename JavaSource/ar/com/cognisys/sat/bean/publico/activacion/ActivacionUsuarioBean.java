package ar.com.cognisys.sat.bean.publico.activacion;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaBean;
import ar.com.cognisys.generico.modelo.comun.AsistenteObjeto;
import ar.com.cognisys.sat.bean.ComunBean;
import ar.com.cognisys.sat.bean.Redireccionamiento;
import ar.com.cognisys.sat.bean.asistente.AsistenteActivacion;
import ar.com.cognisys.sat.core.administrador.AdministradorUsuario;
import ar.com.cognisys.sat.core.modelo.comun.comunicacion.EncriptacionSAT;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;
import ar.com.cognisys.sat.modelo.comun.Mensaje;

import org.primefaces.context.RequestContext;

@ManagedBean(name = "ActivacionUsuario")
@ViewScoped
public class ActivacionUsuarioBean extends PaginaBean {
	
	private static final long serialVersionUID = -6671753371160972978L;
	private String cuit;
	private String codigo;
	
	@Override
	protected void inicializacion() {
		this.recuperarDatos();
		this.refrescar();
	}
	
	@Override
	public void refrescar() {}
	
	public void activar() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		validar();
		AsistenteActivacion.activar(ComunBean.sacarMascaraCuit(this.getCuit()), this.getCodigo());
		RequestContext.getCurrentInstance().execute("PF('modalFin').show();");
	}
	
	private void validar() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		if( !AsistenteObjeto.tieneContenido(this.cuit) || !CUIT.validar( ComunBean.sacarMascaraCuit( this.cuit ) ) )
			throw new ExcepcionControladaAlerta("El cuit no es correcto");
		if( !AsistenteObjeto.tieneContenido( this.codigo ) || this.codigo.length() < 5 )
			throw new ExcepcionControladaAlerta("El codigo no es correcto");
	
		this.validarCuit();
	}
	
	public void validarCuit() throws ExcepcionControladaAlerta, ExcepcionControladaError {	
		try {
			if (!AdministradorUsuario.existeUsuario( CUIT.quitarMascara(cuit) ))
				throw new ExcepcionControladaAlerta("La CUIL/CUIT ingresada no se encuentra registrada");
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e);
		}
		
		Mensaje.emitirMensajeAviso("El CUIT es correcto");
	}
	
	private void recuperarDatos() {
		Map<String, String> request = null;
		
		try {
			request = (new Redireccionamiento()).recuperarDatos();;
		} catch (Exception e) {}
		
		if (request != null && request.get("ct") != null)
			this.setCuit( (new EncriptacionSAT()).desencriptar( request.get("ct") ) );	
	}
	
	public void redireccionar() throws ExcepcionControladaError {
		Redireccionamiento r = (new Redireccionamiento("/pages/pub/generar_codigo.xhtml"));
		r.agregarParametro("ct", (new EncriptacionSAT()).encriptar( CUIT.quitarMascara(cuit) ));
		try {
			r.redireccionar();
		} catch (ar.com.cognisys.common.exception.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}