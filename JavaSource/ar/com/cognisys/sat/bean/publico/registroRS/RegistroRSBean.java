package ar.com.cognisys.sat.bean.publico.registroRS;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaWizardBean;
import ar.com.cognisys.common.jsfbean.factory.FactoryParametrizacionWizard;
import ar.com.cognisys.common.jsfbean.parametrizacion.ParametrizacionWizardBean;
import ar.com.cognisys.sat.bean.publico.registroSat.ActivacionRegistroBean;
import ar.com.cognisys.sat.bean.publico.registroSat.TerminosRegistroBean;

@ManagedBean(name = "RegistroRS")
@ViewScoped
public class RegistroRSBean extends PaginaWizardBean {
	
	private static final long serialVersionUID = 6122772731044260563L;
	
	@ManagedProperty(value = "#{DatosRegistroRS}")
	private DatosRegistroRSBean datosRegistroRSBean;

	@ManagedProperty(value = "#{TerminosRegistro}")
	private TerminosRegistroBean terminosRegistroBean;

	@ManagedProperty(value = "#{ActivacionRegistro}")
	private ActivacionRegistroBean activacionRegistroBean;
	
	@Override
	protected void inicializacion() {
		this.inicializarWizardBean();
	}
	
	@Override
	protected ParametrizacionWizardBean generarParametrizacionWizard() {
		
		ParametrizacionWizardBean pw = FactoryParametrizacionWizard.generarInstancia();
		pw.agregar(1, this.getDatosRegistroRSBean());
		pw.agregar(2, this.getTerminosRegistroBean());
		pw.agregar(3, this.getActivacionRegistroBean());
		
		return pw;
	}
	
	@Override
	protected void actualizarVista() {}
	
	@Override
	public void refrescar() throws ExcepcionControladaError {}

	public DatosRegistroRSBean getDatosRegistroRSBean() {
		return datosRegistroRSBean;
	}

	public void setDatosRegistroRSBean(DatosRegistroRSBean datosRegistroRSBean) {
		this.datosRegistroRSBean = datosRegistroRSBean;
	}

	public TerminosRegistroBean getTerminosRegistroBean() {
		return terminosRegistroBean;
	}

	public void setTerminosRegistroBean(TerminosRegistroBean terminosRegistroBean) {
		this.terminosRegistroBean = terminosRegistroBean;
	}

	public ActivacionRegistroBean getActivacionRegistroBean() {
		return activacionRegistroBean;
	}

	public void setActivacionRegistroBean(ActivacionRegistroBean activacionRegistroBean) {
		this.activacionRegistroBean = activacionRegistroBean;
	}
}