package ar.com.cognisys.sat.bean.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.ActividadComercial;

public class DatoActividadConverter implements Converter{
	
		private List<ActividadComercial> listaActividadComercial;

	    public DatoActividadConverter(List<ActividadComercial> listaActividadComercial) {
	        super();
	        this.listaActividadComercial = listaActividadComercial;
	    }

	    @Override
	    public Object getAsObject(FacesContext context, UIComponent component, String value) {
	        for ( ActividadComercial a : this.listaActividadComercial )
	            if ( a.getNombre().equals( value ) )
	                return a;

	        return null;
	    }

	    @Override
	    public String getAsString(FacesContext context, UIComponent component, Object value) {
	        if ( value instanceof String )
	            return ( String ) value;

	        if ( value instanceof ActividadComercial )
	            return ( ( ActividadComercial ) value ).getNombre();

	        return null;
	        
	    }
}
