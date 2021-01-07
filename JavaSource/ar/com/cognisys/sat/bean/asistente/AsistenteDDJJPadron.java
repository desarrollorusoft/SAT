package ar.com.cognisys.sat.bean.asistente;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import ar.com.cognisys.filemanager.controlador.FTPFileManager;
import ar.com.cognisys.filemanager.excepciones.FileManagerCommonException;
import ar.com.cognisys.filemanager.modelo.ArchivoFTP;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.core.administrador.AdministradorRegimenSimplificado;
import ar.com.cognisys.sat.core.contenedor.ContenedorTipoRechazoRS;
import ar.com.cognisys.sat.core.modelo.comun.Archivo;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.Actividades;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.DDJJCarteleria;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.DDJJOEP;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.DDJJSV;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.TipoCartel;
import ar.com.cognisys.sat.core.modelo.comun.rs.Comercio;
import ar.com.cognisys.sat.core.modelo.comun.rs.DDJJRS;
import ar.com.cognisys.sat.core.modelo.comun.rs.PadronRS;
import ar.com.cognisys.sat.core.modelo.comun.rs.validador.ValidadorDDJJRS;
import ar.com.cognisys.sat.core.modelo.comun.rs.versiones.VersionPadronRS;
import ar.com.cognisys.sat.core.modelo.comun.rs.versiones.VersionPadronRS2018;
import ar.com.cognisys.sat.core.modelo.comun.rs.versiones.VersionPadronRS2019;
import ar.com.cognisys.sat.core.modelo.comun.rs.versiones.VersionPadronRS2020;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.modelo.factory.rs.FactoryValidadorDDJJRS;
import ar.com.cognisys.sat.v2.core.controlador.administrador.properties.AdministradorProperties;
import ar.com.cognisys.sat.v2.core.modelo.bo.ConsultaArchivo;
import ar.com.cognisys.sat.v2.core.modelo.exception.ErrorLecturaPropertiesException;
import ar.com.cognisys.sat.v2.vista.modelo.v2.view.rs.ConflictosDDJJRS;

public class AsistenteDDJJPadron {

	public static PadronRS recuperarPadron(String ano, String numeroCuenta) {
		return SesionBean.getUsuarioLogueado().getComercio().obtenerPadronDeclaracion( ano, numeroCuenta );
	}

	public static void validarPyP(VersionPadronRS version, boolean poseeCarteleria, boolean poseeOep) throws ExcepcionControladaAlerta {
		switch ( version.getAno() ) {
			case 2018:	validarCarteleria2018( (VersionPadronRS2018) version, poseeCarteleria, poseeOep );	break;
			case 2019:	validarCarteleria2019( (VersionPadronRS2019) version, poseeCarteleria, poseeOep );	break;
			case 2020:	validarCarteleria2020( (VersionPadronRS2020) version, poseeCarteleria, poseeOep );	break;
			default: 	break;
		}
	}
	
	public static Actividades obtenerActividades(VersionPadronRS versionEnCurso) {
		switch ( versionEnCurso.getAno() ) {
			case 2018:	return ((VersionPadronRS2018) versionEnCurso).getActividades();
			case 2019:	return ((VersionPadronRS2019) versionEnCurso).getActividades();
			case 2020:	return ((VersionPadronRS2020) versionEnCurso).getActividades();
		}
		
		return null;
	}
	
	public static List<DDJJCarteleria> obtenerCarteleria(VersionPadronRS versionEnCurso) {
		switch ( versionEnCurso.getAno() ) {
			case 2018:	return ((VersionPadronRS2018) versionEnCurso).getListaCarteleria();
			case 2019:	return ((VersionPadronRS2019) versionEnCurso).getListaCarteleria();
			case 2020:	return ((VersionPadronRS2020) versionEnCurso).getListaCarteleria();
		}
		return null;
	}

	public static List<DDJJOEP> obtenerOEP(VersionPadronRS versionEnCurso) {
		switch ( versionEnCurso.getAno() ) {
			case 2018:	return ((VersionPadronRS2018) versionEnCurso).getListaOEP();
			case 2019:	return ((VersionPadronRS2019) versionEnCurso).getListaOEP();
			case 2020:	return ((VersionPadronRS2020) versionEnCurso).getListaOEP();
			default: 	break;
		}
		return null;
	}
	
	private static void validarCarteleria2018(VersionPadronRS2018 version, boolean poseeCarteleria, boolean poseeOep) throws ExcepcionControladaAlerta {
		if( poseeCarteleria && ( version.getListaCarteleria() == null  || version.getListaCarteleria().isEmpty() ) )
			throw new ExcepcionControladaAlerta("Debe ingresar al menos una Carteleróa");
		
		if( poseeOep && ( version.getListaOEP() == null || version.getListaOEP().isEmpty() ) )
			throw new ExcepcionControladaAlerta("Debe ingresar al menos una OEP");
	}

