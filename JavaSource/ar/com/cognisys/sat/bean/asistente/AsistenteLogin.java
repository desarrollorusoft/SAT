package ar.com.cognisys.sat.bean.asistente;

import java.util.List;

import ar.com.cognisys.sat.bean.publico.ingreso.LoginTracer;
import ar.com.cognisys.sat.core.administrador.*;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.modelo.factory.cuenta.FactoryCuentasUsuario;
import ar.com.cognisys.sat.core.modelo.factory.seguimiento.FactoryNavegacionSAT;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;
import ar.com.cognisys.sat.v2.core.controlador.administrador.AdministradorCuentas;
import ar.com.cognisys.sat.v2.core.modelo.exception.PersistenceSATException;

public class AsistenteLogin {

	public static Usuario ingreso(String cuit, String clave, LoginTracer tracer) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		tracer.avanzar();
		Usuario usuario = AdministradorUsuarioCF.buscar( CUIT.quitarMascara(cuit), clave );
		
		tracer.avanzar();
		usuario.setComercio( AdministradorComercio.recuperarCompleto( usuario.getCuit() ) );
		
		List<Cuenta> listaCuentas;
		try {
			tracer.avanzar();
			listaCuentas = new AdministradorCuentas().recuperarCuentasPorUsuario( usuario.getIdUsuario() );
			
			tracer.avanzar();
			AdministradorCuenta.cargarImportes( listaCuentas );
			usuario.setCuentas( FactoryCuentasUsuario.generar(listaCuentas) );

			tracer.avanzar();
			usuario.setPlanes( AdministradorPlanDePago.obtenerPlanesDePagoUsuario( usuario.getCuit() ) );

			tracer.avanzar();
			usuario.setTramiteSubsidio( AdministradorTramiteSubsidio.recuperarTramite( usuario.getCuit() ) );

			AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("ingreso"));
			
			return usuario;
		} catch (PersistenceSATException e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static void aceptoTyC(Usuario usuario) throws ExcepcionControladaError {
		AdministradorContribuyente.actualizarTyC(usuario);
	}
}