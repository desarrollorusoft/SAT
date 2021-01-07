package ar.com.cognisys.sat.bean.pagina.externo.plataformaconsultas;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.jsfbean.abstracto.SeccionGeneralBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteAdministradorFormularioConsultas;
import ar.com.cognisys.sat.modelo.enums.TipoDatoConsultas;
import ar.com.cognisys.sat.modelo.validador.ValidadorFormularioConsultas;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;

@ManagedBean
@ViewScoped
public class FormularioPlataformaBean extends SeccionGeneralBean {

	private static final long serialVersionUID = -2808427299703943822L;
	
	@ManagedProperty( value = "#{formularioConsultasBean}" )
	private FormularioConsultasBean formularioConsultasBean;
	

	@Override
	public void cargar() throws ar.com.cognisys.common.exception.ExcepcionControladaError {
	}

	@Override
	public void cargar(Object consulta) throws ar.com.cognisys.common.exception.ExcepcionControladaError {
		this.getFormularioConsultasBean().cargar( (Consulta) consulta );
	}

	@Override
	public void siguiente() throws ar.com.cognisys.common.exception.ExcepcionControladaError {
	}

	@Override
	public void refrescar() throws ar.com.cognisys.common.exception.ExcepcionControladaError {
	}

	@Override
	protected void inicializacion() {
	}

	public void enviar() throws ExcepcionControladaError, ExcepcionControladaAlerta, IOException {
		try {
			if ( this.getFormularioConsultasBean().getDatoSeleccionado() != null && this.getFormularioConsultasBean().getDatoSeleccionado().getNombre().equals( TipoDatoConsultas.cuit.getDescripcion() ) ) {
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

			
			this.irA( NavegacionPlataformaConsultas.SOPORTE, this.getFormularioConsultasBean().getFormulario().getId() );
			
		} catch ( Exception e ) {
			throw new ExcepcionControladaAlerta( e.getMessage(), e );
		}
	}

	public void volver() throws ar.com.cognisys.common.exception.ExcepcionControladaError {
		this.irA( NavegacionPlataformaConsultas.LISTA );
	}
	
	

	public FormularioConsultasBean getFormularioConsultasBean() {
		return formularioConsultasBean;
	}

	public void setFormularioConsultasBean(FormularioConsultasBean formularioConsultasBean) {
		this.formularioConsultasBean = formularioConsultasBean;
	}

	
	
	
}
