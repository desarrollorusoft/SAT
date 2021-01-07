package ar.com.cognisys.sat.bean.pagina.externo.plataformaconsultas;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionGeneralBean;

@ManagedBean
@ViewScoped
public class BloqueoPlataformaBean extends SeccionGeneralBean{

	private static final long serialVersionUID = -8840946991417193161L;

	public String getTitulo(){
		return "Plataforma de Consultas";
	}

	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object arg0) throws ExcepcionControladaError {}

	@Override
	public void siguiente() throws ExcepcionControladaError {}

	@Override
	public void refrescar() throws ExcepcionControladaError {}

	@Override
	protected void inicializacion() {}
	
}
