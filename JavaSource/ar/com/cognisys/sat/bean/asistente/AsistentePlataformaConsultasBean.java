package ar.com.cognisys.sat.bean.asistente;

import java.util.List;

import ar.com.cognisys.sat.control.administrador.plataformaconsultas.AdministradorPlataformaConsultas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.modelo.comun.ArchivoConsulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.ConsultaAsociada;

public class AsistentePlataformaConsultasBean {

	public static Consulta recuperarConsulta(String idConsulta) throws ExcepcionControladaError {
		return AdministradorPlataformaConsultas.recuperarConsulta( Integer.valueOf( idConsulta ) );
	}

	public static List<Consulta> recuperarConsultasAsociadas(Consulta consulta) throws ExcepcionControladaError {
		return AdministradorPlataformaConsultas.recuperarConsultasAsociadas( consulta );
	}

	public static void enviarNuevaConsulta(ConsultaAsociada nuevaConsulta) throws ExcepcionControladaError {
		AdministradorPlataformaConsultas.enviarNuevaConsulta( nuevaConsulta );
	}

	public static void reabrirConsulta(Consulta consulta) throws ExcepcionControladaError {
		AdministradorPlataformaConsultas.reabrirConsulta(consulta);
	}

	public static void agregarArchivo(ArchivoConsulta archivo, Long idConsulta) throws ExcepcionControladaError {
		AdministradorPlataformaConsultas.agregarArchivo( archivo, idConsulta );		
	}

	public static List<ArchivoConsulta> recuperarArchivos(Consulta consulta) throws ExcepcionControladaError {
		return AdministradorPlataformaConsultas.recuperarArchivos( consulta );
	}

	public static void eliminarArchivo(ArchivoConsulta archivo) throws ExcepcionControladaError {
		AdministradorPlataformaConsultas.eliminarArchivo( archivo );
	}

}
