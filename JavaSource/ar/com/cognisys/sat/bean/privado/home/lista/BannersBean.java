package ar.com.cognisys.sat.bean.privado.home.lista;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;

@ManagedBean(name = "Banners")
@ViewScoped
public class BannersBean extends SeccionBean {
	
	private static final long serialVersionUID = -8949646055281700588L;
	
	@Override
	protected void inicializacion() {}
	
	@Override
	public void cargar() {}

	@Override
	public void refrescar() {}

	@Override
	public void cargar(Object arg0) throws ExcepcionControladaError {}

	@Override
	public void siguiente() throws ExcepcionControladaError {}
}