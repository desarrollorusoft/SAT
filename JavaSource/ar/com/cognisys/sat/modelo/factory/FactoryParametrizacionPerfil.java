package ar.com.cognisys.sat.modelo.factory;

import ar.com.cognisys.sat.modelo.parametrizacion.ParametrizacionPerfil;

public class FactoryParametrizacionPerfil {

	public static ParametrizacionPerfil generarIntanciaVacia() {
		
		return (new ParametrizacionPerfil());
	}
	
	public static ParametrizacionPerfil generarIntanciaCompleta(String nombre) {
		
		ParametrizacionPerfil pp = generarIntanciaVacia();
		pp.setNombre(nombre);
		
		return pp;
	}
}