package ar.com.cognisys.sat.seguridad;

import java.io.Serializable;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import ar.com.cognisys.sat.excepcion.NavegacionNoAutorizadaException;


public class SecureNavigationHandler extends NavigationHandler implements Serializable {

	private static final long serialVersionUID = -2093274470665469654L;
	private NavigationHandler handler;

	public SecureNavigationHandler(NavigationHandler handler) {
		
		this.handler = handler;
	}

	@Override
	public void handleNavigation(FacesContext contexto, String dondeOcurreAccion, String acceso) {
		
		/* Si se agrega una pagina publica, recordar agregarla en el RequestInterceptor! */
		if (acceso == null || 
			"".contentEquals(acceso) || 
			"inicio".contentEquals(acceso) || 
			"inicio_rs".contentEquals(acceso) || 
			"registro".contentEquals(acceso) || 
			"registro_rs".contentEquals(acceso) || 
			"olvido_clave".contentEquals(acceso) || 
			"olvido_clave_rs".contentEquals(acceso) || 
			"no_activo_cuenta".contentEquals(acceso) ||
			"about".contentEquals(acceso) ||
			"generar_codigo".contentEquals(acceso) || 
			"inc_correo".contentEquals(acceso) || 
			
			"acceso_externo".contentEquals(acceso) || 
			"usuario_no_registrado".contentEquals(acceso) || 
			"sesion_expirada".contentEquals(acceso) || 
			"ingreso_hsat".contentEquals(acceso) ||
			"fracaso".contentEquals(acceso) ||
			"preguntas_frecuentes".contentEquals(acceso) ||
			"guia_tramites".contentEquals(acceso) ||
			"reimpresion_boleta".contentEquals(acceso) ||
			"formulario_consultas".contentEquals(acceso) ||
			"plataforma-consultas".contentEquals(acceso) ||

			"carga_nivel3".contentEquals(acceso) ||
			"cambio_clave_temporal".contentEquals(acceso) ||
			Autorizador.tieneAcceso(acceso)) {
			
			handler.handleNavigation(contexto, dondeOcurreAccion, acceso);
		} else {
			throw new NavegacionNoAutorizadaException();
		}
	}
}