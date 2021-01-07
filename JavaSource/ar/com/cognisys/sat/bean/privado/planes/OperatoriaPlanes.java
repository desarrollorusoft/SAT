package ar.com.cognisys.sat.bean.privado.planes;

import ar.com.cognisys.common.jsfbean.abstracto.AbstractOperatoria;

public enum OperatoriaPlanes implements AbstractOperatoria {
	
	LISTA, DETALLE;

	@Override
	public String getClave() {
		return this.name();
	}

	@Override
	public boolean sos(String arg0) {
		return this.name().equals( arg0 );
	}
}