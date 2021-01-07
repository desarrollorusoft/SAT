package ar.com.cognisys.sat.modelo.parametrizacion;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import ar.com.cognisys.sat.modelo.factory.FactoryParametrizacionArbol;
import ar.com.cognisys.sat.modelo.factory.FactoryParametrizacionPagina;
import ar.com.cognisys.sat.modelo.factory.FactoryParametrizacionPerfil;

public class Parametrizacion {

	private ParametrizacionArbol arbol;
	
	public Parametrizacion() {
		
		this.setArbol( this.generarArbol(this.cargarXML()) );
	}
	
	private String cargarXML() {
		Reader fileReader = null;
		String linea = null;
		String respuesta = "";
		
		try {
			InputStream is = this.getClass().getResourceAsStream("parametrizacion.xml");
			
			if (null != is) {
			    fileReader = new InputStreamReader(is);
			    
			    BufferedReader buffer = new BufferedReader(fileReader);
				
				respuesta = new String();
				while((linea = buffer.readLine()) != null) {
				    respuesta += linea;
				}
			}		
		} catch (Exception e) {
		}
		
		return respuesta.replaceAll("\t", "");
	}
	
	private ParametrizacionArbol generarArbol(String xml) {
		
		String xmlArbol = xml.substring(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").length()); 
		
		return FactoryParametrizacionArbol.generarIntanciaCompleta( this.generarListaPaginas(xmlArbol) );
	}
	
	private List<ParametrizacionPagina> generarListaPaginas(String porcionXML) {
		
		List<ParametrizacionPagina> listaPaginas = new ArrayList<ParametrizacionPagina>();
		
		String parametrizacionPaginas = recuperarValorEntre(porcionXML, "parametrizacion-paginas");
		
		while (parametrizacionPaginas.indexOf("<pagina>") >= 0) {
			
			String pagina = recuperarValorEntre(parametrizacionPaginas, "pagina");
			listaPaginas.add( this.generarPagina(pagina) );
			parametrizacionPaginas = parametrizacionPaginas.substring(rearmarSeccion("pagina", pagina).length());
		}
		
		return listaPaginas;
	}

	private ParametrizacionPagina generarPagina(String porcionXML) {

		return FactoryParametrizacionPagina.generarIntanciaCompleta(recuperarValorEntre(porcionXML, "nombre-pagina"), 
																	generarListaPerfiles( recuperarValorEntre(porcionXML, "perfiles-asociados") ));
	}

	private List<ParametrizacionPerfil> generarListaPerfiles(String porcionXML) {
		
		List<ParametrizacionPerfil> listaPerfiles = new ArrayList<ParametrizacionPerfil>();
		
		while (porcionXML.indexOf("<perfil>") >= 0) {
			
			String perfil = recuperarValorEntre(porcionXML, "perfil");
			listaPerfiles = this.generarPerfil(perfil);
			porcionXML = porcionXML.substring(rearmarSeccion("perfil", perfil).length());
		}
		
		return listaPerfiles;
	}

	private List<ParametrizacionPerfil> generarPerfil(String porcionXML) {
		
		List<ParametrizacionPerfil> listaPerfiles = new ArrayList<ParametrizacionPerfil>();
		
		while (porcionXML.indexOf("<nombre-perfil>") >= 0) {
			
			String perfil = recuperarValorEntre(porcionXML, "nombre-perfil");
			listaPerfiles.add( FactoryParametrizacionPerfil.generarIntanciaCompleta( perfil ) );
			porcionXML = porcionXML.substring(rearmarSeccion("nombre-perfil", perfil).length());
		}
		
		return listaPerfiles;
	}

	private static String recuperarValorEntre(String porcionXML, String nombreTag) {
		
		return porcionXML.substring(porcionXML.indexOf("<" + nombreTag + ">") + ("<" + nombreTag + ">").length(), porcionXML.indexOf("</" + nombreTag + ">"));
	}
	
	private static String rearmarSeccion(String nombreTag, String seccionInterna) {
		
		return ("<" + nombreTag + ">" + seccionInterna + "</" + nombreTag + ">");
	}
	
	public ParametrizacionArbol getArbol() {
		return arbol;
	}

	public void setArbol(ParametrizacionArbol arbol) {
		this.arbol = arbol;
	}

	public static void main(String[] args) {
		Parametrizacion parametrizacion = new Parametrizacion();
		parametrizacion.getArbol();
	}	
}