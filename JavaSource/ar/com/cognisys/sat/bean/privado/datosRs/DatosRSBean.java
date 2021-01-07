package ar.com.cognisys.sat.bean.privado.datosRs;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.jsfbean.abstracto.PaginaBean;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.RSDatos;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;

@ManagedBean(name = "DatosRS")
@ViewScoped
public class DatosRSBean extends PaginaBean {
	
	private static final long serialVersionUID = -2802679879346620864L;
	private RSDatos datos;
	
	@Override
	protected void inicializacion() {
		this.refrescar();
	}

	@Override
	public void refrescar() {
//		this.setDatos( SesionBean.getUsuarioLogueado().getRsDatos() );
	}
	
	public String getCuit() {
		return CUIT.ponerMascara( this.getDatos().getActividad().getCuit() );
	}
	
	public String getCorreoActividad() {
		return this.getDatos().getActividad().getCorreo();
	}
	
	public String getTelefonoActividad() {
		return this.getDatos().getActividad().getTelefono();
	}
	
	public String getCelularActividad() {
		return this.getDatos().getActividad().getCelular();
	}
	
	public String getNombreSolicitante() {
		return this.getDatos().getSolicitante().getNombre();
	}
	
	public String getApellidoSolicitante() {
		return this.getDatos().getSolicitante().getApellido();
	}
	
	public String getCaracterSolicitante() {
		return this.getDatos().getSolicitante().getCaracter().getNombre();
	}
	
	public String getCorreoSolicitante() {
		return this.getDatos().getSolicitante().getCorreo();
	}
	
	public String getTelefonoSolicitante() {
		return this.getDatos().getSolicitante().getTelefono();
	}
	
	public String getCelularSolicitante() {
		return this.getDatos().getSolicitante().getCelular();
	}
	
	public RSDatos getDatos() {
		return datos;
	}

	public void setDatos(RSDatos datos) {
		this.datos = datos;
	}
}