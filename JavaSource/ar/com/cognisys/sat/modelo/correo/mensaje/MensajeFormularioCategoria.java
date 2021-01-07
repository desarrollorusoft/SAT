package ar.com.cognisys.sat.modelo.correo.mensaje;

import java.util.List;

import ar.com.cognisys.sat.core.correo.MensajeCorreo;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;

public class MensajeFormularioCategoria extends MensajeCorreo {
	
	private String destinatario;
	private String categoria;
	private String subCategoria;
	private String motivo;
	private Consulta consulta;

	@Override
	public String getEncabezado() {
		return "MVL SAT - Consulta (" + this.getCategoria() + ")";
	}

	@Override
	public String getMensaje() {
		if ( this.getMotivo() == null || this.getMotivo().isEmpty() )
			return "Se genero una nueva consulta con el siguiente motivo: " + this.getCategoria() + ".";
		else
			return "Se Re Abrió el caso Nº:"+this.getConsulta().getId()+" por el siguiente motivo: '"+this.getMotivo()+"'";
	}

	@Override
	public String getDestinatario() {
		return destinatario;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getSubCategoria() {
		return subCategoria;
	}

	public void setSubCategoria(String subCategoria) {
		this.subCategoria = subCategoria;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	
	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	@Override
	public String getPathArchivo() {
		// TODO Auto-generated method stub
		return null;
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
}