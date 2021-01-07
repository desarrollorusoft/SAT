package ar.com.cognisys.sat.bean.privado.rs.declaracion;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaBean;
import ar.com.cognisys.sat.bean.Redireccionamiento;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteRegimenSimplificado;
import ar.com.cognisys.sat.core.modelo.comun.rs.Comercio;
import ar.com.cognisys.sat.core.modelo.comun.rs.DDJJRS;
import ar.com.cognisys.sat.core.modelo.comun.rs.EstadoDeclaracion;
import ar.com.cognisys.sat.core.modelo.comun.rs.PadronRS;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.modelo.comun.Mensaje;

@ManagedBean(name = "RS")
@ViewScoped
public class RSBean extends PaginaBean {
	
	private static final long serialVersionUID = 1361878875021084249L;
	private Comercio comercio;
	private boolean bonificacionPendiente;
	
	@ManagedProperty( value = "#{TileResumenRS}")
	private TileResumenRSBean tileResumen;
	
	@Override
	protected void inicializacion() {
		this.setComercio( SesionBean.getUsuarioLogueado().getComercio() );
		try {
			this.refrescar();
		} catch (ExcepcionControladaError e) {
			Mensaje.emitirMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void refrescar() throws ExcepcionControladaError {
		AsistenteRegimenSimplificado.cargarDeclaraciones( this.getComercio() );
		this.cargarEstadoBonificacion();
	}
	
	private void cargarEstadoBonificacion() {
		this.setBonificacionPendiente(false);
		
		Calendar c = new GregorianCalendar(2019,7,1);
		
		for (DDJJRS ddjj : this.getComercio().getListaDeclaraciones())
			if ( ddjj.estaPendiente() && ddjj.getAno().equals( 2019 ) && c.after( Calendar.getInstance() ))
				this.setBonificacionPendiente(true);
	}

	public void completar( DDJJRS declaracion, PadronRS padron ) throws ExcepcionControladaError {
		AsistenteRegimenSimplificado.vaACompletar( declaracion, padron );
		Redireccionamiento r = new Redireccionamiento("/pages/pri/rs_padron.xhtml");
		r.agregarParametro("d", declaracion.getAno().toString());
		r.agregarParametro("p", padron.getNumero());
		r.redireccionar();
	}
	
	public void rectificar( DDJJRS declaracion, PadronRS padron ) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		AsistenteRegimenSimplificado.vaARectificar( declaracion, padron );
		Redireccionamiento r = new Redireccionamiento("/pages/pri/rs_padron.xhtml");
		r.agregarParametro("d", declaracion.getAno().toString());
		r.agregarParametro("p", padron.getNumero());
		r.redireccionar();
	}
	
	public void cancelarRectificativa( DDJJRS declaracion, PadronRS padron ) throws ExcepcionControladaError {
		AsistenteRegimenSimplificado.cancelarRectificativa( declaracion, padron );
	}
	
	public void verDetalle( PadronRS padron ) throws ExcepcionControladaError {
		this.getTileResumen().cargar( padron );
	}
	
	public void verImportes( DDJJRS declaracion ) throws ExcepcionControladaError {
		Redireccionamiento r = new Redireccionamiento("/pages/pri/confirmar_rs.xhtml");
		r.agregarParametro("d", declaracion.getAno().toString());
		r.redireccionar();
	}
	
	public String obtenerLeyenda( DDJJRS declaracion ) {
		switch ( declaracion.getEstado() ) {
			case PENDIENTE:	return "PENDIENTE DE CONFIRMACIÓN";
			case CONFIRMADA:return "CONFIRMADA";
			case RECHAZADA:	return "RECHAZADO POR EXCEDER REQUISITOS";
			case NO_APLICA:	return "NO APLICA";
			default: 		return "";
		}
	}
		
	public String getIcono(DDJJRS declaracion, PadronRS padron) {
		if (declaracion.getEstado().equals( EstadoDeclaracion.NO_APLICA ) ||
			declaracion.getEstado().equals( EstadoDeclaracion.RECHAZADA ))
			return "fas fa-times icon-pasive";
			
		else if (declaracion.getEstado().equals( EstadoDeclaracion.CONFIRMADA ))
			return "fas fa-check";
			
		else if (padron.tieneDeclaracionEnCurso())
			return "fas fa-exclamation-triangle icon-warn";
		else
			return "fas fa-check icon-success";	
	}
	
	public Comercio getComercio() {
		return comercio;
	}

	public void setComercio(Comercio comercio) {
		this.comercio = comercio;
	}

	public TileResumenRSBean getTileResumen() {
		return tileResumen;
	}

	public void setTileResumen(TileResumenRSBean tileResumen) {
		this.tileResumen = tileResumen;
	}

	public boolean isBonificacionPendiente() {
		return bonificacionPendiente;
	}

	public void setBonificacionPendiente(boolean bonificacionPendiente) {
		this.bonificacionPendiente = bonificacionPendiente;
	}
}