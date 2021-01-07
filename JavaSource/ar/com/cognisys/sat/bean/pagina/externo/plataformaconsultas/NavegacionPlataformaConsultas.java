package ar.com.cognisys.sat.bean.pagina.externo.plataformaconsultas;

import ar.com.cognisys.common.jsfbean.abstracto.AbstractNavegacion;
import ar.com.cognisys.common.jsfbean.abstracto.AbstractOperatoria;

public enum NavegacionPlataformaConsultas implements AbstractNavegacion,AbstractOperatoria{
	LISTA,SOPORTE,FORMULARIO,BLOQUEO;

	@Override
	public String getClave() {
		return this.name();
	}

	@Override
	public boolean sos(String nombre) {
		return this.name().equals( nombre );
	}
}
