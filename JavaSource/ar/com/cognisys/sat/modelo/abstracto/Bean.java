package ar.com.cognisys.sat.modelo.abstracto;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;


@ManagedBean
public abstract class Bean implements Serializable {

	private static final long serialVersionUID = -688750062812335478L;
	
	@PostConstruct
	protected abstract void inicializacion() throws ExcepcionControladaError, ExcepcionControladaAlerta;
}