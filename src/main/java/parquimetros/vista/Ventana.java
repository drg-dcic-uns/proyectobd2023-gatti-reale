package parquimetros.vista;

import parquimetros.controlador.Controlador;

public interface Ventana {

	/**
	 * Permite informarle a la ventana donde está el controlador
	 * 
	 * @param c Controlador
	 */
	public void registrarControlador(Controlador c);

	/**
	 * Muestra una ventana previamente creada
	 * 
	 * @throws Exception si la ventana no existe.
	 */
	public void mostrarVentana() throws Exception; 
	
	/**
	 * Informa a la vista que debe eliminar la ventana.
	 * 
	 * La eliminación es explícita. 
	 */
	public void eliminarVentana();
	
	/**
	 * Informa a la vista que debe mostrar al usuario un mensaje que es pasado como parámetro.
	 * 
	 * @param mensaje
	 */
	public void informar(String mensaje);
	
}
