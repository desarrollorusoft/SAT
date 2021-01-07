package ar.com.cognisys.sat.bean.privado.rs.declaracion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionWizardBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteDDJJPadron;
import ar.com.cognisys.sat.bean.converter.TipoCartelConverter;
import ar.com.cognisys.sat.core.administrador.AdministradorArchivo;
import ar.com.cognisys.sat.core.modelo.comun.Archivo;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.DDJJCarteleria;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.DDJJOEP;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.TipoCartel;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.TipoOEP;
import ar.com.cognisys.sat.core.modelo.comun.rs.PadronRS;
import ar.com.cognisys.sat.core.modelo.comun.rs.versiones.VersionPadronRS;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.factory.cuenta.ddjj.FactoryDDJJCarteleria;
import ar.com.cognisys.sat.core.modelo.factory.cuenta.ddjj.FactoryDDJJOEP;
import ar.com.cognisys.sat.core.modelo.factory.cuenta.ddjj.FactoryTipoCartel;
import ar.com.cognisys.sat.excepcion.Mensaje;
import ar.com.cognisys.sat.v2.core.modelo.contenedor.ContenedorCartelerias;

@ManagedBean(name = "PyPOEP")
@ViewScoped
public class PyPOepBean extends SeccionWizardBean {
	
	private static final long serialVersionUID = -8320913849340911566L;
	private PadronRS padron;
	private List<TipoCartel> tiposCarteleria;
	private TipoOEP[] tiposOep;
	private TipoOEP tipoOEPSeleccionado;
	private Float ancho;
	private Float alto;
	private Archivo archivo;
	private String urlImagen;
	private TipoCartel tipoCartelSeleccionado;
	private float valorOEP;
	private boolean poseeCarteleria;
	private boolean poseeOep;
	private TipoCartel cartelOtros;
	private VersionPadronRS versionEnCurso;
	private TipoCartelConverter tipoCartelConverter;
	private List<DDJJCarteleria> listaCarteleria;
	private List<DDJJOEP> listaOEP;
	private UploadedFile file;

	@Override
	protected void inicializacion() {
		this.setTiposOep( TipoOEP.values() );
		this.setTipoOEPSeleccionado( this.getTiposOep()[0] );
		this.setCartelOtros( FactoryTipoCartel.generar(0, "Otros") );
		this.poseeCarteleria = true;
		this.poseeOep = true;
	}

	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object dato) throws ExcepcionControladaError {
		this.setPadron((PadronRS) dato);
		this.setVersionEnCurso( this.getPadron().getVersionEnCurso() );
		this.setListaCarteleria( AsistenteDDJJPadron.obtenerCarteleria( this.getVersionEnCurso() ) );
		this.setListaOEP( AsistenteDDJJPadron.obtenerOEP( this.getVersionEnCurso() ) );
		this.refrescar();
	}

	@Override
	public void refrescar() throws ExcepcionControladaError {
		this.setTiposCarteleria( new ArrayList<TipoCartel>() );
		for (TipoCartel tc : ContenedorCartelerias.getInstancia().getLista( this.getPadron().getVersionEnCurso().getAno() ))
			this.getTiposCarteleria().add( tc );
		
		this.getTiposCarteleria().add( this.getCartelOtros() );
		this.setTipoCartelConverter( new TipoCartelConverter( this.getTiposCarteleria() ) );
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {
		try {
			validar();
			AsistenteDDJJPadron.actualizarPyP(this.getVersionEnCurso(), this.getListaCarteleria(), this.getListaOEP());
			this.irAlSiguiente(this.getPadron());
		} catch (ExcepcionControladaAlerta e) {
			Mensaje.addMessageWarn(e.getMessage());
		}
	}

	private void validar() throws ExcepcionControladaAlerta {
		AsistenteDDJJPadron.validarPyP(this.getVersionEnCurso(), poseeCarteleria, poseeOep);	
	}
	
	public void agregarNuevaCarteleria() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		if( this.getTipoCartelSeleccionado() == null 
				|| this.getAncho() == null 
				|| this.getAncho() <= 0
				|| this.getAlto() == null 
				|| this.getAlto() <= 0
				|| this.getArchivo() == null)
			throw new ExcepcionControladaAlerta( "Para agregar una carteleria primero debe completar todos los datos requeridos" );
		
		try {
			this.urlImagen = AsistenteDDJJPadron.subirImagenPyP(this.getArchivo(), this.getPadron().getNumero(), this.getVersionEnCurso().getAno(), this.getTipoCartelSeleccionado());
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e.getCause());
		}
		
