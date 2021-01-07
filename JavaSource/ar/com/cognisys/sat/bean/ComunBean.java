package ar.com.cognisys.sat.bean;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.sat.core.modelo.comun.Contribuyente;
import ar.com.cognisys.sat.core.modelo.comun.usuarioSat.Usuario;
import ar.com.cognisys.sat.modelo.abstracto.Bean;
import ar.com.cognisys.sat.modelo.administrador.Navegacion;

@ManagedBean
@ViewScoped
public class ComunBean extends Bean {

	private static final long serialVersionUID = -8272554446703411975L;
	
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;
	
	@Override
	protected void inicializacion() {}

	public void actualizarUsuario() {
		this.getSesionBean().getUsuario().setFechaUltimoIngreso(new Date());
	}
	
	public String cuitUsuario() {
		String dato = SesionBean.getUsuarioLogueado().getCuit();
		
		if (dato != null && dato.length() == 11)
			dato = dato.substring(0, 2) + "-" + dato.substring(2, 10) + "-" + dato.substring(10);
	
		return dato;
	}

	public String irALogin() {
		return Navegacion.inicio.toString();
	}
	
	public String irAblConsultarDeuda() {
		return Navegacion.abl_consultar_deuda.toString();
	}
	
	public String irCuentas() {
		return Navegacion.cuentas.toString();
	}
	
	public String irAMisDatos() {
		return Navegacion.mis_datos.toString();
	}

	public String irAComercio() {
		return Navegacion.comercio.toString();
	}
	
	public String irACementerioConsultarDeuda() {
		return Navegacion.cementerio_consultar_deuda.toString();
	}
	
	public String irACementerioBoletaPago() {
		return Navegacion.cementerio_boleta_pago.toString();
	}
	
	public String irACementerioSimulacionPPC() {
		return Navegacion.cementerio_simulacion_ppc.toString();
	}

	public String irASubsidio() {
		return Navegacion.subsidio.toString();
	}

	public String cerrarSesion() {
		this.getSesionBean().cerrarSesionContribuyente();
		
		return Navegacion.inicio.toString();
	}
	
	public static String ajustarFechaHora(Date fecha) {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(fecha);
	}
	
	public static String fechaActual(){
		return ComunBean.ajustarFecha(new Date());
	}
	
	public static String ajustarFecha(Date fecha) {
		
		if (fecha == null || fecha.equals("")) {
			return "";
		} else {
			return new SimpleDateFormat("dd-MM-yyyy").format(fecha);
		}
	}
	
	public static String ajustarTelefonoFax(String telefono, String fax) {
		
		return telefono + " / " + fax;
	}
	
	public static String returnDecimal(float resultado) {
		return "$ " + String.format("%.2f", resultado);
	}

	public static String sacarMascaraCuit( String cuit ){
		return (cuit != null ? cuit.replace("-", "") : "");
	}

	public static String formatearMoneda(float numero) {
		Locale locale = new Locale("es","AR");
		Currency currency = Currency.getInstance(locale);
		NumberFormat format = NumberFormat.getCurrencyInstance(locale);
		format.setCurrency(currency);
		return format.format(numero).replace("$", "$ ");
	}
	
	public static String getNombreCompleto(Contribuyente contribuyente) {
		
		return contribuyente.getNombre() + " " + contribuyente.getApellido();
	}
	
	public static String ajustarPeriodo(String periodo) {
		
		String pAux ;
		
		if(periodo.trim().length()==6){
			pAux = periodo.replace("-", "-0");
			periodo = pAux;
		}
		
		return periodo;
	}
	
	public static String ajustarPeriodoReciboAVencer(String periodo){
		String pAux ;

		if (periodo.substring(periodo.trim().length()-1, periodo.trim().length()).equals("-")) {
			pAux = periodo.replace("-", "-0");
			periodo = pAux.substring(0, 7);
		}
		
		return periodo;
	}
	
	public static String ajustarPeriodoSimulacion(String periodo) {
		
		String pAux ;
		
		pAux = periodo.replace("-0", "");
		
		if (pAux.trim().length()==6) {
			periodo = pAux.replace("-", "-0");
		} else {
			periodo = pAux;
		}
		
		return periodo;
	}
	
	public static Date fechaMaximaCon(int dias) {
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, dias);
		
		return c.getTime();
	}
	
	public String navegarA_RS() {
		// Previamente se pregunto si tenia comercio
		if (SesionBean.getUsuarioLogueado().getComercio().tieneSolicitante()) 
			return Navegacion.rs.name();
		else
			return Navegacion.actualizacion_datos.name();	
	}
	
	public boolean correspondeRS() {
		return (SesionBean.getUsuarioLogueado().tieneComercio());
	}
	
	public boolean esContribuyente() {
		Usuario usuario = SesionBean.getUsuarioLogueado();
		return usuario != null && usuario.esContribuyente();
	}

	public boolean correspondeSubsidio() {
		return SesionBean.getUsuarioLogueado().getTramiteSubsidio() != null;
	}
	
	public Usuario getUsuario() {
		return SesionBean.getUsuarioLogueado();
	}
	
	public SesionBean getSesionBean() {
		return sesionBean;
	}

	public void setSesionBean(SesionBean sesionBean) {
		this.sesionBean = sesionBean;
	}
}