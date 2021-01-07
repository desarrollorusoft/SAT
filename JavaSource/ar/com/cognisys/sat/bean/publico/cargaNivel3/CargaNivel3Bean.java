package ar.com.cognisys.sat.bean.publico.cargaNivel3;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.cognisys.common.exception.ExcepcionControladaError;
import ar.com.cognisys.common.jsfbean.abstracto.PaginaBean;
import ar.com.cognisys.generico.modelo.comun.AsistenteObjeto;
import ar.com.cognisys.sat.bean.ComunBean;
import ar.com.cognisys.sat.bean.asistente.AsistenteCargaNivel3Bean;
import ar.com.cognisys.sat.bean.asistente.AsistenteRegistracion;
import ar.com.cognisys.sat.core.administrador.AdministradorCuenta;
import ar.com.cognisys.sat.core.administrador.AdministradorUsuario;
import ar.com.cognisys.sat.core.modelo.abstracto.Cuenta;
import ar.com.cognisys.sat.core.modelo.enums.TiposCuentas;
import ar.com.cognisys.sat.core.modelo.enums.TiposDocumento;
import ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaAlerta;
import ar.com.cognisys.sat.core.modelo.validador.CUIT;
import ar.com.cognisys.sat.core.modelo.validador.ValidadorContribuyente;
import ar.com.cognisys.sat.modelo.comun.Mensaje;

@ManagedBean(name = "CargaNivel3")
@ViewScoped
public class CargaNivel3Bean extends PaginaBean {
	
	private static final long serialVersionUID = -4663522239689947362L;
	private String cuit;
	private String correo;
	private String nombre;
	private String apellido;
	private String telefono;
	private String celular;
	private TiposCuentas tipoCuenta;
	private String padron;
	private Cuenta cuentaBuscada;
	private String tipoDocumento;
    private String nroDocumento;
    private List<Cuenta> listaCuentasBuscadas;
	private List<Cuenta> listaCuentasAsignadas;
	
	@Override
	protected void inicializacion() {
		try {
			this.refrescar();
		} catch (ExcepcionControladaError e) {
			Mensaje.emitirMensajeError(e.getMessage());
		}
	}
	
	@Override
	public void refrescar() throws ExcepcionControladaError {
		this.setApellido(null);
		this.setCelular(null);
		this.setCorreo(null);
		this.setCuit(null);
		this.setNombre(null);
		this.setTelefono(null);
		this.setTipoDocumento(null);
		this.setNroDocumento(null);
		this.setPadron(null);
		this.setListaCuentasAsignadas(new ArrayList<Cuenta>());
		this.setListaCuentasBuscadas(new ArrayList<Cuenta>());
		this.setTipoCuenta(TiposCuentas.ABL);
	}
	
	public void confirmar() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		this.validarCompleto();
		
		try {
			AsistenteCargaNivel3Bean.registrar(this.getNombre(), 
											   this.getApellido(), 
											   ComunBean.sacarMascaraCuit( this.getCuit() ), 
											   this.getTipoDocumento(), 
											   this.getNroDocumento(),
											   this.getCorreo(),
											   this.getTelefono(),
											   this.getCelular(),
											   this.getListaCuentasAsignadas());
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e.getMessage(), e);
		}
		
		this.refrescar();
		Mensaje.emitirMensajeAviso("Se cargaron los datos correctamente");
	}
	
	public void blanquear() throws ExcepcionControladaError {
		this.refrescar();
	} 
	
	private void validarCompleto() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		
		if( !AsistenteObjeto.tieneContenido( this.getCuit() ) || !CUIT.validar( ComunBean.sacarMascaraCuit( this.getCuit() ) ) )
			throw new ExcepcionControladaAlerta( "El CUIT no es correcto" );
		if( !AsistenteObjeto.tieneContenido( this.getCorreo() ) || !ValidadorContribuyente.esCorreoValido( this.getCorreo() ) )
			throw new ExcepcionControladaAlerta( "El correo no es valido" );
		
		if( !AsistenteObjeto.tieneContenido( this.getNombre() ) )
			throw new ExcepcionControladaAlerta( "El nombre no es valido" );
		if( !AsistenteObjeto.tieneContenido( this.getApellido() ) )
			throw new ExcepcionControladaAlerta( "El apellido no es valido" );
		if( !AsistenteObjeto.tieneContenido( this.getNroDocumento() ) )
			throw new ExcepcionControladaAlerta( "El documento no es valido" );
		
		if( this.getListaCuentasAsignadas().isEmpty() )
			throw new ExcepcionControladaAlerta( "Debe ingresar al menos una cuenta" );
		
