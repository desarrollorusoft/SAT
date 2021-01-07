package ar.com.cognisys.sat.bean.publico.registroSat;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionWizardBean;
import ar.com.cognisys.generico.modelo.comun.AsistenteObjeto;
import ar.com.cognisys.sat.bean.ComunBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteRegistracion;
import ar.com.cognisys.sat.core.administrador.AdministradorComercio;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.administrador.AdministradorUsuario;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.rs.Comercio;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.enums.TiposDocumento;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;
import ar.com.cognisys.sat.core.modelo.validador.ValidadorContribuyente;
import ar.com.cognisys.sat.modelo.comun.Mensaje;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "DatosRegistro")
@ViewScoped
public class DatosRegistroBean extends SeccionWizardBean {
	
	private static final long serialVersionUID = 8339093289662486518L;
	private String cuit;
	private String correo;
	private String correoRepetido;
	private String clave;
	private String claveRepetida;

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
	public void refrescar() {}
	
	@Override
	public void siguiente() throws ExcepcionControladaError {
		try {
			this.validarCompleto();
			Usuario usuario = AsistenteRegistracion.generarUsuario(ComunBean.sacarMascaraCuit( this.getCuit() ), 
																   this.getCorreo(), 
																   this.getClave(), 
																   this.getClaveRepetida());
			this.irAlSiguiente(usuario);
		} catch (ExcepcionControladaAlerta e) {
			Mensaje.emitirMensajeAlerta(e.getMessage());
		}
	}
	
	private void validarCompleto() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		
		if( !AsistenteObjeto.tieneContenido( this.getCuit() ) || !CUIT.validar( ComunBean.sacarMascaraCuit( this.getCuit() ) ) )
			throw new ExcepcionControladaAlerta( "El CUIT no es correcto" );
		if( !AsistenteObjeto.tieneContenido( this.getCorreo() ) || !ValidadorContribuyente.esCorreoValido( this.getCorreo() ) )
			throw new ExcepcionControladaAlerta( "El correo no es valido" );
		if( !AsistenteObjeto.tieneContenido( this.getCorreoRepetido() ) || !ValidadorContribuyente.esCorreoValido( this.getCorreoRepetido() ) )
			throw new ExcepcionControladaAlerta( "La confirmacion del correo no es valido" );
		if( !this.getCorreo().equals( this.getCorreoRepetido() ))
			throw new ExcepcionControladaAlerta( "Los correos ingresados no coinciden" );
		if( !AsistenteObjeto.tieneContenido(this.clave) || 
				!AsistenteObjeto.tieneContenido(this.claveRepetida) || 
				!this.clave.equals(this.claveRepetida) )
			throw new ExcepcionControladaAlerta("Las contraseñas deben coincidir");

		this.validarCuit();
	}

	public void validarCuit() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		try {
			if (AdministradorUsuario.existeUsuario( CUIT.quitarMascara(cuit) ))
				throw new ExcepcionControladaAlerta("La CUIL/CUIT ingresada ya se encuentra registrada");
			else if (AdministradorUsuario.existeUsuarioPorCorreo(correo) )
				throw new ExcepcionControladaAlerta("El correo ingresado ya se encuentra registrado");
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e);
		}
		
		Mensaje.emitirMensajeAviso("El CUIT es correcto");
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		if (correo != null)
			this.correo = correo.toLowerCase();
		else
			this.correo = correo;
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

	public String getCorreoRepetido() {
		return correoRepetido;
	}

	public void setCorreoRepetido(String correoRepetido) {
		this.correoRepetido = correoRepetido;
	}
}