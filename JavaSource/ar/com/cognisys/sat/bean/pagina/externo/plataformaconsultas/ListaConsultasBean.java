package ar.com.cognisys.sat.bean.pagina.externo.plataformaconsultas;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionGeneralBean;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.ConsultaYLista;
import ar.com.cognisys.sat.core.modelo.factory.consultas.FactoryConsulta;

@ManagedBean
@ViewScoped
public class ListaConsultasBean extends SeccionGeneralBean {

	private static final long serialVersionUID = 6976634011338794469L;
	
	private Consulta consulta;
	private List<Consulta> listaConsultasAsociadas;
	
	public void verConsulta(Consulta consulta) throws ExcepcionControladaError {
		this.irA( NavegacionPlataformaConsultas.SOPORTE, consulta );
	}

	@Override
	public void cargar() {}

	@Override
	public void cargar(Object c) {
		ConsultaYLista lc = (ConsultaYLista) c; 
		
		this.setConsulta( lc.getConsulta() );
		this.setListaConsultasAsociadas( lc.getListaConsultas() );
	}

	@Override
	public void siguiente() {}

	@Override
	public void refrescar() {}

	@Override
	protected void inicializacion() {
		this.consulta = FactoryConsulta.generarInstancia();
	}
	
	public void irAFormulario() throws ExcepcionControladaError{
		this.irA( NavegacionPlataformaConsultas.FORMULARIO, this.getConsulta() );
	}
	
	public Consulta getConsulta() {
		return consulta;
	}
	
	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}
	
	public List<Consulta> getListaConsultasAsociadas() {
		return listaConsultasAsociadas;
	}
	
	public void setListaConsultasAsociadas(List<Consulta> listaConsultasAsociadas) {
		this.listaConsultasAsociadas = listaConsultasAsociadas;
	}
}