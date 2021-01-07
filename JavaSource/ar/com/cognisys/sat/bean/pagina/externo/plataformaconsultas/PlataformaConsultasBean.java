package ar.com.cognisys.sat.bean.pagina.externo.plataformaconsultas;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaGeneralBean;
import ar.com.cognisys.common.jsfbean.factory.FactoryParametrizacionBean;
import ar.com.cognisys.common.jsfbean.parametrizacion.ParametrizacionGeneralBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteAdministradorFormularioConsultas;
import ar.com.cognisys.sat.bean.asistente.AsistentePlataformaConsultasBean;
import ar.com.cognisys.sat.core.correo.AdministradorMails;
import ar.com.cognisys.sat.core.modelo.comun.comunicacion.EncriptacionURL;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.ConsultaYLista;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.excepcion.SATRuntimeException;
import ar.com.cognisys.sat.modelo.factory.FactoryMensajeFormularioCategoria;
import ar.com.cognisys.sat.modelo.factory.FactoryMensajeFormularioConsultas;

@ManagedBean
@ViewScoped
public class PlataformaConsultasBean extends PaginaGeneralBean {

	private static final long serialVersionUID = 7610126602418671968L;

	private Consulta consulta;
	private List<Consulta> listaConsultas;

	@ManagedProperty( value = "#{listaConsultasBean}" )
	private ListaConsultasBean listaConsultasBean;

	@ManagedProperty( value = "#{soporteConsultaBean}" )
	private SoporteConsultaBean soporteConsultaBean;

	@ManagedProperty( value = "#{formularioPlataformaBean}" )
	private FormularioPlataformaBean formularioPlataformaBean;

	@ManagedProperty( value = "#{bloqueoPlataformaBean}" )
	private BloqueoPlataformaBean bloqueoPlataformaBean;

	@Override
	public void inicializacion() {
		Map<String, String> mapa = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		String idConsulta = this.recuperarParametro( mapa, "t" );
		String seccion = this.recuperarParametro( mapa, "b" );

		try {
			if ( idConsulta == null || seccion == null ) {
				inicializarParametrizacion( NavegacionPlataformaConsultas.BLOQUEO );
			} else {
				this.consulta = AsistentePlataformaConsultasBean.recuperarConsulta( idConsulta );
				navegarAseccion( seccion );

				this.getSoporteConsultaBean().cargar( this.getConsulta() );
				this.getListaConsultasBean().cargar( new ConsultaYLista( this.consulta, this.listaConsultas ) );
			}
			RequestContext.getCurrentInstance().update( "form:panelSecciones" );
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
		pg.agregar( NavegacionPlataformaConsultas.BLOQUEO, this.getBloqueoPlataformaBean(), "" );

		return pg;
	}

	@Override
	public void refrescar() throws ar.com.cognisys.common.exception.ExcepcionControladaError {}

	public void intervencionLista(NavegacionPlataformaConsultas nav, Consulta c) throws ExcepcionControladaError {
		try {
			this.setConsulta( AsistentePlataformaConsultasBean.recuperarConsulta( c.getId().toString() ) );
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
			
			AdministradorMails.enviar( FactoryMensajeFormularioConsultas.generarInstancia( c.getCorreo(), c.getId().toString(), host ) );
			AdministradorMails.enviar( FactoryMensajeFormularioCategoria.generarInstancia( c.getCategoria().getNombre(), AsistenteAdministradorFormularioConsultas.obtenerCategoriaCorreo( c.getCategoria() ) ) );
			
			this.getListaConsultas().add( c );
			this.avanzar( nav, c );
		} catch ( ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e ) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public void intervencionFormulario(NavegacionPlataformaConsultas nav) throws ExcepcionControladaError {
		this.avanzar( nav );
	}

	public void intervencionSoporte(NavegacionPlataformaConsultas nav) throws ExcepcionControladaError {
		if (nav.equals(NavegacionPlataformaConsultas.LISTA)) {
			this.avanzar(nav, new ConsultaYLista(this.consulta, this.listaConsultas));
			return;
		}

		if (nav.equals(NavegacionPlataformaConsultas.FORMULARIO)) {
			this.avanzar(nav, this.consulta);
			return;
		}

		this.avanzar(nav);
	}

	private String recuperarParametro(Map<String, String> mapa, String clave) {

		String dato = null;

		try {
			dato = EncriptacionURL.decode( mapa.get( clave ) );

			if ( dato == null )
				dato = EncriptacionURL.decode( mapa.get( clave ) + "=" );
		} catch ( Exception e ) {
			throw new SATRuntimeException(e);
		}

		return dato;
	}

	private void navegarAseccion(String seccion) throws ExcepcionControladaError {
		try {
			if ( this.getConsulta() == null )
				inicializarParametrizacion( NavegacionPlataformaConsultas.BLOQUEO );
			else {
				this.setListaConsultas( AsistentePlataformaConsultasBean.recuperarConsultasAsociadas( this.getConsulta() ) );
				decidirSeccion( seccion );
			}
		} catch ( ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e ) {
			throw new ExcepcionControladaError( e );
		}
	}

	private void decidirSeccion(String seccion) throws ExcepcionControladaError {
		if ( seccion.equals( "soporte" ) )
			inicializarParametrizacion( NavegacionPlataformaConsultas.SOPORTE );
		else {
			inicializarParametrizacion( NavegacionPlataformaConsultas.LISTA );
			this.getFormularioPlataformaBean().cargar( this.getConsulta() );
		}
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

	public void irAError() throws ExcepcionControladaError {
		this.avanzar( NavegacionPlataformaConsultas.BLOQUEO );
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

	public BloqueoPlataformaBean getBloqueoPlataformaBean() {
		return bloqueoPlataformaBean;
	}

	public void setBloqueoPlataformaBean(BloqueoPlataformaBean bloqueoPlataformaBean) {
		this.bloqueoPlataformaBean = bloqueoPlataformaBean;
	}

	public void setFormularioPlataformaBean(FormularioPlataformaBean formularioPlataformaBean) {
		this.formularioPlataformaBean = formularioPlataformaBean;
	}
}