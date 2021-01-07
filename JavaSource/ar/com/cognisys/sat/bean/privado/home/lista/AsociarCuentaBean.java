package ar.com.cognisys.sat.bean.privado.home.lista;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteAsociarCuentas;
import ar.com.cognisys.sat.core.administrador.AdministradorComercio;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.natatorios.CuentaPileta;
import ar.com.cognisys.sat.core.modelo.comun.rs.Comercio;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.enums.TiposDocumento;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.validador.ValidadorContribuyente;

import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "AsociarCuenta")
@ViewScoped
public class AsociarCuentaBean extends SeccionBean {
	
	private static final long serialVersionUID = -8949646055281700588L;
	private TiposCuentas[] tiposCuenta;
	private TiposCuentas tipoSeleccionado;
	private String padron;
	private String digitoVerificador;
	private Cuenta cuentaBuscada;
	private String contribuyente;
	private String descripcion;
	private String alias;
	private String tipoDocumento;
	private String nroDocumento;
	private boolean encontrado;
	
	@ManagedProperty( value="#{ListaCuentas}" )
	private ListaCuentasBean listaCuentasBean;
	
	public AsociarCuentaBean() {
		this.refrescar();
	}
	
	@Override
	protected void inicializacion() {}
	
	@Override
	public void cargar() {}

	@Override
	public void refrescar() {
		this.setTiposCuenta( TiposCuentas.values() );
		this.reiniciar();
	}

	@Override
	public void cargar(Object arg0) throws ExcepcionControladaError {}

	@Override
	public void siguiente() throws ExcepcionControladaError {}
	
	public void buscarCuenta() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		this.validar();
		
		if ( this.esComercio() ) {
			Comercio comercio;
			try {
				comercio = AdministradorComercio.recuperarComercioAsociar( this.getPadron(), SesionBean.getUsuarioLogueado() );
			} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
				throw new ExcepcionControladaError(e);
			}
			
			if ( comercio == null )
				throw new ExcepcionControladaAlerta("La cuenta no se encuentra en el sistema");
			
			this.setContribuyente( comercio.getRazonSocial() );
			this.setDescripcion( comercio.getCuit() );
			this.setEncontrado(true);
		} else if (!this.tipoSeleccionado.sos( "PILETAS" )) {
			Cuenta cuentaBuscada = AsistenteAsociarCuentas.buscarCuenta(SesionBean.getUsuarioLogueado(),
																	    this.getTipoSeleccionado(), 
																	    this.getPadron(), 
																	    this.getDigitoVerificador());
			if (cuentaBuscada != null) {
				this.setContribuyente( cuentaBuscada.getContribuyente().getNombreApellido() );
				this.setDescripcion( cuentaBuscada.getDescripcion() );
				this.setCuentaBuscada( cuentaBuscada );
				this.setEncontrado(true);
			}
		} else {
			Cuenta cuentaBuscada = AsistenteAsociarCuentas.buscarCuentaPileta(SesionBean.getUsuarioLogueado(),
					this.getTipoDocumento(),
					this.getNroDocumento());
			if (cuentaBuscada != null) {
				CuentaPileta cuentaBuscadaPileta = ((CuentaPileta) cuentaBuscada);
				String nombreYApellidoSocio = (cuentaBuscadaPileta.getNombre() + " " + cuentaBuscadaPileta.getApellido()).toUpperCase();
				this.setContribuyente( nombreYApellidoSocio );
				this.setDescripcion( nombreYApellidoSocio );
				this.setCuentaBuscada( cuentaBuscada );
				this.setEncontrado(true);
			}
		}
	}
	
	private void validar() throws ExcepcionControladaAlerta {
		ValidadorContribuyente.validarDatosCuenta(this.getTipoSeleccionado(), this.getPadron(), this.getDigitoVerificador());
	}

	public void asociarCuenta() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		if (esComercio())
			AsistenteAsociarCuentas.asociarRS(SesionBean.getUsuarioLogueado(), this.getAlias());
		else
			AsistenteAsociarCuentas.asociar(SesionBean.getUsuarioLogueado(), this.getCuentaBuscada(), this.getAlias());
		
		this.getListaCuentasBean().refrescar();
		this.reiniciar();
	}
	
	public void reiniciar() {
		this.setTipoSeleccionado(TiposCuentas.ABL);
		this.setPadron("");
		this.setDigitoVerificador("");
		this.setCuentaBuscada(null);
		this.setContribuyente(null);
		this.setDescripcion(null);
		this.setAlias("");
		this.setEncontrado(false);
	}

	public boolean muestroTipoDocumento() {
		return this.getTipoSeleccionado() != null && this.getTipoSeleccionado().sos( "PILETAS" );
	}

	public List<String> obtenerTiposDocumento() {
		TiposDocumento[] tipos = TiposDocumento.class.getEnumConstants();
		List<String> tiposStr = new ArrayList<String>();
		for (TiposDocumento tipo : tipos)
			tiposStr.add(tipo.getNombrePiletas());
		return tiposStr;
	}
	
	public boolean muestroResultado() {
		return this.isEncontrado();
	}
	
	public String getLabelDenominacion() {
		return this.getTipoSeleccionado().getDocumento();
	}
	
	public boolean muestroDV() {
		return this.getTipoSeleccionado().equals(TiposCuentas.ABL);
	}
	
	public boolean esComercio() {
		return this.getTipoSeleccionado() != null && this.getTipoSeleccionado().sos(TiposCuentas.COMERCIOS);
	}

	public boolean cuentaBuscadaEsPileta() {
		return this.getCuentaBuscada() != null && this.getCuentaBuscada().esPileta();
	}

	public TiposCuentas[] getTiposCuenta() {
		return tiposCuenta;
	}

	public void setTiposCuenta(TiposCuentas[] tiposCuenta) {
		this.tiposCuenta = tiposCuenta;
	}

	public TiposCuentas getTipoSeleccionado() {
		return tipoSeleccionado;
	}

	public void setTipoSeleccionado(TiposCuentas tipoSeleccionado) {
		this.tipoSeleccionado = tipoSeleccionado;
	}

	public String getPadron() {
		if (esComercio())
			return SesionBean.getUsuarioLogueado().getCuit();
		else
			return padron;
	}

	public void setPadron(String padron) {
		if (padron != null)
			this.padron = padron.toUpperCase();
		else
			this.padron = padron;
	}

	public String getDigitoVerificador() {
		return digitoVerificador;
	}

	public void setDigitoVerificador(String digitoVerificador) {
		this.digitoVerificador = digitoVerificador;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Cuenta getCuentaBuscada() {
		return cuentaBuscada;
	}

	public void setCuentaBuscada(Cuenta cuentaBuscada) {
		this.cuentaBuscada = cuentaBuscada;
	}

	public String getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(String contribuyente) {
		this.contribuyente = contribuyente;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isEncontrado() {
		return encontrado;
	}

	public void setEncontrado(boolean encontrado) {
		this.encontrado = encontrado;
	}

	public ListaCuentasBean getListaCuentasBean() {
		return listaCuentasBean;
	}

	public void setListaCuentasBean(ListaCuentasBean listaCuentasBean) {
		this.listaCuentasBean = listaCuentasBean;
	}

	public String getTipoDocumento() { return tipoDocumento; }

	public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

	public String getNroDocumento() { return nroDocumento; }

	public void setNroDocumento(String nroDocumento) { this.nroDocumento = nroDocumento; }

}