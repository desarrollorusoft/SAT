package ar.com.cognisys.sat.modelo.factory;

import ar.com.cognisys.sat.modelo.correo.mensaje.MensajeFormularioConsulta;

public class FactoryMensajeFormularioConsultas {

	public static MensajeFormularioConsulta generarInstancia(String destinatario, String idConsulta, String host){
		MensajeFormularioConsulta m = new MensajeFormularioConsulta();
		m.setDestinatario(destinatario);
		m.setIdConsulta( idConsulta );
		m.setHost( host );
		
		return m;
	}
}