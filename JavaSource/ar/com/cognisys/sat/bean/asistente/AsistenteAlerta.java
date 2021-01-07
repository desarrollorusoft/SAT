package ar.com.cognisys.sat.bean.asistente;

import java.util.Date;
import java.util.List;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.v2.core.controlador.administrador.AdministradorAlertas;
import ar.com.cognisys.sat.v2.core.controlador.facade.FacadeAlertas;
import ar.com.cognisys.sat.v2.core.modelo.bo.IAlerta;
import ar.com.cognisys.sat.v2.core.modelo.exception.PersistenceSATException;
import ar.com.cognisys.sat.v2.vista.modelo.creator.AlertaViewCreator;
import ar.com.cognisys.sat.v2.vista.modelo.v2.interfaz.IUsuarioAlertaView;
import ar.com.cognisys.sat.v2.vista.modelo.v2.view.AlertaView;
import ar.com.cognisys.sat.v2.vista.modelo.v2.view.UsuarioAlertaView;

public class AsistenteAlerta {

	public static List<IAlerta> recuperar() throws ExcepcionControladaError {
		try {
			return new AdministradorAlertas().recuperar( SesionBean.getUsuarioLogueado().getCuit() );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
	
	public static ScheduleModel generarModelo(List<IAlerta> lista) {
		ScheduleModel calendario = new DefaultScheduleModel();
		for (IAlerta alerta : lista)
			calendario.addEvent(new DefaultScheduleEvent(alerta.getDescripcion(), alerta.getFechaInicio(), alerta.getFechaFin()));
		return calendario;
	}

	public static void crear(Date fecha, String descripcion) throws ExcepcionControladaError {
				
		try {
			(new FacadeAlertas()).agregar( crearUsuarioAlertaView(fecha, descripcion) );
		} catch (PersistenceSATException e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	private static IUsuarioAlertaView crearUsuarioAlertaView(Date fecha, String descripcion) {
		
		AlertaView av = (new AlertaViewCreator()).title( descripcion ).clave( "Personal" ).start( fecha ).end( fecha ).redireccion( null ).create();
		UsuarioAlertaView uav = new UsuarioAlertaView();
		uav.setAlerta(av);
		uav.setNombreUsuario( SesionBean.getUsuarioLogueado().getCuit() );
		
		return uav;
	}

	public static void eliminar(IAlerta alerta) throws ExcepcionControladaError {
		try {
			(new FacadeAlertas()).borrar( crearUsuarioAlertaView(alerta) );
		} catch (PersistenceSATException e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	private static IUsuarioAlertaView crearUsuarioAlertaView(IAlerta alerta) {
		
		AlertaView av = (new AlertaViewCreator()).title( alerta.getDescripcion() )
												 .clave( alerta.getTitulo() )
												 .start( alerta.getFechaInicio() )
												 .end( alerta.getFechaFin() )
												 .redireccion( alerta.getRedireccion() )
												 .create();
		UsuarioAlertaView uav = new UsuarioAlertaView();
		uav.setAlerta(av);
		uav.setNombreUsuario( SesionBean.getUsuarioLogueado().getCuit() );
		
		return uav;
	}
}