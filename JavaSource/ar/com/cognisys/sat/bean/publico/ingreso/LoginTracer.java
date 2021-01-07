package ar.com.cognisys.sat.bean.publico.ingreso;

public class LoginTracer {

	private EstadoLogin estado;
	
	public LoginTracer() {
		this.setEstado( EstadoLogin.INICIO );
	}
	
	public void avanzar() {
		this.setEstado( this.getEstado().getSiguiente() );
	}

	public EstadoLogin getEstado() {
		return estado;
	}

	public void setEstado(EstadoLogin estado) {
		this.estado = estado;
	}
}