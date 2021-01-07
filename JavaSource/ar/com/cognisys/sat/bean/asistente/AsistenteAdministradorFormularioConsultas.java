package ar.com.cognisys.sat.bean.asistente;

import java.util.List;

import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.administrador.AdministradorFormularioConsultas;
import ar.com.cognisys.sat.core.modelo.comun.ArchivoConsulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Caracter;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Categoria;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.ConsultaAsociada;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Dato;
import ar.com.cognisys.sat.core.modelo.comun.consultas.FormularioConsulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Subcategoria;
import ar.com.cognisys.sat.core.modelo.comun.consultas.TipoConsulta;

public class AsistenteAdministradorFormularioConsultas {

	public static List<Categoria> obtenerCategorias() throws ExcepcionControladaError {
			try {
				return AdministradorFormularioConsultas.obtenerCategorias();
			} catch (Exception e) {
				throw new ExcepcionControladaError(e.getMessage(), e);
			}
	}

	public static List<Caracter> obtenerCaracteres() throws ExcepcionControladaError {

		try {
            return AdministradorFormularioConsultas.obtenerCaracteres();
        } catch (Exception e) {
            throw new ExcepcionControladaError(e.getMessage(), e);
        }
	}

	public static List<TipoConsulta> obtenerTipoConsultas() throws ExcepcionControladaError {
		try {
			return AdministradorFormularioConsultas.obtenerTipoConsultas();
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(),e);
		}
	}

	public static void enviarConsulta(FormularioConsulta formulario,
									  Categoria categoria,
									  Subcategoria subcategoria,
									  Caracter caracter,
									  Dato dato) throws ExcepcionControladaError {
		try {
			formulario.setIdCaracter(caracter.getId());
			formulario.setIdCategoria(categoria.getId());
//			formulario.setIdSubcategoria(subcategoria.getId());
			formulario.setIdDato(dato.getId());
			AdministradorFormularioConsultas.registrarConsulta(formulario);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static void enviarConsulta(FormularioConsulta formulario,
									  Categoria categoria,
									  Dato dato) throws ExcepcionControladaError {
		try {
			formulario.setIdCategoria(categoria.getId());
			formulario.setIdDato(dato.getId());
			AdministradorFormularioConsultas.registrarConsulta(formulario);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static String obtenerCategoriaCorreo(Categoria categoria) throws ExcepcionControladaError {
		try {
			return AdministradorFormularioConsultas.getCorreoCategoria(categoria);
		} catch (Exception e) {
			throw new ExcepcionControladaError("Error al obtener el correo de la categoria", e);
		}
	}

	public static Consulta recuperarConsulta(Integer idConsulta) throws ExcepcionControladaError {
		try {
			return AdministradorFormularioConsultas.buscarConsulta( idConsulta );
		} catch (Exception e) {
			throw new ExcepcionControladaError("Error al obtener el correo de la categoria", e);
		}
	}

	public static List<Consulta> recuperarAsociadas(Consulta consulta) throws ExcepcionControladaError {
		try {
			return AdministradorFormularioConsultas.buscarConsultasAsociadas( consulta );
		} catch (Exception e) {
			throw new ExcepcionControladaError("Error al obtener el correo de la categoria", e);
		}
	}

	public static void enviarNuevaConsulta(ConsultaAsociada nuevaConsulta) throws ExcepcionControladaError {
		try {
			AdministradorFormularioConsultas.enviarNuevaConsulta( nuevaConsulta );
		} catch (Exception e) {
			throw new ExcepcionControladaError("Error al enviar la nueva consulta", e);
		}
	}

	public static void actualizarConsulta(Consulta consulta) throws ExcepcionControladaError {
		try {
			AdministradorFormularioConsultas.actualizarConsulta( consulta );
		} catch (Exception e) {
			throw new ExcepcionControladaError("Error al actualizar la consulta", e);
		}	
	}

	public static void agregarArchivo(ArchivoConsulta archivo, Long idConsulta) throws ExcepcionControladaError {
		try {
			AdministradorFormularioConsultas.guardarArchivo( archivo, idConsulta );
		} catch (Exception e) {
			throw new ExcepcionControladaError("Error al adjuntar el archivo", e);
		}	
	}

	public static List<ArchivoConsulta> recuperarArchivos(Consulta consulta) throws ExcepcionControladaError {
		try {
			return AdministradorFormularioConsultas.recuperarArchivos( consulta );
		} catch (Exception e) {
			throw new ExcepcionControladaError("Error al recuperar los archivos", e);
		}	
	}

	public static void eliminarArchivo(ArchivoConsulta archivo) throws ExcepcionControladaError {
		try {
			 AdministradorFormularioConsultas.eliminarArchivo( archivo );
		} catch (Exception e) {
			throw new ExcepcionControladaError("Error al eliminar el archivo", e);
		}	
	}
}