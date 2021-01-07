package ar.com.cognisys.sat.bean.privado.planes.detalle;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionWizardBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteDetalleCuenta;
import ar.com.cognisys.sat.bean.privado.home.detalle.BoletaPiletasBean;
import ar.com.cognisys.sat.bean.privado.home.detalle.DetalleDeudaBean;
import ar.com.cognisys.sat.bean.privado.home.detalle.InformacionCuentaBean;
import ar.com.cognisys.sat.bean.privado.home.detalle.PPCBean;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.planDePago.PlanDePagoAPagar;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.modelo.comun.Mensaje;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "DetallePlan")
@ViewScoped
public class DetallePlanBean extends SeccionWizardBean {
	
	private static final long serialVersionUID = -8949646055281700588L;
	private PlanDePagoAPagar plan;
	
	@ManagedProperty(value = "#{PagoPlan}")
	private PagoPlanBean pagoPlanBean;

	@Override
	protected void inicializacion() {}
	
	@Override
	public void cargar() {}

	@Override
	public void cargar(Object plan) throws ExcepcionControladaError {
		this.setPlan((PlanDePagoAPagar) plan);
		this.refrescar();
	}
	
	@Override
	public void refrescar() throws ExcepcionControladaError {
		this.getPagoPlanBean().cargar(this.getPlan());
		this.getPagoPlanBean().setUpdatePath(":form:tabsPlan:");
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {}
	
	public void volverALista(){
		this.volverAlPrimero();
	}

	public String getTitulo() {
		return  "Plan de Pago - N°" + this.getPlan().getNroPlan() + " - " + this.getPlan().getSolicitante();
	}

	public PlanDePagoAPagar getPlan() {
		return plan;
	}

	public void setPlan(PlanDePagoAPagar plan) {
		this.plan = plan;
	}

	public PagoPlanBean getPagoPlanBean() {
		return pagoPlanBean;
	}

	public void setPagoPlanBean(PagoPlanBean pagoPlanBean) {
		this.pagoPlanBean = pagoPlanBean;
	}
}