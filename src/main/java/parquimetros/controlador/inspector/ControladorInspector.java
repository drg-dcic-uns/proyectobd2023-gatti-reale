package parquimetros.controlador.inspector;

import java.util.ArrayList;

import parquimetros.controlador.ControladorAppParquimetro;
import parquimetros.modelo.beans.InspectorBean;
import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.UbicacionBean;

public interface ControladorInspector extends ControladorAppParquimetro {

	@Override
	public void cerrarSesion();

	@Override
	public void salirAplicacion();

	@Override
	public void ejecutar();
	
	/**
	 * Informa al controlador que se seleccionó una nueva ubicación y deberá buscar los parquimetros asociados a 
	 * la misma y actualizar la vista.
	 * 
	 * @param ubicacion
	 */
	public void cambiarUbicacion(UbicacionBean ubicacion);
	
	/**
	 * Le informa al controlador que el Inspector logueado desea conectarse al parquimetro.
	 * 
	 * Se registra la conexión si la misma está habilitada o se informa en caso que no sea posible, 
	 * ya sea por algún error de conexión o porque el inspector no esté habilitado en dicha ubicación.
	 * 
	 * @param parquimetro al que se desea conectar
	 */
	public void conectarParquimetro(ParquimetroBean parquimetro);
	

	/**
	 * <p>Le informa al controlador que se desea agregar la patente a la lista de patentes estacionadas en la ubicación actual.</p>
	 * <p>El controlador además controla el estado de dicha patente. Los estados pueden ser Registrado o No Registrado.</p>
	 * <p>Registrado corresponde a un estacionamiento abierto en dicha ubicación (OK), No Registrado corresponde a un estacionamiento
	 * que deberá ser multado.</p>
	 * <br>
	 * <p>Se debe verificar que la patente esté registrada en el sistema antes de a agregarlo al listado. Caso contrario debe informar
	 * que dicha patente no existe para que se verifique el ingreso.</p>
	 * 
	 * @param patente Un String que corresponde a la patente. 
	 */
	public void agregarPatente(String patente);
	
	/**
	 * Le informa al controlador quien se encuentra logueado registrando patentes y multas. El controlador deberá llevar
	 * registro de ello para futuras acciones que haran referencia implícita al inspectorLogueado.
	 * 
	 * @param inspectorLogueado
	 */
	public void registrarInspectorLogueado(InspectorBean inspectorLogueado);
	
	/**
	 * Informa al controlador que el usuario desea quitar de la lista de patentes estacionadas a las 
	 * patentes que se informan en listaPatentesQuitar
	 * 
	 * @param listaPatentesQuitar
	 */
	public void quitarPatentesEstacionadas(ArrayList<String> listaPatentesQuitar);

	/**
	 * Informa al controlador que el usuario desea limpiar la lista de patentes estacionadas (TODAS).
	 */
	public void limpiarListaPatentesEstacionadas();

	/**
	 * Informa al controlador que desea registrar las multas de aquellas patentes que no tengan un estacionamiento válido.
	 * 
	 * @param listaPatentes
	 */
	public void registraMultas(ArrayList<String> listaPatentes);

	


}
