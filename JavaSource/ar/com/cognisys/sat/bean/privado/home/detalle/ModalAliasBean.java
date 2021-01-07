package ar.com.cognisys.sat.bean.privado.home.detalle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.ModalBean;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.CuentaABL;

@ManagedBean(name = "ModalAlias")
@ViewScoped
public class ModalAliasBean extends ModalBean {

	private static final long serialVersionUID = -2878937004356818347L;
	private ISolicitanteModalCambioAlias solicitante;
	private String alias;

	@Override
	protected void inicializacion() {
		this.inicializarModal();
	}
	
	@Override
	protected void inicializarModal() {
		this.setNombreModal("ModalAlias");
	}

	public void cargar(ISolicitanteModalCambioAlias solicitante, String alias) {
		this.solicitante = solicitante;
		this.alias = alias;
	}

	public void actualizar() throws ExcepcionControladaError {
		this.getSolicitante().actualizarAlias( this.getAlias() );
	}

	public void cancelar() {
		this.getSolicitante().cancelarActualizacionAlias();
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public ISolicitanteModalCambioAlias getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(ISolicitanteModalCambioAlias solicitante) {
		this.solicitante = solicitante;
	}
}