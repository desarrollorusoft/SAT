package ar.com.cognisys.sat.modelo.navegacion;

import ar.com.cognisys.common.jsfbean.abstracto.AbstractNavegacion;

public enum Navegacion implements AbstractNavegacion {

	home, lista_cuentas, detalle_cuenta,
	;
	
	public static Navegacion obtenerNavegacion(String clave) {
        
        for (Navegacion n : Navegacion.values())
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