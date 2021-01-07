package ar.com.cognisys.sat.bean.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import ar.com.cognisys.sat.core.modelo.comun.cuenta.ddjj.TipoCartel;

public class TipoCartelConverter implements Converter{
	
		private List<TipoCartel> listaTipoCartel;

	    public TipoCartelConverter(List<TipoCartel> listaTipoCartel) {
	        super();
	        this.listaTipoCartel = listaTipoCartel;
	    }

	    @Override
	    public Object getAsObject(FacesContext context, UIComponent component, String value) {
	        for ( TipoCartel a : this.listaTipoCartel )
	            if ( a.getNombre().equals( value ) )
	                return a;

	        return null;
	    }

	    @Override
	    public String getAsString(FacesContext context, UIComponent component, Object value) {
	        if ( value instanceof String )
	            return ( String ) value;

	        if ( value instanceof TipoCartel )
	            return ( ( TipoCartel ) value ).getNombre();

	        return null;
	        
	    }
}
