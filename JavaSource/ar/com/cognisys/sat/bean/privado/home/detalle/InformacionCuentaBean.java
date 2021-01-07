package ar.com.cognisys.sat.bean.privado.home.detalle;

import java.io.IOException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.sat.bean.Map4JSF;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteInformacionCuenta;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.excepcion.Mensaje;

@ManagedBean(name = "InformacionCuenta")
@ViewScoped
public class InformacionCuentaBean extends SeccionBean {
	
	private static final long serialVersionUID = -8949646055281700588L;
	private Cuenta cuenta;
	private Map4JSF mapa; 
	private List<Consulta> listaConsultas;
	private String aliasAnterior;
	
	@Override
	protected void inicializacion() {}
	
	@Override
	public void cargar() {}

	@Override
	public void cargar(Object cuenta) throws ExcepcionControladaError {
		this.setCuenta((Cuenta) cuenta);
		this.setAliasAnterior(this.getCuenta().getAlias());
		this.setMapa(new Map4JSF( AsistenteInformacionCuenta.generarMapaDatos(this.getCuenta()) ));
		try {
			this.setListaConsultas( AsistenteInformacionCuenta.recueprarConsultas(SesionBean.getUsuarioLogueado().getCuit(), this.getCuenta()) );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
	
	@Override
	public void refrescar() {}

	@Override
	public void siguiente() throws ExcepcionControladaError {}

	public void actualizarCuenta() throws ExcepcionControladaError {
		if (!this.getAliasAnterior().equals(this.getCuenta().getAlias())) {
			try {
				AdministradorCuenta.actualizarAlias(this.getCuenta() , SesionBean.getUsuarioLogueado().getIdUsuario() );
				this.setAliasAnterior(this.getCuenta().getAlias());
				Mensaje.addMessageAviso("Se actualizó el alias con éxito");
			} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
				throw new ExcepcionControladaError(e.getMessage(), e);
			}
		}
	}
	
	public void mostrarConsulta(Consulta consulta) throws ExcepcionControladaError {
		try {
			String url = obtenerUrl();
			url += "/pages/pri/consultas.xhtml?t=" + consulta.getId().toString();
			FacesContext.getCurrentInstance().getExternalContext().redirect( url );
		} catch ( IOException e ) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public void nuevaConsulta() throws ExcepcionControladaError {
		try {
			String url = obtenerUrl();

			url += "/pages/pri/consultas.xhtml?s=f";
			FacesContext.getCurrentInstance().getExternalContext().redirect( url );
		} catch ( IOException e ) {
			throw new ExcepcionControladaError( e );
		}
	}
	
	private String obtenerUrl() {
		String host = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get( "referer" );
		return host.substring( 0, host.indexOf( "/pages" ) );
	}
	
	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Map4JSF getMapa() {
		return mapa;
	}

	public void setMapa(Map4JSF mapa) {
		this.mapa = mapa;
	}

	public List<Consulta> getListaConsultas() {
		return listaConsultas;
	}

	public void setListaConsultas(List<Consulta> listaConsultas) {
		this.listaConsultas = listaConsultas;
	}

	public String getAliasAnterior() {
		return aliasAnterior;
	}

	public void setAliasAnterior(String aliasAnterior) {
		this.aliasAnterior = aliasAnterior;
	}
	
}