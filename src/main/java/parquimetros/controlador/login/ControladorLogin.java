package parquimetros.controlador.login;

import parquimetros.controlador.Controlador;

public interface ControladorLogin extends Controlador {

	public static final String INSPECTOR = "Inspector";     // estos valores son los que se encuentran en usuarios.properties
	public static final String PARQUIMETRO = "Parquimetro"; 
	
	/**
	 * Informa al controlador que se desea ingresar con un usuario con rol INSPECTOR
	 * 
	 * @param username
	 * @param password
	 */
	public void ingresarComoInspector(String legajo, char[] password);
	
	/**
	 * Intenta conectarse a la BD con utilizando el rol Parquimetro
	 * 
	 */
	public void ingresarComoParquimetro();
	
}
