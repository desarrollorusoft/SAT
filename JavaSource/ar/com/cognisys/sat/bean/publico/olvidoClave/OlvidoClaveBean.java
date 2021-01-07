package ar.com.cognisys.sat.bean.publico.olvidoClave;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaBean;
import ar.com.cognisys.generico.modelo.comun.AsistenteObjeto;
import ar.com.cognisys.sat.bean.ComunBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteRecuperarClave;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.enums.TiposDocumento;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;
import ar.com.cognisys.sat.core.modelo.validador.ValidadorContribuyente;
import ar.com.cognisys.sat.excepcion.Mensaje;

@ManagedBean(name = "OlvidoClave")
@ViewScoped
public class OlvidoClaveBean extends PaginaBean {
	
	private static final long serialVersionUID = 6553737261081739709L;
	private String cuit;
	private boolean exito;
	
	@Override
	protected void inicializacion() {
		this.refrescar();
		this.setExito(false);
	}
	
	@Override
	public void refrescar() {}
	
	public void recuperarClave() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		validar();

		AsistenteRecuperarClave.generarNueva( CUIT.quitarMascara(this.getCuit()) );

		Mensaje.addMessageAviso("Se le ha enviado un correo con la nueva contraseña. Por favor revise su cuenta de mail registrada.");
	}
	
	private void validar() throws ExcepcionControladaAlerta {
		if( !AsistenteObjeto.tieneContenido( this.getCuit() ) || !CUIT.validar( ComunBean.sacarMascaraCuit(this.getCuit()) ) )
			throw new ExcepcionControladaAlerta("CUIT/CUIT incorrecto");
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public boolean isExito() {
		return exito;
	}

	public void setExito(boolean exito) {
		this.exito = exito;
	}
}