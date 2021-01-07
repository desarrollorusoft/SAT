package ar.com.cognisys.sat.modelo.dao;

import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;

public class ErrorReportandoException extends ExcepcionControladaError {

	private static final long serialVersionUID = 6764220360173740238L;

	public ErrorReportandoException(Throwable cause) {
		super("Error al Generar Reporte ", cause);
	}
}