//		this.validarCuit();
	}

	public void validarCuit() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		
		try {
			if (AdministradorUsuario.existeUsuario( CUIT.quitarMascara(cuit) ))
				throw new ExcepcionControladaAlerta("La CUIL/CUIT ingresada ya se encuentra registrada");
			else if (AdministradorUsuario.existeUsuarioPorCorreo(correo) )
				throw new ExcepcionControladaAlerta("El correo ingresado ya se encuentra registrado");
		} catch (ar.com.cognisys.sat.core.modelo.excepcion.ExcepcionControladaError e) {
			throw new ExcepcionControladaError(e);
		}
		
		Mensaje.emitirMensajeAviso("El CUIT es correcto");
	} 
	
	public void buscarCuenta() throws ExcepcionControladaAlerta, ExcepcionControladaError {
		
		if (this.validarCuenta()) {
			Cuenta cuenta;
			if (!this.getTipoCuenta().sos( "PILETAS" ))
				cuenta = AsistenteRegistracion.buscarCuenta(this.getTipoCuenta(), this.getPadron());
			else
				cuenta = AsistenteRegistracion.buscarCuentaPileta(this.getTipoDocumento(), this.getNroDocumento());

			if( yaTieneALaCuenta( cuenta ) )
				throw new ExcepcionControladaAlerta("La cuenta ya esta agregada");
			else 
				this.getListaCuentasBuscadas().add( cuenta );
		}
	}
	
	private boolean yaTieneALaCuenta(Cuenta cuenta) {
		
		for( Cuenta c : this.getListaCuentasAsignadas() )
			if( c.sos(cuenta) )
				return true;
		
		return false;
	}

	private boolean validarCuenta() throws ExcepcionControladaAlerta {
		try {
			if( (this.padron == null || this.padron.isEmpty()) &&  (this.nroDocumento == null || this.nroDocumento.isEmpty()))
				throw new ExcepcionControladaAlerta( "Debe ingresar todos los datos" );

			if( this.tipoCuenta.sos( "COMERCIOS" ) )
				this.padron = this.sacarMascaraCuit();
			
			AdministradorCuenta.validarCuentaIngresada( this.padron, this.tipoCuenta );
			
			return true;
		} catch ( ExcepcionControladaAlerta e ) {
			Mensaje.emitirMensajeAlerta( e.getMessage() );
			throw e;
		}
	}

	private String sacarMascaraCuit() {
		return this.padron.replaceAll( "-", "" );
	}
	
	public void asignarCuenta(Cuenta cuenta) {
		cuenta.setAceptaBE(true);
		this.getListaCuentasAsignadas().add(cuenta);
		this.getListaCuentasBuscadas().remove(cuenta);
		this.setPadron( null );
	}
	
	public void desvincularCuenta(Cuenta cuenta) {
		this.getListaCuentasBuscadas().add(cuenta);
		this.getListaCuentasAsignadas().remove(cuenta);
	}

	public List<TiposCuentas> getTiposCuenta() {
		
		List<TiposCuentas> lista = new ArrayList<TiposCuentas>();
		lista.add(TiposCuentas.ABL);
		lista.add(TiposCuentas.VEHICULOS);
		lista.add(TiposCuentas.RODADOS);
		
		return lista;
	}
	
	public String getLabelDenominacion() {
		return this.getTipoCuenta().getDocumento();
	}
	
	public boolean muestroCuenta(){
		return this.tipoCuenta.sos( "ABL" ) || this.tipoCuenta.sos( "CEMENTERIO" );
	}
	
	public boolean muestroCuit(){
		return this.tipoCuenta.sos( "COMERCIOS" );
	}

    public boolean muestroTipoDocumento() {
        return this.tipoCuenta.sos( "PILETAS" );
    }

    public boolean muestroNroDocumento() {
        return this.tipoCuenta.sos( "PILETAS" );
    }
	
	public boolean muestroDominio(){
		return !muestroCuit() && !muestroCuenta() && !muestroTipoDocumento() && !muestroNroDocumento();
	}

    public List<String> obtenerTiposDocumento() {
        TiposDocumento[] tipos = TiposDocumento.class.getEnumConstants();
        List<String> tiposStr = new ArrayList<String>();
        for (TiposDocumento tipo : tipos)
            tiposStr.add(tipo.getNombrePiletas());
        return tiposStr;
    }
	
	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public TiposCuentas getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(TiposCuentas tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	public String getPadron() {
		return padron;
	}

	public void setPadron(String padron) {
		this.padron = padron;
	}

	public Cuenta getCuentaBuscada() {
		return cuentaBuscada;
	}

	public void setCuentaBuscada(Cuenta cuentaBuscada) {
		this.cuentaBuscada = cuentaBuscada;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public List<Cuenta> getListaCuentasBuscadas() {
		return listaCuentasBuscadas;
	}

	public void setListaCuentasBuscadas(List<Cuenta> listaCuentasBuscadas) {
		this.listaCuentasBuscadas = listaCuentasBuscadas;
	}

	public List<Cuenta> getListaCuentasAsignadas() {
		return listaCuentasAsignadas;
	}

	public void setListaCuentasAsignadas(List<Cuenta> listaCuentasAsignadas) {
		this.listaCuentasAsignadas = listaCuentasAsignadas;
	}
}