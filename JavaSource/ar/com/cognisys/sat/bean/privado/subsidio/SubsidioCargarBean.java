package ar.com.cognisys.sat.bean.privado.subsidio;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaWizardBean;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionWizardBean;
import ar.com.cognisys.common.jsfbean.factory.FactoryParametrizacionWizard;
import ar.com.cognisys.common.jsfbean.parametrizacion.ParametrizacionWizardBean;
import ar.com.cognisys.sat.adds.GenericLogger;
import ar.com.cognisys.sat.bean.asistente.AsistenteDatosActividad;
import ar.com.cognisys.sat.bean.asistente.AsistenteSubsidio;
import ar.com.cognisys.sat.bean.privado.planes.detalle.DetallePlanBean;
import ar.com.cognisys.sat.bean.privado.planes.lista.ListaPlanesBean;
import ar.com.cognisys.sat.core.administrador.AdministradorArchivo;
import ar.com.cognisys.sat.core.modelo.comun.Archivo;
import ar.com.cognisys.sat.core.modelo.comun.tramiteSubsidio.DatosComercio;
import ar.com.cognisys.sat.core.modelo.comun.tramiteSubsidio.TramiteSubsidio;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;
import ar.com.cognisys.sat.excepcion.Mensaje;
import org.primefaces.event.FileUploadEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;

@ManagedBean(name = "SubsidioCargar")
@ViewScoped
public class SubsidioCargarBean extends SeccionWizardBean {
	
	private static final long serialVersionUID = 5392608503631110572L;
	private TramiteSubsidio tramite;
	private Archivo archivo;
	private String urlImagen;
	private DatosComercio datosActivos;

	@Override
	protected void inicializacion() {}

	@Override
	public void refrescar() throws ExcepcionControladaError {}

	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object o) throws ExcepcionControladaError {
		this.setTramite((TramiteSubsidio) o);
		this.setDatosActivos(this.getTramite().getDatos().get(0));
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {
		try {
			String host = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get( "referer" );
			String url = host.substring( 0, host.indexOf( "/pages" ) );
			url += "/pages/pri/subsidio.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect( url );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cambiarDatos() {
		if (this.getTramite().getDatos().size() != 1) {
			if (this.getTramite().getDatos().get(0).sos(this.getDatosActivos()))
				this.setDatosActivos(this.getTramite().getDatos().get(1));
			else
				this.setDatosActivos(this.getTramite().getDatos().get(0));
		}
	}

	public void subirArchivos(FileUploadEvent event) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {
			this.archivo = AdministradorArchivo.crearArchivo( event );
			this.urlImagen = AsistenteSubsidio.subirImagen(this.archivo, CUIT.quitarMascara(this.getTramite().getCuit()), 2020);
			this.getDatosActivos().agregarAdjunto(this.archivo, this.urlImagen);
			this.guardar();
		} catch (IOException | ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError("No se puedo completar la subida. Por favor, inténtelo nuevamente.");
		}
	}

	public void validarTramite() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		this.getTramite().validar();
		this.validarABL();
		try {
			AsistenteSubsidio.guardar(this.getTramite());
			AsistenteSubsidio.generarPDF(this.getTramite());
			AsistenteSubsidio.enviar(this.getTramite());
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError("Ocurrió un error al confirmar la declaración", e);
		}
		this.siguiente();
	}

	private void validarABL() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		for (DatosComercio d: this.getTramite().getDatos())
			this.validarCuentaAbl(d.getCuentaAbl());
	}

	public void validarCuentaAbl(Integer padron) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		try {
			AsistenteDatosActividad.validarCuentaABL( padron.toString() );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError("Partida ABL inválida", e);
		}
	}

	public synchronized void guardar() {
		try {
			AsistenteSubsidio.guardar(this.getTramite());
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			GenericLogger.getLogger().error("Fallo al guardar el tramite de subsidio", e);
		}
	}

	public void validarCuenta(DatosComercio d) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		d.validar();
		this.validarCuentaAbl(d.getCuentaAbl());
		this.guardar();
		Mensaje.addMessageAviso("El formulario de la cuenta se encuentra completo");
	}

	public String iconoSolapa(DatosComercio d) {
		return d.estaCompleto() ? "fas fa-check icon-success" : "fas fa-exclamation-triangle icon-warn";
	}

	public TramiteSubsidio getTramite() {
		return tramite;
	}

	public void setTramite(TramiteSubsidio tramite) {
		this.tramite = tramite;
	}

	public Archivo getArchivo() {
		return archivo;
	}

	public void setArchivo(Archivo archivo) {
		this.archivo = archivo;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	public DatosComercio getDatosActivos() {
		return datosActivos;
	}

	public void setDatosActivos(DatosComercio datosActivos) {
		this.datosActivos = datosActivos;
	}
}