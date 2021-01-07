package ar.com.cognisys.sat.bean.privado.perfil;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.core.administrador.AdministradorBajaUsuario;
import ar.com.cognisys.sat.core.administrador.AdministradorContribuyente;
import ar.com.cognisys.sat.core.administrador.AdministradorUsuario;
import ar.com.cognisys.sat.core.contenedor.ContenedorMotivosBaja;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.MotivoBajaUsuario;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;

import javax.ejb.SessionBean;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "ModalBajaUsuario")
@ViewScoped
public class ModalBajaUsuarioBean extends SeccionBean {

	private static final long serialVersionUID = -2802679879346620864L;
	private List<MotivoBajaUsuario> motivos;
	private MotivoBajaUsuario otroMotivo;
	private String nuevoMotivo;

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
		this.setMotivos( this.clonar(ContenedorMotivosBaja.getInstancia().getMotivos()) );
		this.setOtroMotivo( new MotivoBajaUsuario(2, "Otro") );
		this.setNuevoMotivo( null );
	}

	public void confirmar() throws ExcepcionControladaError {
		try {
			Usuario u = SesionBean.getUsuarioLogueado();
			AdministradorBajaUsuario.registrarMotivos( u, this.getListaMotivosFinal() );
			AdministradorContribuyente.borrarUsuario( u.getIdUsuario() );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e, "Fallo la baja del usuario");
		}
	}

	private List<MotivoBajaUsuario> clonar(List<MotivoBajaUsuario> motivos) {
		List<MotivoBajaUsuario> lista = new ArrayList<>();

		for (MotivoBajaUsuario motivo: motivos)
			lista.add( new MotivoBajaUsuario(motivo.getCodigo(), motivo.getDescripcion()) );

		return lista;
	}

	private List<MotivoBajaUsuario> getListaMotivosFinal() {
		List<MotivoBajaUsuario> lista = new ArrayList<>();

		for (MotivoBajaUsuario motivo: this.getMotivos())
			if ( motivo.isSeleccionado() )
				lista.add( motivo );

		if (this.getOtroMotivo().isSeleccionado()) {
			this.getOtroMotivo().setAclaracion( this.getNuevoMotivo() );
			lista.add( this.getOtroMotivo() );
		}

		return lista;
	}

	public List<MotivoBajaUsuario> getMotivos() {
		return motivos;
	}

	public void setMotivos(List<MotivoBajaUsuario> motivos) {
		this.motivos = motivos;
	}

	public MotivoBajaUsuario getOtroMotivo() {
		return otroMotivo;
	}

	public void setOtroMotivo(MotivoBajaUsuario otroMotivo) {
		this.otroMotivo = otroMotivo;
	}

	public String getNuevoMotivo() {
		return nuevoMotivo;
	}

	public void setNuevoMotivo(String nuevoMotivo) {
		this.nuevoMotivo = nuevoMotivo;
	}
}