package ar.com.cognisys.sat.bean.privado.pago;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ar.com.cognisys.common.jsfbean.abstracto.Bean;

@ManagedBean(name = "RedireccionamientoPagos")
@ViewScoped
public class RedireccionamientoPagosBean extends Bean {
	
	private static final long serialVersionUID = 2079126693845670593L;
	private String url;
	private String xml;
	
	@Override
	protected void inicializacion() {
		this.recupearParametros();
	}

	private void recupearParametros() {
		this.setUrl( (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("URL") );
		this.setXml( (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("XML") );
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
}