package ar.com.cognisys.sat.bean.privado.planes;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaWizardBean;
import ar.com.cognisys.common.jsfbean.factory.FactoryParametrizacionWizard;
import ar.com.cognisys.common.jsfbean.parametrizacion.ParametrizacionWizardBean;
import ar.com.cognisys.sat.bean.privado.home.detalle.DetalleCuentaBean;
import ar.com.cognisys.sat.bean.privado.home.lista.ListaCuentasBean;
import ar.com.cognisys.sat.bean.privado.planes.detalle.DetallePlanBean;
import ar.com.cognisys.sat.bean.privado.planes.lista.ListaPlanesBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "Planes")
@ViewScoped
public class PlanesBean extends PaginaWizardBean {
	
	private static final long serialVersionUID = 5392608503631110572L;
	
	@ManagedProperty(value = "#{ListaPlanes}")
	private ListaPlanesBean listaPlanesBean;
	
	@ManagedProperty(value = "#{DetallePlan}")
	private DetallePlanBean detallePlanBean;
	
	@Override
	protected void inicializacion() {
		this.inicializarWizardBean();
		this.getListaPlanesBean().cargar();
	}
	
	@Override
	protected void actualizarVista() {}

	@Override
	protected ParametrizacionWizardBean generarParametrizacionWizard() {
		
		ParametrizacionWizardBean pw = FactoryParametrizacionWizard.generarInstancia();
		pw.agregar(1, this.getListaPlanesBean());
		pw.agregar(2, this.getDetallePlanBean());
		
		return pw;
	}

	@Override
	public void refrescar() throws ExcepcionControladaError {}

	public ListaPlanesBean getListaPlanesBean() {
		return listaPlanesBean;
	}

	public void setListaPlanesBean(ListaPlanesBean listaPlanesBean) {
		this.listaPlanesBean = listaPlanesBean;
	}

	public DetallePlanBean getDetallePlanBean() {
		return detallePlanBean;
	}

	public void setDetallePlanBean(DetallePlanBean detallePlanBean) {
		this.detallePlanBean = detallePlanBean;
	}
}