//		event.getComponent().setTransient( false );
		
		DDJJCarteleria c = FactoryDDJJCarteleria.generar(this.getTipoCartelSeleccionado(), DDJJCarteleria.calcularDimension(this.getAlto() * this.getAncho()), "", this.getUrlImagen());
		this.getListaCarteleria().add( c );
		
		this.setTipoCartelSeleccionado(null);
		this.setAlto(null);
		this.setAncho(null);
		this.setUrlImagen(null);
		this.setArchivo(null);
	}

	public void agregarNuevaOep() throws ExcepcionControladaAlerta {
		if( this.getTipoOEPSeleccionado() == null || this.getValorOEP() <= 0)
			throw new ExcepcionControladaAlerta( "Para agregar un OEP primero debe completar todos los datos requeridos" );
		
		this.getListaOEP().add( FactoryDDJJOEP.generar(this.getTipoOEPSeleccionado(), this.getValorOEP()) );
		
		this.setTipoOEPSeleccionado(null);
		this.setValorOEP(0f);
	}
	
	public void subirArchivos(FileUploadEvent event) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {
			this.archivo = AdministradorArchivo.crearArchivo( event );
		} catch (IOException e) {
			throw new ExcepcionControladaError("No se puedo completar la subida. Por favor, inténtelo nuevamente.");
		}
	}
	
	public Integer posicionesDecimales() {
		if (this.getTipoOEPSeleccionado() == null || this.getTipoOEPSeleccionado().equals( TipoOEP.POSTE ))
			return 0;
		else
			return 2;
	}
	
	public void eliminarCarteleria(DDJJCarteleria c) {
		this.getListaCarteleria().remove( c );
	}
	
	public void eliminarOEP(DDJJOEP oep) {
		this.getListaOEP().remove( oep );
	}
	
	public String labelTipoOEP() {
		if (this.getTipoOEPSeleccionado() == null)
			return " ";
		else if (this.getTipoOEPSeleccionado().sos(TipoOEP.TOLDO))
			return "Metros lineales";
		else
			return "Unidades";
	}
	
	public String descripcionOEP(TipoOEP tipo) {
		if (tipo == TipoOEP.TOLDO)
			return "TOLDO/MARQUESINA";
		else
			return tipo.name();
	}

	public void actualizarPanelCarteleria() {
		if ( !poseeCarteleria )
			this.getListaCarteleria().clear();
	}
	
	public void actualizarPanelOEP() {
		if ( !poseeOep )
			this.getListaOEP().clear();
	}
	
	public PadronRS getPadron() {
		return padron;
	}

	public void setPadron(PadronRS padron) {
		this.padron = padron;
	}

	public List<TipoCartel> getTiposCarteleria() {
		return tiposCarteleria;
	}

	public void setTiposCarteleria(List<TipoCartel> tiposCarteleria) {
		this.tiposCarteleria = tiposCarteleria;
	}

	public TipoOEP[] getTiposOep() {
		return tiposOep;
	}

	public void setTiposOep(TipoOEP[] tiposOep) {
		this.tiposOep = tiposOep;
	}

	public Float getAncho() {
		return ancho;
	}

	public void setAncho(Float ancho) {
		this.ancho = ancho;
	}

	public Float getAlto() {
		return alto;
	}

	public void setAlto(Float alto) {
		this.alto = alto;
	}

	public boolean isPoseeCarteleria() {
		return poseeCarteleria;
	}

	public void setPoseeCarteleria(boolean poseeCarteleria) {
		this.poseeCarteleria = poseeCarteleria;
	}

	public boolean isPoseeOep() {
		return poseeOep;
	}

	public void setPoseeOep(boolean poseeOep) {
		this.poseeOep = poseeOep;
	}

	public TipoCartel getTipoCartelSeleccionado() {
		return tipoCartelSeleccionado;
	}

	public void setTipoCartelSeleccionado(TipoCartel tipoCartelSeleccionado) {
		this.tipoCartelSeleccionado = tipoCartelSeleccionado;
	}

	public TipoOEP getTipoOEPSeleccionado() {
		return tipoOEPSeleccionado;
	}

	public void setTipoOEPSeleccionado(TipoOEP tipoOEPSeleccionado) {
		this.tipoOEPSeleccionado = tipoOEPSeleccionado;
	}

	public float getValorOEP() {
		return valorOEP;
	}

	public void setValorOEP(float valorOEP) {
		this.valorOEP = valorOEP;
	}

	public TipoCartelConverter getTipoCartelConverter() {
		return tipoCartelConverter;
	}

	public void setTipoCartelConverter(TipoCartelConverter tipoCartelConverter) {
		this.tipoCartelConverter = tipoCartelConverter;
	}

	public TipoCartel getCartelOtros() {
		return cartelOtros;
	}

	public void setCartelOtros(TipoCartel cartelOtros) {
		this.cartelOtros = cartelOtros;
	}

	public VersionPadronRS getVersionEnCurso() {
		return versionEnCurso;
	}

	public void setVersionEnCurso(VersionPadronRS versionEnCurso) {
		this.versionEnCurso = versionEnCurso;
	}

	public List<DDJJCarteleria> getListaCarteleria() {
		return listaCarteleria;
	}

	public void setListaCarteleria(List<DDJJCarteleria> listaCarteleria) {
		this.listaCarteleria = listaCarteleria;
	}

	public List<DDJJOEP> getListaOEP() {
		return listaOEP;
	}

	public void setListaOEP(List<DDJJOEP> listaOEP) {
		this.listaOEP = listaOEP;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	public Archivo getArchivo() {
		return archivo;
	}

	public void setArchivo(Archivo archivo) {
		this.archivo = archivo;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
}