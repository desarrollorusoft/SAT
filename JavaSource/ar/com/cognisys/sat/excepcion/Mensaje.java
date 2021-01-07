package ar.com.cognisys.sat.excepcion;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Mensaje implements Serializable {
	
	private static final long serialVersionUID = -4205665289619922583L;

	public static void addMessageError(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				mensaje, null);
		FacesContext.getCurrentInstance().addMessage("", message);
	}
	
	public static void addMessageAviso(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				mensaje, null);
		FacesContext.getCurrentInstance().addMessage("", message);
	}

	public static void addMessageWarn(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
				mensaje, null);
		FacesContext.getCurrentInstance().addMessage("", message);
	}
}
