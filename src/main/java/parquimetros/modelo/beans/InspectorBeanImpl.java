package parquimetros.modelo.beans;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//CLASE IMPLEMENTADA PROVISTA POR LA CATEDRA
//Representa como un objeto una tupla de la tabla "inspectores"
public class InspectorBeanImpl implements Serializable, InspectorBean {

	private static Logger logger = LoggerFactory.getLogger(InspectorBeanImpl.class);
	
	private static final long serialVersionUID = 1L;

	private int legajo;
	private int dni;
	private String apellido;
	private String nombre;
	private String password;
	
	@Override
	public int getLegajo() {
		return legajo;
	}

	@Override
	public void setLegajo(int legajo) {
		this.legajo =  legajo;		
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
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;		
	}

	@Override
	public int getDNI() {
		return this.dni;
	}

	@Override
	public void setDNI(int dni) {
		this.dni=dni;		
	}
}
