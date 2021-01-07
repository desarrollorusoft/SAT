package ar.com.cognisys.sat.bean.pagina.externo.plataformaconsultas;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import ar.com.cognisys.sat.bean.asistente.AsistentePlataformaConsultasBean;
import ar.com.cognisys.sat.excepcion.Mensaje;
import ar.com.cognisys.sat.modelo.comun.EnviarCorreoCategoria;
import ar.com.cognisys.sat.core.administrador.AdministradorArchivo;
import ar.com.cognisys.sat.core.modelo.abstracto.ExcepcionControlada;
import ar.com.cognisys.sat.core.modelo.comun.Archivo;
import ar.com.cognisys.sat.core.modelo.comun.ArchivoConsulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.ConsultaAsociada;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.modelo.factory.FactoryArchivoConsulta;
import ar.com.cognisys.sat.core.modelo.factory.consultas.FactoryNuevaConsulta;
import ar.com.cognisys.sat.v2.vista.modelo.v2.view.RutaNombreView;

@ManagedBean
@ViewScoped
public class ChatBean implements Serializable {

	private static final long serialVersionUID = -6825651697920766085L;

	private Consulta consulta;
	private String motivo;

	private ConsultaAsociada nuevaConsulta;

	public void cargar(Consulta c) {
		this.setConsulta( c );
		this.setNuevaConsulta( FactoryNuevaConsulta.generar( consulta.getId().intValue(), "", true, new Date() ) );
	}

	public void enviarConsulta() throws ExcepcionControladaError {
		if ( this.getNuevaConsulta().getNuevaConsulta().isEmpty() )
			Mensaje.addMessageAviso( "Debe Ingresar una consulta" );
		else {
			enviarCorreoCategoria();
			
			AsistentePlataformaConsultasBean.enviarNuevaConsulta( this.getNuevaConsulta() );
			
			this.getConsulta().getListaConsultasAsociadas().add( this.getNuevaConsulta() );
			
			this.setNuevaConsulta( FactoryNuevaConsulta.generar( consulta.getId().intValue(), "", true, new Date() ) );

			RequestContext.getCurrentInstance().update( "form" );
		}
	}

	private void enviarCorreoCategoria() {
		EnviarCorreoCategoria e = new EnviarCorreoCategoria(this.getConsulta(), this.getMotivo());
		Thread t = new Thread( e );
		t.run();		
	}

	public void subirArchivos(FileUploadEvent event) throws ExcepcionControlada, IOException, ExcepcionControladaError {
		ArchivoConsulta f = FactoryArchivoConsulta.generarInstancia(AdministradorArchivo.crearArchivo( event ), true);
		if(f.getArchivo().getData().length>15000000)
			Mensaje.addMessageAviso( "El archivo no puede superar los 15 Mega Bytes" );
		else{ 
			AsistentePlataformaConsultasBean.agregarArchivo( f, this.getConsulta().getId() );
			RutaNombreView r = new RutaNombreView( f.getArchivo().getNombre(), f.getArchivo().getFilePath() );
			this.consulta.getListaArchivos().add( r );
		}
		event.getComponent().setTransient( false );
	}

	public void eliminar(ArchivoConsulta archivo) throws ExcepcionControladaError{
				
		AsistentePlataformaConsultasBean.eliminarArchivo( archivo );
		this.getConsulta().getArchivos().remove( archivo );
		Mensaje.addMessageAviso( "Se eliminó correctamente el archivo");
	}
	
	public StreamedContent descargarArchivo(Archivo a){
		// TODO IMPLEMENTAR!
		return null;
	}
	
	public boolean isSinConsultas(){
		return this.getConsulta().getListaConsultasAsociadas().isEmpty();
	}
	
	public void reabrirConsulta() throws ExcepcionControladaError, ExcepcionControladaAlerta{
		
		if ( this.getMotivo().isEmpty() )
			throw new ExcepcionControladaAlerta("Debe ingresar un 'MOTIVO' para la Re-Apertura", null);
		
		if ( this.getMotivo().length() < 20 )
			throw new ExcepcionControladaAlerta("El 'MOTIVO' ingresado debe contener más de 20 (veinte) Caracteres", null);
		
		this.setNuevaConsulta( FactoryNuevaConsulta.generar(consulta.getId().intValue(), this.getMotivo(), true, new Date()) );
		
		this.enviarConsulta();
		
		AsistentePlataformaConsultasBean.reabrirConsulta(this.getConsulta());
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public ConsultaAsociada getNuevaConsulta() {
		return nuevaConsulta;
	}

	public void setNuevaConsulta(ConsultaAsociada nuevaConsulta) {
		this.nuevaConsulta = nuevaConsulta;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
}