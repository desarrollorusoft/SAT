package ar.com.cognisys.sat.bean.pagina.externo.plataformaconsultas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.primefaces.event.FileUploadEvent;

import ar.com.cognisys.common.jsfbean.abstracto.SeccionGeneralBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteAdministradorFormularioConsultas;
import ar.com.cognisys.sat.core.administrador.AdministradorArchivo;
import ar.com.cognisys.sat.core.modelo.abstracto.ExcepcionControlada;
import ar.com.cognisys.sat.core.modelo.comun.ArchivoConsulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Caracter;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Categoria;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Consulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Dato;
import ar.com.cognisys.sat.core.modelo.comun.consultas.FormularioConsulta;
import ar.com.cognisys.sat.core.modelo.comun.consultas.Subcategoria;
import ar.com.cognisys.sat.core.modelo.comun.consultas.TipoConsulta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError;
import ar.com.cognisys.sat.core.modelo.factory.FactoryArchivoConsulta;
import ar.com.cognisys.sat.core.modelo.factory.consultas.FactoryFormularioConsultas;
import ar.com.cognisys.sat.modelo.enums.TipoDatoConsultas;

@ManagedBean
@SessionScoped
public class FormularioConsultasBean extends SeccionGeneralBean {

    private static final long serialVersionUID = -1574484082954472052L;

    private String correoCategoria;

    private List<Categoria> categorias;
    private List<TipoConsulta> tipoConsultas;

    private Categoria categoriaSeleccionada;
    private Subcategoria subcategoriaSeleccionada;
    private Caracter caracterSeleccionado;
    private Dato datoSeleccionado;
    private List<Caracter> caracteres = new ArrayList<>();

    private FormularioConsulta formulario;

    private ConverterCaracteres converterCaracter;
    private ConverterCategoria converterCategoria;
    /* private ConverterSubcategoria converterSubcategoria; */
    private ConverterTipoConsulta converterTipoConsulta;

