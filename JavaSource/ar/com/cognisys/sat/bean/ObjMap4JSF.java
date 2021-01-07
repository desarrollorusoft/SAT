package ar.com.cognisys.sat.bean;

import java.io.Serializable;

public class ObjMap4JSF implements Serializable {
	
	private static final long serialVersionUID = 1221691052585475635L;
	private String clave;
	private String valor;
	
	public ObjMap4JSF(String clave, String valor) {
		this.setClave(clave);
		this.setValor(valor);
	}

	public String getClave() {
		return clave;
	}
	
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
}