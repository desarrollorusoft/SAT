package ar.com.cognisys.sat.bean.privado.home.detalle;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.ModalBean;
import ar.com.cognisys.sat.bean.asistente.AsistentePago;
import ar.com.cognisys.sat.core.modelo.comun.deudas.DeudaAdapter;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.excepcion.Mensaje;

@ManagedBean(name = "ModalRecibo")
@ViewScoped
public class ModalReciboBean extends ModalBean {
	
	private static final long serialVersionUID = -2878937004356818347L;
	private Date fechaPago;
	private DetalleDeudaBean bean;
	
	@Override
	protected void inicializacion() {
		this.inicializarModal();
	}
	
	@Override
	protected void inicializarModal() {
		this.setNombreModal("AlertaBoleta");
	}
	
	public void cargar(DetalleDeudaBean bean, DeudaAdapter deuda) {
		this.setBean(bean);
		this.calcularFechaBoleta(deuda);
	}
	
	public void calcularFechaBoleta(DeudaAdapter deuda) {
		if (AsistentePago.seleccionoCoutasVencidas(deuda))
			this.setFechaPago(null);
		else 
			this.setFechaPago( AsistentePago.obtenerMenorFecha(deuda) );
	}
	
	public void aceptarFecha() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		if (this.getFechaPago() != null) 
			this.getBean().pagoBoletaPago( this.getFechaPago() );
		else
			Mensaje.addMessageWarn("Debe seleccionar una fecha de pago");
	}

	public Date getFechaHoy() {
		return new Date();
	}
	
	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	protected DetalleDeudaBean getBean() {
		return bean;
	}

	protected void setBean(DetalleDeudaBean bean) {
		this.bean = bean;
	}
}