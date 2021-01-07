package ar.com.cognisys.sat.bean.publico.ingreso;

import ar.com.cognisys.generico.modelo.comun.AsistenteObjeto;
import ar.com.cognisys.sat.bean.ComunBean;
import ar.com.cognisys.sat.bean.Redireccionamiento;
import ar.com.cognisys.sat.bean.asistente.AsistenteLogin;
import ar.com.cognisys.sat.core.modelo.comun.MD5;
import ar.com.cognisys.sat.core.modelo.comun.comunicacion.EncriptacionSAT;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ClaveTemporalException;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.modelo.excepcion.InconsistenciaCorreoExcepcion;
import ar.com.cognisys.sat.core.modelo.excepcion.UsuarioInactivoException;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;

public class AnalizadorSesion {

	private String cuit;
	private String clave;
	private Usuario usuario;
	private Redireccionamiento redireccionamiento;
	private LoginTracer tracer;
	
	public AnalizadorSesion(String cuit, String clave, LoginTracer tracer) {
		this.setCuit(cuit);
		this.setClave(clave);
		this.setTracer(tracer);
	}
	
	public AnalizadorSesion validar() throws ExcepcionControladaAlerta {
		this.getTracer().avanzar();
		if( !AsistenteObjeto.tieneContenido( this.getCuit() ) ||
				!AsistenteObjeto.tieneContenido( this.getClave() ) ||
				!CUIT.validar( ComunBean.sacarMascaraCuit(this.getCuit()) ) )
			throw new ExcepcionControladaAlerta("El CUIT y/o contraseña es incorrecto");
	
		return this;
	}
	
	public Usuario generarUsuario() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {
			this.setUsuario( AsistenteLogin.ingreso(this.getCuit().trim(), MD5.getMD5(this.getClave().trim()), this.getTracer()) );

			if (this.getUsuario().getTramiteSubsidio() != null && !(this.getUsuario().getTramiteSubsidio().isCompleto() || this.getUsuario().getTramiteSubsidio().isCerrado()))
				this.redirigirCargaSubsidio();
			else if (this.getUsuario().tieneComercio() && !this.getUsuario().getComercio().tieneSolicitante())
				this.redirigirCargaSolicitante();
			else
				this.redirigirHome();
		} catch (ClaveTemporalException e) {
			this.redirigirCambioClaveTemporal(this.getCuit());
		} catch (InconsistenciaCorreoExcepcion e) {
			this.redirigirInconsistenciaCorreo(this.getCuit());
		} catch (UsuarioInactivoException e) {
			this.redirigirActivacion(this.getCuit());
		}
		
		return this.getUsuario();
	}

	private void redirigirCargaSubsidio() {
		Redireccionamiento r = (new Redireccionamiento("/pages/pri/subsidio.xhtml"));
		this.setRedireccionamiento( r );
	}

	private void redirigirCargaSolicitante() {
		Redireccionamiento r = (new Redireccionamiento("/pages/pri/actualizacion-datos.xhtml"));
		this.setRedireccionamiento( r );
	}

	private void redirigirHome() {
		Redireccionamiento r = (new Redireccionamiento("/pages/pri/home.xhtml"));
		this.setRedireccionamiento( r );
	}
	
	private void redirigirCambioClaveTemporal(String cuit) {
		Redireccionamiento r = (new Redireccionamiento("/pages/pub/cambio_clave_temporal.xhtml"));
		r.agregarParametro("ct", (new EncriptacionSAT()).encriptar( CUIT.quitarMascara(cuit) ));
		this.setRedireccionamiento( r );
	}

	private void redirigirInconsistenciaCorreo(String cuit) {
		Redireccionamiento r = (new Redireccionamiento("/pages/pub/inc_correo.xhtml"));
		r.agregarParametro("ct", (new EncriptacionSAT()).encriptar( CUIT.quitarMascara(cuit) ));
		this.setRedireccionamiento( r );
	}
	
	private void redirigirActivacion(String cuit2) {
		Redireccionamiento r = (new Redireccionamiento("/pages/pub/activacion.xhtml"));
		r.agregarParametro("ct", (new EncriptacionSAT()).encriptar( CUIT.quitarMascara(cuit) ));
		this.setRedireccionamiento( r );
	}

	public boolean mostrarTyC() {
		return !this.getUsuario().isAceptoTyC();
	}

	public String getCuit() {
		return cuit;
	}
	
	protected void setCuit(String cuit) {
		this.cuit = cuit;
	}
	
	public String getClave() {
		return clave;
	}
	
	protected void setClave(String clave) {
		this.clave = clave;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	protected void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Redireccionamiento getRedireccionamiento() {
		return redireccionamiento;
	}

	protected void setRedireccionamiento(Redireccionamiento redireccionamiento) {
		this.redireccionamiento = redireccionamiento;
	}

	public LoginTracer getTracer() {
		return tracer;
	}

	protected void setTracer(LoginTracer tracer) {
		this.tracer = tracer;
	}
}