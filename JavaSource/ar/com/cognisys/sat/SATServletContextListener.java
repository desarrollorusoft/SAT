package ar.com.cognisys.sat;

import ar.com.cognisys.conexiones.logger.ConnectionLogger;
import ar.com.cognisys.generics.logger.LoggerAdministrator;
import ar.com.cognisys.generics.logger.excepcion.FileAppenderException;
import ar.com.cognisys.sat.adds.DBLogger;
import ar.com.cognisys.sat.adds.GenericLogger;
import ar.com.cognisys.sat.adds.NormalLogger;
import ar.com.cognisys.sat.core.adds.CommerceDebtLogger;
import ar.com.cognisys.sat.core.adds.MailLogger;
import ar.com.cognisys.sat.core.adds.PPCLogger;
import ar.com.cognisys.sat.core.administrador.AdministradorConexiones;
import ar.com.cognisys.sat.core.contenedor.*;
import ar.com.cognisys.sat.v2.core.modelo.contenedor.ContenedorAlertas;
import ar.com.cognisys.sat.v2.core.modelo.contenedor.ContenedorCartelerias;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SATServletContextListener implements ServletContextListener {

	private static final String FOLDER = "../logs/SAT_logger/";
	private static final String SQL_CHECK_CONNECTION = "SELECT 1 FROM recaudaciones:dual";

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		this.initLoggers();
		GenericLogger.getLogger().info("INIT - Se inicializaron los logguers!");
		try {
			AdministradorConexiones.iniciarInstancia("jdbc/SAT_Informix", SQL_CHECK_CONNECTION);
			ContenedorCaracterSAT.getInstancia().cargar();
			ContenedorActividadComercial.getInstancia().cargar();
			ContenedorAlertas.iniciarInstancia();
			ContenedorCartelerias.iniciarInstancia();
			ContenedorTipoRechazoRS.getInstancia().cargar();
			ContenedorTopesRS.getInstancia().cargar();
			ContenedorTasasComercio.getInstancia().cargar();
			ContenedorMotivosBaja.getInstancia().cargar();
		} catch (Exception e) {
			GenericLogger.getLogger().error("INIT - FALLO!!!", e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	private void initLoggers() {
		try {
			GenericLogger.initLogger( FOLDER + "generic.log" );
			NormalLogger.initLogger( FOLDER + "normal.log" );
			DBLogger.initLogger( FOLDER + "db.log" );
			CommerceDebtLogger.initLogger( FOLDER + "commerce_debt.log" );
			PPCLogger.initLogger( FOLDER + "commerce_debt.log" );
			ConnectionLogger.initLogger(FOLDER + "connection.log");
			MailLogger.initLogger(FOLDER + "mail.log");
		} catch ( FileAppenderException e ) {
			Logger logger = new LoggerAdministrator().getLogger( "SAT_INIT_ERROR" );
			logger.fatal( e.getMessage(), e );
		}
	}
}
