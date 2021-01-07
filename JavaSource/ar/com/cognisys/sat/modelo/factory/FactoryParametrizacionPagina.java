package ar.com.cognisys.sat.modelo.factory;

import java.util.ArrayList;
import java.util.List;

import ar.com.cognisys.sat.modelo.parametrizacion.ParametrizacionPagina;
import ar.com.cognisys.sat.modelo.parametrizacion.ParametrizacionPerfil;

public class FactoryParametrizacionPagina {

	public static ParametrizacionPagina generarIntanciaVacia() {
		
		ParametrizacionPagina pp = new ParametrizacionPagina();
		pp.setListaPerfiles(new ArrayList<ParametrizacionPerfil>());
		
		return pp;
	}
	
	public static ParametrizacionPagina generarIntanciaCompleta(String nombre, List<ParametrizacionPerfil> listaPerfiles) {
		
		ParametrizacionPagina pp = generarIntanciaVacia();
		pp.setNombre(nombre);
		pp.setListaPerfiles(listaPerfiles);
		
		return pp;
	}
}