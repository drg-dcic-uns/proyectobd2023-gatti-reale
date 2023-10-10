package parquimetros.controlador.parquimetro;

import parquimetros.controlador.ControladorAppParquimetro;
import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.TarjetaBean;
import parquimetros.modelo.beans.UbicacionBean;

public interface ControladorParquimetro extends ControladorAppParquimetro {

	@Override
	public void cerrarSesion();

	@Override
	public void salirAplicacion();

	@Override
	public void ejecutar();

	/**
	 * Informa al controlador que se seleccionó una nueva tarjeta.
	 * 
	 * @param tarjeta
	 */
	public void cambiarTarjeta(TarjetaBean tarjeta);
	
	/**
	 * Informa al controlador que se seleccionó una nueva ubicación y deberá buscar los parquimetros asociados a 
	 * la misma y actualizar la vista.
	 * 
	 * @param ubicacion
	 */
	public void cambiarUbicacion(UbicacionBean ubicacion);
	
	/**
	 * Le informa al controlador que la tarjeta se desea conectar al parquimetro para realizar el ingreso o salida de estacionamiento.
	 * 
	 * @param parquimetro al que se conecta
	 * @param tarjeta ingresada
	 */
	public void conectarParquimetro(ParquimetroBean parquimetro, TarjetaBean tarjeta);
	

}
