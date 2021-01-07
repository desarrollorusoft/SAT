package ar.com.cognisys.sat.excepcion.handler;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.LogManager;

import javax.ejb.EJBException;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import ar.com.cognisys.conexiones.logger.ConnectionLogger;
import ar.com.cognisys.sat.adds.DBLogger;
import ar.com.cognisys.sat.adds.GenericLogger;
import ar.com.cognisys.sat.adds.NormalLogger;
import ar.com.cognisys.sat.core.correo.AdministradorMails;
import ar.com.cognisys.sat.core.correo.EnviadorCorreoRunnable;
import ar.com.cognisys.sat.core.correo.mensaje.MensajeProblemasConBase;
import ar.com.cognisys.sat.core.modelo.abstracto.ExcepcionControlada;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.excepcion.Mensaje;

public class MVLExceptionHandler extends ExceptionHandlerWrapper implements Serializable {

	private static final long serialVersionUID = 7874255261933367998L;
	private ExceptionHandler wrapped;
	private static ExcepcionNoControladaProperties excepcionNoControladaProperties = new ExcepcionNoControladaProperties();

	public MVLExceptionHandler(ExceptionHandler exceptionHandler) {
		this.wrapped = exceptionHandler;
		LogManager.getLogManager().getLogger("javax.enterprise.resource.webcontainer.jsf.lifecycle").setLevel(Level.OFF);
		LogManager.getLogManager().getLogger("javax.enterprise.resource.webcontainer.jsf.application").setLevel(Level.OFF);
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}

	@Override
	public void handle() throws FacesException {
		try {
			final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
			
			while (i.hasNext()) {
				ExceptionQueuedEvent event = i.next();
				ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

				Throwable t = context.getException();

				while ((t instanceof FacesException || t instanceof EJBException || t instanceof ELException) && t.getCause() != null)
					t = t.getCause();
				
				String mensaje = null;
				if (t instanceof ExcepcionControlada || t instanceof ar.com.cognisys.common.exception.ExcepcionControlada)
					mensaje = t.getMessage();
				else 
					mensaje = excepcionNoControladaProperties.getMensaje(t.getClass().getSimpleName());

				if (t instanceof ExcepcionControladaAlerta || t instanceof ar.com.cognisys.common.exception.ExcepcionControladaAlerta) {
					Mensaje.addMessageWarn(mensaje);
					GenericLogger.getLogger().warn(mensaje);
				} else {
					Mensaje.addMessageError(mensaje);

					if (isNormal(t))
						NormalLogger.getLogger().error(mensaje, t);
					else
						GenericLogger.getLogger().error(mensaje, t);

					if (isDB(t))
						DBLogger.getLogger().error(mensaje, t);
				}
				
				i.remove();
			}
		} finally {}
	}

	private boolean isNormal(Throwable t) {
		try {
			return t.getMessage().equals( "PWC3999: Cannot create a session after the response has been committed" ) ||
					t.getMessage().equals( "null source" );
		} catch (Throwable e) {
			return false;
		}
	}

	private boolean isDB(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		String text = sw.toString();

		if (text.contains( "Connection not established" ))
			try {
				ConnectionLogger.getLogger().error("Problema de perdida de conexion", t);
				AdministradorMails.enviar( new MensajeProblemasConBase(text) );
			} catch (ExcepcionControlada e1) {}

		return text.contains( "SQLException" );
	}
}