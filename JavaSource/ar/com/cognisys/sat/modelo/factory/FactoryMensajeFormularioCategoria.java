package ar.com.cognisys.sat.modelo.factory;

import ar.com.cognisys.sat.modelo.correo.mensaje.MensajeFormularioCategoria;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;

public class FactoryMensajeFormularioCategoria {
	
	public static MensajeFormularioCategoria generarInstancia() {
		return new MensajeFormularioCategoria();
	}
	
	public static MensajeFormularioCategoria generarInstancia(String categoria, String destinatario) {
		MensajeFormularioCategoria m = generarInstancia();
		m.setCategoria(categoria);
		m.setDestinatario(destinatario);
		return m;
	}

	public static MensajeFormularioCategoria generarInstancia(String categoria, String destinatario, Consulta consulta, String motivo) {
		MensajeFormularioCategoria m = generarInstancia();
		m.setCategoria(categoria);
		m.setDestinatario(destinatario);
		m.setMotivo(motivo);
		m.setConsulta(consulta);
		return m;
	}
}