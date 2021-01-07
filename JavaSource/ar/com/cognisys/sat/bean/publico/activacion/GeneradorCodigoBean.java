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
import ar.com.cognisys.sat.core.modelo.comun.comunicacion.EncriptacionSAT;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "GeneradorCodigo")
@ViewScoped
public class GeneradorCodigoBean extends PaginaBean {
	
	private static final long serialVersionUID = -2308965935889573498L;
	private String cuit;
	
	@Override
	protected void inicializacion() {
		this.recuperarDatos();
		this.refrescar();
	}

	@Override
	public void refrescar() {}

	public void generarCodigo() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		validar();
		AsistenteActivacion.generarCodigo(ComunBean.sacarMascaraCuit( this.getCuit() ) );
		RequestContext.getCurrentInstance().execute("PF('modalFin').show();");
	}

	private void validar() throws ExcepcionControladaAlerta {
		if( !AsistenteObjeto.tieneContenido(this.cuit) || !CUIT.validar( ComunBean.sacarMascaraCuit( this.cuit ) ) )
			throw new ExcepcionControladaAlerta("El cuit no es correcto");
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
		Redireccionamiento r = (new Redireccionamiento("/pages/pub/activacion.xhtml"));
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
}