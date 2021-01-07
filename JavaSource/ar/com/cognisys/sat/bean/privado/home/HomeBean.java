package ar.com.cognisys.sat.bean.privado.home;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaWizardBean;
import ar.com.cognisys.common.jsfbean.factory.FactoryParametrizacionWizard;
import ar.com.cognisys.common.jsfbean.parametrizacion.ParametrizacionWizardBean;
import ar.com.cognisys.sat.bean.privado.home.detalle.DetalleCuentaBean;
import ar.com.cognisys.sat.bean.privado.home.lista.ListaCuentasBean;

@ManagedBean(name = "Home")
@ViewScoped
public class HomeBean extends PaginaWizardBean {
	
	private static final long serialVersionUID = 5392608503631110572L;
	
	@ManagedProperty(value = "#{ListaCuentas}")
	private ListaCuentasBean listaCuentasBean;
	
	@ManagedProperty(value = "#{DetalleCuenta}")
	private DetalleCuentaBean detalleCuentaBean;
	
	@Override
	protected void inicializacion() {
		this.inicializarWizardBean();
		this.getListaCuentasBean().cargar();
	}
	
	@Override
	protected void actualizarVista() {}

	@Override
	protected ParametrizacionWizardBean generarParametrizacionWizard() {
		
		ParametrizacionWizardBean pw = FactoryParametrizacionWizard.generarInstancia();
		pw.agregar(1, this.getListaCuentasBean());
		pw.agregar(2, this.getDetalleCuentaBean());
		
		return pw;
	}

	@Override
	public void refrescar() throws ExcepcionControladaError {}
	
	public ListaCuentasBean getListaCuentasBean() {
		return listaCuentasBean;
	}

	public void setListaCuentasBean(ListaCuentasBean listaCuentasBean) {
		this.listaCuentasBean = listaCuentasBean;
	}

	public DetalleCuentaBean getDetalleCuentaBean() {
		return detalleCuentaBean;
	}

	public void setDetalleCuentaBean(DetalleCuentaBean detalleCuentaBean) {
		this.detalleCuentaBean = detalleCuentaBean;
	}
}