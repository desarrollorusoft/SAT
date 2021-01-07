package ar.com.cognisys.sat.excepcion.handler;

import java.io.IOException;
import java.util.Properties;

public class ExcepcionNoControladaProperties {
	private Properties mensajes;

	public ExcepcionNoControladaProperties() {
		try {
			mensajes = new Properties();
			mensajes.load(getClass().getResourceAsStream("excepcionnocontrolada.properties"));
		} catch (IOException e) {
		}
	}

	public String getMensaje(String error) {
		if (mensajes.containsKey(error)) {
			return mensajes.getProperty(error);
		}
		return "Error no esperado";
	}
}
