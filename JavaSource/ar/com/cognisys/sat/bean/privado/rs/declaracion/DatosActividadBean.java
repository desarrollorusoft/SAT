package ar.com.cognisys.sat.bean.privado.rs.declaracion;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.ibm.icu.text.SimpleDateFormat;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionWizardBean;
import ar.com.cognisys.sat.bean.Redireccionamiento;
import ar.com.cognisys.sat.bean.asistente.AsistenteDDJJPadron;
import ar.com.cognisys.sat.bean.asistente.AsistenteDatosActividad;
import ar.com.cognisys.sat.bean.converter.DatoActividadConverter;
import ar.com.cognisys.sat.core.contenedor.ContenedorActividadComercial;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.ActividadComercial;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.Actividades;
import ar.com.cognisys.sat.core.modelo.comun.rs.PadronRS;
import ar.com.cognisys.sat.core.modelo.comun.rs.versiones.VersionPadronRS;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.excepcion.Mensaje;

@ManagedBean(name = "DatosActividad")
@ViewScoped
public class DatosActividadBean extends SeccionWizardBean {
	
	private static final long serialVersionUID = -8320913849340911566L;
	private PadronRS padron;
	private VersionPadronRS versionEnCurso;
	private Actividades actividades;
	private List<ActividadComercial> listaActividades;
	private DatoActividadConverter datoActividadConverter;
	private boolean cuentaAblValidada;
	
	@Override
	protected void inicializacion() {
	}

	@Override
	public void cargar(Object dato) throws ExcepcionControladaError {
		this.setPadron( (PadronRS) dato );

		this.setListaActividades( ContenedorActividadComercial.getInstancia().getListaActividades( this.getPadron().obtenerAnoDeclaracion() ) );
		this.setDatoActividadConverter(new DatoActividadConverter( this.getListaActividades() ));
		this.setCuentaAblValidada(false);

		this.setVersionEnCurso( this.getPadron().getVersionEnCurso() );
		this.setActividades( AsistenteDDJJPadron.obtenerActividades( this.getVersionEnCurso() ) );
		
		if (this.getVersionEnCurso().getFechaHabilitacion() != null) {
			String fecha = (new SimpleDateFormat("dd-MM-yyyy").format( this.getVersionEnCurso().getFechaHabilitacion() ));
			if (fecha.equals("01-01-1900")) {
				this.getVersionEnCurso().setFechaHabilitacion(null);
			}
		}
	}
	
	@Override
	public void cargar() {}
	
	@Override
	public void siguiente() throws ExcepcionControladaError {
		try {
			validar();
			this.irAlSiguiente( this.getPadron() );
		} catch (ExcepcionControladaAlerta e) {
			Mensaje.addMessageWarn(e.getMessage());
		}
	}
	
	public Date obtenerFechaDeHoy() {
		return new Date();
	}
	
	private void validar() throws ExcepcionControladaAlerta {
		VersionPadronRS version = this.getPadron().getVersionEnCurso();
		
		if ( version.getFechaHabilitacion() == null)
			throw new ExcepcionControladaAlerta("Debe ingresar una Fecha de Habilitación");
		
		if ( version.getCantidadPersonas() == null || version.getCantidadPersonas() <= 0 )
			throw new ExcepcionControladaAlerta("Debe ingresar la cantidad de personas que trabajan en el comercio");
		
		if ( version.getFacturacion() == null )
			throw new ExcepcionControladaAlerta("Debe ingresar la Facturación anual");
		
		if ( !this.isCuentaAblValidada() )
			throw new ExcepcionControladaAlerta( "Debe buscar la cuenta de A.L.C.V.P. y S.V. y C.P.C. asociada a su comercio" );
		
		if ( this.getActividades().getActividadPrincipal() == null )
			throw new ExcepcionControladaAlerta("Debe seleccionar una Actividad Principal");	
	}

	@Override
	public void refrescar() {}

	public void validarCuentaAbl() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		try {
			String padron = this.getPadron().getVersionEnCurso().getCuentaABL();
			
			if (padron != null) {
				AsistenteDatosActividad.validarCuentaABL( padron );
				this.setCuentaAblValidada(true);
				Mensaje.addMessageAviso("El padrón ["+padron.substring(0, padron.indexOf("."))+"] fue verificado");		
			} else {
				this.setCuentaAblValidada(false);
				Mensaje.addMessageWarn("Debe ingresar un padrón de A.L.C.V.P. y S.V. y C.P.C.");
			}
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			this.setCuentaAblValidada(false);
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
	
	public void volverAPadrones() throws ExcepcionControladaError {
		(new Redireccionamiento("/pages/pri/rs.xhtml")).redireccionar();
	}
	
	public int anoFacturacion() {
		return (this.getVersionEnCurso().getAno() - 1);
	}

	public PadronRS getPadron() {
		return padron;
	}

	public void setPadron(PadronRS padron) {
		this.padron = padron;
	}

	public Actividades getActividades() {
		return actividades;
	}

	public void setActividades(Actividades actividades) {
		this.actividades = actividades;
	}

	public List<ActividadComercial> getListaActividades() {
		return listaActividades;
	}

	public void setListaActividades(List<ActividadComercial> listaActividades) {
		this.listaActividades = listaActividades;
	}

	public DatoActividadConverter getDatoActividadConverter() {
		return datoActividadConverter;
	}

	public void setDatoActividadConverter(DatoActividadConverter datoActividadConverter) {
		this.datoActividadConverter = datoActividadConverter;
	}

	public boolean isCuentaAblValidada() {
		return cuentaAblValidada;
	}

	public void setCuentaAblValidada(boolean cuentaAblValidada) {
		this.cuentaAblValidada = cuentaAblValidada;
	}

	public VersionPadronRS getVersionEnCurso() {
		return versionEnCurso;
	}

	public void setVersionEnCurso(VersionPadronRS versionEnCurso) {
		this.versionEnCurso = versionEnCurso;
	}
}