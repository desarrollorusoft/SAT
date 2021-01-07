package ar.com.cognisys.sat.bean.privado.home.detalle.ppc;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.sat.bean.asistente.AsistentePPC;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.deudas.CuotaAdapter;
import ar.com.cognisys.sat.core.modelo.comun.deudas.DeudaAdapter;
import ar.com.cognisys.sat.core.modelo.comun.planDePago.ResultadoSimulacion;

@ManagedBean(name = "SimulacionPPC")
@ViewScoped
public class SimulacionPPCBean extends SeccionBean {
	
	private static final long serialVersionUID = 2973562988873739585L;
	private Cuenta cuenta;
	private ResultadoSimulacion resultadoSimulacion;
	private Integer anticipo;
	private boolean calculado;
	private Integer number2;
	
	@Override
	protected void inicializacion() {
		this.anticipo = 50;
		this.number2 = 50;
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
		this.setResultadoSimulacion(null);
		this.setAnticipo(50);
		this.setCalculado(false);
		AsistentePPC.registrarIngreso(this.getCuenta());
	}
	
	public void calcular() throws ExcepcionControladaError {
		this.setCalculado(false);
		this.setResultadoSimulacion( AsistentePPC.calcularSimulacion( this.getCuenta(), this.getAnticipo() ));
		this.setCalculado(true);
		AsistentePPC.registrarIngresoCalculo(this.getCuenta());
	}
	
	public List<CuotaAdapter> cuotasVencidas() {
		if (this.getCuenta() != null && this.getCuenta().getDeudas() != null)			
			return ((DeudaAdapter) this.getCuenta().getDeudas()).getCuotasVencidas();
		else
			return new ArrayList<CuotaAdapter>();
	}
	
	@Override
	public void siguiente() throws ExcepcionControladaError {}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public ResultadoSimulacion getResultadoSimulacion() {
		return resultadoSimulacion;
	}

	public void setResultadoSimulacion(ResultadoSimulacion resultadoSimulacion) {
		this.resultadoSimulacion = resultadoSimulacion;
	}

	public Integer getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(Integer anticipo) {
		this.anticipo = anticipo;
	}

	public boolean isCalculado() {
		return calculado;
	}

	public void setCalculado(boolean calculado) {
		this.calculado = calculado;
	}

	public Integer getNumber2() {
		return number2;
	}

	public void setNumber2(Integer number2) {
		this.number2 = number2;
	}
}