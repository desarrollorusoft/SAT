package ar.com.cognisys.sat.bean.privado.rs;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaBean;
import ar.com.cognisys.sat.bean.Redireccionamiento;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteConfirmarRSBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteRegimenSimplificado;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.RSDeudaPadron;
import ar.com.cognisys.sat.core.modelo.comun.rs.Comercio;
import ar.com.cognisys.sat.core.modelo.comun.rs.DDJJRS;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.excepcion.SATRuntimeException;
import ar.com.cognisys.sat.modelo.administrador.Navegacion;
import ar.com.cognisys.sat.modelo.comun.Mensaje;

@ManagedBean(name = "RSConfirmar")
@ViewScoped
public class RSConfirmarBean extends PaginaBean {
	
	private static final long serialVersionUID = 2281076223793362111L;
	private DDJJRS declaracion;
	private List<RSDeudaPadron> listaDeudas;

	@Override
	protected void inicializacion() {
		try {
			this.refrescar();
		} catch (ExcepcionControladaError e) {
			Mensaje.emitirMensajeError(e.getMessage());
			e.printStackTrace();
			try {
				(new Redireccionamiento("/pages/pri/rs.xhtml")).redireccionar();
			} catch (ExcepcionControladaError e1) {}
		}
	}

	@Override
	public void refrescar() throws ExcepcionControladaError {
		this.recuperarDeclaracion();
		listaDeudas = AsistenteRegimenSimplificado.recuperarDeuda(this.getDeclaracion(), SesionBean.getUsuarioLogueado());
	}
	
	public String rectificar() throws ExcepcionControladaError {
		return Navegacion.rs.name();
	}
	
	private void recuperarDeclaracion() {
		Map<String, String> mapa;
		try {
			mapa = (new Redireccionamiento()).recuperarDatos();

			if (!mapa.isEmpty()) {
				Comercio c = SesionBean.getUsuarioLogueado().getComercio();
				this.setDeclaracion( c.obtenerDeclaracion( Integer.parseInt(mapa.get("d")) ) );			
			}
		} catch (ExcepcionControladaError e) {
			throw new SATRuntimeException(e);
		}
	}
	
	public String confirmar() throws ExcepcionControladaError, ExcepcionControladaAlerta {
		AsistenteConfirmarRSBean.confirmarRegimen( SesionBean.getUsuarioLogueado(), declaracion );
		return Navegacion.rs.name();
	}

	public DDJJRS getDeclaracion() {
		return declaracion;
	}

	public void setDeclaracion(DDJJRS declaracion) {
		this.declaracion = declaracion;
	}

	public List<RSDeudaPadron> getListaDeudas() {
		return listaDeudas;
	}

	public void setListaDeudas(List<RSDeudaPadron> listaDeudas) {
		this.listaDeudas = listaDeudas;
	}
}