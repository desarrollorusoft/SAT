package ar.com.cognisys.sat.bean.privado.consultas;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaGeneralBean;
import ar.com.cognisys.common.jsfbean.factory.FactoryParametrizacionBean;
import ar.com.cognisys.common.jsfbean.parametrizacion.ParametrizacionGeneralBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteAdministradorFormularioConsultas;
import ar.com.cognisys.sat.bean.asistente.AsistenteConsultasBean;
import ar.com.cognisys.sat.bean.asistente.AsistentePlataformaConsultasBean;
import ar.com.cognisys.sat.bean.pagina.externo.plataformaconsultas.FormularioPlataformaBean;
import ar.com.cognisys.sat.bean.pagina.externo.plataformaconsultas.ListaConsultasBean;
import ar.com.cognisys.sat.bean.pagina.externo.plataformaconsultas.NavegacionPlataformaConsultas;
import ar.com.cognisys.sat.bean.pagina.externo.plataformaconsultas.SoporteConsultaBean;
import ar.com.cognisys.sat.core.correo.AdministradorMails;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.ConsultaYLista;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.modelo.factory.FactoryMensajeFormularioCategoria;

@ManagedBean
@ViewScoped
public class ConsultasBean extends PaginaGeneralBean {

	private static final long serialVersionUID = 7610126602418671968L;

	private Consulta consulta;
	private List<Consulta> listaConsultas;

	@ManagedProperty( value = "#{listaConsultasBean}" )
	private ListaConsultasBean listaConsultasBean;

	@ManagedProperty( value = "#{soporteConsultaBean}" )
	private SoporteConsultaBean soporteConsultaBean;

	@ManagedProperty( value = "#{formularioPlataformaBean}" )
	private FormularioPlataformaBean formularioPlataformaBean;

	@Override
	public void inicializacion() {
		Map<String, String> mapa = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		String idConsulta = mapa.get("t");
		String seccion = mapa.get( "s" );
		
		try {
			this.listaConsultas = AsistenteConsultasBean.recuperarPorCuit();
			if ( idConsulta == null ) {
				this.consulta = AsistenteConsultasBean.generarNuevaConsulta();
				inicializarParametrizacion( seccion == null ? NavegacionPlataformaConsultas.LISTA : NavegacionPlataformaConsultas.FORMULARIO );
			} else {
				this.consulta = AsistentePlataformaConsultasBean.recuperarConsulta( idConsulta );

				this.getSoporteConsultaBean().cargar( this.getConsulta() );
				
				inicializarParametrizacion( NavegacionPlataformaConsultas.SOPORTE );
			}
			this.getListaConsultasBean().cargar( new ConsultaYLista( this.consulta, this.listaConsultas ) );
		} catch ( ExcepcionControladaError e ) {
			throw new RuntimeException( e );
		} catch ( ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e ) {
			throw new RuntimeException( e );
		}

	}

	private void inicializarParametrizacion(NavegacionPlataformaConsultas clave) {

		this.inicializarGeneralBean( clave );

	}

	@Override
	protected ParametrizacionGeneralBean generarParametrizacion() {

		ParametrizacionGeneralBean pg = FactoryParametrizacionBean.generarIntancia();

		pg.agregar( NavegacionPlataformaConsultas.LISTA, this.getListaConsultasBean(), "intervencionLista" );
		pg.agregar( NavegacionPlataformaConsultas.FORMULARIO, this.getFormularioPlataformaBean(), "intervencionFormulario" );
		pg.agregar( NavegacionPlataformaConsultas.SOPORTE, this.getSoporteConsultaBean(), "intervencionSoporte" );

		return pg;
	}

	@Override
	public void refrescar() throws ar.com.cognisys.common.exception.ExcepcionControladaError {
	}

	public void intervencionLista(NavegacionPlataformaConsultas nav, Consulta c) throws ExcepcionControladaError {
		try {
			if(c.getId()!=null)
				this.setConsulta( AsistentePlataformaConsultasBean.recuperarConsulta( c.getId().toString() ) );
			else this.setConsulta(AsistenteConsultasBean.generarNuevaConsulta() );
			this.avanzar( nav, this.consulta );
		} catch ( ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e ) {
			throw new ExcepcionControladaError( e );
		}
	}

	public void intervencionFormulario(NavegacionPlataformaConsultas nav, Long idConsulta) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		
		try {
			Consulta c = AsistentePlataformaConsultasBean.recuperarConsulta( idConsulta.toString() );
			String host = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get( "referer" );
			host = host.substring( 0,host.indexOf( "/pages" ) );
			
			AdministradorMails.enviar( FactoryMensajeFormularioCategoria.generarInstancia( c.getCategoria().getNombre(), AsistenteAdministradorFormularioConsultas.obtenerCategoriaCorreo( c.getCategoria() ) ) );
			
			this.getListaConsultas().add( c );
			this.avanzar( nav, c );
		} catch ( ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e ) {
			throw new ExcepcionControladaError(e);
		}
	}

	public void intervencionFormulario(NavegacionPlataformaConsultas nav) throws ExcepcionControladaError {
		this.avanzar( nav );
	}

	public void intervencionSoporte(NavegacionPlataformaConsultas nav) throws ExcepcionControladaError {
		this.avanzar( nav );
	}


	public void irALista(Consulta c) throws ExcepcionControladaError {
		this.avanzar( NavegacionPlataformaConsultas.LISTA, this.getConsulta() );
	}

	public void irASoporte(Consulta c) throws ExcepcionControladaError {
		this.getSoporteConsultaBean().cargar( c );
	}

	public void irAFormulario() throws ExcepcionControladaError {
		this.getFormularioPlataformaBean().cargar( this.getConsulta() );
	}


	public boolean isLista() {
		return this.muestro( NavegacionPlataformaConsultas.LISTA.name() );
	}

	public boolean isSoporte() {
		return this.muestro( NavegacionPlataformaConsultas.SOPORTE.name() );
	}

	public boolean isFormulario() {
		return this.muestro( NavegacionPlataformaConsultas.FORMULARIO.name() );
	}

	public boolean isError() {
		return this.muestro( NavegacionPlataformaConsultas.BLOQUEO.name() );
	}

	public ListaConsultasBean getListaConsultasBean() {
		return listaConsultasBean;
	}

	public void setListaConsultasBean(ListaConsultasBean listaConsultasBean) {
		this.listaConsultasBean = listaConsultasBean;
	}

	public SoporteConsultaBean getSoporteConsultaBean() {
		return soporteConsultaBean;
	}

	public void setSoporteConsultaBean(SoporteConsultaBean soporteConsultaBean) {
		this.soporteConsultaBean = soporteConsultaBean;
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public FormularioPlataformaBean getFormularioPlataformaBean() {
		return formularioPlataformaBean;
	}

	public List<Consulta> getListaConsultas() {
		return listaConsultas;
	}

	public void setListaConsultas(List<Consulta> listaConsultas) {
		this.listaConsultas = listaConsultas;
	}

	public void setFormularioPlataformaBean(FormularioPlataformaBean formularioPlataformaBean) {
		this.formularioPlataformaBean = formularioPlataformaBean;
	}

}
