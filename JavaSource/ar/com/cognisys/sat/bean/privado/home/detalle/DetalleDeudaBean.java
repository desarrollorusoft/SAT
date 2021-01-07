package ar.com.cognisys.sat.bean.privado.home.detalle;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistentePago;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.deudas.CuotaAdapter;
import ar.com.cognisys.sat.core.modelo.comun.deudas.DeudaAdapter;
import ar.com.cognisys.sat.core.modelo.comun.planDePago.TotalCuota;
import ar.com.cognisys.sat.core.modelo.dao.rs.BonificacionRS;
import ar.com.cognisys.sat.core.modelo.enums.MediosPago;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.generador.pagoOnline.PaquetePago;
import ar.com.cognisys.sat.modelo.comun.Mensaje;

@ManagedBean(name = "DetalleDeuda")
@ViewScoped
public class DetalleDeudaBean extends SeccionBean {
	
	private static final long serialVersionUID = -8949646055281700588L;

	private static final Float MONTO_MAXIMO_COMERCIO = 4000000f;
	private static final Float MONTO_MAXIMO_RESTO = 40000f;
	private Cuenta cuenta;
	private DeudaAdapter deuda;
	private Float total;
	private TotalCuota totalCancelacion;
	private Float descuentoCancelacion;
	private boolean cancelaDeuda;
	private boolean cancelaAVencer;
	private BonificacionRS bonificacion;
	private float descuentoSemestral;
	private boolean pagoSemestral;

	@ManagedProperty(value = "#{ModalRecibo}")
	private ModalReciboBean modalReciboBean;
	
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
	public void refrescar() throws ExcepcionControladaError {
		try {
			this.setDeuda( AsistentePago.recuperarDeuda(this.getCuenta()) );
			this.setBonificacion( AsistentePago.recuperarBonificacion(this.getCuenta()) );
			
			// Esto se hace, porque se necesitan mostrar las cuotas, y no vienen en la DEUDA, vienen en la Bonificacion
//			if (this.getBonificacion() != null && this.getBonificacion().isCorresponde()) {
//				this.getDeuda().agregarLista( this.getBonificacion().getCuotas() );
//				this.getDeuda().generarAdapters();
//			}
			
			this.setTotalCancelacion( AsistentePago.recuperarTotalCancelacion(this.getCuenta()) );
			this.inicializar();
			this.setDescuentoCancelacion( AsistentePago.calcularDescuento(this.getDeuda(), this.getTotalCancelacion()) );
			
			if ( this.esComercio() )
				this.controlBonificacionRS();

			if (AsistentePago.muestroCartelAblSemestral(this.getCuenta(), this.getDeuda()))
				RequestContext.getCurrentInstance().execute("PF('AblSemestral').show()");

			if (AsistentePago.muestroCarcelCancelacionSemestral(this.getCuenta(), this.getDeuda()))
				this.setDescuentoSemestral( AsistentePago.calcularDescuentoSemestral(this.getCuenta(), this.getDeuda()) );
		} catch (ExcepcionControladaAlerta e) {
			Mensaje.emitirMensajeAlerta(e.getMessage());
		}
	}

	private void inicializar() {
		AsistentePago.limpiarDeuda(this.getDeuda());
		this.setTotal(0f);
		this.setCancelaAVencer(false);
		this.setCancelaDeuda(false);
		this.setPagoSemestral(false);
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {}
	
	public boolean seCancelaDeuda() {
		return AsistentePago.cancelaDeuda(this.getDeuda());
	}
	
	public void cancelarDeuda() {
		AsistentePago.cancelarDeuda(this.getDeuda());
		this.actualizarTotal();
		this.setCancelaDeuda(true);
	}

	public void pagoSemestral() {
		AsistentePago.pagoSemestral(this.getDeuda());
		this.actualizarTotal();
		this.setPagoSemestral(true);
	}
	
	public Float totalConDescuento() {
		return AsistentePago.calcularTotalConDescuento(this.getTotal(), this.getDescuentoMostrable(), this.getDescuentoSemestral(), this.cancelaDeuda, this.pagoSemestral);
	}
	
	public void abrirModalRecibo() throws ExcepcionControladaAlerta {
		this.validar();

//		if (this.superaDscuentoPermitido() && AsistentePago.cancelaDeuda( this.getDeuda() ))
//			RequestContext.getCurrentInstance().execute("PF('descuentoPresencial').show()");
//		else
			if (!this.permiteMontoRecibo())
			RequestContext.getCurrentInstance().execute("PF('montoReciboExcedido').show()");
			
		else {			
			this.getModalReciboBean().cargar(this, this.getDeuda());
			RequestContext.getCurrentInstance().execute("PF('AlertaBoleta').show()");
		}
	}
	
	private boolean permiteMontoRecibo() {
		return AsistentePago.noExcedeMontoRecibo(this.getDeuda());
	}

	public void abrirModal( String nombre ) throws ExcepcionControladaAlerta {
		this.validar();
		
//		if (this.superaDscuentoPermitido())
//			RequestContext.getCurrentInstance().execute("PF('descuentoPresencial').show()");
//		else
			RequestContext.getCurrentInstance().execute("PF('"+nombre+"').show()");
	}
	
	private void validar() throws ExcepcionControladaAlerta {
		if( AsistentePago.obtenerSeleccionadas(this.getDeuda()).isEmpty() )
			throw new ExcepcionControladaAlerta("Debe Seleccionar al menos un período para pagar");
	}

	public void pagoBoletaPago(Date fechaPago) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		this.abrirPopupRedirect( this.obtenerPaqueteRecibo( fechaPago ) );
		this.finalizarPago();
		AsistentePago.registrarEstadisticaRecibos(this.getCuenta());
	}
	
