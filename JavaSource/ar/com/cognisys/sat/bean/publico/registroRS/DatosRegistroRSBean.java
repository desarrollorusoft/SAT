package ar.com.cognisys.sat.bean.publico.registroRS;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionWizardBean;
import ar.com.cognisys.generico.modelo.comun.AsistenteObjeto;
import ar.com.cognisys.sat.bean.ComunBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteRegistracion;
import ar.com.cognisys.sat.modelo.comun.Mensaje;
import ar.com.cognisys.sat.core.administrador.AdministradorUsuario;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;
import ar.com.cognisys.sat.core.modelo.validador.ValidadorContribuyente;
import ar.com.cognisys.sat.v2.core.controlador.facade.FacadeCaracteresRS;

@ManagedBean(name = "DatosRegistroRS")
@ViewScoped
public class DatosRegistroRSBean extends SeccionWizardBean {
	
	private static final long serialVersionUID = 8339093289662486518L;
	private String cuit;
	private String correoAct;
	private String telefonoAct;
	private String celularAct;
	private String clave;
	private String claveRepetida;
	private String nombre;
	private String apellido;
	private String correoSol;
	private String telefonoSol;
	private String celularSol;
	private String cuitSol;
	private String[] listaCaracteres;
	private String caracterSeleccionado;
	
	@Override
	protected void inicializacion() {
		this.refrescar();
	}
	
	@Override
	public void cargar() throws ExcepcionControladaError {
		this.refrescar();
	}

	@Override
	public void cargar(Object arg0) throws ExcepcionControladaError {}
	
	@Override
	public void refrescar() {
		this.setListaCaracteres( new FacadeCaracteresRS().recuperarTodos() );
	}
	
	@Override
	public void siguiente() throws ExcepcionControladaError {
		try {
			this.validarCompleto();
			this.cuit = ComunBean.sacarMascaraCuit( this.cuit );
			Usuario usuario = AsistenteRegistracion.generarUsuarioRS(this.getCuit(), 
																	 this.getCorreoAct(), 
																	 this.getTelefonoAct(), 
																	 this.getCelularAct(), 
																	 this.getClave(), 
																	 this.getClaveRepetida(), 
																	 this.getCorreoSol(), 
																	 this.getTelefonoSol(), 
																	 this.getCelularSol(), 
																	 this.getNombre(), 
																	 this.getApellido(), 
																	 this.getCaracterSeleccionado(),
																	 this.getCuitSol());
			this.irAlSiguiente(usuario);
		} catch (ExcepcionControladaAlerta e) {
			Mensaje.emitirMensajeAlerta(e.getMessage());
		}
	}

	public void validarCuit() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		
		try {
			if (AdministradorUsuario.existeUsuario( CUIT.quitarMascara(cuit) ))
				throw new ExcepcionControladaAlerta("La CUIL/CUIT ingresada ya se encuentra registrada");
			else if (AdministradorUsuario.existeUsuarioPorCorreo(correoAct) )
				throw new ExcepcionControladaAlerta("El correo ingresado ya se encuentra registrado");
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e);
		}
		
		Mensaje.emitirMensajeAviso("El CUIT es correcto");
	}
	
	private void validarCompleto() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		
		if( !AsistenteObjeto.tieneContenido( this.getCuit() ) || !CUIT.validar( ComunBean.sacarMascaraCuit( this.getCuit() ) ) )
			throw new ExcepcionControladaAlerta( "El CUIT no es correcto" );
		
		if( !AsistenteObjeto.tieneContenido( this.correoAct ) || !ValidadorContribuyente.esCorreoValido( this.correoAct) )
			throw new ExcepcionControladaAlerta( "El correo de la actividad no es valido" );
		
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
		
		if( !AsistenteObjeto.tieneContenido(this.clave) || 
				!AsistenteObjeto.tieneContenido(this.claveRepetida) || 
				!this.clave.equals(this.claveRepetida) )
			throw new ExcepcionControladaAlerta("Las contraseñas deben coincidir");
		
		this.validarCuit();
	}
	
	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getCorreoAct() {
		return correoAct;
	}

	public void setCorreoAct(String correoAct) {
		if (correoAct != null)
			this.correoAct = correoAct.toLowerCase();
		else
			this.correoAct = correoAct;
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

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getClaveRepetida() {
		return claveRepetida;
	}

	public void setClaveRepetida(String claveRepetida) {
		this.claveRepetida = claveRepetida;
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