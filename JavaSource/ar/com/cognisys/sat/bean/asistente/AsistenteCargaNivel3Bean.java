package ar.com.cognisys.sat.bean.asistente;

import java.util.List;

import ar.com.cognisys.sat.core.administrador.AdministradorBoletaElectronica;
import ar.com.cognisys.sat.core.administrador.AdministradorContribuyente;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.administrador.AdministradorPermisoUsuario;
import ar.com.cognisys.sat.core.administrador.AdministradorUsuario;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.MD5;
import ar.com.cognisys.sat.core.modelo.comun.natatorios.CuentaPileta;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.modelo.factory.cuenta.FactoryCuentasUsuario;
import ar.com.cognisys.sat.v2.core.controlador.administrador.AdministradorCuentas;
import ar.com.cognisys.sat.v2.core.modelo.exception.PersistenceSATException;

public class AsistenteCargaNivel3Bean {

	public static void registrar(String nombre, String apellido, String cuit, String tipoDocumento, String nroDocumento, String correo, 
								 String telefono, String celular, List<Cuenta> listaCuentas) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		
		correo = correo.toLowerCase().trim();
		
		AdministradorUsuario.registrarCargaNivel3(nombre, apellido, cuit, tipoDocumento, nroDocumento, correo, telefono, celular);
		
		Usuario actual = AdministradorUsuario.buscar(cuit);
		Integer idUsuario = null;
		
		if (actual == null)
			idUsuario = AdministradorUsuario.registrarDatosContribuyente(cuit, 
																		 correo, 
																		 MD5.getMD5(AdministradorUsuario.CLAVE_TEMPORAL), 
																		 FactoryCuentasUsuario.generar(listaCuentas));
		else {
			idUsuario = actual.getIdUsuario();
			
			List<Cuenta> cuentas;
			try {
				cuentas = new AdministradorCuentas().recuperarCuentasPorUsuario( idUsuario );
				actual.setCuentas( FactoryCuentasUsuario.generar(cuentas) );
			} catch (PersistenceSATException e) {}
			
			for (Cuenta cuenta : listaCuentas) {
				if (!actual.tieneCuenta(cuenta)) {
					if (!cuenta.esPileta())
						AdministradorCuenta.asociarCuenta(idUsuario, cuenta.getTipoCuenta(), cuenta.getDatoCuenta(), "");
					else {
						CuentaPileta cuentaPileta = (CuentaPileta) cuenta;
						AdministradorCuenta.asociarCuentaPileta(idUsuario, cuentaPileta.getTipoDocumento().getNombrePiletas(), cuentaPileta.getNumeroDocumento().toString(), "");
					}
				}
			}
		}
		
		AdministradorContribuyente.actualizarDatosUsuario(idUsuario, correo, telefono, celular, nombre, apellido, 3);
		
		AdministradorPermisoUsuario.registrar(idUsuario, listaCuentas);
		
		AdministradorBoletaElectronica.cargarCuentas(idUsuario, listaCuentas);
		
		if (actual == null)
			AdministradorUsuario.notificarClaveNivel3(correo);
		else
			AdministradorUsuario.notificarBE(correo, listaCuentas);
	}
}