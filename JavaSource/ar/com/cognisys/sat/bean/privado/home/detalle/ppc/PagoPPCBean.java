package ar.com.cognisys.sat.bean.privado.home.detalle.ppc;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.privado.planes.detalle.PagoPlanBean;
import org.primefaces.context.RequestContext;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.sat.bean.asistente.AsistentePPC;
import ar.com.cognisys.sat.bean.asistente.AsistentePago;
import ar.com.cognisys.sat.bean.privado.home.detalle.ModalReciboBean;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.planDePago.CuotaPPC;
import ar.com.cognisys.sat.core.modelo.comun.planDePago.PlanDePagoAPagar;
import ar.com.cognisys.sat.core.modelo.enums.MediosPago;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.generador.pagoOnline.PaquetePago;

@ManagedBean(name = "PagoPPC")
@ViewScoped
public class PagoPPCBean extends SeccionBean {
	
	private static final long serialVersionUID = 2973562988873739585L;
	private Cuenta cuenta;
	private List<PlanDePagoAPagar> planes;
	private boolean seleccionado;

	@ManagedProperty(value = "#{PagoPlan}")
	private PagoPlanBean pagoPlanBean;

	@Override
	protected void inicializacion() {
		this.seleccionado = false;
	}
	
	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object dato) throws ExcepcionControladaError {
		this.setCuenta((Cuenta) dato);
		this.refrescar();
	}
	
	@Override
	public void refrescar() throws ExcepcionControladaError {
		this.inicializacion();
		AsistentePPC.registrarPlanes(this.getCuenta());
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {}
	
	public void cargarPlan(PlanDePagoAPagar plan) throws ExcepcionControladaError {
		this.getPagoPlanBean().cargar( plan );
		this.getPagoPlanBean().setUpdatePath(":form:tabsCuentas:tabsPPC:");
		this.seleccionado = true;
	}

	public boolean muestroDetalle() {
		return this.seleccionado;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public List<PlanDePagoAPagar> getPlanes() {
		return planes;
	}

	public void setPlanes(List<PlanDePagoAPagar> planes) {
		this.planes = planes;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public PagoPlanBean getPagoPlanBean() {
		return pagoPlanBean;
	}

	public void setPagoPlanBean(PagoPlanBean pagoPlanBean) {
		this.pagoPlanBean = pagoPlanBean;
	}
}