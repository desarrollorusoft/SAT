package ar.com.cognisys.sat.bean.privado.home.detalle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionWizardBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteDetalleCuenta;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.modelo.comun.Mensaje;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "DetalleCuenta")
@ViewScoped
public class DetalleCuentaBean extends SeccionWizardBean {
	
	private static final long serialVersionUID = -8949646055281700588L;
	private Cuenta cuenta;
	private Cuenta original;
	private boolean hayDeclaraciones;
	
	@ManagedProperty(value = "#{InformacionCuenta}")
	private InformacionCuentaBean informacionCuentaBean;
	
	@ManagedProperty(value = "#{DetalleDeuda}")
	private DetalleDeudaBean detalleDeudaBean;
	
	@ManagedProperty(value = "#{PPCCuenta}")
	private PPCBean ppcBean;
	
	@ManagedProperty(value = "#{BoletaPiletas}")
	private BoletaPiletasBean boletaPiletasBean;
	
	@Override
	protected void inicializacion() {}
	
	@Override
	public void cargar() {}

	@Override
	public void cargar(Object cuenta) throws ExcepcionControladaError {
		try {
			Cuenta c = (Cuenta) cuenta;
			this.original = c;
			this.setCuenta( AsistenteDetalleCuenta.cargarCuenta( c ) );
			this.getCuenta().setAlias(c.getAlias());
			if (((Cuenta) cuenta).sos(TiposCuentas.VEHICULOS) || (((Cuenta) cuenta).sos(TiposCuentas.RODADOS))) {
				RequestContext.getCurrentInstance().execute("PF('modalLibreDeuda').show()");
			}
		} catch (ExcepcionControladaAlerta e) {
			Mensaje.emitirMensajeAlerta(e.getMessage());
		}
		this.refrescar();
	}
	
	@Override
	public void refrescar() throws ExcepcionControladaError {
		if (this.muestroBoletaPiletas())
			this.getBoletaPiletasBean().cargar(this.getCuenta());
		else {
			this.getInformacionCuentaBean().cargar(this.getCuenta());
			this.getDetalleDeudaBean().cargar(this.getCuenta());

			if (this.muestroPPC())
				this.getPpcBean().cargar(this.getCuenta());
		}
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {}
	
	public void volverALista(){
		this.original.setAlias( this.cuenta.getAlias());
		this.volverAlPrimero();
	}
	public String getTitulo() {
		return this.getCuenta().getDatoCuenta() + " - " + this.getCuenta().getDescripcion();
	}
	
	public boolean muestroPPC() {
		return this.getCuenta().correspondePPC();
	}
	
	public boolean muestroDDJJ() {
		return this.getCuenta().correspondeDDJJ();
	}
	
	public boolean muestroEstadoDeuda() {
		return (cuenta != null && !esPiletas());
	}
	
	public boolean muestroInfo() {
		return (cuenta != null && !esPiletas());
	}
	
	public boolean muestroBoletaPiletas() {
		return (cuenta != null && esPiletas());
	}
	
	public boolean esPiletas() {
		return cuenta.sos(TiposCuentas.PILETAS);
	}
	
	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public InformacionCuentaBean getInformacionCuentaBean() {
		return informacionCuentaBean;
	}

	public void setInformacionCuentaBean(InformacionCuentaBean informacionCuentaBean) {
		this.informacionCuentaBean = informacionCuentaBean;
	}

	public DetalleDeudaBean getDetalleDeudaBean() {
		return detalleDeudaBean;
	}

	public void setDetalleDeudaBean(DetalleDeudaBean detalleDeudaBean) {
		this.detalleDeudaBean = detalleDeudaBean;
	}

	public PPCBean getPpcBean() {
		return ppcBean;
	}

	public void setPpcBean(PPCBean ppcBean) {
		this.ppcBean = ppcBean;
	}

	public boolean isHayDeclaraciones() {
		return hayDeclaraciones;
	}

	public void setHayDeclaraciones(boolean hayDeclaraciones) {
		this.hayDeclaraciones = hayDeclaraciones;
	}

	public BoletaPiletasBean getBoletaPiletasBean() {
		return boletaPiletasBean;
	}

	public void setBoletaPiletasBean(BoletaPiletasBean boletaPiletasBean) {
		this.boletaPiletasBean = boletaPiletasBean;
	}
}