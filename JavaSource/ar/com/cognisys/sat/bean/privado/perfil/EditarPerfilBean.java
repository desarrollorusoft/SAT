package ar.com.cognisys.sat.bean.privado.perfil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.generico.modelo.comun.AsistenteObjeto;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistentePerfil;
import ar.com.cognisys.sat.excepcion.Mensaje;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.validador.ValidadorContribuyente;

@ManagedBean(name = "EditarPerfil")
@ViewScoped
public class EditarPerfilBean extends SeccionBean {
	
	private static final long serialVersionUID = -2802679879346620864L;
	private String nombre, apellido;
	private String correo;
	private String telefono1;
	private String telefono2;
	private boolean aceptaNewsletter;
	private Usuario usuario;
	
	@Override
	protected void inicializacion() {
		this.refrescar();
	}
	
	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object arg0) throws ExcepcionControladaError {}

	@Override
	public void siguiente() throws ExcepcionControladaError {}

	@Override
	public void refrescar() {
		this.setUsuario( SesionBean.getUsuarioLogueado() );
		this.setCorreo( this.getUsuario().getCorreo() );
		this.setNombre( this.getUsuario().getNombre() );
		this.setApellido( this.getUsuario().getApellido() );
		this.setTelefono1( this.getUsuario().getTelefono1() );
		this.setTelefono2( this.getUsuario().getTelefono2() );
		this.setAceptaNewsletter( this.getUsuario().isNewsLetter() );
	}
	
	public void cambiarDatos() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		this.validar();
		AsistentePerfil.cambiarDatos(this.getNombre(), this.getApellido(), this.getUsuario(),
										this.getCorreo(), this.getTelefono1(), this.getTelefono2());
		Mensaje.addMessageAviso("Su perfil ha sido actualizado");
	}

	private void validar() throws ExcepcionControladaAlerta {
		if( !AsistenteObjeto.tieneContenido(this.getCorreo() ) || !ValidadorContribuyente.esCorreoValido(this.getCorreo()))
			throw new ExcepcionControladaAlerta("El correo no es valido");
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public boolean isAceptaNewsletter() {
		return aceptaNewsletter;
	}

	public void setAceptaNewsletter(boolean aceptaNewsletter) {
		this.aceptaNewsletter = aceptaNewsletter;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
	
}