package ar.com.cognisys.sat.excepcion;

public class SATRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 6937601598158055875L;

	public SATRuntimeException(Throwable t) {
		super(t.getMessage(), t);
	}
}