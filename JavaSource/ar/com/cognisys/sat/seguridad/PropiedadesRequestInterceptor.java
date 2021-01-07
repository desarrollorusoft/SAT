package ar.com.cognisys.sat.seguridad;

import java.io.IOException;
import java.util.Properties;

public class PropiedadesRequestInterceptor {

	private Properties propiedades;
	
	public PropiedadesRequestInterceptor() {
		try {
			// se crea una instancia a la clase Properties
			propiedades = new Properties();
			
			// se leen el archivo .properties
			propiedades.load(getClass().getResourceAsStream("requestInterceptor.properties"));
		} catch (IOException ex) {
		}
	}

	public String getTimeOutSesion() {
		return propiedades.getProperty("TIME_OUT_SESION");
	}

	public Object getCorreoAdmin() {
		return propiedades.getProperty("CORREO");
	}
}