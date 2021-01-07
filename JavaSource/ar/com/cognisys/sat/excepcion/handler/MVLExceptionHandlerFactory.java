package ar.com.cognisys.sat.excepcion.handler;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class MVLExceptionHandlerFactory extends ExceptionHandlerFactory {
	
	private ExceptionHandlerFactory parent;

	public MVLExceptionHandlerFactory(ExceptionHandlerFactory parent) {
		this.parent = parent;
	}

	@Override
	public ExceptionHandler getExceptionHandler() {

		ExceptionHandler handler = new MVLExceptionHandler(parent.getExceptionHandler());

		return handler;
	}
}
