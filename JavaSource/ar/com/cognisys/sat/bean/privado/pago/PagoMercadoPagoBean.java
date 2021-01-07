package ar.com.cognisys.sat.bean.privado.pago;

import ar.com.cognisys.common.jsfbean.abstracto.Bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "PagoMercadoPago")
@ViewScoped
public class PagoMercadoPagoBean extends Bean {
	
	private static final long serialVersionUID = -2638182110664588893L;
	private String parametro;
	
	@Override
	protected void inicializacion() {
		this.recuperarParametro();
	}

	private void recuperarParametro() {
		this.setParametro( (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("funcion") );
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
}