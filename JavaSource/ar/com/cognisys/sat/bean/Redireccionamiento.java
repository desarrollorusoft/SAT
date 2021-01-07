package ar.com.cognisys.sat.bean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.core.modelo.comun.comunicacion.EncriptacionSAT;

public class Redireccionamiento {

	private String path; 
	private Map<String, String> parametros;
	
	public Redireccionamiento() {}
	
	public Redireccionamiento(String path) {
		this.setPath(path);
	}
	
	public Redireccionamiento(String path, Map<String, String> parametros) {
		this.setPath(path);
		this.setParametros(parametros);
	}
	
	public void redireccionar() throws ExcepcionControladaError {
		
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context.redirect( context.getRequestContextPath() + this.getPath() + generarMensaje() );
		} catch (IOException e) {
			throw new ExcepcionControladaError("Falló el redireccionamiento", e);
		}
	}

	public Map<String, String> recuperarDatos() throws ExcepcionControladaError {
		
		Map<String, String> datos = new HashMap<String, String>();
		
		try {
			Map<String, String> request = (Map<String, String>) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			
			if ( hayDatosMapa(request) ) {
				String p = request.get( "p" );
				
				if ( !p.isEmpty() ) {
					String[] campos = (new EncriptacionSAT()).desencriptar( p ).split( "&" );
					for (String campo : campos) {
						String[] partes = new String[2];
						int pos = campo.indexOf("=");
						partes[0] = campo.substring(0, pos);
						partes[1] = campo.substring(pos+1);
						datos.put( partes[0], partes[1] );
					}
				}
			}
		} catch (Exception e) {
			throw new ExcepcionControladaError("Falló la recuperacion de datos", e);
		}
		
		return datos;
	}
	
	public void agregarParametro(String clave, String valor) {
		if (!hayParametros())
			this.setParametros(new HashMap<String, String>());
	
		this.getParametros().put(clave, valor);
	}
	
	private String generarMensaje() {
		
		String mensaje = "";
		
		if ( hayParametros() ) {
			String p = ""; 
			for (Entry<String, String> entry : this.getParametros().entrySet())
				p += (p.isEmpty() ? "":"&") + entry.getKey() + "=" + entry.getValue();
			
			mensaje += "?p=" + (new EncriptacionSAT()).encriptar( p );
		}
		
		return mensaje;
	}
	
	private boolean hayParametros() {
		return hayDatosMapa( this.getParametros() );
	}
	
	private boolean hayDatosMapa(Map<String, String> datos) {
		return (datos != null && !datos.isEmpty());
	}
	
	public boolean esRedireccionamientoPrivado() {
		return (this.getPath() != null && this.getPath().contains("/pri/"));
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, String> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, String> parametros) {
		this.parametros = parametros;
	}
}