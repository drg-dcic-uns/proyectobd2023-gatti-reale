package parquimetros.vista.parquimetro;

import java.util.ArrayList;

import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.TarjetaBean;
import parquimetros.modelo.beans.UbicacionBean;
import parquimetros.modelo.parquimetro.dto.EntradaEstacionamientoDTO;
import parquimetros.modelo.parquimetro.dto.SalidaEstacionamientoDTO;
import parquimetros.vista.Ventana;

public interface VentanaParquimetro extends Ventana {

	/**
	 * Carga el componente que permite seleccionar una tarjeta
	 * 
	 * @param List<TarjetaBean> tarjetas 
	 */
	public void poblarTarjetas(ArrayList<TarjetaBean> tarjetas);

	/**
	 * Carga el componente que permite seleccionar una calle y altura
	 * 
	 * @param List<UbicacionesBean> ubicaciones  
	 */
	public void poblarUbicaciones(ArrayList<UbicacionBean> ubicaciones);
	
	/**
	 * Carga el componente que permite seleccionar un parquimetro presente en una calle y altura previamente dado.
	 * 
	 * @param List<ParquimetrosBean> parquimetros  
	 */
	public void poblarParquimetros(ArrayList<ParquimetroBean> parquimetros);

	
	
	/**
	 * Muestra en la ventana la información de la tarjeta que se ha conectado. 
	 * 
	 * Debería tener la información además de: Tipo de Tarjeta, Automovil, Conductor
	 * 
	 */
	public void mostrarInformacionTarjeta(TarjetaBean tarjeta);
	
	/**
	 * Muestra en la ventana que se produjo un ingreso al estacionamiento.
	 * 
	 */
	public void mostrarIngresoEstacionamiento(EntradaEstacionamientoDTO entrada);

	/**
	 * Muestra en la ventana la información del parquimetro y la ubicación a la que está conectado el inspector.
	 * 
	 */
	public void mostrarSalidaEstacionamiento(SalidaEstacionamientoDTO salida);

	/**
	 * Limpia la componente donde se va a mostrar los resultados.
	 */
	public void limpiarEstacionamientoResultado();

	

}
