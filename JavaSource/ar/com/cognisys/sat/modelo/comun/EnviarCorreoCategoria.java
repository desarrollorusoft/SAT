package ar.com.cognisys.sat.modelo.comun;

import java.io.Serializable;

import ar.com.cognisys.sat.bean.asistente.AsistenteAdministradorFormularioConsultas;
import ar.com.cognisys.sat.core.correo.AdministradorMails;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.modelo.factory.FactoryMensajeFormularioCategoria;

public class EnviarCorreoCategoria implements Runnable, Serializable {

	private static final long serialVersionUID = 4259132298359153250L;

	private Consulta consulta;
	private String motivo;
	
	public EnviarCorreoCategoria() {}
	
	public EnviarCorreoCategoria(Consulta consulta, String motivo) {
		this.setConsulta(consulta);
		this.setMotivo(motivo);
	}

	@Override
	public void run() {
		try {
			AdministradorMails.enviar(  FactoryMensajeFormularioCategoria.generarInstancia( this.getConsulta().getCategoria().getNombre(), AsistenteAdministradorFormularioConsultas.obtenerCategoriaCorreo( this.getConsulta().getCategoria() ), this.getConsulta(), this.getMotivo() ) );
		} catch ( ExcepcionControladaAlerta e ) {
			e.printStackTrace();
		} catch ( ExcepcionControladaError e ) {
			e.printStackTrace();
		}
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
}