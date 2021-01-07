package ar.com.cognisys.sat.bean.pagina.externo;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class OtrasConsultasMiBarrioBean {
	
	public static void irMiBarrio() throws IOException {
		ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
		ex.redirect("https://www.vicentelopez.gov.ar/atencionalvecino/");
	}
	
}
