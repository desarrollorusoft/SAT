package ar.com.cognisys.sat.bean.privado.pago;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ar.com.cognisys.common.jsfbean.abstracto.Bean;

@ManagedBean(name = "PagoTarjetaCredito")
@ViewScoped
public class PagoTarjetaCreditoBean extends Bean {
	
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