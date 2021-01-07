package ar.com.cognisys.sat.modelo.enums;

public enum TipoDatoConsultas {

	cuenta("Cuenta"),
	dominio("Dominio"),
	padron("N� Padron"),
	cuit("CUIT"),
	rey("Rey o Jurisdiccional"),
	dni("DNI"),
	cuentaDominio("Cuenta / Dominio"),
	fiscalizacion("ID Fiscalizaci�n/N� Fiscalizaci�n")
	;
	
	private String descripcion;
	
	private TipoDatoConsultas(String descripcion){
		this.setDescripcion(descripcion);
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