	private PaquetePago obtenerPaqueteRecibo(Date fechaPago) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		return AsistentePago.generarRecibo(this.getCuenta(), 
										   fechaPago, 
										   AsistentePago.obtenerSeleccionadas(this.getDeuda()),
										   AsistentePago.cancelaDeuda(this.getDeuda()), 
										   this.getDeuda().isTieneDeudaLegales(),
										   SesionBean.getUsuarioLogueado().getCorreo());
	}

	public void pagoTarjetaCredito() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		this.abrirPopupCredito( this.obtenerPaquetePago( MediosPago.TARJETAS_CREDITO ) );
		this.finalizarPago();
		AsistentePago.registrarEstadisticaCredito(this.getCuenta());
	}
	
	public void pagoLink() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		this.abrirPopupLink( this.obtenerPaquetePago( MediosPago.LINK ) );
		this.finalizarPago();
		AsistentePago.registrarEstadisticaLink(this.getCuenta());
	}

	public void pagoMP() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		this.abrirPopupMercadoPago( this.obtenerPaquetePago( MediosPago.MERCADO_PAGO ) );
		this.finalizarPago();
		AsistentePago.registrarEstadisticaMP(this.getCuenta());
	}
	
	public void pagoPMC() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		this.abrirPopupRedirect( this.obtenerPaquetePago( MediosPago.PAGOMISCUENTAS ) );
		this.finalizarPago();
		AsistentePago.registrarEstadisticaPMC(this.getCuenta());
	}

	public void pagoInterbanking() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		this.abrirPopupRedirect( this.obtenerPaquetePago( MediosPago.INTERBANKING ) );
		this.finalizarPago();
		AsistentePago.registrarEstadisticaInterbanking(this.getCuenta());
	}

	private PaquetePago obtenerPaquetePago(MediosPago medioPago) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		return AsistentePago.generarPago(this.getCuenta(), 
										 new Date(), // Porque pagos online va con la fecha del día 
										 medioPago, 
										 AsistentePago.obtenerSeleccionadas(this.getDeuda()), 
										 AsistentePago.cancelaDeuda(this.getDeuda()), 
										 this.getDeuda().isTieneDeudaLegales(),
										 SesionBean.getUsuarioLogueado().getCorreo());
	}

	private void abrirPopupCredito(PaquetePago paquete) {
		this.establecerParametros(paquete);
		RequestContext.getCurrentInstance().execute("window.open('pagos/pago_tarjeta_credito.xhtml', 'WindowName', 'dependent=yes, menubar=no, toolbar=no, scrollbars=yes, resizable=yes, fullscreen=yes');");
	}
	
	private void abrirPopupLink(PaquetePago paquete) {
		this.establecerParametros(paquete);
		RequestContext.getCurrentInstance().execute("window.open('pagos/pago_link_pagos.xhtml', 'WindowName', 'dependent=yes, menubar=no, toolbar=no, scrollbars=yes, resizable=yes, fullscreen=yes');");
	}

	private void abrirPopupMercadoPago(PaquetePago paquete) {
		this.establecerParametros(paquete);
		RequestContext.getCurrentInstance().execute("window.open('pagos/pago_mercado_pago.xhtml', 'WindowName', 'dependent=yes, menubar=no, toolbar=no, scrollbars=yes, resizable=yes, fullscreen=yes');");
	}
	
	private void abrirPopupRedirect(PaquetePago paquete) {
		this.establecerParametros(paquete);
		RequestContext.getCurrentInstance().execute("window.open('pagos/redireccionamiento_pagos.xhtml', 'WindowName', 'dependent=yes, menubar=no, toolbar=no, scrollbars=yes, resizable=yes, fullscreen=yes');");
	}
	
	private void establecerParametros(PaquetePago paquete) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().putAll(paquete.getParametros());
	}
	
	private void finalizarPago() {
		this.inicializar();
	}
	
	public void verificarSeleccion(CuotaAdapter cuota) {
		AsistentePago.verificarMora(cuota, this.getDeuda());
		this.actualizarTotal();
	}

	public void verificarSeleccionVencer(CuotaAdapter cuota) {
		AsistentePago.verificarCuotasGrupales(this.getCuenta(), cuota, this.getDeuda());
		this.setPagoSemestral( AsistentePago.pagoSemestralSeleccionado(this.getCuenta(), this.getDeuda()) );
		this.actualizarTotal();
	}
	
	public void actualizarTotal() {
		this.setTotal( AsistentePago.calcularMonto(this.getDeuda()) );
	}
	
	public void seleccionCompletaDeuda() {
		if (this.getCuenta().esRS())
			AsistentePago.cambiarSeleccionRS(this.getDeuda().getCuotasVencidas(), this.isCancelaDeuda(), this.getBonificacion());
		else
			AsistentePago.cambiarSeleccion(this.getDeuda().getCuotasVencidas(), this.isCancelaDeuda());
		this.actualizarTotal();
	}
	
	public void seleccionCompletaAVencer() {
		if (this.getCuenta().esRS())
			AsistentePago.cambiarSeleccionRS(this.getDeuda().getCuotasAVencer(), this.isCancelaAVencer(), this.getBonificacion());
		else {
			AsistentePago.cambiarSeleccion(this.getDeuda().getCuotasAVencer(), this.isCancelaAVencer());
			this.setPagoSemestral( this.isCancelaAVencer() && this.getDescuentoSemestral()>0 );
		}
		this.actualizarTotal();
	}

	public boolean muestroCarcelCancelacion() {
		return this.tienePagosVencidosOAVencer() && !this.getDeuda().isTieneDeudaLegales();
	}

	public boolean tienePagosVencidosOAVencer() {
		return this.getDeuda().tieneCoutasVencidas() || this.getDeuda().tieneCuotasAVencer();
	}
	
	/*Bloque de RS, que se debe eliminar*/
	private void controlBonificacionRS() {
		if ( AsistentePago.muestroBonificacionEfectiva(this.getBonificacion()) )
			RequestContext.getCurrentInstance().execute("PF('BonificacionEfectiva').show();");

//		if ( AsistentePago.muestroBonificacionPendiente(this.getBonificacion()) )
//			RequestContext.getCurrentInstance().execute("PF('BonificacionPendiente').show();");
	}
	
	public boolean seAbonaRS(CuotaAdapter cuota) {
		return (! (AsistentePago.esRS(this.getCuenta()) && AsistentePago.estaBinificada(cuota, this.getBonificacion())) );
	}
	/*Bloque de RS, que se debe eliminar*/

	public boolean aplicaDescuento() {
		return !(this.getDeuda().isTieneDeudaLegales() || this.getCuenta().esRS());
	}
	
	public Float getDescuentoMostrable() {
		if (this.getDescuentoCancelacion() == null)
			return 0f;
//		else if (this.superaDscuentoPermitido())
//			if (this.esComercio())
//				return MONTO_MAXIMO_COMERCIO;
//			else
//				return MONTO_MAXIMO_RESTO;
		else
			return this.getDescuentoCancelacion();
	}

