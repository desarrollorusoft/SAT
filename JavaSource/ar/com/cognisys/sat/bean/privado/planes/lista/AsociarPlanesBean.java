package ar.com.cognisys.sat.bean.privado.planes.lista;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteAsociarCuentas;
import ar.com.cognisys.sat.core.administrador.AdministradorComercio;
import ar.com.cognisys.sat.core.administrador.AdministradorPlanDePago;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.natatorios.CuentaPileta;
import ar.com.cognisys.sat.core.modelo.comun.planDePago.PlanDePagoAPagar;
import ar.com.cognisys.sat.core.modelo.comun.rs.Comercio;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.enums.TiposDocumento;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.validador.ValidadorContribuyente;

import javax.ejb.SessionBean;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "AsociarPlan")
@ViewScoped
public class AsociarPlanesBean extends SeccionBean {
	
	private static final long serialVersionUID = -8949646055281700588L;
	private Integer numeroPlan;
	private boolean encontrado;
	private PlanDePagoAPagar planEncontrado;
	private String alias;

	@ManagedProperty( value="#{ListaPlanes}" )
	private ListaPlanesBean listaPlanesBean;

	public AsociarPlanesBean() {
		this.refrescar();
	}

	@Override
	protected void inicializacion() {}

	@Override
	public void cargar() {}

	@Override
	public void refrescar() {
		this.reiniciar();
	}

	@Override
	public void cargar(Object arg0) throws ExcepcionControladaError {}

	@Override
	public void siguiente() throws ExcepcionControladaError {}

	public void buscar() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		this.validar();

		try {
			this.setPlanEncontrado(AdministradorPlanDePago.buscarPlan( this.getNumeroPlan() ));

			if ( this.getPlanEncontrado() == null ) {
				this.setEncontrado(false);
				throw new ExcepcionControladaAlerta("No se ha encontrado un Plan con el dato ingresado");
			} else
				this.setEncontrado(true);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	private void validar() throws ExcepcionControladaAlerta {
		if ( this.getNumeroPlan() == null )
			throw new ExcepcionControladaAlerta("Debe ingresar un numero de plan");

		if ( SesionBean.getUsuarioLogueado().tienePlan( this.getNumeroPlan() ) )
			throw new ExcepcionControladaAlerta("El plan ya se encuentra asociado");
	}

	public void asociarPlan() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {
			AdministradorPlanDePago.asociarPlan( this.getPlanEncontrado(), SesionBean.getUsuarioLogueado(), this.getAlias() );

			this.getListaPlanesBean().refrescar();

			this.reiniciar();
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public void reiniciar() {
		this.setNumeroPlan( null );
		this.setPlanEncontrado( null );
		this.setAlias("");
		this.setEncontrado(false);
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getNumeroPlan() {
		return numeroPlan;
	}

	public void setNumeroPlan(Integer numeroPlan) {
		this.numeroPlan = numeroPlan;
	}

	public PlanDePagoAPagar getPlanEncontrado() {
		return planEncontrado;
	}

	public void setPlanEncontrado(PlanDePagoAPagar planEncontrado) {
		this.planEncontrado = planEncontrado;
	}

	public boolean isEncontrado() {
		return encontrado;
	}

	public void setEncontrado(boolean encontrado) {
		this.encontrado = encontrado;
	}

	public ListaPlanesBean getListaPlanesBean() {
		return listaPlanesBean;
	}

	public void setListaPlanesBean(ListaPlanesBean listaPlanesBean) {
		this.listaPlanesBean = listaPlanesBean;
	}
}