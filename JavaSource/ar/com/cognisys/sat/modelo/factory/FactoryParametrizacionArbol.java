package ar.com.cognisys.sat.modelo.factory;

import java.util.ArrayList;
import java.util.List;

import ar.com.cognisys.sat.modelo.parametrizacion.ParametrizacionArbol;
import ar.com.cognisys.sat.modelo.parametrizacion.ParametrizacionPagina;

public class FactoryParametrizacionArbol {

	public static ParametrizacionArbol generarIntanciaVacia() {
		
		ParametrizacionArbol pa = new ParametrizacionArbol();
		pa.setListaPaginas(new ArrayList<ParametrizacionPagina>());
		
		return pa;
	}
	
	public static ParametrizacionArbol generarIntanciaCompleta(List<ParametrizacionPagina> listaPaginas) {
		
		ParametrizacionArbol pa = generarIntanciaVacia();
		pa.setListaPaginas(listaPaginas);
		
		return pa;
	}
}