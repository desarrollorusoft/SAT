package ar.com.cognisys.sat.bean.asistente;

import java.util.List;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.administrador.AdministradorRegimenSimplificado;
import ar.com.cognisys.sat.core.administrador.AdministradorUsuario;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.rs.DDJJRS;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.factory.cuenta.FactoryCuentasUsuario;
import ar.com.cognisys.sat.v2.core.controlador.administrador.AdministradorCuentas;
import ar.com.cognisys.sat.v2.core.modelo.exception.PersistenceSATException;

public class AsistenteConfirmarRSBean {

	public static void confirmarRegimen(Usuario usuario, DDJJRS declaracion) throws ExcepcionControladaError, ExcepcionControladaAlerta {
				
		try {
			if ( !AdministradorRegimenSimplificado.confirmarDDJJRS(usuario.getCuit(), declaracion) )
				throw new ExcepcionControladaAlerta("Sus ingresos exceden lo permitido para el Régimen Simplificado");
			
			AdministradorUsuario.validarRSAsociado(usuario);
			List<Cuenta> listaCuentas = new AdministradorCuentas().recuperarCuentasPorUsuario( usuario.getIdUsuario() );
			AdministradorCuenta.cargarImportes( listaCuentas );
			usuario.setCuentas( FactoryCuentasUsuario.generar(listaCuentas) );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		} catch (PersistenceSATException e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
}