package ar.com.cognisys.sat.seguridad;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.el.ELContext;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.sat.bean.Redireccionamiento;
import ar.com.cognisys.sat.bean.SesionBean;
import ar.com.cognisys.sat.core.modelo.abstracto.UsuarioSat;
import ar.com.cognisys.sat.excepcion.SATRuntimeException;

public class RequestInterceptor implements PhaseListener, Serializable {

	private static final long serialVersionUID = -8060053020563585360L;

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

	@Override
	public void afterPhase(PhaseEvent event) {

		FacesContext context = event.getFacesContext();
		
		if (event.getFacesContext().getExternalContext().getSession(false) == null) {
			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			RequestContext requestContext = RequestContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
			HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
	
			if (externalContext.isResponseCommitted()) {
				return; // redirect is not possible
			}
	
			ResponseWriter responseWriter = null;
			try {
				if (((requestContext != null && RequestContext.getCurrentInstance().isAjaxRequest()) || 
					 (facesContext != null && facesContext.getPartialViewContext().isPartialRequest())) &&
				    facesContext.getResponseWriter() == null && facesContext.getRenderKit() == null) {
	
					response.setCharacterEncoding(request.getCharacterEncoding());
	
					RenderKitFactory factory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	
					RenderKit renderKit = factory.getRenderKit(facesContext, 
															   facesContext.getApplication().getViewHandler().calculateRenderKitId(facesContext));
	
					responseWriter = renderKit.createResponseWriter(response.getWriter(), null, request.getCharacterEncoding());
					responseWriter = new PartialResponseWriter(responseWriter);
					facesContext.setResponseWriter(responseWriter);
	
					externalContext.redirect(externalContext.getRequestContextPath() + "/pages/sesion_expirada.xhtml");
					cerrarConexiones();
					
					return;
				}
			} catch (IOException e) {
				throw new FacesException(e);
			} finally {
				if (responseWriter != null) {
					try {
						responseWriter.close();
					} catch (Exception e2) {}
				}
			}
		}
		
		/* Esta es una pagina con sesion*/
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		String viewId = context.getViewRoot() == null ? null : context.getViewRoot().getViewId();
		if ( !esPaginaPublica(viewId) ) {

			UsuarioSat user = SesionBean.getUsuarioLogueado();
			
			if (!loggedIn(user)) {
				try {
					new Redireccionamiento( "/pages/pub/login.xhtml" ).redireccionar();
				} catch ( ExcepcionControladaError e ) {
					throw new SATRuntimeException( e );
				}
				
				return;
				
			} else if (isExpired(user)) {
				SesionBean sesionBean = (SesionBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "sesionBean");
				sesionBean.cerrarSesionContribuyente();
				
				try {
					new Redireccionamiento( "/pages/sesion_expirada.xhtml" ).redireccionar();
				} catch ( ExcepcionControladaError e ) {
					throw new SATRuntimeException( e );
				}
				
				return;
				
			} else {
				SesionBean.getUsuarioLogueado().setFechaUltimoIngreso(new Date());
			}
		}
	}

	private boolean esPaginaPublica(String viewId) {
		
		if ( viewId == null )
			return false;
		
		String[] paginas = new String[]{"ingreso","login","login_rs","registro","registro_rs","olvido_clave","olvido_clave_rs",
										"activacion","sesion_expirada","acceso_externo","fracasoTransaccion",
										"ingreso_hsat","preguntas_frecuentes","guia_tramites","reimpresion_boleta","about",
										"formulario_consultas","plataforma-consultas","generar_codigo","inc_correo","carga_nivel3",
										"cambio_clave_temporal"};
		for (String pagina : paginas)
			if (viewId.contains(pagina))
				return true;

		return false;
	}
	
	@Override
	public void beforePhase(PhaseEvent event) {}

	private Boolean loggedIn(UsuarioSat user) {
		return user != null;
	}

	private Boolean isExpired(UsuarioSat usuario) {
		
		if (usuario.getNombreUsuario().equals((new PropiedadesRequestInterceptor()).getCorreoAdmin())) {
			return false;
		}
		
		int timeOut = Integer.parseInt( (new PropiedadesRequestInterceptor()).getTimeOutSesion() );

		if (usuario.getFechaUltimoIngreso() != null && 
			usuario.getFechaUltimoIngreso().getTime() < new Date().getTime() - timeOut) {
			
			return true;
		} else {
			return false;
		}
	}
	
	private void cerrarConexiones(){
//		AdministradoConexiones.getInstance().cerrarConexiones();
	}
}