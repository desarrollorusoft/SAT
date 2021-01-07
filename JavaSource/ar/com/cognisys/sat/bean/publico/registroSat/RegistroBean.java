package ar.com.cognisys.sat.bean.publico.registroSat;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaWizardBean;
import ar.com.cognisys.common.jsfbean.factory.FactoryParametrizacionWizard;
import ar.com.cognisys.common.jsfbean.parametrizacion.ParametrizacionWizardBean;

@ManagedBean(name = "Registro")
@ViewScoped
public class RegistroBean extends PaginaWizardBean {

	private static final long serialVersionUID = 3932430500329370509L;
	
	@ManagedProperty(value = "#{DatosRegistro}")
	private DatosRegistroBean datosRegistroBean;
	
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
		pw.agregar(1, this.getDatosRegistroBean());
		pw.agregar(2, this.getTerminosRegistroBean());
		pw.agregar(3, this.getActivacionRegistroBean());
		
		return pw;
	}
	
	@Override
	protected void actualizarVista() {}
	
	@Override
	public void refrescar() throws ExcepcionControladaError {}

	public DatosRegistroBean getDatosRegistroBean() {
		return datosRegistroBean;
	}

	public void setDatosRegistroBean(DatosRegistroBean datosRegistroBean) {
		this.datosRegistroBean = datosRegistroBean;
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