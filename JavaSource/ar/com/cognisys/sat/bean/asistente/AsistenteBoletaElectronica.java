package ar.com.cognisys.sat.bean.asistente;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.com.cognisys.sat.core.administrador.AdministradorBoletaElectronica;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.comun.permiso.PermisoUsuario;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;

public class AsistenteBoletaElectronica {

	public static boolean estaHabilitado(Usuario usuario) {
		return usuario.sosNivel(3);
	}

	public static void cargarListas(Usuario usuario, List<Cuenta> cuentasDisponibles, List<Cuenta> cuentasAsignadas) {
		
		for (Cuenta c : usuario.getCuentas().getListaCuentas())
			for (PermisoUsuario p : usuario.getListaPermisos())
				if (c.sos( p.getTipoCuenta() ) && c.sos( p.getDatoCuenta() ))
					cuentasDisponibles.add( c );
		
		for (Cuenta c : usuario.getCuentas().obtenerCuentasBE())
			if (cuentasDisponibles.contains( c ))
				cuentasAsignadas.add( c );
		
		cuentasDisponibles.removeAll( cuentasAsignadas );
	}

	public static TiposCuentas[] obtenerTiposCuenta(List<Cuenta> cuentasDisponibles) {
		
		Set<TiposCuentas> conjunto = new HashSet<TiposCuentas>();
		
		for (Cuenta cuenta : cuentasDisponibles)
			conjunto.add(cuenta.getTipoCuenta());
		
		return conjunto.toArray( new TiposCuentas[] {} );
	}

	public static List<Cuenta> obtenerCuentas(TiposCuentas tipo, List<Cuenta> listaCuentas) {
		
		List<Cuenta> lista = new ArrayList<Cuenta>();
		
		for (Cuenta c : listaCuentas)
			if (c.sos( tipo ))
				lista.add(c);
		
		return lista;
	}

	public static void asociar(Cuenta cuenta, Usuario usuario) throws ExcepcionControladaError {
		AdministradorBoletaElectronica.activarBE( cuenta, usuario.getIdUsuario() );
	}
	
	public static void desasociar(Cuenta cuenta, Usuario usuario) throws ExcepcionControladaError {
		AdministradorBoletaElectronica.desactivarBE( cuenta, usuario.getIdUsuario() );
	}
}