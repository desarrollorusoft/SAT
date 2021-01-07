package ar.com.cognisys.sat.bean.privado.rs;

import ar.com.cognisys.common.jsfbean.abstracto.AbstractNavegacion;
import ar.com.cognisys.common.jsfbean.abstracto.AbstractOperatoria;

public enum NavegacionRS implements AbstractNavegacion, AbstractOperatoria {

	actualizacion_datos, padrones, datos_actividad, pyp_oep, servicios_varios, resumen, confirmar_rs;
	
	public static NavegacionRS obtenerNavegacion(String clave) {	
		for (NavegacionRS n : NavegacionRS.values())
			if (n.name().equals(clave))
				return n;
		
		return null;
	}

	@Override
	public String getClave() {
		return this.name();
	}

	@Override
	public boolean sos(String arg0) {
	return this.getClave().equals(arg0);
	}
}