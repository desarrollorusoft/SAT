package ar.com.cognisys.sat.bean.privado.be;

import java.io.IOException;
import java.net.URL;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.ModalBean;
import ar.com.cognisys.sat.modelo.comun.Mensaje;

@ManagedBean(name = "ModalAdhesionBE")
@ViewScoped
public class ModalAdhesionBEBean extends ModalBean {
	
	private static final long serialVersionUID = 520703515715079140L;
	private boolean descargoEnlace;
	private IBoletaElectronicaBean padre;
	private StreamedContent archivo;
	
	@Override
	protected void inicializacion() {
		this.inicializarModal();
	}
	
	@Override
	protected void inicializarModal() {
		this.setNombreModal("ModalAdhesionBE");
	}
	
	public void mostrar(IBoletaElectronicaBean padre) {
		try {
			this.setPadre(padre);
			this.setDescargoEnlace(false);
			
			URL url = new URL("https://www.vicentelopez.gov.ar/static-iipp/p/SAT/pdfs/Terminos_y_Condiciones-Boleta_Electronica.pdf");
			archivo = new DefaultStreamedContent(url.openStream(), "application/pdf", "Terminos y Condiciones - Boleta Electronica.pdf");
		
			RequestContext.getCurrentInstance().execute( "PF('"+this.getNombreModal()+"').show()" );
			RequestContext.getCurrentInstance().update( "form:"+this.getNombreModal() );
		} catch (IOException e) {
			Mensaje.emitirMensajeError("No se pudo recuperar el documento de Términos y Condiciones");
		}
	}

	public void mostrarTyC() {
		this.setDescargoEnlace(true);
	}
	
	public void confirmar() throws ExcepcionControladaError {
		if (this.isDescargoEnlace()) {
			this.getPadre().adherir();
			RequestContext.getCurrentInstance().execute( "PF('"+this.getNombreModal()+"').hide()" );
			RequestContext.getCurrentInstance().update( ":form:cuentas" );
		} else
			Mensaje.emitirMensajeAlerta("Debe descargar los Términos y Condiciones");
	}

	public boolean isDescargoEnlace() {
		return descargoEnlace;
	}

	public void setDescargoEnlace(boolean descargoEnlace) {
		this.descargoEnlace = descargoEnlace;
	}

	public IBoletaElectronicaBean getPadre() {
		return padre;
	}

	public void setPadre(IBoletaElectronicaBean padre) {
		this.padre = padre;
	}

	public StreamedContent getArchivo() {
		return archivo;
	}

	public void setArchivo(StreamedContent archivo) {
		this.archivo = archivo;
	}
}