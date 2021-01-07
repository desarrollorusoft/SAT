package ar.com.cognisys.sat.modelo.correo.mensaje;

import java.util.List;

import ar.com.cognisys.sat.core.correo.MensajeCorreo;
import ar.com.cognisys.sat.core.modelo.comun.comunicacion.EncriptacionURL;

public class MensajeFormularioConsulta extends MensajeCorreo {

	private String destinatario;
	private String idConsulta;
	private String host;
	@Override
	public String getEncabezado() {
		return "MVL SAT - Consultas";
	}

	@Override
	public String getMensaje() {
		return "<div style=\"\n" +
				"font-family: sans-serif;\n" +
				"color: rgb(70, 70, 70);\n" +
				"font-size: 16px;\n" +
				"max-width: 55rem;\n" +
				"padding: 1rem;\">\n" +
				"<p>Estimado/a Contribuyente</p>\n" +
				"<p>Le informamos que recibimos correctamente su solicitud, la cual est&aacute; siendo procesada por nuestros agentes tributarios. Recuerde que puede realizar tr&aacute;mites tributarios en <a href=\"https://www.vicentelopez.gov.ar/ingresos-publicos/inicio\" target=\"_blank\">www.vicentelopez.gov.ar/ingresos-publicos/inicio</>/.</p>" +
				"<p>Para Acceder a la misma, haga click en el siguiente enlace:</p>" +
				"  <div>\n" +
				"      <a href=\""+this.getHost()+"/pages/externos/plataforma-consultas.xhtml?t="+EncriptacionURL.encode(this.getIdConsulta())+"&b="+EncriptacionURL.encode( "soporte")+"\" style=\"text-decoration:none;\"><button style=\"background: #2962FF; color: #fff; width: 9rem;height: 2rem; border-radius: 3px; border-color: #2962FF; font-weight: 700\">Ver Respuesta</button></a>\n" +
				"  </div>\n" +
				"\n" +
				"  <br>\n" +
				"  <p>Secretar&iacute;a de Ingresos P&uacute;blicos <br/> Municipalidad de Vicente L&oacute;pez</p>\n" +
				"  <hr style=\"width: 34.5rem; margin-left: 0\">\n" +
				"  <p style=\"font-size: 0.65rem\">Este mail se genera autom&eacute;ticamente. Por favor, no responder.<br/>\n" +
				"      La Municipalidad garantiza el cumplimiento de las previsiones contempladas en la Ley 25.326 - Ley de protecci&oacute;n de los <br/>\n" +
				"      datos personales.\n" +
				"  </p>\n" +
				"\n" +
				"</div>";
	}

	@Override
	public String getDestinatario() {
		return this.destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getIdConsulta() {
		return idConsulta;
	}

	public void setIdConsulta(String idConsulta) {
		this.idConsulta = idConsulta;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public ar.com.cognisys.sat.core.modelo.comun.Contribuyente getRemitente() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ar.com.cognisys.sat.core.modelo.comun.Contribuyente> getDestinatarios() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathArchivo() {
		// TODO Auto-generated method stub
		return null;
	}
}