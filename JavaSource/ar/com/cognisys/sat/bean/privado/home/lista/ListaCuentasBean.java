package ar.com.cognisys.sat.bean.privado.home.lista;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.sat.bean.privado.home.detalle.ISolicitanteModalCambioAlias;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import org.primefaces.context.RequestContext;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionWizardBean;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteListaCuenta;
import ar.com.cognisys.sat.bean.privado.home.detalle.ModalAliasBean;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.CuentasUsuario;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;

@ManagedBean(name = "ListaCuentas")
@ViewScoped
public class ListaCuentasBean extends SeccionWizardBean implements ISolicitanteModalCambioAlias {
	
	private static final long serialVersionUID = -8949646055281700588L;
	private CuentasUsuario cuentas;
	private TiposCuentas[] tiposCuenta;
	private Cuenta cuentaADesvincular;
	private boolean tablaSeleccionada;
	private Cuenta cuentaParaAlias;

	@ManagedProperty( value="#{ModalAlias}" )
	private ModalAliasBean modalAliasBean;

	@Override
	protected void inicializacion() {
		this.getModalAliasBean().setBeanPadre( this );
	}
	
	@Override
	public void cargar() {
		this.refrescar();
	}

	@Override
	public void refrescar() {
		this.setCuentas( SesionBean.getUsuarioLogueado().getCuentas() );
		TiposCuentas[] tcs = this.getCuentas().obtenerTiposCuenta();
		Arrays.sort(tcs);
		this.setTiposCuenta(tcs);
		this.setCuentaADesvincular(null);
		this.tablaSeleccionada = this.cuentas.getListaCuentas().size() > 6;
		RequestContext.getCurrentInstance().update("form:cambio-disposicion");
	}

	@Override
	public void cargar(Object arg0) throws ExcepcionControladaError {}

	@Override
	public void siguiente() throws ExcepcionControladaError {}

	public void cargarModalAlias( Cuenta c ){
		this.setCuentaParaAlias( c );
		this.modalAliasBean.cargar( this, c.getAlias() );
	}

	public List<Cuenta> recuperarCuentas(TiposCuentas tipo) {
		return this.getCuentas().obtenerCuentas(tipo);
	}
	
	public void detalle(Cuenta cuenta) throws ExcepcionControladaError {
		super.irAlSiguiente(cuenta);
	}

	public void irDetalle(Cuenta cuenta) throws ExcepcionControladaError {
		super.irAlSiguiente(cuenta);
	}
	
	public void desvincular(Cuenta cuenta) {
		this.setCuentaADesvincular(cuenta);
		RequestContext.getCurrentInstance().execute( "PF('AlertaDesvinculacion').show()");
	}
	
	public void confirmarDesvinculacion() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		AsistenteListaCuenta.desvincular(SesionBean.getUsuarioLogueado(), this.getCuentaADesvincular());
		SesionBean.getUsuarioLogueado().getCuentas().quitar(this.getCuentaADesvincular());
		this.refrescar();
	}

	@Override
	public void actualizarAlias(String nuevoAlias) throws ExcepcionControladaError {
		try {
			this.getCuentaParaAlias().setAlias( nuevoAlias );
			AdministradorCuenta.actualizarAlias(this.getCuentaParaAlias(), SesionBean.getUsuarioLogueado().getIdUsuario());
			this.refrescar();
			RequestContext.getCurrentInstance().update(":form:tabsCuentas:seccionPagos");
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError excepcionControladaError) {
			throw new ExcepcionControladaError(excepcionControladaError.getMessage(),excepcionControladaError);
		}
	}

	@Override
	public void cancelarActualizacionAlias() {
		this.setCuentaParaAlias(null);
	}

	public String iconoSolapa(TiposCuentas tributo) {
		switch (tributo) {
			case ABL: return "fa fa-home";
			case VEHICULOS: return "fa fa-car";
			case RODADOS: return "fa fa-motorcycle";
			case CEMENTERIO: return "fab fa-pagelines";
			case COMERCIOS: return "fa fa-industry";
			case PILETAS: return "fa fa-swimmer";
			default: return "";
		}
	}
	
	public boolean isGrillaSeleccionada(){
		return !isTablaSeleccionada();
	}
	
	public void mostrarTabla(){
		this.tablaSeleccionada = true;
	}
	public void mostrarGrilla(){
		this.tablaSeleccionada = false;
	}
	
	public TiposCuentas[] getTiposCuenta() {
		return tiposCuenta;
	}

	public void setTiposCuenta(TiposCuentas[] tiposCuenta) {
		this.tiposCuenta = tiposCuenta;
	}

	public CuentasUsuario getCuentas() {
		return cuentas;
	}

	public void setCuentas(CuentasUsuario cuentas) {
		this.cuentas = cuentas;
	}

	public Cuenta getCuentaADesvincular() {
		return cuentaADesvincular;
	}

	public void setCuentaADesvincular(Cuenta cuentaADesvincular) {
		this.cuentaADesvincular = cuentaADesvincular;
	}

	public boolean isTablaSeleccionada() {
		return tablaSeleccionada;
	}

	public void setTablaSeleccionada(boolean tablaSeleccionada) {
		this.tablaSeleccionada = tablaSeleccionada;
	}

	public ModalAliasBean getModalAliasBean() {
		return modalAliasBean;
	}

	public void setModalAliasBean(ModalAliasBean modalAliasBean) {
		this.modalAliasBean = modalAliasBean;
	}

	public Cuenta getCuentaParaAlias() {
		return cuentaParaAlias;
	}

	public void setCuentaParaAlias(Cuenta cuentaParaAlias) {
		this.cuentaParaAlias = cuentaParaAlias;
	}
}