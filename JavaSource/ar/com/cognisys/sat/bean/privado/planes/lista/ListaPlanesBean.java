package ar.com.cognisys.sat.bean.privado.planes.lista;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionWizardBean;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteListaCuenta;
import ar.com.cognisys.sat.bean.privado.home.detalle.ISolicitanteModalCambioAlias;
import ar.com.cognisys.sat.bean.privado.home.detalle.ModalAliasBean;
import ar.com.cognisys.sat.core.administrador.AdministradorPlanDePago;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.planDePago.PlanDePagoAPagar;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.CuentasUsuario;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import org.primefaces.context.RequestContext;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.Arrays;
import java.util.List;

@ManagedBean(name = "ListaPlanes")
@ViewScoped
public class ListaPlanesBean extends SeccionWizardBean implements ISolicitanteModalCambioAlias {
	
	private static final long serialVersionUID = -8949646055281700588L;
	private List<PlanDePagoAPagar> planes;
	private PlanDePagoAPagar planADesvincular;
	private PlanDePagoAPagar planParaAlias;

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
		this.setPlanes( SesionBean.getUsuarioLogueado().getPlanes() );
		this.setPlanADesvincular(null);
	}

	@Override
	public void cargar(Object arg0) throws ExcepcionControladaError {}

	@Override
	public void siguiente() throws ExcepcionControladaError {}

	public void detalle(PlanDePagoAPagar plan) throws ExcepcionControladaError {
		super.irAlSiguiente(plan);
	}
	
	public void desvincular(PlanDePagoAPagar plan) {
		this.setPlanADesvincular( plan );
		RequestContext.getCurrentInstance().execute( "PF('AlertaDesvinculacion').show()");
	}
	
	public void confirmarDesvinculacion() throws ExcepcionControladaError {
		try {
			AdministradorPlanDePago.deasociarPlan( SesionBean.getUsuarioLogueado(), this.getPlanADesvincular() );

			this.refrescar();
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public void cargarModalAlias( PlanDePagoAPagar p ){
		this.setPlanParaAlias( p );
		this.modalAliasBean.cargar( this, p.getAlias() );
	}

	@Override
	public void actualizarAlias(String nuevoAlias) throws ExcepcionControladaError {
		try {
			this.getPlanParaAlias().setAlias( nuevoAlias );
			AdministradorPlanDePago.actualizarAlias(this.getPlanParaAlias(), SesionBean.getUsuarioLogueado().getIdUsuario());
			this.refrescar();
			RequestContext.getCurrentInstance().update(":form:tablaPlanes");
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError excepcionControladaError) {
			throw new ExcepcionControladaError(excepcionControladaError.getMessage(),excepcionControladaError);
		}
	}

	@Override
	public void cancelarActualizacionAlias() {
		this.setPlanParaAlias(null);
	}

	public ModalAliasBean getModalAliasBean() {
		return modalAliasBean;
	}

	public void setModalAliasBean(ModalAliasBean modalAliasBean) {
		this.modalAliasBean = modalAliasBean;
	}

	public List<PlanDePagoAPagar> getPlanes() {
		return planes;
	}

	public void setPlanes(List<PlanDePagoAPagar> planes) {
		this.planes = planes;
	}

	public PlanDePagoAPagar getPlanADesvincular() {
		return planADesvincular;
	}

	public void setPlanADesvincular(PlanDePagoAPagar planADesvincular) {
		this.planADesvincular = planADesvincular;
	}

	public PlanDePagoAPagar getPlanParaAlias() {
		return planParaAlias;
	}

	public void setPlanParaAlias(PlanDePagoAPagar planParaAlias) {
		this.planParaAlias = planParaAlias;
	}
}