	private static void validarCarteleria2019(VersionPadronRS2019 version, boolean poseeCarteleria, boolean poseeOep) throws ExcepcionControladaAlerta {
		if( poseeCarteleria && ( version.getListaCarteleria() == null  || version.getListaCarteleria().isEmpty() ) )
			throw new ExcepcionControladaAlerta("Debe ingresar al menos una Carteleria");
		
		if( poseeOep && ( version.getListaOEP() == null || version.getListaOEP().isEmpty() ) )
			throw new ExcepcionControladaAlerta("Debe ingresar al menos una OEP");
	}
	
	private static void validarCarteleria2020(VersionPadronRS2020 version, boolean poseeCarteleria, boolean poseeOep) throws ExcepcionControladaAlerta {
		if( poseeCarteleria && ( version.getListaCarteleria() == null  || version.getListaCarteleria().isEmpty() ) )
			throw new ExcepcionControladaAlerta("Debe ingresar al menos una Carteleria");
		
		if( poseeOep && ( version.getListaOEP() == null || version.getListaOEP().isEmpty() ) )
			throw new ExcepcionControladaAlerta("Debe ingresar al menos una OEP");
	}
	
	public static void actualizarPyP(VersionPadronRS versionEnCurso, List<DDJJCarteleria> listaCarteleria, List<DDJJOEP> listaOEP) {
		switch ( versionEnCurso.getAno() ) {
			case 2018:	
				VersionPadronRS2018 v2018 = (VersionPadronRS2018) versionEnCurso;
				v2018.setListaCarteleria(listaCarteleria);
				v2018.setListaOEP(listaOEP);
				break;
			case 2019:
				VersionPadronRS2019 v2019 = (VersionPadronRS2019) versionEnCurso;
				v2019.setListaCarteleria(listaCarteleria);
				v2019.setListaOEP(listaOEP);
				break;
			case 2020:
				VersionPadronRS2020 v2020 = (VersionPadronRS2020) versionEnCurso;
				v2020.setListaCarteleria(listaCarteleria);
				v2020.setListaOEP(listaOEP);
				break;
			default: 	
				break;
		}
	}

	public static DDJJSV obtenerServiciosVarios(VersionPadronRS versionEnCurso) {
		switch ( versionEnCurso.getAno() ) {
			case 2018:	return ((VersionPadronRS2018) versionEnCurso).getServiciosVarios();
			case 2019:	return ((VersionPadronRS2019) versionEnCurso).getServiciosVarios();
			case 2020:	return ((VersionPadronRS2020) versionEnCurso).getServiciosVarios();
			default: 	break;
		}
		
		return null;
	}
	
	public static void validarSV(VersionPadronRS versionEnCurso, boolean poseeSV) throws ExcepcionControladaAlerta {
		if (poseeSV) {
			switch ( versionEnCurso.getAno() ) {
				case 2018:	validarServiciosVarios2018( (VersionPadronRS2018) versionEnCurso );	break;
				case 2019:	validarServiciosVarios2019( (VersionPadronRS2019) versionEnCurso );	break;
				case 2020:	validarServiciosVarios2020( (VersionPadronRS2020) versionEnCurso );	break;
				default: 	break;
			}			
		}
	}

	private static void validarServiciosVarios2018(VersionPadronRS2018 versionEnCurso) throws ExcepcionControladaAlerta {
		if( (versionEnCurso.getServiciosVarios().getCalderas() == null || versionEnCurso.getServiciosVarios().getCalderas() <= 0)
				&& (versionEnCurso.getServiciosVarios().getMotores() == null || versionEnCurso.getServiciosVarios().getMotores() <= 0) )
			throw new ExcepcionControladaAlerta("Debe ingresar la cantidad de calderas y/o cantidad de motores que posee");	
	}

	private static void validarServiciosVarios2019(VersionPadronRS2019 versionEnCurso) throws ExcepcionControladaAlerta {
		if( (versionEnCurso.getServiciosVarios().getCalderas() == null || versionEnCurso.getServiciosVarios().getCalderas() <= 0)
				&& (versionEnCurso.getServiciosVarios().getMotores() == null || versionEnCurso.getServiciosVarios().getMotores() <= 0) )
			throw new ExcepcionControladaAlerta("Debe ingresar la cantidad de calderas y/o cantidad de motores que posee");	
	}
	