    @Override
    protected void inicializacion() {
        try {
            this.setFormulario(FactoryFormularioConsultas.generarInstancia());
            this.caracteres = AsistenteAdministradorFormularioConsultas.obtenerCaracteres();
            this.setCategorias(AsistenteAdministradorFormularioConsultas.obtenerCategorias());
            this.setTipoConsultas(AsistenteAdministradorFormularioConsultas.obtenerTipoConsultas());
            
        	categoriaSeleccionada = null;
            this.seleccionarCategoria();

            this.setConverterCaracter(new ConverterCaracteres());
            this.setConverterCategoria(new ConverterCategoria());  
            /*
             * EN DESUSO this.setConverterSubcategoria(new ConverterSubcategoria());
             */
            this.setConverterTipoConsulta(new ConverterTipoConsulta());
        } catch (ExcepcionControladaError e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cargar() {
        // TODO Auto-generated method stub

    }

    @Override
    public void cargar(Object consulta) {
        Consulta c = (Consulta) consulta;
        this.setFormulario(FactoryFormularioConsultas.generarInstancia(c.getNombre(),
                c.getApellido(),
                c.getCorreo(),
                c.getCuit(),
                c.getTelefono(),
                c.getTelefono2()));

    }

    public boolean isPrivada() {
        String refered = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("referer");
        return (refered != null && refered.contains("/pri"));
    }

    public boolean tieneNombre() {
        String nombre = this.getFormulario().getNombre();
        return nombre != null && !nombre.isEmpty();
    }

    public boolean tieneApellido() {
        String apellido = this.getFormulario().getApellido();
        return apellido != null && !apellido.isEmpty();
    }

    @Override
    public void siguiente() {
    }

    @Override
    public void refrescar() {
    }

    public boolean cuentaSeleccionado() throws ExcepcionControladaAlerta {
        try {
            if ( this.hayCategoriaSeleccionada() && this.existeDatoSeleccionado())
                return this.getDatoSeleccionado().getNombre().toUpperCase().equals(TipoDatoConsultas.cuenta.getDescripcion().toUpperCase());
        } catch (Exception e) {
            throw new ExcepcionControladaAlerta("No se pudo generar el campo 'Cuenta'", e);
        }
        return false;
    }

    public boolean dominioSeleccionado() throws ExcepcionControladaAlerta {
        try {
            if (this.hayCategoriaSeleccionada() && this.existeDatoSeleccionado())
                return this.getDatoSeleccionado().getNombre().toUpperCase().equals(TipoDatoConsultas.dominio.getDescripcion().toUpperCase());
        } catch (Exception e) {
            throw new ExcepcionControladaAlerta("No se pudo generar el campo 'Dominio'", e);
        }
        return false;
    }

    public boolean padronSeleccionado() throws ExcepcionControladaAlerta {
        try {
            if (this.existeDatoSeleccionado())
                return this.getDatoSeleccionado().getNombre().toUpperCase().equals(TipoDatoConsultas.padron.getDescripcion().toUpperCase());
        } catch (Exception e) {
            throw new ExcepcionControladaAlerta("No se pudo generar el campo 'Padron'", e);
        }
        return false;
    }

    public boolean cuitSeleccionado() throws ExcepcionControladaAlerta {
        try {
            if (this.hayCategoriaSeleccionada() && this.existeDatoSeleccionado())
                return this.getDatoSeleccionado().getNombre().toUpperCase().equals(TipoDatoConsultas.cuit.getDescripcion().toUpperCase());
        } catch (Exception e) {
            throw new ExcepcionControladaAlerta("No se pudo generar el campo 'C.U.I.T.'", e);
        }
        return false;
    }

    public boolean reySeleccionado() throws ExcepcionControladaAlerta {
        try {
            if (this.hayCategoriaSeleccionada() && this.existeDatoSeleccionado())
                return this.getDatoSeleccionado().getNombre().toUpperCase().equals(TipoDatoConsultas.rey.getDescripcion().toUpperCase());
        } catch (Exception e) {
            throw new ExcepcionControladaAlerta("No se pudo generar el campo 'REY o Juridicci�n'", e);
        }
        return false;
    }

    public boolean dniSeleccionado() throws ExcepcionControladaAlerta {
        try {
            if (this.hayCategoriaSeleccionada() && this.existeDatoSeleccionado())
                return this.getDatoSeleccionado().getNombre().toUpperCase().equals(TipoDatoConsultas.dni.getDescripcion().toUpperCase());
        } catch (Exception e) {
            throw new ExcepcionControladaAlerta("No se pudo generar el campo 'D.N.I.'", e);
        }
        return false;
    }
    
    public boolean cuentaDominioSeleccionado() throws ExcepcionControladaAlerta {
        try {
            if (this.hayCategoriaSeleccionada() && this.existeDatoSeleccionado())
                return this.getDatoSeleccionado().getNombre().toUpperCase().equals(TipoDatoConsultas.cuentaDominio.getDescripcion().toUpperCase());
        } catch (Exception e) {
            throw new ExcepcionControladaAlerta("No se pudo generar el campo 'Cuenta/Dominio'", e);
        }
        return false;
    }

    public boolean fiscalizacionSeleccionada() throws ExcepcionControladaAlerta {
        try {
            if (this.hayCategoriaSeleccionada() && this.existeDatoSeleccionado())
                return this.getDatoSeleccionado().getNombre().toUpperCase().equals(TipoDatoConsultas.fiscalizacion.getDescripcion().toUpperCase());
        } catch (Exception e) {
            throw new ExcepcionControladaAlerta("No se pudo generar el campo 'fiscalizacion'", e);
        }
        return false;
    }

    // public void seleccionarSubcategoria(){
    // this.setDatoSeleccionado(this.getSubcategoriaSeleccionada().getDato());
    // }

    public void seleccionarCategoria() throws ExcepcionControladaError {
        if (this.getCategoriaSeleccionada() == null) {
            this.setDatoSeleccionado(null);
            this.caracteres = AsistenteAdministradorFormularioConsultas.obtenerCaracteres();
            return;
        }

        this.setDatoSeleccionado(this.getCategoriaSeleccionada().getDato());
        this.caracteres = this.getCategoriaSeleccionada().getCaracteresPosibles();
    }

    private boolean existeDatoSeleccionado() {
        if (this.getDatoSeleccionado() != null)
            return this.getDatoSeleccionado().getNombre() != null;
        return false;
    }

    public void blanquear() {
        this.setDatoSeleccionado(null);
        this.setSubcategoriaSeleccionada(null);
    }

    public boolean hayCategoriaSeleccionada() {
        return this.getCategoriaSeleccionada() != null && !this.getCategoriaSeleccionada().getNombre().equals("Comercio e Industria");
    }

    public boolean haySubcategoriaSeleccionada() {
        return this.getSubcategoriaSeleccionada() != null;
    }


    public void subirArchivos(FileUploadEvent event) throws ExcepcionControlada, IOException {
        ArchivoConsulta f = FactoryArchivoConsulta.generarInstancia(AdministradorArchivo.crearArchivo(event), true);
        this.getFormulario().getArchivos().add(f);
        event.getComponent().setTransient(false);
    }


    public void eliminar(ArchivoConsulta a) {
        this.getFormulario().getArchivos().remove(a);
    }

    public boolean esRequerido() {
        return !this.getDatoSeleccionado().isOpcional();
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<TipoConsulta> getTipoConsultas() {
        return tipoConsultas;
    }

    public void setTipoConsultas(List<TipoConsulta> tipoConsultas) {
        this.tipoConsultas = tipoConsultas;
    }

    public Categoria getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(Categoria categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }

    public Subcategoria getSubcategoriaSeleccionada() {
        return subcategoriaSeleccionada;
    }

    public void setSubcategoriaSeleccionada(Subcategoria subcategoriaSeleccionada) {
        this.subcategoriaSeleccionada = subcategoriaSeleccionada;
    }

    public Caracter getCaracterSeleccionado() {
        return caracterSeleccionado;
    }

    public void setCaracterSeleccionado(Caracter caracterSeleccionado) {
        this.caracterSeleccionado = caracterSeleccionado;
    }

    public Dato getDatoSeleccionado() {
        return datoSeleccionado;
    }

    public void setDatoSeleccionado(Dato datoSeleccionado) {
        this.datoSeleccionado = datoSeleccionado;
    }

    public FormularioConsulta getFormulario() {
        return formulario;
    }

    public void setFormulario(FormularioConsulta formulario) {
        this.formulario = formulario;
    }

    public ConverterCaracteres getConverterCaracter() {
        return converterCaracter;
    }

    public void setConverterCaracter(ConverterCaracteres converterCaracter) {
        this.converterCaracter = converterCaracter;
    }

    public ConverterCategoria getConverterCategoria() {
        return converterCategoria;
    }

    public void setConverterCategoria(ConverterCategoria converterCategoria) {
        this.converterCategoria = converterCategoria;
    }

    public ConverterTipoConsulta getConverterTipoConsulta() {
        return converterTipoConsulta;
    }

    public void setConverterTipoConsulta(ConverterTipoConsulta converterTipoConsulta) {
        this.converterTipoConsulta = converterTipoConsulta;
    }

    private class ConverterCategoria implements Converter {

        @Override
        public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
            for (Categoria p : getCategorias()) {
                if (p.getNombre().contentEquals(value))
                    return p;
            }
            return null;
        }

        @Override
        public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
            if (value instanceof String) {
                return (String) value;
            } else if (value instanceof Categoria) {
                Categoria aux = (Categoria) value;
                for (Categoria p : getCategorias()) {
                    if (p == aux) {
                        return p.getNombre();
                    }
                }
            }

            return "";
        }
    }

    private class ConverterTipoConsulta implements Converter {

        @Override
        public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
            for (TipoConsulta p : getTipoConsultas()) {
                if (p.getNombre().contentEquals(value))
                    return p;
            }
            return null;
        }

        @Override
        public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
            if (value instanceof String) {
                return (String) value;
            } else if (value instanceof TipoConsulta) {
                TipoConsulta aux = (TipoConsulta) value;
                for (TipoConsulta p : getTipoConsultas()) {
                    if (p == aux) {
                        return p.getNombre();
                    }
                }
            }

            return "";
        }
    }

    // private class ConverterSubcategoria implements Converter {
    //
    // @Override
    // public Object getAsObject(FacesContext arg0, UIComponent arg1, String
    // value) {
    // for (Subcategoria p : getCategoriaSeleccionada().getSubcategorias()){
    // if (p.getNombre().contentEquals(value))
    // return p;
    // }
    // return null;
    // }
    //
    // @Override
    // public String getAsString(FacesContext arg0, UIComponent arg1, Object
    // value) {
    // if (value instanceof String) {
    // return (String) value;
    // } else if (value instanceof Subcategoria) {
    // Subcategoria aux = (Subcategoria) value;
    // for (Subcategoria p : getCategoriaSeleccionada().getSubcategorias()) {
    // if (p == aux) {
    // return p.getNombre();
    // }
    // }
    // }
    //
    // return "";
    // }
    // }

    private class ConverterCaracteres implements Converter {

        @Override
        public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
            for (Caracter p : getCategoriaSeleccionada().getCaracteresPosibles()) {
                if (p.getNombre().contentEquals(value))
                    return p;
            }
            return null;
        }

        @Override
        public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
            if (value instanceof String) {
                return (String) value;
            } else if (value instanceof Caracter) {
                Caracter aux = (Caracter) value;
                for (Caracter p : getCategoriaSeleccionada().getCaracteresPosibles()) {
                    if (p == aux) {
                        return p.getNombre();
                    }
                }
            }

            return "";
        }
    }

    public String getCorreoCategoria() {
        return correoCategoria;
    }

    public void setCorreoCategoria(String correoCategoria) {
        this.correoCategoria = correoCategoria;
    }

    public List<Caracter> getCaracteres() {
        return caracteres;
    }

    public void setCaracteres(List<Caracter> caracteres) {
        this.caracteres = caracteres;
    }
}