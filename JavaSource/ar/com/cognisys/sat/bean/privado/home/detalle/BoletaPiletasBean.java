package ar.com.cognisys.sat.bean.privado.home.detalle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ar.com.cognisys.sat.v2.core.modelo.exception.ErrorLecturaPropertiesException;
import org.primefaces.context.RequestContext;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.SeccionBean;
import ar.com.cognisys.sat.bean.asistente.AsistentePago;
import ar.com.cognisys.sat.core.administrador.AdministradorPiletas;
import ar.com.cognisys.sat.core.modelo.comun.natatorios.CuentaPileta;
import ar.com.cognisys.sat.core.modelo.generador.pagoOnline.PaquetePago;

import java.io.FileNotFoundException;

@ManagedBean(name = "BoletaPiletas")
@ViewScoped
public class BoletaPiletasBean extends SeccionBean {
	
	private static final long serialVersionUID = 9076616999399507307L;
	private CuentaPileta cuenta;
	
	@Override
	protected void inicializacion() {}
	
	@Override
	public void cargar() throws ExcepcionControladaError {}

	@Override
	public void cargar(Object dato) throws ExcepcionControladaError {
		this.setCuenta((CuentaPileta) dato);
	}

	@Override
	public void siguiente() throws ExcepcionControladaError {}

	@Override
	public void refrescar() throws ExcepcionControladaError {}

	public void generarRecibo() throws ExcepcionControladaError  {
		this.abrirPopupRedirect( this.obtenerPaqueteRecibo() );
		AsistentePago.registrarEstadisticaRecibos(this.getCuenta());
	}
	
	private PaquetePago obtenerPaqueteRecibo() throws ExcepcionControladaError {
		try {
			return AdministradorPiletas.generarRecibo( this.getCuenta() );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
	
	private void abrirPopupRedirect(PaquetePago paquete) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().putAll(paquete.getParametros());
		RequestContext.getCurrentInstance().execute("window.open('pagos/redireccionamiento_pagos.xhtml', 'WindowName', 'dependent=yes, menubar=no, toolbar=no, scrollbars=yes, resizable=yes, fullscreen=yes');");
	}
	
	public CuentaPileta getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaPileta cuenta) {
		this.cuenta = cuenta;
	}
}