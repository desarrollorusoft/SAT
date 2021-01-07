package ar.com.cognisys.sat.bean.privado.be;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaBean;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteBoletaElectronica;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.modelo.comun.Mensaje;

@ManagedBean(name = "BELista")
@ViewScoped
public class BEListaBean extends PaginaBean implements IBoletaElectronicaBean {
	
	private static final long serialVersionUID = -1886365381087863455L;
	private List<Cuenta> listaCuentasAsociadas;
	private List<Cuenta> listaCuentasDisponibles;
	private TiposCuentas[] tiposCuentaAsignadas;
	private TiposCuentas[] tiposCuentaDisponibles;
	private Cuenta cuentaSeleccionada;
	private TiposCuentas tipoSeleccionado;
	private String datoCuentaSeleccionado;
	
	@ManagedProperty(value = "#{ModalAdhesionBE}")
	private ModalAdhesionBEBean modalAdhesionBEBean;
	
	@Override
	protected void inicializacion() {
		try {
			this.setListaCuentasDisponibles(new ArrayList<Cuenta>());
			this.setListaCuentasAsociadas(new ArrayList<Cuenta>());
			this.refrescar();
		} catch (ExcepcionControladaError e) {
			Mensaje.emitirMensajeError(e.getMessage());
		}
	}
	
	@Override
	public void refrescar() throws ExcepcionControladaError {
		AsistenteBoletaElectronica.cargarListas(SesionBean.getUsuarioLogueado(), 
												this.getListaCuentasDisponibles(), 
												this.getListaCuentasAsociadas());
		this.refrecarTiposCuentasDiponibles();
	}
	
	private void refrecarTiposCuentasDiponibles() {
		this.setDatoCuentaSeleccionado(null);
		TiposCuentas[] tcs = AsistenteBoletaElectronica.obtenerTiposCuenta( this.getListaCuentasDisponibles() );
		Arrays.sort(tcs);
		this.setTiposCuentaDisponibles(tcs);
		
		tcs = AsistenteBoletaElectronica.obtenerTiposCuenta( this.getListaCuentasAsociadas() );
		Arrays.sort(tcs);
		this.setTiposCuentaAsignadas(tcs);
		
		if (tcs != null && tcs.length > 0) {
			this.setTipoSeleccionado(tcs[0]);
			
			List<Cuenta> l = this.recuperarCuentasDisponibles();
			if (l != null && !l.isEmpty())
				this.setDatoCuentaSeleccionado( l.get(0).getDatoCuenta() );
		}
		
		RequestContext.getCurrentInstance().update("form:cuentas");
	}
	
	private void asociar(Cuenta c) throws ExcepcionControladaError {
		try {
			AsistenteBoletaElectronica.asociar( c, SesionBean.getUsuarioLogueado() );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
		
		this.getListaCuentasAsociadas().add( c );
		this.getListaCuentasDisponibles().remove( c );
		this.refrecarTiposCuentasDiponibles();
	}
	
	private void desasociar(Cuenta c) throws ExcepcionControladaError {
		try {
			AsistenteBoletaElectronica.desasociar( c, SesionBean.getUsuarioLogueado() );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
		
		this.getListaCuentasDisponibles().add( c );
		this.getListaCuentasAsociadas().remove( c );
		this.refrecarTiposCuentasDiponibles();
	}
	
	public String iconoSolapa(TiposCuentas tributo) {
		switch (tributo) {
			case ABL: return "fa fa-home";
			case VEHICULOS: return "fa fa-car";
			case RODADOS: return "fa fa-motorcycle";
			case COMERCIOS: return "fa fa-industry";
			default: return "";
		}
	}
	
	public List<Cuenta> recuperarCuentas(TiposCuentas tipo) {
		return AsistenteBoletaElectronica.obtenerCuentas(tipo, this.getListaCuentasAsociadas());
	}

	public List<Cuenta> recuperarCuentasDisponibles() {
		return AsistenteBoletaElectronica.obtenerCuentas(this.getTipoSeleccionado(), this.getListaCuentasDisponibles());
	}
	
	public void solicitudVinculacion() {
		if (this.getDatoCuentaSeleccionado() == null)
			Mensaje.emitirMensajeAlerta("Debe seleccionar una cuenta");
		else {
			this.setCuentaSeleccionada( this.recuperarCuentaSelecionada() );
			this.getModalAdhesionBEBean().mostrar( this );
		}		
	}
	
	private Cuenta recuperarCuentaSelecionada() {
		for (Cuenta c : this.getListaCuentasDisponibles())
			if (c.sos( this.getDatoCuentaSeleccionado() ))
				return c;
		
		return null;
	}

	public void confirmarDesvinculacion(Cuenta cuenta) throws ExcepcionControladaError {
		this.setCuentaSeleccionada(cuenta);
		this.desasociar( this.getCuentaSeleccionada() );
	}
	
	@Override
	public void adherir() throws ExcepcionControladaError {
		this.asociar( this.getCuentaSeleccionada() );
	}

	public List<Cuenta> getListaCuentasAsociadas() {
		return listaCuentasAsociadas;
	}

	public void setListaCuentasAsociadas(List<Cuenta> listaCuentasAsociadas) {
		this.listaCuentasAsociadas = listaCuentasAsociadas;
	}

	public List<Cuenta> getListaCuentasDisponibles() {
		return listaCuentasDisponibles;
	}

	public void setListaCuentasDisponibles(List<Cuenta> listaCuentasDisponibles) {
		this.listaCuentasDisponibles = listaCuentasDisponibles;
	}

	public TiposCuentas[] getTiposCuentaAsignadas() {
		return tiposCuentaAsignadas;
	}

	public void setTiposCuentaAsignadas(TiposCuentas[] tiposCuentaAsignadas) {
		this.tiposCuentaAsignadas = tiposCuentaAsignadas;
	}

	public TiposCuentas[] getTiposCuentaDisponibles() {
		return tiposCuentaDisponibles;
	}

	public void setTiposCuentaDisponibles(TiposCuentas[] tiposCuentaDisponibles) {
		this.tiposCuentaDisponibles = tiposCuentaDisponibles;
	}

	public Cuenta getCuentaSeleccionada() {
		return cuentaSeleccionada;
	}

	public void setCuentaSeleccionada(Cuenta cuentaSeleccionada) {
		this.cuentaSeleccionada = cuentaSeleccionada;
	}

	public TiposCuentas getTipoSeleccionado() {
		return tipoSeleccionado;
	}

	public void setTipoSeleccionado(TiposCuentas tipoSeleccionado) {
		this.tipoSeleccionado = tipoSeleccionado;
	}

	public String getDatoCuentaSeleccionado() {
		return datoCuentaSeleccionado;
	}

	public void setDatoCuentaSeleccionado(String datoCuentaSeleccionado) {
		this.datoCuentaSeleccionado = datoCuentaSeleccionado;
	}

	public ModalAdhesionBEBean getModalAdhesionBEBean() {
		return modalAdhesionBEBean;
	}

	public void setModalAdhesionBEBean(ModalAdhesionBEBean modalAdhesionBEBean) {
		this.modalAdhesionBEBean = modalAdhesionBEBean;
	}
}