package ar.com.cognisys.sat.bean.privado.subsidio;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaWizardBean;
import ar.com.cognisys.common.jsfbean.factory.FactoryParametrizacionBean;
import ar.com.cognisys.common.jsfbean.factory.FactoryParametrizacionWizard;
import ar.com.cognisys.common.jsfbean.parametrizacion.ParametrizacionGeneralBean;
import ar.com.cognisys.common.jsfbean.parametrizacion.ParametrizacionWizardBean;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteSubsidio;
import ar.com.cognisys.sat.bean.privado.planes.detalle.DetallePlanBean;
import ar.com.cognisys.sat.bean.privado.planes.lista.ListaPlanesBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "Subsidio")
@ViewScoped
public class SubsidioBean extends PaginaWizardBean {
	
	private static final long serialVersionUID = 5392608503631110572L;
	
	@ManagedProperty(value = "#{SubsidioCargar}")
	private SubsidioCargarBean subsidioCargarBean;
	
	@ManagedProperty(value = "#{SubsidioCompleto}")
	private SubsidioCompletoBean subsidioCompletoBean;
	
	@Override
	protected void inicializacion() {
		this.inicializarWizardBean();
		try {
			if ( AsistenteSubsidio.estaCompleto(SesionBean.getUsuarioLogueado().getTramiteSubsidio()) )  {
				this.avanzar();
				this.getSubsidioCompletoBean().cargar( SesionBean.getUsuarioLogueado().getTramiteSubsidio() );
			} else
				this.getSubsidioCargarBean().cargar( SesionBean.getUsuarioLogueado().getTramiteSubsidio() );
		} catch (ExcepcionControladaError e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void actualizarVista() {}

	@Override
	protected ParametrizacionWizardBean generarParametrizacionWizard() {
		ParametrizacionWizardBean pw = FactoryParametrizacionWizard.generarInstancia();
		pw.agregar(1, this.getSubsidioCargarBean());
		pw.agregar(2, this.getSubsidioCompletoBean());
		
		return pw;
	}

	@Override
	public void refrescar() throws ExcepcionControladaError {}

	public SubsidioCargarBean getSubsidioCargarBean() {
		return subsidioCargarBean;
	}

	public void setSubsidioCargarBean(SubsidioCargarBean subsidioCargarBean) {
		this.subsidioCargarBean = subsidioCargarBean;
	}

	public SubsidioCompletoBean getSubsidioCompletoBean() {
		return subsidioCompletoBean;
	}

	public void setSubsidioCompletoBean(SubsidioCompletoBean subsidioCompletoBean) {
		this.subsidioCompletoBean = subsidioCompletoBean;
	}
}