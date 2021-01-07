package ar.com.cognisys.sat.bean.publico.olvidoClave;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.jsfbean.abstracto.PaginaBean;
import ar.com.cognisys.generico.modelo.comun.AsistenteObjeto;
import ar.com.cognisys.sat.bean.ComunBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteRecuperarClaveRS;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;
import ar.com.cognisys.sat.excepcion.Mensaje;
import ar.com.cognisys.sat.modelo.administrador.Navegacion;

@ManagedBean(name = "OlvidoClaveRS")
@ViewScoped
public class OlvidoClaveRSBean extends PaginaBean {
	
	private static final long serialVersionUID = 6553737261081739709L;
	private String cuit;
	
	@Override
	protected void inicializacion() {
		this.refrescar();
	}
	
	@Override
	public void refrescar() {}
	
	public void recuperarClave() throws ExcepcionControladaAlerta {
		validar();
		AsistenteRecuperarClaveRS.generarNueva( ComunBean.sacarMascaraCuit( this.getCuit() ));
		Mensaje.addMessageAviso("Se le ha enviado un correo con la nueva contraseña. Por favor revise su cuenta de mail.");
	}
	
	private void validar() throws ExcepcionControladaAlerta {
		if( !AsistenteObjeto.tieneContenido(this.cuit) || !CUIT.validar( ComunBean.sacarMascaraCuit( this.cuit ) ) )
			throw new ExcepcionControladaAlerta("El cuit no es correcto");
	}
	
	public String volverAlLogin(){
		return Navegacion.inicio_rs.name();
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
}