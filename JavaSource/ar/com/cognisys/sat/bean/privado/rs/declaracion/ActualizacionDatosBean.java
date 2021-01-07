package ar.com.cognisys.sat.bean.privado.rs.declaracion;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaBean;
import ar.com.cognisys.generico.modelo.comun.AsistenteObjeto;
import ar.com.cognisys.sat.bean.ComunBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteRegimenSimplificado;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;
import ar.com.cognisys.sat.core.modelo.validador.ValidadorContribuyente;
import ar.com.cognisys.sat.modelo.administrador.Navegacion;
import ar.com.cognisys.sat.modelo.comun.Mensaje;
import ar.com.cognisys.sat.v2.core.controlador.facade.FacadeCaracteresRS;

@ManagedBean(name = "ActualizacionDatos")
@ViewScoped
public class ActualizacionDatosBean extends PaginaBean {
	
	private static final long serialVersionUID = 1361878875021084249L;
	private String telefonoAct;
	private String celularAct;
	private String nombre;
	private String apellido;
	private String correoSol;
	private String telefonoSol;
	private String celularSol;
	private String[] listaCaracteres;
	private String caracterSeleccionado;
	private String cuitSol;
	
	@Override
	protected void inicializacion() {
		try {
			this.refrescar();
		} catch (ExcepcionControladaError e) {
			Mensaje.emitirMensajeError(e.getMessage());
		}
	}
	
	@Override
	public void refrescar() throws ExcepcionControladaError {
		this.setListaCaracteres( new FacadeCaracteresRS().recuperarTodos() );
	}
	
	public String actualizar() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		this.validar();
		AsistenteRegimenSimplificado.actualizar(this.getTelefonoAct(), 
												this.getCelularAct(), 
												this.getNombre(), 
												this.getApellido(),
												this.getCaracterSeleccionado(), 
												this.getCorreoSol(),
												this.getTelefonoSol(),
												this.getCelularSol(),
												this.getCuitSol());
		return Navegacion.rs.name();
	}

	private void validar() throws ExcepcionControladaAlerta {
		
		if( !AsistenteObjeto.tieneContenido( this.telefonoAct ) )
			throw new ExcepcionControladaAlerta( "El telefono de la actividad no es correcto" );
		
		if( !AsistenteObjeto.tieneContenido( this.celularAct ) )
			throw new ExcepcionControladaAlerta( "El celular de la actividad no es correcto" );
		
		if( !AsistenteObjeto.tieneContenido( this.correoSol ) || !ValidadorContribuyente.esCorreoValido( this.correoSol) )
			throw new ExcepcionControladaAlerta( "El correo del solicitante no es valido" );
		
		if( !AsistenteObjeto.tieneContenido( this.telefonoSol ) )
			throw new ExcepcionControladaAlerta( "El telefono del solicitante no es correcto" );
		
		if( !AsistenteObjeto.tieneContenido( this.celularSol ) )
			throw new ExcepcionControladaAlerta( "El celular del solicitante no es correcto" );

		if( !AsistenteObjeto.tieneContenido( this.caracterSeleccionado ) )
			throw new ExcepcionControladaAlerta( "Debe seleccionar un caráter" );
		
		if( !AsistenteObjeto.tieneContenido( this.getCuitSol() ) || !CUIT.validar( ComunBean.sacarMascaraCuit( this.getCuitSol() ) ) )
			throw new ExcepcionControladaAlerta( "El CUIT del Solicitante no es correcto" );
	}

	public String getTelefonoAct() {
		return telefonoAct;
	}

	public void setTelefonoAct(String telefonoAct) {
		this.telefonoAct = telefonoAct;
	}

	public String getCelularAct() {
		return celularAct;
	}

	public void setCelularAct(String celularAct) {
		this.celularAct = celularAct;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCorreoSol() {
		return correoSol;
	}

	public void setCorreoSol(String correoSol) {
		if (correoSol != null)
			this.correoSol = correoSol.toLowerCase();
		else
			this.correoSol = correoSol;
	}

	public String getTelefonoSol() {
		return telefonoSol;
	}

	public void setTelefonoSol(String telefonoSol) {
		this.telefonoSol = telefonoSol;
	}

	public String getCelularSol() {
		return celularSol;
	}

	public void setCelularSol(String celularSol) {
		this.celularSol = celularSol;
	}

	public String[] getListaCaracteres() {
		return listaCaracteres;
	}

	public void setListaCaracteres(String[] listaCaracteres) {
		this.listaCaracteres = listaCaracteres;
	}

	public String getCaracterSeleccionado() {
		return caracterSeleccionado;
	}

	public void setCaracterSeleccionado(String caracterSeleccionado) {
		this.caracterSeleccionado = caracterSeleccionado;
	}

	public String getCuitSol() {
		return cuitSol;
	}

	public void setCuitSol(String cuitSol) {
		this.cuitSol = cuitSol;
	}
}