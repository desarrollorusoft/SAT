package ar.com.cognisys.sat.bean.privado.planes.detalle;

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
import org.primefaces.context.RequestContext;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "PagoPlan")
@ViewScoped
public class PagoPlanBean extends SeccionBean {
	
	private static final long serialVersionUID = 2973562988873739585L;
	private PlanDePagoAPagar plan;
	private Float descuentoMostrable;
	private List<CuotaPPC> listaCuotas;
	private Float total;
	private Float totalDeuda;
	private List<CuotaPPC> listaCuotasSeleccionadas;
	private String updatePath;
	
	@Override
	protected void inicializacion() {
		this.setListaCuotas( new ArrayList<CuotaPPC>() );
		this.setTotal( 0f );
		this.setTotalDeuda( 0f );
		this.setDescuentoMostrable( 0f );
	}
	
	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object dato) throws ExcepcionControladaError {
		this.setPlan((PlanDePagoAPagar) dato);
		this.refrescar();
	}
	
	@Override
	public void refrescar() throws ExcepcionControladaError {
		this.inicializacion();
		this.setListaCuotas( AsistentePPC.recuperarCuotas( this.getPlan() ) );
		this.setPlan( this.getPlan() );
		this.setTotalDeuda( this.calcularTotal() );
		this.setDescuentoMostrable( this.getTotalDeuda() - this.getPlan().getImporteCancelacion() );
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {}

	public void abrirModal(String nombre) throws ExcepcionControladaAlerta{
		AsistentePPC.validarAlgunoSeleccionado( this.getListaCuotas() );
		RequestContext.getCurrentInstance().execute("PF('"+nombre+"').show();");
	}
	
	public Float calcularTotal() {
		if (this.muestroDetalle())
			return AsistentePPC.calularTotal(this.getListaCuotas());
		else 
			return 0f;
	}
	
	public boolean muestroDetalle() {
		return this.getListaCuotas() != null && !this.getListaCuotas().isEmpty();
	}
	
	public void actualizarTotal() {
		this.setTotal( AsistentePPC.calcularMonto( this.getListaCuotas() ) );
	}
	
	public void actualizarTotal( CuotaPPC cuota ) {
		AsistentePPC.seleccionarSoloCuota(cuota, this.listaCuotas );
		this.setTotal( AsistentePPC.calcularMonto( this.getListaCuotas() ) );
	}
	
	private void inicializar() {
		AsistentePPC.limpiarSeleccion( this.getListaCuotas() );
		this.setTotal( 0f );
	}
	
	public void pagoTarjetaCredito() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		this.abrirPopupCredito( this.obtenerPaquetePago( MediosPago.TARJETAS_CREDITO ) );
		this.finalizarPago();
	}

	public void pagoLink() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		this.abrirPopupLink( this.obtenerPaquetePago( MediosPago.LINK ) );
		this.finalizarPago();
	}

	public void pagoMP() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		this.abrirPopupMercadoPago( this.obtenerPaquetePago( MediosPago.MERCADO_PAGO ) );
		this.finalizarPago();
	}

	public void pagoInterbanking() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		this.abrirPopupRedirect( this.obtenerPaquetePago( MediosPago.INTERBANKING ) );
		this.finalizarPago();
	}
	
	public void pagoPMC() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		this.abrirPopupRedirect( this.obtenerPaquetePago( MediosPago.PAGOMISCUENTAS ) );
		this.finalizarPago();
	}
	
	private PaquetePago obtenerPaquetePago(MediosPago medioPago) throws ExcepcionControladaAlerta, ExcepcionControladaError {
		return AsistentePPC.generarPago(this.getPlan(),
										this.getSeleccionadas(),
										medioPago,
										this.seCancelaDeuda());
	}

	private List<CuotaPPC> getSeleccionadas() {
		List<CuotaPPC> lista = new ArrayList<CuotaPPC>();
		
		for( CuotaPPC cuota: this.getListaCuotas() )
			if( cuota.isPagar() )
				lista.add(cuota);
		
		return lista;
	}

	public void cancelarDeuda() {
		AsistentePPC.cancelarDeuda( this.getListaCuotas() );
	}

	public void limpiarDeuda() {
		AsistentePPC.limpiarDeuda( this.getListaCuotas() );
		this.actualizarTotal();
	}

	public boolean seCancelaDeuda() {
		return AsistentePPC.todoSelccionado( this.getListaCuotas() );
	}

	private void abrirPopupCredito(PaquetePago paquete) {
		this.establecerParametros(paquete);
		RequestContext.getCurrentInstance().execute("window.open('pagos/pago_tarjeta_credito.xhtml', 'WindowName', 'dependent=yes, menubar=no, toolbar=no, scrollbars=yes, resizable=yes, fullscreen=yes');");
	}
	
	private void abrirPopupLink(PaquetePago paquete) {
		this.establecerParametros(paquete);
		RequestContext.getCurrentInstance().execute("window.open('pagos/pago_link_pagos.xhtml', 'WindowName', 'dependent=yes, menubar=no, toolbar=no, scrollbars=yes, resizable=yes, fullscreen=yes');");
	}
	
	private void abrirPopupRedirect(PaquetePago paquete) {
		this.establecerParametros(paquete);
		RequestContext.getCurrentInstance().execute("window.open('pagos/redireccionamiento_pagos.xhtml', 'WindowName', 'dependent=yes, menubar=no, toolbar=no, scrollbars=yes, resizable=yes, fullscreen=yes');");
	}

	private void abrirPopupMercadoPago(PaquetePago paquete) {
		this.establecerParametros(paquete);
		RequestContext.getCurrentInstance().execute("window.open('pagos/pago_mercado_pago.xhtml', 'WindowName', 'dependent=yes, menubar=no, toolbar=no, scrollbars=yes, resizable=yes, fullscreen=yes');");
	}
	
	private void establecerParametros(PaquetePago paquete) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().putAll(paquete.getParametros());
	}

	private void finalizarPago() {
		this.inicializar();
		blanquearCuotas();
	}

	private void blanquearCuotas() {
		for (CuotaPPC cuotaPPC : listaCuotas)
			cuotaPPC.setPagar(false);
	}

	public List<CuotaPPC> getListaCuotas() {
		return listaCuotas;
	}

	public void setListaCuotas(List<CuotaPPC> listaCuotas) {
		this.listaCuotas = listaCuotas;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public Float getTotalDeuda() {
		return totalDeuda;
	}

	public void setTotalDeuda(Float totalDeuda) {
		this.totalDeuda = totalDeuda;
	}

	public List<CuotaPPC> getListaCuotasSeleccionadas() {
		return listaCuotasSeleccionadas;
	}

	public void setListaCuotasSeleccionadas(List<CuotaPPC> listaCuotasSeleccionadas) {
		this.listaCuotasSeleccionadas = listaCuotasSeleccionadas;
	}

	public PlanDePagoAPagar getPlan() {
		return plan;
	}

	public void setPlan(PlanDePagoAPagar plan) {
		this.plan = plan;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Float getDescuentoMostrable() {
		return descuentoMostrable;
	}

	public void setDescuentoMostrable(Float descuentoMostrable) {
		this.descuentoMostrable = descuentoMostrable;
	}

	public String getUpdatePath() {
		return updatePath;
	}

	public void setUpdatePath(String updatePath) {
		this.updatePath = updatePath;
	}
}