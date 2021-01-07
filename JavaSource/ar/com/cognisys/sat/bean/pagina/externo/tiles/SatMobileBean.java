package ar.com.cognisys.sat.bean.pagina.externo.tiles;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class SatMobileBean {
	
	public static void irApple() throws IOException {
		ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
		ex.redirect("https://itunes.apple.com/ar/app/sat-m%C3%B3vil/id1168697918?l=en&mt=8");
	}
	
	public static void irAndroid() throws IOException {
		ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
		ex.redirect("https://play.google.com/store/apps/details?id=ar.com.cognisys.mvl.sat.android&hl=es");
	}
	
	public static void irWindows() throws IOException {
		ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
		ex.redirect("https://www.microsoft.com/es-ar/store/p/sat-movil/9nw9t804zdgv");
	}
	
}
