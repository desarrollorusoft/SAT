package ar.com.cognisys.sat.bean.privado.home.detalle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistentePPC;
import ar.com.cognisys.sat.bean.privado.home.detalle.ppc.PagoPPCBean;
import ar.com.cognisys.sat.bean.privado.home.detalle.ppc.SimulacionPPCBean;
import ar.com.cognisys.sat.excepcion.SATRuntimeException;
import ar.com.cognisys.sat.modelo.comun.Mensaje;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.abstracto.Deuda;
import ar.com.cognisys.sat.core.modelo.comun.planDePago.PlanDePagoAPagar;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;

@ManagedBean(name = "PPCCuenta")
@ViewScoped
public class PPCBean extends SeccionBean {
	
	private static final long serialVersionUID = -8949646055281700588L;
	private Cuenta cuenta;
	private Deuda deuda;
	private List<PlanDePagoAPagar> planes;
	
	@ManagedProperty(value = "#{SimulacionPPC}")
	private SimulacionPPCBean simulacionPPCBean;
	
	@ManagedProperty(value = "#{PagoPPC}")
	private PagoPPCBean pagoPPCBean;
	
	@Override
	protected void inicializacion() {}
	
	@Override
	public void cargar() {}

	@Override
	public void cargar(Object cuenta) throws ExcepcionControladaError {
		this.setCuenta((Cuenta) cuenta);
		this.refrescar();
	}
	
	@Override
	public void refrescar() {
		try {
			this.setDeuda( AdministradorCuenta.recuperarDeudas(this.getCuenta().getTipoCuenta(), this.getCuenta().getDatoCuenta()) );
			this.getCuenta().setDeudas( this.getDeuda() );
			this.getSimulacionPPCBean().cargar( this.getCuenta() );
			this.setPlanes( AsistentePPC.recuperarPlanes( this.getCuenta(), SesionBean.getUsuarioLogueado()) );
			if (tienePPC()){
				this.getPagoPPCBean().setPlanes( this.getPlanes() );
				this.getPagoPPCBean().cargar(this.getCuenta());
			}
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new SATRuntimeException(e);
		} catch (ExcepcionControladaAlerta e) {
			Mensaje.emitirMensajeAlerta(e.getMessage());
		} catch (ExcepcionControladaError e) {
			throw new SATRuntimeException(e);
		}
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {}

	public boolean muestroSimulacion() {
		return AsistentePPC.tieneDeuda( this.getDeuda() );
	}
	
	public boolean tienePPC() {
		return AsistentePPC.tienePPC( this.getPlanes() );
	}
	
	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Deuda getDeuda() {
		return deuda;
	}

	public void setDeuda(Deuda deuda) {
		this.deuda = deuda;
	}

	public SimulacionPPCBean getSimulacionPPCBean() {
		return simulacionPPCBean;
	}

	public void setSimulacionPPCBean(SimulacionPPCBean simulacionPPCBean) {
		this.simulacionPPCBean = simulacionPPCBean;
	}

	public PagoPPCBean getPagoPPCBean() {
		return pagoPPCBean;
	}

	public void setPagoPPCBean(PagoPPCBean pagoPPCBean) {
		this.pagoPPCBean = pagoPPCBean;
	}

	public List<PlanDePagoAPagar> getPlanes() {
		return planes;
	}

	public void setPlanes(List<PlanDePagoAPagar> planes) {
		this.planes = planes;
	}
}