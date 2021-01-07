package ar.com.cognisys.sat.bean.asistente;

import java.util.List;

import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.core.administrador.AdministradorFormularioConsultas;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.modelo.factory.consultas.FactoryConsulta;

public class AsistenteConsultasBean {

	public static List<Consulta> recuperarPorCuit() throws ExcepcionControladaError {
		
		return AdministradorFormularioConsultas.buscarConsultasPorCuit( SesionBean.getUsuarioLogueado().getCuit() );
		
	}

	public static Consulta generarNuevaConsulta() {
		Usuario u = SesionBean.getUsuarioLogueado();
		return FactoryConsulta.generarInstancia(u.getNombre(), u.getApellido(), u.getCuit(),u.getCorreo(),u.getTelefono1(),u.getTelefono2() );
	}

}
