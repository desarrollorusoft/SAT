package ar.com.cognisys.sat.bean.asistente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.core.administrador.AdministradorBoletaPago;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.administrador.AdministradorPlanDePago;
import ar.com.cognisys.sat.core.administrador.AdministradorPlanPago;
import ar.com.cognisys.sat.core.administrador.AdministradorSeguimientos;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.abstracto.Deuda;
import ar.com.cognisys.sat.core.modelo.comun.planDePago.CuotaPPC;
import ar.com.cognisys.sat.core.modelo.comun.planDePago.PlanDePagoAPagar;
import ar.com.cognisys.sat.core.modelo.comun.planDePago.ResultadoSimulacion;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.core.modelo.enums.MediosPago;
import ar.com.cognisys.sat.core.modelo.enums.Sistema;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.factory.seguimiento.FactoryNavegacionSAT;
import ar.com.cognisys.sat.core.modelo.generador.pagoOnline.MedioPago;
import ar.com.cognisys.sat.core.modelo.generador.pagoOnline.PaquetePago;

public class AsistentePPC {

	public static boolean tieneDeuda(Deuda deuda) {
		return deuda != null && deuda.tieneCoutasVencidas();
	}
	
	public static boolean tienePPC(List<PlanDePagoAPagar> planes) {
		return (planes != null && !planes.isEmpty());
	}

