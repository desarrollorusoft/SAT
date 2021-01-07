package ar.com.cognisys.sat.bean.asistente;

import ar.com.cognisys.filemanager.controlador.FTPFileManager;
import ar.com.cognisys.filemanager.excepciones.FileManagerCommonException;
import ar.com.cognisys.filemanager.modelo.ArchivoFTP;
import ar.com.cognisys.sat.core.administrador.AdministradorArchivo;
import ar.com.cognisys.sat.core.administrador.AdministradorTramiteSubsidio;
import ar.com.cognisys.sat.core.modelo.comun.Archivo;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.TipoCartel;
import ar.com.cognisys.sat.core.modelo.comun.tramiteSubsidio.EstadoTramite;
import ar.com.cognisys.sat.core.modelo.comun.tramiteSubsidio.TramiteSubsidio;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.modelo.generador.subsidio.GeneradorPDFSubsidio;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;
import ar.com.cognisys.sat.v2.core.controlador.administrador.properties.AdministradorProperties;
import ar.com.cognisys.sat.v2.core.modelo.bo.ConsultaArchivo;
import ar.com.cognisys.sat.v2.core.modelo.exception.ErrorLecturaPropertiesException;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AsistenteSubsidio {

    public static boolean estaCompleto(TramiteSubsidio tramite) {
        return (tramite != null && tramite.getEstado() == EstadoTramite.ENVIADO || tramite.getEstado() == EstadoTramite.CERRADO);
    }

    public static void guardar(TramiteSubsidio tramite) throws ExcepcionControladaError {
        AdministradorTramiteSubsidio.guardar(tramite);
    }

    public static void enviar(TramiteSubsidio tramite)  throws ExcepcionControladaError {
        AdministradorTramiteSubsidio.enviar(tramite);
    }

    public static String subirImagen(Archivo a, String cuit, Integer ano) throws ExcepcionControladaError {
        try {
            String time = new Long((new Date()).getTime()).toString();

            a.setFilePath(AdministradorProperties.getInstancia().getPropiedad("FTP_DIRECTORIO_DDJJ_RS") + "subsidio/" + cuit + "/" + ano + "/");
            ConsultaArchivo c = new ConsultaArchivo();
            c.setData(a.getData());
            c.setDirectorio(a.getFilePath());
            c.setNombre( cuit+"-"+ano+"-"+time+"-"+a.getNombre());
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

    public static void generarPDF(TramiteSubsidio tramite) throws ExcepcionControladaError {
        GeneradorPDFSubsidio g = new GeneradorPDFSubsidio(tramite);
        g.generarPDF();

        try {
            String path = g.getPathArchibo();

            String directorioFtp = AdministradorProperties.getInstancia().getPropiedad("FTP_DIRECTORIO_DDJJ_RS") + "subsidio/" + CUIT.quitarMascara(tramite.getCuit()) + "/" + 2020 + "/";
            SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-YYYY" );

            String nombre =  CUIT.quitarMascara(tramite.getCuit()) + "-" + sdf.format( new Date() ) + "-" + new Date().getTime() + ".pdf";

            AdministradorArchivo.guardarEnFTP( directorioFtp , nombre, new FileInputStream( path ) );

            String url = AdministradorProperties.getInstancia().getPropiedad( "ENCABEZADO_FTP_PUBLICO_DESCARGAS" ) + directorioFtp + nombre;
            tramite.setUrlPDF(url);
        } catch (FileNotFoundException | ErrorLecturaPropertiesException e ) {
            throw new ExcepcionControladaError( "Ocurrio un error al recuperar el archivo", e );
        }
    }
}