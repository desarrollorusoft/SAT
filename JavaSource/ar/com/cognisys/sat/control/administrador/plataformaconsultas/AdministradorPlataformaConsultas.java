package ar.com.cognisys.sat.control.administrador.plataformaconsultas;

import java.util.List;

import ar.com.cognisys.sat.bean.asistente.AsistenteAdministradorFormularioConsultas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.modelo.comun.ArchivoConsulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.ConsultaAsociada;
import ar.com.cognisys.sat.core.modelo.enums.EstadoConsulta;

public class AdministradorPlataformaConsultas {

	public static Consulta recuperarConsulta(Integer idConsulta) throws ExcepcionControladaError {
		
		return AsistenteAdministradorFormularioConsultas.recuperarConsulta( idConsulta );
		
	}

	public static List<Consulta> recuperarConsultasAsociadas(Consulta consulta) throws ExcepcionControladaError {
		
		return AsistenteAdministradorFormularioConsultas.recuperarAsociadas( consulta );
		
	}

	public static void enviarNuevaConsulta(ConsultaAsociada nuevaConsulta) throws ExcepcionControladaError {
		AsistenteAdministradorFormularioConsultas.enviarNuevaConsulta( nuevaConsulta );
	}

	public static void reabrirConsulta(Consulta consulta) throws ExcepcionControladaError {
		consulta.setEstado( EstadoConsulta.DEVUELTA );
		AsistenteAdministradorFormularioConsultas.actualizarConsulta(consulta);
	}

	public static void agregarArchivo(ArchivoConsulta archivo, Long idConsulta) throws ExcepcionControladaError {
		AsistenteAdministradorFormularioConsultas.agregarArchivo( archivo, idConsulta );
	}

	public static List<ArchivoConsulta> recuperarArchivos(Consulta consulta) throws ExcepcionControladaError {
		return AsistenteAdministradorFormularioConsultas.recuperarArchivos( consulta );
	}

	public static void eliminarArchivo(ArchivoConsulta archivo) throws ExcepcionControladaError {

		AsistenteAdministradorFormularioConsultas.eliminarArchivo(archivo);		
	}

}
