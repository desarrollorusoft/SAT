package ar.com.cognisys.sat.bean.privado.rs;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaWizardBean;
import ar.com.cognisys.common.jsfbean.factory.FactoryParametrizacionWizard;
import ar.com.cognisys.common.jsfbean.parametrizacion.ParametrizacionWizardBean;
import ar.com.cognisys.sat.bean.Redireccionamiento;
import ar.com.cognisys.sat.bean.asistente.AsistenteDDJJPadron;
import ar.com.cognisys.sat.bean.privado.rs.declaracion.DatosActividadBean;
import ar.com.cognisys.sat.bean.privado.rs.declaracion.PyPOepBean;
import ar.com.cognisys.sat.bean.privado.rs.declaracion.ResumenRSBean;
import ar.com.cognisys.sat.bean.privado.rs.declaracion.ServiciosVariosBean;
import ar.com.cognisys.sat.core.modelo.comun.rs.PadronRS;
import ar.com.cognisys.sat.excepcion.SATRuntimeException;
import ar.com.cognisys.sat.modelo.comun.Mensaje;

@ManagedBean(name = "RSPadron")
@ViewScoped
public class RSPadronesBean extends PaginaWizardBean {
	
	private static final long serialVersionUID = -8052682533150177288L;
	private PadronRS padron;
	private Integer ano;
	
	@ManagedProperty(value = "#{DatosActividad}")
	private DatosActividadBean datosActividadBean;

	@ManagedProperty(value = "#{PyPOEP}")
	private PyPOepBean pyPOepBean;

	@ManagedProperty(value = "#{ServiciosVarios}")
	private ServiciosVariosBean serviciosVariosBean;

	@ManagedProperty(value = "#{ResumenRS}")
	private ResumenRSBean resumenRSBean;
	
	@Override
	protected void inicializacion() {
		this.inicializarWizardBean();
		try {
			this.setPadron( this.recuperarPadron() );
			
			if (this.getPadron() == null)
				this.enviarDevueltaARS();
			else
				this.getDatosActividadBean().cargar( this.getPadron() );
			
			this.setAno( this.getPadron().obtenerAnoDeclaracion() );
		} catch (ExcepcionControladaError e) {
			e.printStackTrace();
			this.enviarDevueltaARS();
		}
	}

	@Override
	protected ParametrizacionWizardBean generarParametrizacionWizard() {
		ParametrizacionWizardBean pw = FactoryParametrizacionWizard.generarInstancia();
		pw.agregar(1, this.getDatosActividadBean());
		pw.agregar(2, this.getPyPOepBean());
		pw.agregar(3, this.getServiciosVariosBean());
		pw.agregar(4, this.getResumenRSBean());
		
		return pw;
	}
	
	@Override
	protected void actualizarVista() {}
	
	@Override
	public void refrescar() throws ExcepcionControladaError {}

	private PadronRS recuperarPadron() {
		Map<String, String> mapa;
		try {
			mapa = (new Redireccionamiento()).recuperarDatos();

			if (!mapa.isEmpty())
				return AsistenteDDJJPadron.recuperarPadron( mapa.get("d"), mapa.get("p") );
			
		} catch (ExcepcionControladaError e) {
			throw new SATRuntimeException(e);
		}
		
		return null;
	}
	
	private void enviarDevueltaARS() {
		Mensaje.emitirMensajeError("No se pudo recuperar la cuenta");
		try {
			(new Redireccionamiento("/pages/pri/rs.xhtml")).redireccionar();
		} catch (ExcepcionControladaError e1) {
			e1.printStackTrace();
		}
	}
	
	public void guardarPadron() {
		try {
			AsistenteDDJJPadron.guardarPadron( this.getPadron() );
			Mensaje.emitirMensajeAviso("Guardado automático exitoso");
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			Mensaje.emitirMensajeAlerta("No se pudo hacer el guardado automático ("+e.getMessage()+")");
		}
	}
	
	public float dimensionBarraProgreso() {
		int cantidadTotal = this.getParametrizacionWizard().getTablaParametrizacion().size();
		int cantidadActual = this.getOrdenActual();
		
		return ( (cantidadActual * 100f) - 50f) / cantidadTotal;
	}
	
	public float dimensionPaso() {
		return 100f / this.getParametrizacionWizard().getTablaParametrizacion().size();
	}
	
	public String estilosPaso(int pasoItem) {
		if (this.getOrdenActual().equals( pasoItem ))
			return "active";
		else if (this.getOrdenActual() > pasoItem )
			return "activated";
		else
			return "";
	}
	
	public DatosActividadBean getDatosActividadBean() {
		return datosActividadBean;
	}

	public void setDatosActividadBean(DatosActividadBean datosActividadBean) {
		this.datosActividadBean = datosActividadBean;
	}

	public PyPOepBean getPyPOepBean() {
		return pyPOepBean;
	}

	public void setPyPOepBean(PyPOepBean pyPOepBean) {
		this.pyPOepBean = pyPOepBean;
	}

	public ServiciosVariosBean getServiciosVariosBean() {
		return serviciosVariosBean;
	}

	public void setServiciosVariosBean(ServiciosVariosBean serviciosVariosBean) {
		this.serviciosVariosBean = serviciosVariosBean;
	}

	public ResumenRSBean getResumenRSBean() {
		return resumenRSBean;
	}

	public void setResumenRSBean(ResumenRSBean resumenRSBean) {
		this.resumenRSBean = resumenRSBean;
	}

	public PadronRS getPadron() {
		return padron;
	}

	public void setPadron(PadronRS padron) {
		this.padron = padron;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
}