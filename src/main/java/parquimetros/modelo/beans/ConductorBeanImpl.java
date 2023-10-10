package parquimetros.modelo.beans;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//CLASE IMPLEMENTADA PROVISTA POR LA CATEDRA
//Representa como un objeto una tupla de la tabla "pasajeros"
public class ConductorBeanImpl implements Serializable, ConductorBean  {

	private static Logger logger = LoggerFactory.getLogger(ConductorBeanImpl.class);
	
	private static final long serialVersionUID = 1L;

	private int nroDocumento;
	private int registro;
	private String apellido;
	private String nombre;
	private String direccion;	
	private String telefono;
	
	@Override
	public String toString() {
		return "PasajeroBeanImpl [nroDocumento=" + nroDocumento + ", apellido=" + apellido + ", "
				+ "nombre=" + nombre + ", direccion=" + direccion + ", telefono=" + telefono
				+ ", registro=" + registro + "]";
	}
	
	@Override
	public int getNroDocumento() {
		return nroDocumento;
	}
	
	@Override
	public void setNroDocumento(int nroDocumento) {
		this.nroDocumento = nroDocumento;
	}
	
	@Override
	public int getRegistro() {
		return registro;
	}
	
	@Override
	public void setRegistro(int registro) {
		this.registro = registro;
	}

	@Override
	public String getApellido() {
		return apellido;
	}
	
	@Override
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	@Override
	public String getNombre() {
		return nombre;
	}
	
	@Override
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Override
	public String getDireccion() {
		return direccion;
	}
	
	@Override
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	@Override
	public String getTelefono() {
		return telefono;
	}
	
	@Override
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
		
}
