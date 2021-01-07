package ar.com.cognisys.sat.bean.publico.ingreso;

public enum EstadoLogin {

									REDIRECCIONANDO(7, "", 0, null),
								RECUPERANDO_TRAMITE_SUBSIDIO(6, "", 0, REDIRECCIONANDO),
							RECUPERANDO_PLANES(6, "", 0, RECUPERANDO_TRAMITE_SUBSIDIO),
						RECUPERANDO_DEUDAS(5, "", 0, RECUPERANDO_PLANES),
					RECUPERANDO_CUENTAS(4, "", 0, RECUPERANDO_DEUDAS), 
				RECUPERANDO_COMERCIO(3, "", 0, RECUPERANDO_CUENTAS), 
			RECUPERANDO_DATOS(2, "", 0, RECUPERANDO_COMERCIO), 
		VALIDACION(1, "", 5, RECUPERANDO_DATOS), 
	INICIO(0, "", 0, VALIDACION), 
	;
	
	private int paso;
	private String descripcion;
	private int porcentaje;
	private EstadoLogin siguiente;
	
	private EstadoLogin(int paso, String descripcion, int porcentaje, EstadoLogin siguiente) {
		this.paso = paso;
		this.descripcion = descripcion;
		this.porcentaje = porcentaje;
		this.siguiente = siguiente;
	}

	public int getPaso() {
		return paso;
	}

	public void setPaso(int paso) {
		this.paso = paso;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(int porcentaje) {
		this.porcentaje = porcentaje;
	}

	public EstadoLogin getSiguiente() {
		return siguiente;
	}

	public void setSiguiente(EstadoLogin siguiente) {
		this.siguiente = siguiente;
	}
}