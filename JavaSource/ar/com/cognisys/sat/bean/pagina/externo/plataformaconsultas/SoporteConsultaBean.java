package ar.com.cognisys.sat.bean.pagina.externo.plataformaconsultas;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionGeneralBean;
import ar.com.cognisys.sat.bean.asistente.AsistentePlataformaConsultasBean;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.core.modelo.factory.consultas.FactoryNuevaConsulta;

@ManagedBean
@ViewScoped
public class SoporteConsultaBean extends SeccionGeneralBean {

	private static final long serialVersionUID = 6977245631177525916L;

	private Consulta consulta;

	@ManagedProperty( value = "#{chatBean}" )
	private ChatBean chatBean;

	@Override
	public void cargar() throws ExcepcionControladaError {
	}

	@Override
	public void cargar(Object consulta) throws ExcepcionControladaError {
		this.setConsulta( ( Consulta ) consulta );
		try {
			this.getConsulta().setArchivos( AsistentePlataformaConsultasBean.recuperarArchivos( this.getConsulta() ) );

			if ( this.consulta.getDescripcion() != null && !this.consulta.getDescripcion().isEmpty() )
				this.consulta.getListaConsultasAsociadas().add( 0,
						FactoryNuevaConsulta.generar( this.consulta.getId().intValue(), this.consulta.getDescripcion(), true, this.consulta.getFechaCreacion() ) );

			this.getChatBean().cargar( this.consulta );
		} catch ( ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e ) {
			throw new ExcepcionControladaError( e );
		}
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {
	}

	@Override
	public void refrescar() throws ExcepcionControladaError {
	}

	@Override
	protected void inicializacion() {
	}

	public void irAFormulario() throws ExcepcionControladaError{
		this.irA( NavegacionPlataformaConsultas.FORMULARIO);
	}


	public void irALista() throws ExcepcionControladaError {
		this.irA( NavegacionPlataformaConsultas.LISTA );
	}
	
	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public ChatBean getChatBean() {
		return chatBean;
	}

	public void setChatBean(ChatBean chatBean) {
		this.chatBean = chatBean;
	}
}