package ar.com.cognisys.sat.bean.asistente;

import java.util.ArrayList;
import java.util.List;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.privado.rs.declaracion.ResumenView;
import ar.com.cognisys.sat.core.administrador.AdministradorComercio;
import ar.com.cognisys.sat.core.administrador.AdministradorContribuyente;
import ar.com.cognisys.sat.core.administrador.AdministradorRegimenSimplificado;
import ar.com.cognisys.sat.core.administrador.AdministradorSolicitanteRS;
import ar.com.cognisys.sat.core.contenedor.ContenedorCaracterSAT;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.RSDeudaPadron;
import ar.com.cognisys.sat.core.modelo.comun.rs.Comercio;
import ar.com.cognisys.sat.core.modelo.comun.rs.DDJJRS;
import ar.com.cognisys.sat.core.modelo.comun.rs.EstadoDeclaracion;
import ar.com.cognisys.sat.core.modelo.comun.rs.PadronRS;
import ar.com.cognisys.sat.core.modelo.comun.rs.versiones.VersionPadronRS;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.CaracterSAT;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.factory.rs.FactorySolicitanteRS;
import ar.com.cognisys.sat.modelo.comun.Mensaje;

public class AsistenteRegimenSimplificado {

	public static void actualizar(String telefonoAct, String celularAct, String nombre, String apellido,
								  String caracterSeleccionado, String correoSol, String telefonoSol, 
								  String celularSol, String cuitSol) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		
		try {
			Usuario u = SesionBean.getUsuarioLogueado();
			Comercio comercio = u.getComercio();
			
			if (comercio == null) {
				comercio = AdministradorComercio.recuperarSimple( u.getCuit() );
				u.setComercio( comercio );
			}
			
			CaracterSAT caracter = ContenedorCaracterSAT.getInstancia().buscar(caracterSeleccionado);			
			comercio.setSolicitanteRS( FactorySolicitanteRS.generar(cuitSol, nombre, apellido, caracter, correoSol, telefonoSol, celularSol) );
			
			AdministradorSolicitanteRS.registrar( comercio );
			
			AdministradorContribuyente.actualizarDatosUsuario(u.getIdUsuario(), 
															  u.getCorreo(), 
															  telefonoAct, 
															  celularAct, 
															  u.getNombre(), 
															  u.getApellido(), 
															  u.getNivel());
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(),e );
		}
	}

	public static void cargarDeclaraciones(Comercio comercio) throws ExcepcionControladaError {
		try {
			AdministradorRegimenSimplificado.cargarDeclaraciones( comercio );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError( e );
		}
	}
	
	public static void vaACompletar(DDJJRS declaracion, PadronRS padron) {
		padron.iniciarModificacion();
	}

	public static void vaARectificar(DDJJRS declaracion, PadronRS padron) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {
			if (AdministradorRegimenSimplificado.puedeRectificar(declaracion, padron)) {
				declaracion.iniciarRectificacion( padron );
				AdministradorRegimenSimplificado.actualizarInicioRectificacion( declaracion, padron, SesionBean.getUsuarioLogueado() );
				padron.iniciarModificacion();				
			} else
				throw new ExcepcionControladaAlerta("No se puede efectuar esta rectificativa por sistema. Para iniciar el trámite, debe dirigirse a la Municipalidad.");
			
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError( e );
		}
	}
	
	public static List<RSDeudaPadron> recuperarDeuda(DDJJRS ddjj, Usuario usuario) throws ExcepcionControladaError {
		try {
			return AdministradorRegimenSimplificado.recuperarDeudaRS( ddjj, usuario );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError( e );
		}
	}

	public static void cancelarRectificativa(DDJJRS declaracion, PadronRS padron) throws ExcepcionControladaError {
		try {
			AdministradorRegimenSimplificado.cancelarRectificativa( declaracion, padron );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError( e );
		}
	}

	public static List<ResumenView> generarResumenes(PadronRS padron) throws ExcepcionControladaError {		
		try {
			List<ResumenView> lista = new ArrayList<>();
			
			VersionPadronRS vc = padron.getVersionEnCurso();

			if (vc != null)
				lista.add( new ResumenView(padron, vc, "Declaración en Curso") );
			else {
				VersionPadronRS ultima = padron.obtenerUltimaVersion();
				
				for (VersionPadronRS v : padron.getListaVersiones()) {
					VersionPadronRS aux = null;
					
					if ( !v.getVersion().equals( ultima.getVersion() ) || padron.getEstado().equals( EstadoDeclaracion.CONFIRMADA ))
						aux = AdministradorRegimenSimplificado.buscarDeclaracionConfirmada(padron, v);
					else
						aux = v;
					
					if (aux.getVersion() == 0)
						lista.add( new ResumenView(padron, aux, "Declaración Original") );
					else
						lista.add( new ResumenView(padron, aux, "Declaración Rectificada ("+aux.getVersion()+")") );
				}
			}
			
			return lista;
			
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError( e );
		}
	}
}