	private static void validarServiciosVarios2020(VersionPadronRS2020 versionEnCurso) throws ExcepcionControladaAlerta {
		if( (versionEnCurso.getServiciosVarios().getCalderas() == null || versionEnCurso.getServiciosVarios().getCalderas() <= 0)
				&& (versionEnCurso.getServiciosVarios().getMotores() == null || versionEnCurso.getServiciosVarios().getMotores() <= 0) )
			throw new ExcepcionControladaAlerta("Debe ingresar la cantidad de calderas y/o cantidad de motores que posee");	
	}
	
	public static void actualizarSV(VersionPadronRS versionEnCurso, DDJJSV sv) {
		switch ( versionEnCurso.getAno() ) {
			case 2018:	((VersionPadronRS2018) versionEnCurso).setServiciosVarios( sv ); break;
			case 2019:	((VersionPadronRS2019) versionEnCurso).setServiciosVarios( sv ); break;
			case 2020:	((VersionPadronRS2020) versionEnCurso).setServiciosVarios( sv ); break;
			default: 	break;
		}
	}
	
	public static ConflictosDDJJRS[] vaidarVersion(VersionPadronRS version) {
		ValidadorDDJJRS validador = FactoryValidadorDDJJRS.generar( version );
		validador.validar();
		return validador.obtenerConflictos();
	}

	public static void completarPadron(PadronRS padron) throws ExcepcionControladaError {
		padron.getVersionEnCurso().setCompleto(true);
		guardarPadron( padron );
		padron.setVersionEnCurso(null);
	}
	
	public static void guardarPadron(PadronRS padron) throws ExcepcionControladaError {
		padron.prepararGuardado();
		AdministradorRegimenSimplificado.guardar( padron, SesionBean.getUsuarioLogueado() );
	}

	public static void registrarRechazo(PadronRS padron, ConflictosDDJJRS[] errores) throws ExcepcionControladaError {		
		guardarPadron( padron );
		
		Integer codigoError = ContenedorTipoRechazoRS.getInstancia().obtenerCodigoError( errores );
		DDJJRS ddjj = recuperarDDJJRS( padron );
		
		AdministradorRegimenSimplificado.rechazar( ddjj, codigoError );
	}

	private static DDJJRS recuperarDDJJRS(PadronRS padron) {
		Comercio c = SesionBean.getUsuarioLogueado().getComercio();
		
		return c.obtenerDeclaracion( padron );
	}
	
	public static String subirImagenPyP(Archivo a, String cuenta, Integer ano, TipoCartel tipo) throws ExcepcionControladaError {
        try {
        	String time = new Long((new Date()).getTime()).toString();
        	
            a.setFilePath(AdministradorProperties.getInstancia().getPropiedad("FTP_DIRECTORIO_DDJJ_RS") + "/" + cuenta + "/" + ano + "/");
            ConsultaArchivo c = new ConsultaArchivo();
            c.setData(a.getData());
            c.setDirectorio(a.getFilePath());
            c.setNombre( cuenta+"-"+ano+"-"+tipo.getCodigo()+"-"+time+"-"+a.getNombre());
            c.normalizarNombre();
            c.setTipo(a.getTipoContenido());
            c.generarNombre();

            ArchivoFTP af = new ArchivoFTP();
            af.setArchivo( new ByteArrayInputStream( c.getData() ) );
            af.setNombre( c.getNombreGenerado() );
    		
    		try {
    			FTPFileManager.guardarArchivo( AdministradorProperties.getInstancia().getPropiedad( "HOST_FTP" ), 
    										   Integer.valueOf( AdministradorProperties.getInstancia().getPropiedad( "PUERTO_FTP" ) ), 
    										   AdministradorProperties.getInstancia().getPropiedad( "USUARIO_FTP" ), 
    										   AdministradorProperties.getInstancia().getPropiedad( "PASS_FTP" ) , 
    										   AdministradorProperties.getInstancia().getPropiedad( "DIRECTORIO_PUBLICO" ) + c.getDirectorio(), 
    										   af );
    		} catch ( FileManagerCommonException e ) {
    			throw new ExcepcionControladaError( e.getMessage(), e );
    		}

    		return AdministradorProperties.getInstancia().getPropiedad("ENCABEZADO_FTP_PUBLICO_DESCARGAS")
                    + c.getDirectorio()
                    + c.getNombreGenerado();
    		
//            a.setFilePath(AdministradorProperties.getInstancia().getPropiedad("ENCABEZADO_FTP_PUBLICO_DESCARGAS")
//                    + c.getDirectorio()
//                    + c.getNombreGenerado());
        } catch (ErrorLecturaPropertiesException e) {
            throw new ExcepcionControladaError(e.getMessage(), e);
        }
	}
}