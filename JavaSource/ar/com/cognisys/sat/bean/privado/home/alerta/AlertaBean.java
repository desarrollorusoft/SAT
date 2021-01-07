package ar.com.cognisys.sat.bean.privado.home.alerta;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.ScheduleModel;

import ar.com.cognisys.common.exception.ExcepcionControladaAlerta;
import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.generico.modelo.comun.AsistenteObjeto;
import ar.com.cognisys.sat.bean.asistente.AsistenteAlerta;
import ar.com.cognisys.sat.excepcion.Mensaje;
import ar.com.cognisys.sat.excepcion.SATRuntimeException;
import ar.com.cognisys.sat.v2.core.modelo.bo.IAlerta;

@ManagedBean(name = "Alertas")
@ViewScoped
public class AlertaBean extends SeccionBean {
	
	private static final long serialVersionUID = -8712468342257013836L;
	private Date fecha;
	private String descripcion;
	private ScheduleModel calendario;
	private List<IAlerta> listaAlertas;

	@Override
	protected void inicializacion() {
		try {
			this.refrescar();
		} catch (ExcepcionControladaError e) {
			throw new SATRuntimeException(e);
		}
	}
	
	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object arg0) throws ExcepcionControladaError {}

	@Override
	public void siguiente() throws ExcepcionControladaError {}
	
	@Override
	public void refrescar() throws ExcepcionControladaError {
		this.cargarModelo();
		this.inicializar();
	}

	private void cargarModelo() throws ExcepcionControladaError {
		this.setListaAlertas( AsistenteAlerta.recuperar() );
		this.setCalendario( AsistenteAlerta.generarModelo(this.getListaAlertas()) );
    }

	private void inicializar() {
		this.setFecha(null);
		this.setDescripcion(null);
	}
	
	public void agregarAlterta() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		validar();
		AsistenteAlerta.crear(this.getFecha(), this.getDescripcion());
		this.refrescar();
		Mensaje.addMessageAviso("La alerta ha sido agregada");
	}
	
	private void validar() throws ExcepcionControladaAlerta {
		if( !AsistenteObjeto.existe(this.fecha) )
			throw new ExcepcionControladaAlerta( "Debe ingresar una fecha para la alerta" );
		if( !AsistenteObjeto.tieneContenido(this.descripcion) )
			throw new ExcepcionControladaAlerta( "Debe ingresar una descripcion para la alerta" );
	}

	public void eliminarAlerta(IAlerta alerta) throws ExcepcionControladaError {
		AsistenteAlerta.eliminar(alerta);
		this.refrescar();
		Mensaje.addMessageAviso("La alerta ha sido eliminada");
	}

	public Date getHoy(){
		return new Date();
	}
	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public ScheduleModel getCalendario() {
		return calendario;
	}

	public void setCalendario(ScheduleModel calendario) {
		this.calendario = calendario;
	}

	public List<IAlerta> getListaAlertas() {
		return listaAlertas;
	}

	public void setListaAlertas(List<IAlerta> listaAlertas) {
		this.listaAlertas = listaAlertas;
	}
}