	public static ResultadoSimulacion calcularSimulacion(Cuenta cuenta, Integer anticipo) throws ExcepcionControladaError {
		try {
			return AdministradorPlanPago.calcularPlan(cuenta, anticipo, cuenta.getDeudas().getListaCuotasVencidas());
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static List<PlanDePagoAPagar> recuperarPlanes(Cuenta cuenta, Usuario usuario) {
		List<PlanDePagoAPagar> lista = new ArrayList<>();

		for (PlanDePagoAPagar plan: usuario.getPlanes())
			if (plan.getCuenta().equals( cuenta.getNumero() ))
				lista.add( plan );

		return lista;
	}

	public static List<CuotaPPC> recuperarCuotas(PlanDePagoAPagar plan) throws ExcepcionControladaError {
		try {
			return AdministradorPlanDePago.obtenerCuotas(plan.getNroPlan());
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
	
	public static CuotaPPC recuperarCancelacion(PlanDePagoAPagar plan) throws ExcepcionControladaError {
		try {
			return AdministradorPlanDePago.obtenerCancelacionPlan( plan );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	public static void limpiarSeleccion(List<CuotaPPC> listaCuotas) {
		if (listaCuotas != null && !listaCuotas.isEmpty())
			for (CuotaPPC cuota : listaCuotas)
				cuota.setPagar(false);
	}

	public static Float calcularMonto(List<CuotaPPC> listaCuotas) {
		float monto = 0f;
		for (CuotaPPC cuota : listaCuotas)
			if (cuota.isPagar())
				monto += cuota.getTotal();
		
		return monto;
	}

	public static Float calularTotal(List<CuotaPPC> listaCuotas) {
		float monto = 0f;
		for (CuotaPPC cuota : listaCuotas)
			monto += cuota.getTotal();
		
		return monto;
	}

	public static boolean cancelaDeuda(List<CuotaPPC> listaCuotas) {
		if (listaCuotas == null || listaCuotas.isEmpty())
			return false;
		else {
			int cantidad = 0;
			for (CuotaPPC cuota : listaCuotas)
				if (cuota.isPagar())
					cantidad++;
			
			return cantidad == listaCuotas.size();
		}
	}

	public static Float calcularDescuento(Float deuda, CuotaPPC datosCancelacion) {
		if (deuda == null || datosCancelacion == null)
			return 0f;
		else
			return deuda - datosCancelacion.getTotal();
	}

	public static void cargarCancelacion(List<CuotaPPC> listaCuotas) {
		for (CuotaPPC cuota : listaCuotas)
			cuota.setPagar(true);
	}

	public static PaquetePago generarPago(PlanDePagoAPagar plan, List<CuotaPPC> listaCuotas, MediosPago medioPago,
										  boolean cancelaDeuda) throws ExcepcionControladaAlerta, ExcepcionControladaError {

		float total;
		String numeroComprobante;

		try {
			if ( cancelaDeuda ) {
				total = plan.getImporteCancelacion();
				numeroComprobante = generarComprobanteCancelacion( plan.getNroPlan() );
			} else {
				CuotaPPC cuota = obtenerCuotaPago(listaCuotas);
				if (!pagaUna(cuota))
					throw new ExcepcionControladaAlerta("Debe seleccionar una Cuota o Cancelar el Plan");

				total = cuota.getTotal();
				numeroComprobante = generarComprobante(plan.getNroPlan(), cuota.getNumero());
			}

			return AdministradorBoletaPago.generarPagoOnline(plan.getCuenta().toString(),
															 TiposCuentas.recuperarParaPPC( plan.getSistema() ),
															 "Plan de Pagos",
															 plan.getSolicitante(),
															 SesionBean.getUsuarioLogueado().getCorreo(), 
															 new Date(),
															 total,
															 numeroComprobante, 
															 transformarMedioPago( medioPago ), 
															 Sistema.SAT );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	private static String generarComprobanteCancelacion(Integer numeroPlan) throws ExcepcionControladaError {
		try {
			return AdministradorCuenta.generarComprobanteCancelacionPPC( numeroPlan );
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}

	private static String generarComprobante(Integer numeroPlan, Integer cuota) throws ExcepcionControladaError {
		try {
			return AdministradorCuenta.generarComprobantePPC(numeroPlan, cuota);
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
	}
	
	private static CuotaPPC obtenerCuotaPago(List<CuotaPPC> listaCuotas) throws ExcepcionControladaAlerta {
		if (listaCuotas == null || listaCuotas.isEmpty())
			throw new ExcepcionControladaAlerta("Debe seleccionar una cuota a pagar");
		else {
			for (CuotaPPC cuota : listaCuotas)
				if (cuota.isPagar())
					return cuota;
			
			return null;
		}
	}
	
	private static boolean pagaUna(CuotaPPC cuota) {
		return (cuota != null);
	}
	
	private static MedioPago transformarMedioPago(MediosPago medioPago) {
		switch ( medioPago ) {
			case INTERBANKING: 		return MedioPago.interbanking;
			case LINK: 				return MedioPago.link_pagos;
			case PAGOMISCUENTAS:	return MedioPago.pmc;
			case TARJETAS_CREDITO:	return MedioPago.tarjeta_credito;
			case MERCADO_PAGO:	return MedioPago.mercado_pago;
			default: break;
		}
		
		return null;
	}

	public static void seleccionarSoloCuota(CuotaPPC cuota, List<CuotaPPC> listaCuotas) {
		
		for (CuotaPPC cuotaPPC : listaCuotas)
			if( !cuotaPPC.equals( cuota ) )
				cuotaPPC.setPagar( false );
		
	}

	public static void validarAlgunoSeleccionado(List<CuotaPPC> listaCuotas) throws ExcepcionControladaAlerta {
		
		for (CuotaPPC cuotaPPC : listaCuotas) 
			if( cuotaPPC.isPagar() )
				return;
		
		throw new ExcepcionControladaAlerta("Debe Seleccionar al menos un período");	
	}

	public static void registrarIngreso(Cuenta cuenta) {
		Usuario usuario = SesionBean.getUsuarioLogueado();
		
		switch ( cuenta.getTipoCuenta() ) {
			case ABL: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("abl_simulacion_ppc"));
				break;
			case VEHICULOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_simulacion_ppc")); 
				break;
			case RODADOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_simulacion_ppc")); 
				break;
			case CEMENTERIO: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("cementerio_simulacion_ppc")); 
				break;
			case COMERCIOS:
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("rs_simulacion_ppc"));
				break;
			default: break;
		}
	}

	public static void registrarIngresoCalculo(Cuenta cuenta) {
		Usuario usuario = SesionBean.getUsuarioLogueado();
		
		switch ( cuenta.getTipoCuenta() ) {
			case ABL: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("abl_simulacion_ppc_calculo"));
				break;
			case VEHICULOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_simulacion_ppc_calculo")); 
				break;
			case RODADOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_simulacion_ppc_calculo")); 
				break;
			case CEMENTERIO: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("cementerio_simulacion_ppc_calculo")); 
				break;
			case COMERCIOS:
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("rs_simulacion_ppc_calculo"));
				break;
			default: break;
		}
	}

	public static void registrarPlanes(Cuenta cuenta) {
		Usuario usuario = SesionBean.getUsuarioLogueado();
		
		switch ( cuenta.getTipoCuenta() ) {
			case ABL: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("abl_plan_de_pago"));
				break;
			case VEHICULOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_plan_de_pago")); 
				break;
			case RODADOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_plan_de_pago")); 
				break;
			default: break;
		}
	}
	
	public static void registrarPagoPlanes(Cuenta cuenta) {
		Usuario usuario = SesionBean.getUsuarioLogueado();
		
		switch ( cuenta.getTipoCuenta() ) {
			case ABL: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("abl_plan_credito"));
				break;
			case VEHICULOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_plan_credito")); 
				break;
			case RODADOS: 
				AdministradorSeguimientos.registrarAccesoWeb(usuario.getCorreo(), FactoryNavegacionSAT.generarInstancia("automotores_plan_credito")); 
				break;
			default: break;
		}
	}

    public static void cancelarDeuda(List<CuotaPPC> listaCuotas) {
		for (CuotaPPC cuota: listaCuotas)
			cuota.setPagar(true);
	}

	public static void limpiarDeuda(List<CuotaPPC> listaCuotas) {
		for (CuotaPPC cuota: listaCuotas)
			cuota.setPagar(false);
	}

	public static boolean todoSelccionado(List<CuotaPPC> listaCuotas) {
		for (CuotaPPC cuota: listaCuotas)
			if (!cuota.isPagar())
				return false;
		return true;
	}
}