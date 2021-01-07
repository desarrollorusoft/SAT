package ar.com.cognisys.sat.modelo.enums;

public enum TipoDatoConsultas {

	cuenta("Cuenta"),
	dominio("Dominio"),
	padron("Nº Padron"),
	cuit("CUIT"),
	rey("Rey o Jurisdiccional"),
	dni("DNI"),
	cuentaDominio("Cuenta / Dominio"),
	fiscalizacion("ID Fiscalización/N° Fiscalización")
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
