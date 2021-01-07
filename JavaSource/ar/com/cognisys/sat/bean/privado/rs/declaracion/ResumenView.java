package ar.com.cognisys.sat.bean.privado.rs.declaracion;

import java.io.Serializable;
import java.util.List;

import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.ActividadComercial;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.Actividades;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.DDJJCarteleria;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.DDJJOEP;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.DDJJSV;
import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.TipoOEP;
import ar.com.cognisys.sat.core.modelo.comun.rs.PadronRS;
import ar.com.cognisys.sat.core.modelo.comun.rs.versiones.VersionPadronRS;
import ar.com.cognisys.sat.core.modelo.comun.rs.versiones.VersionPadronRS2018;
import ar.com.cognisys.sat.core.modelo.comun.rs.versiones.VersionPadronRS2019;
import ar.com.cognisys.sat.core.modelo.comun.rs.versiones.VersionPadronRS2020;

public class ResumenView implements Serializable {
	
	private static final long serialVersionUID = 2184864444113661257L;
	private PadronRS padron;
	private VersionPadronRS version;
	private String nombre;
	private Actividades actividades;
	private List<DDJJCarteleria> listaCarteleria;
	private List<DDJJOEP> listaOEP;
	private DDJJSV serviciosVarios;
	private boolean original;
	
	public ResumenView(PadronRS padron, VersionPadronRS version, String nombre) {
		this.setPadron(padron);
		this.setVersion(version);
		this.setNombre(nombre);
		
		this.cargarDatos();
	}
	
	private void cargarDatos() {
		switch (this.getVersion().getAno()) {
			case 2018:	
				VersionPadronRS2018 v2018 = ((VersionPadronRS2018) this.getVersion());
				this.setActividades( v2018.getActividades() );
				this.setListaCarteleria( v2018.getListaCarteleria() );
				this.setListaOEP( v2018.getListaOEP() );
				this.setServiciosVarios( v2018.getServiciosVarios() );
				break;
			
			case 2019:
				VersionPadronRS2019 v2019 = ((VersionPadronRS2019) this.getVersion());
				this.setActividades( v2019.getActividades() );
				this.setListaCarteleria( v2019.getListaCarteleria() );
				this.setListaOEP( v2019.getListaOEP() );
				this.setServiciosVarios( v2019.getServiciosVarios() );
				break;
			
			case 2020:
				VersionPadronRS2020 v2020 = ((VersionPadronRS2020) this.getVersion());
				this.setActividades( v2020.getActividades() );
				this.setListaCarteleria( v2020.getListaCarteleria() );
				this.setListaOEP( v2020.getListaOEP() );
				this.setServiciosVarios( v2020.getServiciosVarios() );
				break;
				
			default: 
				break;
		}
	}
	
	public boolean tieneActividadPrincipal() {
		return this.getActividades() != null && this.getActividades().getActividadPrincipal() != null;
	}

	public boolean tieneActividadSecundaria() {
		return this.getActividades() != null &&
			   this.getActividades().getOtrasActividades() != null &&
			   !this.getActividades().getOtrasActividades().isEmpty();
	}

	public boolean tieneCartelerias() {
		return this.getListaCarteleria() != null && !this.getListaCarteleria().isEmpty();
	}
	
	public boolean tieneOep() {
		return this.getListaOEP() != null && !this.getListaOEP().isEmpty();
	}
	
	public boolean tieneSV() {
		return this.getServiciosVarios() != null && 
			  (this.getServiciosVarios().getCalderas() != null || this.getServiciosVarios().getMotores() != null);
	}
	
	public String actividadesSecundarias() {
		String resultado = "";
		
		for (ActividadComercial a : this.getActividades().getOtrasActividades()) {
			resultado += resultado.isEmpty()? "":", ";
			if( a.getNombre().lastIndexOf(".") > 0)
				resultado +=  a.getNombre().substring(0, a.getNombre().lastIndexOf("."));
			else resultado += a.getNombre(); 
		}
		
		return resultado;
	}
	
	public String obtenerValor( DDJJOEP oep ) {
		return oep.getTipo().equals( TipoOEP.POSTE ) ? obtenerCantidadPostes( oep.getValor() ): oep.getValor() + "Mts.";
	}
	
	private String obtenerCantidadPostes(float valor) {
		return String.valueOf( valor ).substring(0, String.valueOf(valor).indexOf(".")  );
	}
	
	public int getAnoFacturacion() {
		return (this.getVersion() == null ? 0 : this.getVersion().getAno() - 1);
	}
	
	public String getPadronABL() {
		if (this.getVersion() == null)
			return "";
		
		String c = this.getVersion().getCuentaABL();
		
		if (c.contains("."))
			return c.substring(0, c.indexOf("."));
		else
			return c;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actividades == null) ? 0 : actividades.hashCode());
		result = prime * result + ((listaCarteleria == null) ? 0 : listaCarteleria.hashCode());
		result = prime * result + ((listaOEP == null) ? 0 : listaOEP.hashCode());
		result = prime * result + (original ? 1231 : 1237);
		result = prime * result + ((padron == null) ? 0 : padron.hashCode());
		result = prime * result + ((serviciosVarios == null) ? 0 : serviciosVarios.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResumenView other = (ResumenView) obj;
		if (actividades == null) {
			if (other.actividades != null)
				return false;
		} else if (!actividades.equals(other.actividades))
			return false;
		if (listaCarteleria == null) {
			if (other.listaCarteleria != null)
				return false;
		} else if (!listaCarteleria.equals(other.listaCarteleria))
			return false;
		if (listaOEP == null) {
			if (other.listaOEP != null)
				return false;
		} else if (!listaOEP.equals(other.listaOEP))
			return false;
		if (original != other.original)
			return false;
		if (padron == null) {
			if (other.padron != null)
				return false;
		} else if (!padron.equals(other.padron))
			return false;
		if (serviciosVarios == null) {
			if (other.serviciosVarios != null)
				return false;
		} else if (!serviciosVarios.equals(other.serviciosVarios))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	public PadronRS getPadron() {
		return padron;
	}
	
	public void setPadron(PadronRS padron) {
		this.padron = padron;
	}
	
	public VersionPadronRS getVersion() {
		return version;
	}
	
	public void setVersion(VersionPadronRS version) {
		this.version = version;
	}
	
	public Actividades getActividades() {
		return actividades;
	}
	
	public void setActividades(Actividades actividades) {
		this.actividades = actividades;
	}
	
	public List<DDJJCarteleria> getListaCarteleria() {
		return listaCarteleria;
	}
	
	public void setListaCarteleria(List<DDJJCarteleria> listaCarteleria) {
		this.listaCarteleria = listaCarteleria;
	}
	
	public List<DDJJOEP> getListaOEP() {
		return listaOEP;
	}
	
	public void setListaOEP(List<DDJJOEP> listaOEP) {
		this.listaOEP = listaOEP;
	}
	
	public DDJJSV getServiciosVarios() {
		return serviciosVarios;
	}
	
	public void setServiciosVarios(DDJJSV serviciosVarios) {
		this.serviciosVarios = serviciosVarios;
	}
	
	public boolean isOriginal() {
		return original;
	}
	
	public void setOriginal(boolean original) {
		this.original = original;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}