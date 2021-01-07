package ar.com.cognisys.sat.modelo.propiedades;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class Propiedades implements Serializable{
	
	private static final long serialVersionUID = -411225281073844656L;
	
	private Properties propiedades;
	
	public Propiedades() {
		try {
			// se crea una instancia a la clase Properties
			propiedades = new Properties();
			
			// se leen el archivo .properties
			propiedades.load(getClass().getResourceAsStream("mvl.properties"));
		} catch (IOException ex) {
		}
	}
	
	public String getHost_OficinaVirtual() {
		return propiedades.getProperty("informix_ov_host");
	}

	public String getPort_OficinaVirtual() {
		return propiedades.getProperty("informix_ov_port");
	}

	public String getBase_OficinaVirtual() {
		return propiedades.getProperty("informix_ov_base");
	}
	
	public String getInstancia_OficinaVirtual() {
		return propiedades.getProperty("informix_ov_instancia");
	}
	
	public String getUsuario_OficinaVirtual() {
		return propiedades.getProperty("informix_ov_usuario");
	}
	
	public String getClave_OficinaVirtual() {
		return propiedades.getProperty("informix_ov_clave");
	}
	
	public String getMySql_Host_OficinaVirtual() {
		return propiedades.getProperty("mysql_ov_host");
	}

	public String getMySql_Port_OficinaVirtual() {
		return propiedades.getProperty("mysql_ov_port");
	}

	public String getMySql_Base_OficinaVirtual() {
		return propiedades.getProperty("mysql_ov_base");
	}
	
	public String getMySql_Usuario_OficinaVirtual() {
		return propiedades.getProperty("mysql_ov_usuario");
	}
	
	public String getMySql_Clave_OficinaVirtual() {
		return propiedades.getProperty("mysql_ov_clave");
	}
	
	public String getUrlLoginContribuyente() {
		return propiedades.getProperty("url_index_mvl");
	}
	
	public String getClaveEncriptacionXml() {
		return propiedades.getProperty("clave_encriptacion_xml");
	}
	
	public String getMySql_Host_Reportes() {
		return propiedades.getProperty("mysql_reportes_host");
	}

	public String getMySql_Port_Reportes() {
		return propiedades.getProperty("mysql_reportes_port");
	}

	public String getMySql_Base_Reportes() {
		return propiedades.getProperty("mysql_reportes_base");
	}
	
	public String getMySql_Usuario_Reportes() {
		return propiedades.getProperty("mysql_reportes_usuario");
	}
	
	public String getMySql_Clave_Reportes() {
		return propiedades.getProperty("mysql_reportes_clave");
	}
	
	public String getURL_Clave_Fiscal() {
		return propiedades.getProperty("url_clave_fiscal");
	}
	
	public String getURL_Activacion_Usuario() {
		return propiedades.getProperty("url_activacion_sat");
	}
	
	public String getURL_INICIO() {
		return propiedades.getProperty("pagina_inicio");
	}
	
	public String getURL_VicenteLopez() {
		return propiedades.getProperty("pagina_vicentelopez");
	}
	
	public String getURL_Municipio() {
		return propiedades.getProperty("pagina_municipio");
	}
	
	public String getURL_ForosVecinales() {
		return propiedades.getProperty("pagina_forosvecinales");
	}
	
	public String getURL_DesarrolloEconomico() {
		return propiedades.getProperty("pagina_desarrolloeconomico");
	}
	
	public String getURL_EmpleadoMunicipal() {
		return propiedades.getProperty("pagina_empleadomunicipal");
	}
}