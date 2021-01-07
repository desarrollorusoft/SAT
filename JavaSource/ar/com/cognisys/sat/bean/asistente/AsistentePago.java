package ar.com.cognisys.sat.bean.asistente;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.core.administrador.AdministradorBoletaPago;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.administrador.AdministradorRegimenSimplificado;
import ar.com.cognisys.sat.core.administrador.AdministradorSeguimientos;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.abstracto.Deuda;
import ar.com.cognisys.sat.core.modelo.comun.deudas.Cuota;
import ar.com.cognisys.sat.core.modelo.comun.deudas.CuotaAdapter;
import ar.com.cognisys.sat.core.modelo.comun.deudas.DeudaAdapter;
import ar.com.cognisys.sat.core.modelo.comun.planDePago.TotalCuota;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.dao.rs.BonificacionRS;
import ar.com.cognisys.sat.core.modelo.enums.MediosPago;
import ar.com.cognisys.sat.core.modelo.enums.Sistema;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.factory.seguimiento.FactoryNavegacionSAT;
import ar.com.cognisys.sat.core.modelo.generador.pagoOnline.MedioPago;
import ar.com.cognisys.sat.core.modelo.generador.pagoOnline.PaquetePago;
import ar.com.cognisys.sat.core.modelo.generador.pagoOnline.URLsPago;
import ar.com.cognisys.sat.excepcion.Mensaje;
import ar.com.cognisys.sat.v2.core.modelo.exception.CoreSATException;

public class AsistentePago {

	public static PaquetePago generarRecibo(Cuenta cuenta, Date fechaPago, List<Cuota> listaCuotas, boolean cancelaDeuda, boolean tieneDeudaLegales, 
								     String correo) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		
		Deuda deudaRecalculada;

		if ( cuenta.getTipoCuenta().sos(TiposCuentas.COMERCIOS) )
			deudaRecalculada = obtenerDeudaComercio(cuenta, fechaPago, listaCuotas);
		else
			deudaRecalculada = obtenerDeuda(cuenta, fechaPago, listaCuotas, cancelaDeuda, tieneDeudaLegales);
		
