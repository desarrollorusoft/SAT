package ar.com.cognisys.sat.bean.publico.ingreso;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import ar.com.cognisys.sat.bean.Redireccionamiento;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteLogin;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.modelo.abstracto.Bean;
import ar.com.cognisys.sat.modelo.administrador.Navegacion;

@ManagedBean
@ViewScoped
public class LoginBean extends Bean {

	private static final long serialVersionUID = 1057088577844082382L;
	private String clave, cuit;
	private Usuario usuario;
	private Redireccionamiento redireccionamiento;
	
	@ManagedProperty(value="#{sesionBean}")
	private SesionBean sesionBean;
	
	@Override
	protected void inicializacion() {}
	
	public void logueo() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		
		AnalizadorSesion as = new AnalizadorSesion(this.getCuit(), this.getClave(), new LoginTracer());
		this.setUsuario( as.validar().generarUsuario() );
		this.setRedireccionamiento( as.getRedireccionamiento() );
		
		as.getTracer().avanzar();
		if (this.getRedireccionamiento().esRedireccionamientoPrivado()) {
			if (as.mostrarTyC())
				this.mostrarTyC();
			else {
				this.registrar();
				this.redirigir();
			}
		} else
			this.redirigir();
	}
	
	private void mostrarTyC() {
		RequestContext.getCurrentInstance().execute( "PF('ModalTyC').show()" );
	}
	
	public void aceptaTyC() throws ExcepcionControladaError {
		this.getUsuario().setAceptoTyC( true );
		AsistenteLogin.aceptoTyC( this.getUsuario() );
		this.registrar();
		this.redirigir();
	}

	private void registrar() {
		this.getSesionBean().setUsuario( this.getUsuario() );
	}
	
	private void redirigir() throws ExcepcionControladaError {
		try {
			this.getRedireccionamiento().redireccionar();
		} catch (ar.com.cognisys.common.exception.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public String primeraVez() {
		return Navegacion.registro.toString();
	}

	public String olvidoClave() {
		return Navegacion.olvido_clave.toString();
	}
	
	public String problemasIngreso() {
		return Navegacion.problemas_ingreso.toString();
	}
	
	public String noActivoCuenta() {
		return Navegacion.no_activo_cuenta.toString();
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public SesionBean getSesionBean() {
		return sesionBean;
	}

	public void setSesionBean(SesionBean sesionBean) {
		this.sesionBean = sesionBean;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Redireccionamiento getRedireccionamiento() {
		return redireccionamiento;
	}

	public void setRedireccionamiento(Redireccionamiento redireccionamiento) {
		this.redireccionamiento = redireccionamiento;
	}
}