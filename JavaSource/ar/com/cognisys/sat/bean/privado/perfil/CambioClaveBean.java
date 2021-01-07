package ar.com.cognisys.sat.bean.privado.perfil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.generico.modelo.comun.AsistenteObjeto;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistentePerfil;
import ar.com.cognisys.sat.bean.publico.cambioClaveTemporal.ICambioClaveExterno;
import ar.com.cognisys.sat.core.administrador.AdministradorUsuario;
import ar.com.cognisys.sat.core.modelo.comun.MD5;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.excepcion.Mensaje;

@ManagedBean(name = "CambioClave")
@ViewScoped
public class CambioClaveBean extends SeccionBean {
	
	private static final long serialVersionUID = 773665476918818289L;
	private String claveActual;
	private String claveNueva;
	private String claveNuevaRepetida;
	private Usuario usuario;
	private ICambioClaveExterno externo;
	
	@Override
	protected void inicializacion() {
		this.refrescar();
	}
	
	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object dato) throws ExcepcionControladaError {}
	
	public void cargar(Usuario usuario, ICambioClaveExterno externo) throws ExcepcionControladaError {
		this.setUsuario(usuario);
		this.setExterno(externo);
	}
	
	@Override
	public void refrescar() {
		this.setClaveActual(null);
		this.setClaveNueva(null);
		this.setClaveNuevaRepetida(null);
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {}
	
	public void cambiarClave() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		if (noHayUsuario())
			this.setUsuario( SesionBean.getUsuarioLogueado() );
		
		this.validar();
		
		AsistentePerfil.cambiarClave(this.getUsuario(), this.getClaveActual(), this.getClaveNueva());
		this.refrescar();
		
		if (hayExterno())
			this.getExterno().claveCambiada();
		else			
			Mensaje.addMessageAviso("Su contraseña ha sigo modificada");
	}
	
	private void validar() throws ExcepcionControladaAlerta {
		if( !MD5.getMD5( this.getClaveActual() ).toString().equals( this.getUsuario().getClave().trim() ) ||
				!AsistenteObjeto.tieneContenido( this.getClaveNueva() ) ||
				!AsistenteObjeto.tieneContenido( this.getClaveNuevaRepetida() ) ||
				!this.getClaveNueva().equals(this.getClaveNuevaRepetida() ) )
			throw new ExcepcionControladaAlerta("Los datos ingresados no son correctos");
		
		if (this.getClaveNueva().equals( AdministradorUsuario.CLAVE_TEMPORAL ))
			throw new ExcepcionControladaAlerta("La nueva contraseña ingresada, no puede ser usada");
	}
	
	private boolean noHayUsuario() {
		return this.getUsuario() == null;
	}
	
	private boolean hayExterno() {
		return this.getExterno() != null;
	}

	public String getClaveActual() {
		return claveActual;
	}

	public void setClaveActual(String claveActual) {
		this.claveActual = claveActual;
	}

	public String getClaveNueva() {
		return claveNueva;
	}

	public void setClaveNueva(String claveNueva) {
		this.claveNueva = claveNueva;
	}

	public String getClaveNuevaRepetida() {
		return claveNuevaRepetida;
	}

	public void setClaveNuevaRepetida(String claveNuevaRepetida) {
		this.claveNuevaRepetida = claveNuevaRepetida;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public ICambioClaveExterno getExterno() {
		return externo;
	}

	public void setExterno(ICambioClaveExterno externo) {
		this.externo = externo;
	}
}