		try {
			String path = AdministradorBoletaPago.generarRecibo( cuenta.obtenerCodigo(), 
															     cuenta.getTipoCuenta(), 
															     deudaRecalculada.getNumeroComprobante(), 
															     fechaPago, 
															     deudaRecalculada.getTotalCoutasGeneral().getTotal(), 
															     deudaRecalculada.getListaCuotas(), 
															     correo, 
															     cuenta.getDescripcion(), 
															     cuenta.getContribuyente().getNombreApellido() );
			
			HashMap<String, String> parametros = new HashMap<String, String>();
			parametros.put("URL", path);
			return (new PaquetePago((new URLsPago()).getURL_PMC(), parametros));
		} catch (CoreSATException e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static PaquetePago generarPago(Cuenta cuenta, Date fechaPago, MediosPago medioPago, List<Cuota> listaCuotas, boolean cancelaDeuda, 
										  boolean tieneDeudaLegales, String correo) throws ExcepcionControladaError, ExcepcionControladaAlerta {

		Deuda deudaRecalculada;

		if ( cuenta.getTipoCuenta().sos(TiposCuentas.COMERCIOS) )
			deudaRecalculada = obtenerDeudaComercio(cuenta, fechaPago, listaCuotas);
		else
			deudaRecalculada = obtenerDeuda(cuenta, fechaPago, listaCuotas, cancelaDeuda, tieneDeudaLegales);
		
		try {
			return AdministradorBoletaPago.generarPagoOnline(cuenta.obtenerCodigo(), 
															 cuenta.getTipoCuenta(), 
															 cuenta.getDescripcion(), 
															 cuenta.getContribuyente().getNombreApellido(), 
															 correo, 
															 fechaPago, 
															 deudaRecalculada.getTotalCoutasGeneral().getTotal(), 
															 deudaRecalculada.getNumeroComprobante(), 
															 transformarMedioPago( medioPago ), 
															 Sistema.SAT );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	private static Deuda obtenerDeudaComercio(Cuenta cuenta, Date fechaPago, List<Cuota> listaCuotas) throws ExcepcionControladaError {
		try {
			return AdministradorCuenta.recalcularDeudaComercio(cuenta, fechaPago, listaCuotas);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	private static Deuda obtenerDeuda(Cuenta cuenta, Date fechaPago, List<Cuota> listaCuotas, boolean cancelaDeuda, boolean tieneDeudaLegales) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {
			return AdministradorCuenta.recalcularDeudaComun(tieneDeudaLegales, cancelaDeuda, cuenta, fechaPago, listaCuotas);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
	
	private static MedioPago transformarMedioPago(MediosPago medioPago) {
		switch ( medioPago ) {
			case INTERBANKING: 		return MedioPago.interbanking;
			case LINK: 				return MedioPago.link_pagos;
			case PAGOMISCUENTAS:	return MedioPago.pmc;
			case TARJETAS_CREDITO:	return MedioPago.tarjeta_credito;
			case MERCADO_PAGO:		return MedioPago.mercado_pago;
			default: break;
		}
		
		return null;
	}

	public static List<Cuota> obtenerSeleccionadas(Deuda deuda) {
		List<Cuota> lista =  new ArrayList<Cuota>();
		
		for (Cuota cuota : deuda.getListaCuotasAVencer())
			if (cuota.isPagar())
				lista.add(cuota);
		
		for (Cuota cuota : deuda.getListaCuotasVencidas())
			if (cuota.isPagar())
				lista.add(cuota);
		
		return lista;
	}

	public static boolean cancelaDeuda(Deuda deuda) {
		int cantidad = 0;
		for (Cuota cuota : deuda.getListaCuotasVencidas())
			if (cuota.isPagar())
				cantidad++;
		
		return (deuda.getListaCuotasVencidas().size() == cantidad);
	}
	
	public static boolean seleccionoCoutasVencidas(Deuda deuda) {
		if (deuda.getListaCuotasVencidas() != null && !deuda.getListaCuotasVencidas().isEmpty())
			for (Cuota cuota : deuda.getListaCuotasVencidas())
				if (cuota.isPagar())
					return true;
		
		return false;
	}

	public static Date obtenerMenorFecha(Deuda deuda) {
		Date fecha = null;
		
		for (Cuota cuota : deuda.getListaCuotasAVencer())
			if (cuota.isPagar() && (fecha == null || fecha.after( cuota.getFechaVencimiento() )))
				fecha = cuota.getFechaVencimiento();
		
		return fecha;
	}

	public static Float calcularMonto(Deuda deuda) {
		return calcularMonto(deuda.getListaCuotasAVencer(), deuda.getListaCuotasVencidas());
	}
	
	private static Float calcularMonto(List<Cuota> listaCuotaAVencer, List<Cuota> listaCuotaVencidas) {
		
		float monto = 0f;
		
		for (Cuota cuota : listaCuotaVencidas)
			if (cuota.isPagar())
				monto += cuota.getTotal().floatValue();
		
		for (Cuota cuota : listaCuotaAVencer)
			if (cuota.isPagar())
				monto += cuota.getTotal().floatValue();
		
		return monto;
	}
	
	public static void verificarMora(CuotaAdapter cuota, DeudaAdapter deuda) {
		if (cuota.isMora())
			cambiarMora(deuda, cuota.isPagar());
		
		if (cuota.isMora() && cuota.isPagar())
			Mensaje.addMessageAviso("Los periodos marcados corresponden a una deuda en proceso de ser reclamada judicialmente. Deberá ser abonada en su totalidad.");
	}

	private static void cambiarMora(DeudaAdapter deuda, boolean pagar) {
		for (CuotaAdapter cuota : deuda.getCuotasVencidas())
			if (cuota.isMora())
				cuota.setPagar(pagar);
	}

	// Comentar esto
    public static boolean muestroCartelAblSemestral(Cuenta cuenta, DeudaAdapter deuda) {
        if (cuenta.getTipoCuenta().sos(TiposCuentas.ABL)) {
            boolean tiene7aVencer = false;
            for (CuotaAdapter c :  deuda.getCuotasAVencer()) {
                if (c.getPeriodo().trim().endsWith("7"))
                    tiene7aVencer = true;
            }

            return tiene7aVencer;
        } else
            return false;
    }

	// Comentar esto
	public static boolean muestroCarcelCancelacionSemestral(Cuenta cuenta, DeudaAdapter deuda) {
		if (cuenta.getTipoCuenta().sos(TiposCuentas.ABL)) {
			if (deuda.getCuotasAVencer().size() != 6)
				return false;

			boolean tiene7aVencer = false;
			for (CuotaAdapter c :  deuda.getCuotasAVencer()) {
				if (c.getPeriodo().trim().endsWith("7"))
					tiene7aVencer = true;
			}

			return tiene7aVencer;
		} else
			return false;
	}

	// Comentar esto
	public static void verificarCuotasGrupales(Cuenta cuenta, CuotaAdapter cuota, DeudaAdapter deuda) {
		if (cuenta.getTipoCuenta().sos(TiposCuentas.ABL)) {
			boolean tiene7 = false;
			for (CuotaAdapter c :  deuda.getCuotasAVencer()) {
				if (c.getPeriodo().trim().endsWith("7"))
					tiene7 = true;
			}

			String periodo = cuota.getPeriodo().trim();
			if ( (periodo.endsWith("9") || periodo.endsWith("10") || periodo.endsWith("11") || periodo.endsWith("12")) && tiene7) {
				// Pongo la seleccion segun el ultimo evento
				for (CuotaAdapter c :  deuda.getCuotasAVencer()) {
					if ( cuota.isPagar() || (!c.getPeriodo().trim().endsWith("7") && !c.getPeriodo().trim().endsWith("8")))
						c.setPagar( cuota.isPagar() );
				}

				if (cuota.isPagar())
					Mensaje.addMessageAviso("Los periodos 9 a 12, solo pueden ser abonados si abona el semestre");
			}

			if ( !cuota.isPagar() && tiene7 && ((periodo.endsWith("7") || periodo.endsWith("8"))) ) {
                for (CuotaAdapter c :  deuda.getCuotasAVencer()) {
                    if ( (!c.getPeriodo().trim().endsWith("7") && !c.getPeriodo().trim().endsWith("8")))
                        c.setPagar( false );
                }
			}
		}
	}

	public static boolean pagoSemestralSeleccionado(Cuenta cuenta, DeudaAdapter deuda) {
		if (cuenta.getTipoCuenta().sos(TiposCuentas.ABL)) {
			boolean tiene7 = false;
			boolean todoSeleciconado = true;
			for (CuotaAdapter c :  deuda.getCuotasAVencer()) {
				if (c.getPeriodo().trim().endsWith("7"))
					tiene7 = true;
				if (!c.isPagar())
					todoSeleciconado = false;
			}
			return (tiene7 && todoSeleciconado);
		} else
			return false;
	}

	public static void limpiarDeuda(Deuda deuda) {
		if (deuda != null) {
			for (Cuota cuota : deuda.getListaCuotasVencidas())
				cuota.setPagar(false);
			
			for (Cuota cuota : deuda.getListaCuotasAVencer())
				cuota.setPagar(false);
		}
	}

	public static DeudaAdapter recuperarDeuda(Cuenta cuenta) throws ExcepcionControladaError, ExcepcionControladaAlerta {
		try {
			return AdministradorCuenta.recuperarDeudas(cuenta.getTipoCuenta(), cuenta.getDatoCuenta());
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		} finally {
			registrarEstadisticaCosulta(cuenta);
		}
	}

	public static TotalCuota recuperarTotalCancelacion(Cuenta cuenta) throws ExcepcionControladaError {
		try {
			return AdministradorCuenta.recuperarTotalCancelacion(cuenta, new Date());
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static void cancelarDeuda(DeudaAdapter deuda) {
		if (deuda != null)
			for (Cuota cuota : deuda.getListaCuotasVencidas())
				cuota.setPagar(true);
	}

	public static void pagoSemestral(DeudaAdapter deuda) {
		if (deuda != null)
			for (Cuota cuota : deuda.getListaCuotasAVencer())
				cuota.setPagar(true);
	}

	public static Float calcularTotalConDescuento(Float total, Float descuentoContado, Float descuentoSemestral,
												  boolean cancelaDeuda, boolean pagoSemestral) {
		if (total == null || descuentoContado == null)
			return 0f;
		else {
			float aux = total;
			if (cancelaDeuda)
				aux -= descuentoContado;
			if (pagoSemestral)
				aux -= descuentoSemestral;
			return aux;
		}
	}

	public static Float calcularDescuento(DeudaAdapter deuda, TotalCuota totalCancelacion) {
		if (deuda == null || totalCancelacion == null || deuda.isTieneDeudaLegales() || deuda.getTotalCoutasVencidas() == null ||
			deuda.getTotalCoutasVencidas().getTotal() == 0f)
			return 0f;
		else {
			TotalCuota ini = deuda.getTotalCoutasVencidas();
			TotalCuota fin = totalCancelacion;
			
			return (ini.getTotal() - ini.getCapital() - fin.getRecargo() - fin.getMulta());
		}
	}

	public static Float calcularDescuentoSemestral(Cuenta cuenta, DeudaAdapter deuda) {
		if (cuenta.getTipoCuenta().sos(TiposCuentas.ABL)) {
			float sum = 0f;
			for (CuotaAdapter c :  deuda.getCuotasAVencer())
				sum += c.getTotal();
			return sum * (float)0.1;
		} else
			return 0f;
	}
	
	private static void registrarEstadisticaCosulta(Cuenta cuenta) {
		
		Usuario usuario = SesionBean.getUsuarioLogueado();
		
		switch ( cuenta.getTipoCuenta() ) {
			case ABL: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("abl_consultar_deuda"));
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("abl_consultar_deuda"));
				break;
			case VEHICULOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_consultar_deuda")); 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_boleta_de_pago")); 
				break;
			case RODADOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_consultar_deuda")); 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_boleta_de_pago")); 
				break;
			case CEMENTERIO: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("cementerio_consultar_deuda")); 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("cementerio_boleta_de_pago")); 
				break;
			case COMERCIOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("rs_consultar_deuda")); 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("rs_boleta_de_pago")); 
				break;
			default: break;
		}
	}
	
	public static void registrarEstadisticaInterbanking(Cuenta cuenta) {
		
		Usuario usuario = SesionBean.getUsuarioLogueado();
		
		switch ( cuenta.getTipoCuenta() ) {
			case ABL: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("abl_pagar_interbanking"));
				break;
			case VEHICULOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_pagar_interbankig")); 
				break;
			case RODADOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_pagar_interbankig")); 
				break;
			case CEMENTERIO: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("cementerio_pagar_interbanking")); 
				break;
			case COMERCIOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("rs_pagar_interbanking")); 
				break;
			default: break;
		}	
	}

	public static void registrarEstadisticaPMC(Cuenta cuenta) {
		
		Usuario usuario = SesionBean.getUsuarioLogueado();
		
		switch ( cuenta.getTipoCuenta() ) {
			case ABL: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("abl_pagar_PMC"));
				break;
			case VEHICULOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_pagar_PMC")); 
				break;
			case RODADOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_pagar_PMC")); 
				break;
			case CEMENTERIO: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("cementerio_pagar_PMC")); 
				break;
			case COMERCIOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("rs_pagar_PMC")); 
				break;
			default: break;
		}
	}

	public static void registrarEstadisticaLink(Cuenta cuenta) {
		Usuario usuario = SesionBean.getUsuarioLogueado();
		
		switch ( cuenta.getTipoCuenta() ) {
			case ABL: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("abl_pagar_link_pagos"));
				break;
			case VEHICULOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_pagar_link_pagos")); 
				break;
			case RODADOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_pagar_link_pagos")); 
				break;
			case CEMENTERIO: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("cementerio_pagar_link_pagos")); 
				break;
			case COMERCIOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("rs_pagar_link_pagos")); 
				break;
			default: break;
		}
	}

	public static void registrarEstadisticaMP(Cuenta cuenta) {
		Usuario usuario = SesionBean.getUsuarioLogueado();

		switch ( cuenta.getTipoCuenta() ) {
			case ABL:
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("abl_pagar_mp"));
				break;
			case VEHICULOS:
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_pagar_mp"));
				break;
			case RODADOS:
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_pagar_mp"));
				break;
			case CEMENTERIO:
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("cementerio_pagar_mp"));
				break;
			case COMERCIOS:
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("rs_pagar_mp"));
				break;
			default: break;
		}
	}

	public static void registrarEstadisticaCredito(Cuenta cuenta) {
	
		Usuario usuario = SesionBean.getUsuarioLogueado();
		
		switch ( cuenta.getTipoCuenta() ) {
			case ABL: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("abl_pagar_credito"));
				break;
			case VEHICULOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_pagar_credito")); 
				break;
			case RODADOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_pagar_credito")); 
				break;
			case CEMENTERIO: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("cementerio_pagar_credito")); 
				break;
			case COMERCIOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("rs_pagar_credito")); 
				break;
			default: break;
		}
	}

	public static void registrarEstadisticaRecibos(Cuenta cuenta) {
		Usuario usuario = SesionBean.getUsuarioLogueado();
		
		switch ( cuenta.getTipoCuenta() ) {
			case ABL: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("abl_generacion_recibo"));
				break;
			case VEHICULOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_generacion_recibo")); 
				break;
			case RODADOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_generacion_recibo")); 
				break;
			case CEMENTERIO: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("cementerio_generacion_recibo")); 
				break;
			case COMERCIOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("rs_generacion_recibo")); 
				break;
			default: break;
		}
	}

	public static void cambiarSeleccionRS(List<CuotaAdapter> cuotasAVencer, boolean seleccion, BonificacionRS bonificacion) {
		if (cuotasAVencer != null) {
			for (CuotaAdapter c : cuotasAVencer)
				if (!estaBinificada(c, bonificacion))
					c.setPagar(seleccion);
		}
	}

	public static void cambiarSeleccion(List<CuotaAdapter> cuotasAVencer, boolean seleccion) {
		if (cuotasAVencer != null)
			for (CuotaAdapter c : cuotasAVencer)
				c.setPagar(seleccion);
	}

	public static boolean esRS(Cuenta cuenta) {
		return cuenta.sos(TiposCuentas.COMERCIOS);
	}

	public static boolean muestroBonificacionEfectiva(BonificacionRS bonificacion) {
		return ( bonificacion != null && bonificacion.isCorresponde() );
	}

	public static boolean estaBinificada(CuotaAdapter cuota, BonificacionRS bonificacion) {
		
		if (cuota != null && bonificacion != null) {
			for (Cuota c : bonificacion.getCuotas())
				if (cuota.getPeriodo().equals(c.getPeriodo()))
					return true;
			
			return false;
		} else
			return false;
	}

	public static boolean noExcedeMontoRecibo(DeudaAdapter deuda) {
		List<Cuota> lista = obtenerSeleccionadas( deuda );
		float monto = 0f;
		
		for (Cuota cuota : lista)
			monto += cuota.getTotal();
		
		return (monto < 1000000f);
	}
	
	public static BonificacionRS recuperarBonificacion(Cuenta cuenta) throws ExcepcionControladaError {
		try {			
			if (esRS(cuenta))
//				return AdministradorRegimenSimplificado.recuperarBonificacion( 0 );
				return AdministradorRegimenSimplificado.recuperarBonificacion( cuenta.getNumero() );
			else
				return null;
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
}