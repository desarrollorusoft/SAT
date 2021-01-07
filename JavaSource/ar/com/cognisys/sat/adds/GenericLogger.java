package ar.com.cognisys.sat.adds;

import ar.com.cognisys.generics.logger.LoggerAdministrator;
import ar.com.cognisys.generics.logger.excepcion.FileAppenderException;
import org.apache.log4j.Logger;

public class GenericLogger {

    private static final String KEY_LOGGER = "SAT";

    // @formatter:off
    private GenericLogger() { }
    // @formatter:on

    public static Logger getLogger() {
        return new LoggerAdministrator().getLogger( KEY_LOGGER );
    }

    public static void initLogger(String ruta) throws FileAppenderException {
        new LoggerAdministrator().createFileLogger( KEY_LOGGER, ruta );
    }
}