package ar.com.cognisys.sat.modelo.validador;

import ar.com.cognisys.sat.core.modelo.comun.consultas.Caracter;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Categoria;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Dato;
import ar.com.cognisys.sat.core.modelo.comun.consultas.FormularioConsulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Subcategoria;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;

public class ValidadorFormularioConsultas {

	public static void validar(FormularioConsulta f, Categoria categoria, Subcategoria subcategoria, Caracter caracter, Dato dato) throws ExcepcionControladaAlerta {
		if(!tieneValor(f.getNombre())){
			throw new ExcepcionControladaAlerta("El nombre no puede estar vacío", null);
		}
		else if(!tieneValor(f.getApellido())){
			throw new ExcepcionControladaAlerta("El apellido no puede estar vacío", null);
		}
		else if(!tieneValor(f.getCorreo())){
			throw new ExcepcionControladaAlerta("El correo no puede estar vacío", null);
		}
		
		else if(!tieneValor(f.getTelefono())){
			throw new ExcepcionControladaAlerta("El telefono no puede estar vacío", null);
		}
		
		else if(!f.getCorreo().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
			throw new ExcepcionControladaAlerta("El correo no tiene el formato correcto", null);
		}
		else if(!tieneValor(f.getCuit())){
			throw new ExcepcionControladaAlerta("El cuit no puede estar vacío", null);
		}
		else if(!CUIT.validar(f.getCuit().replaceAll("-", ""))){
			throw new ExcepcionControladaAlerta("El cuit no es correcto", null);
		}
		
		else if(!tieneValor(categoria)){
			throw new ExcepcionControladaAlerta("Debe seleccionar una categoría", null);
		}
//		else if(!tieneValor(subcategoria)){
//			throw new ExcepcionControladaAlerta("Debe seleccionar una subcategoría", null);
//		}
		else
			if (!categoria.getNombre().equals("Comercio e Industria"))
			if(!tieneValor(caracter)){
			throw new ExcepcionControladaAlerta("Debe seleccionar un caracter", null);
		}

		
		else if(!tieneValor(f.getDato())&&esRequerido(dato)){
			throw new ExcepcionControladaAlerta("El campo "+dato.getNombre()+" no puede estar vacio", null);
		}
		if(tieneValor(f.getDato())){
			if(dato.getExpresionRegular()!=null && !f.getDato().matches(dato.getExpresionRegular())){
				throw new ExcepcionControladaAlerta(dato.getMensajeError(), null);
			}
		}
	}

	private static boolean esRequerido(Dato dato) {
		return dato != null && !dato.isOpcional();
	}

	private static boolean tieneValor(Object objeto) {
		return objeto!=null;
	}

	private static boolean tieneValor(String dato) {
		return !(dato == null || dato.equals(""));
	}
}