//	public boolean superaDscuentoPermitido() {
//		return (this.getDescuentoCancelacion() != null &&
//				(this.esComercio() && this.getDescuentoCancelacion() >= MONTO_MAXIMO_COMERCIO || !this.esComercio() && this.getDescuentoCancelacion() >= MONTO_MAXIMO_RESTO));
//	}
	
	public boolean esComercio() {
		return AsistentePago.esRS( this.getCuenta() );
	}
	
	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public DeudaAdapter getDeuda() {
		return deuda;
	}

	public void setDeuda(DeudaAdapter deuda) {
		this.deuda = deuda;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public ModalReciboBean getModalReciboBean() {
		return modalReciboBean;
	}

	public void setModalReciboBean(ModalReciboBean modalReciboBean) {
		this.modalReciboBean = modalReciboBean;
	}

	public TotalCuota getTotalCancelacion() {
		return totalCancelacion;
	}

	public void setTotalCancelacion(TotalCuota totalCancelacion) {
		this.totalCancelacion = totalCancelacion;
	}

	public Float getDescuentoCancelacion() {
		return descuentoCancelacion;
	}

	public void setDescuentoCancelacion(Float descuentoCancelacion) {
		this.descuentoCancelacion = descuentoCancelacion;
	}

	public boolean isCancelaDeuda() {
		return cancelaDeuda;
	}

	public void setCancelaDeuda(boolean cancelaDeuda) {
		this.cancelaDeuda = cancelaDeuda;
	}

	public boolean isCancelaAVencer() {
		return cancelaAVencer;
	}

	public void setCancelaAVencer(boolean cancelaAVencer) {
		this.cancelaAVencer = cancelaAVencer;
	}

	public BonificacionRS getBonificacion() {
		return bonificacion;
	}

	public void setBonificacion(BonificacionRS bonificacion) {
		this.bonificacion = bonificacion;
	}

	public float getDescuentoSemestral() {
		return descuentoSemestral;
	}

	public void setDescuentoSemestral(float descuentoSemestral) {
		this.descuentoSemestral = descuentoSemestral;
	}

	public boolean isPagoSemestral() {
		return pagoSemestral;
	}

	public void setPagoSemestral(boolean pagoSemestral) {
		this.pagoSemestral = pagoSemestral;
	}
}