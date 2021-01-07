package ar.com.cognisys.sat.bean.pagina.externo.plataformaconsultas;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import ar.com.cognisys.sat.bean.asistente.AsistenteAdministradorFormularioConsultas;
import ar.com.cognisys.sat.core.correo.EnviadorCorreoRunnable;
import ar.com.cognisys.sat.core.correo.MensajeCorreo;
import ar.com.cognisys.sat.core.modelo.comun.comunicacion.EncriptacionURL;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.excepcion.Mensaje;
import ar.com.cognisys.sat.modelo.enums.TipoDatoConsultas;
import ar.com.cognisys.sat.modelo.factory.FactoryMensajeFormularioCategoria;
import ar.com.cognisys.sat.modelo.factory.FactoryMensajeFormularioConsultas;
import ar.com.cognisys.sat.modelo.validador.ValidadorFormularioConsultas;

@ManagedBean
@ViewScoped
public class FormularioConsultasGeneralBean implements Serializable {

	private static final long serialVersionUID = 9081432317341902836L;

	@ManagedProperty( value = "#{formularioConsultasBean}")
	private FormularioConsultasBean formularioConsultasBean;

	public void enviar() throws ExcepcionControladaError {
		try {
			this.enviarConsulta();
			this.envioNotificacion();
			this.redireccionamiento();
		} catch (ExcepcionControladaError e ) {
			throw e;
		} catch ( ExcepcionControladaAlerta e2 ){
			Mensaje.addMessageWarn(e2.getMessage());
		}
	}
	
	private void enviarConsulta() throws ExcepcionControladaError,ExcepcionControladaAlerta  {
		
		try {
			if ( this.getFormularioConsultasBean().getDatoSeleccionado() != null && 
				 this.getFormularioConsultasBean().getDatoSeleccionado().getNombre() != null && 
				 this.getFormularioConsultasBean().getDatoSeleccionado().getNombre().equals( TipoDatoConsultas.cuit.getDescripcion() ) ) {
				
				this.getFormularioConsultasBean().getFormulario().setDato( this.getFormularioConsultasBean().getFormulario().getCuit() );
			}
			
			ValidadorFormularioConsultas.validar( this.getFormularioConsultasBean().getFormulario(), 
												  this.getFormularioConsultasBean().getCategoriaSeleccionada(), 
												  this.getFormularioConsultasBean().getSubcategoriaSeleccionada(), 
												  this.getFormularioConsultasBean().getCaracterSeleccionado(), 
												  this.getFormularioConsultasBean().getDatoSeleccionado() );

			if (!this.getFormularioConsultasBean().getCategoriaSeleccionada().getNombre().equals("Comercio e Industria")) {
				AsistenteAdministradorFormularioConsultas.enviarConsulta(this.getFormularioConsultasBean().getFormulario(),
						this.getFormularioConsultasBean().getCategoriaSeleccionada(),
						this.getFormularioConsultasBean().getSubcategoriaSeleccionada(),
						this.getFormularioConsultasBean().getCaracterSeleccionado(),
						this.getFormularioConsultasBean().getDatoSeleccionado());
			}
			else {
				AsistenteAdministradorFormularioConsultas.enviarConsulta(this.getFormularioConsultasBean().getFormulario(),
						this.getFormularioConsultasBean().getCategoriaSeleccionada(),
						this.getFormularioConsultasBean().getDatoSeleccionado());
			}
		} catch ( ExcepcionControladaError e ) {
			throw e;
		} catch ( ExcepcionControladaAlerta  e2 ) {
			throw e2;
		}
	}
	
	private void envioNotificacion() {
		
		MensajeCorreo m = null; 
		
		try {
			/* Envio del Correo al Contribuyente */
			m = FactoryMensajeFormularioConsultas.generarInstancia( this.getFormularioConsultasBean().getFormulario().getCorreo(), 
																    this.getFormularioConsultasBean().getFormulario().getId().toString(), 
																    this.obtenerHost() );
			(new Thread( new EnviadorCorreoRunnable(m) )).start();
		} catch ( Exception e ) {
			// No es necesario, Notificar error por mail
		}
		
		try {
			this.getFormularioConsultasBean().setCorreoCategoria( AsistenteAdministradorFormularioConsultas.obtenerCategoriaCorreo( this.getFormularioConsultasBean().getCategoriaSeleccionada() ) );
			
			/* Envio del Correo al Grupo de Soporte */
			m = FactoryMensajeFormularioCategoria.generarInstancia( this.getFormularioConsultasBean().getCategoriaSeleccionada().getNombre(), 
					   												this.getFormularioConsultasBean().getCorreoCategoria() );
			(new Thread( new EnviadorCorreoRunnable(m) )).start();
		} catch ( Exception e ) {
			// No es necesario, Notificar error por mail
		}
	}
	
	private String obtenerHost() {
		
		String host = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get( "referer" );
		host = host.substring( 0,host.indexOf( "/pages" ) );
		
		return host;
	}

	private void redireccionamiento() throws ExcepcionControladaError {
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect( generarURLConsulta() );
		} catch (IOException e) {
			throw new ExcepcionControladaError("No se pudo acceder a su Bandeja de Conultas y Reclamos. Por favor, verifique su bandeja de correo.", e);
		}
	}
	
	private String generarURLConsulta() throws ExcepcionControladaError {
		String idFormulario = this.getFormularioConsultasBean().getFormulario().getId().toString();
		
		String formulario = EncriptacionURL.encode( idFormulario );
		String soporte = EncriptacionURL.encode( "soporte" );
		
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+"/pages/externos/plataforma-consultas.xhtml?t="+formulario+"&b="+soporte;
	}

	public void volver() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect( "https://www.vicentelopez.gov.ar/ingresos-publicos/" );
	}
	
	public FormularioConsultasBean getFormularioConsultasBean() {
		return formularioConsultasBean;
	}

	public void setFormularioConsultasBean(FormularioConsultasBean formularioConsultasBean) {
		this.formularioConsultasBean = formularioConsultasBean;
	}
}