package ar.com.cognisys.sat.modelo.parametrizacion;

import java.util.List;

public class ParametrizacionPagina {

	private String nombre;
	private List<ParametrizacionPerfil> listaPerfiles;
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<ParametrizacionPerfil> getListaPerfiles() {
		return listaPerfiles;
	}
	
	public void setListaPerfiles(List<ParametrizacionPerfil> listaPerfiles) {
		this.listaPerfiles = listaPerfiles;
	}
}