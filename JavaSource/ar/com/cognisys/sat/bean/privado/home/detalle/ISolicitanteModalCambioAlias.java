package ar.com.cognisys.sat.bean.privado.home.detalle;

import ar.com.cognisys.common.exception.ExcepcionControladaError;

public interface ISolicitanteModalCambioAlias {

    void actualizarAlias(String nuevoAlias) throws ExcepcionControladaError;

    void cancelarActualizacionAlias();
}