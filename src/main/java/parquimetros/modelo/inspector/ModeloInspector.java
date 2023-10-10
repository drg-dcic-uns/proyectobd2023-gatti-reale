package parquimetros.modelo.inspector;

import java.util.ArrayList;

import parquimetros.modelo.Modelo;
import parquimetros.modelo.beans.InspectorBean;
import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.UbicacionBean;
import parquimetros.modelo.inspector.dto.EstacionamientoPatenteDTO;
import parquimetros.modelo.inspector.dto.MultaPatenteDTO;
import parquimetros.modelo.inspector.exception.AutomovilNoEncontradoException;
import parquimetros.modelo.inspector.exception.ConexionParquimetroException;
import parquimetros.modelo.inspector.exception.InspectorNoAutenticadoException;
import parquimetros.modelo.inspector.exception.InspectorNoHabilitadoEnUbicacionException;

public interface ModeloInspector extends Modelo {
	
	/**
	 * Verifica que el usuario con el que intenta realizar el login corresponde a un inspector de la aplicación parquímetros.
	 * Antes de poder autenticar deberá estar conectado a la BD con el usuario Inspector de BD. ver conectar/2
	 * Retorna el inspector para que el controlador pueda llevar registro del inspector logueado y que el modelo pueda atender
	 * diferentes controladores.
	 *  
	 * @param legajo
	 * @param password
	 * @return InspectorBean inspector logueado
	 * @throws InspectorNoAutenticadoException
	 * @throws Exception Produce una excepción si hay algún problema con los parámetros o de conexión.
	 */
	public InspectorBean autenticar(String legajo, String password) throws InspectorNoAutenticadoException, Exception;
	
	/**
	 * Recupera una lista de ubicaciones
	 * 
	 * @return ArrayList<UbicacionBean>
	 */
	public ArrayList<UbicacionBean> recuperarUbicaciones() throws Exception;

	/**
	 * Recupera una lista de parquimetros presentes en una ubicacion
	 * 
	 * @return ArrayList<ParquimetroBean>
	 */
	public ArrayList<ParquimetroBean> recuperarParquimetros(UbicacionBean ubicacion) throws Exception;
	
	/**
	 * Intenta realizar una conexión al parquimetro con el inspector que se encuentra logueado. 
	 * En caso exitoso se registra en la tabla ACCEDE su acceso y retorna exitosamente.
	 * 
	 * En caso de falla se produce una excepción de ConexionParquimetroException si por ejemplo el inspector no 
	 * esta habilitado a acceder a ese parquimetro en este momento.
	 *  
	 * O se produce una Exception genérica si es un error no controlado.
	 * 
	 * @param parquimetro
	 * @param inspectorLogueado
	 * @throws ConexionParquimetroException
	 * @throws Exception
	 */
	public void conectarParquimetro(ParquimetroBean parquimetro, InspectorBean inspectorLogueado) throws ConexionParquimetroException, Exception;	
	
	/**
	 * Recibe un parquimetro y busca la ubicación en la que se encuentra. 
	 * Busca en la BD la ubicación y actualiza el valor de la ubicación 
	 * que tenia el objeto (ya sea que fuera null o no).
	 * 
	 * @param parquimetro 
	 * @return UbicacionBean con los datos donde se encuentra el parquimetro
	 * @throws Exception si se produce algún error de conexión con la BD. Si no encuentra la ubicación también retorna
	 * una exception ya que es un error grave que no debería ocurrir.
	 */
	public UbicacionBean recuperarUbicacion(ParquimetroBean parquimetro) throws Exception;
	
	/**
	 * Verifica que la patente recibida se encuentra en la tabla de automoviles
	 * 
	 * @param patente
	 * @throws AutomovilNoEncontradoException Si no encuentra la patente
	 * @throws Exception si se produce algun error en la conexion
	 */
	public void verificarPatente(String patente) throws AutomovilNoEncontradoException, Exception;

	/**
	 * Verifica si existe un estacionamiento registrado para dicha patente en la ubicación, y
	 * en caso exitoso retorna con estado Registrado, caso contrario sale con estado No Registrado.
	 * 
	 * @param patente
	 * @param ubicacion
	 * @return EstacionamientoPatenteDTO registro con datos del estacionamiento. Si es No Registrado la fecha y hora de entrada salen con vacio "".
	 */
	public EstacionamientoPatenteDTO recuperarEstacionamiento(String patente, UbicacionBean ubicacion) throws Exception;

	
	/**
	 * Genera una multa para la patente en la ubicación a la fecha y hora actual y con inspectorLogueado.
	 * Se asume que ya se chequeo que tenia uno estacionamiento no registrado, es decir, no tenia un estacionamiento abierto
	 * para dicha ubicación.
	 * 
	 * @param ArrayList<String> listaPatentes lista de patentes estacionadas en la ubicación, algunas con estacionamiento habilitado y otras quizas no.
	 * @param ubicacion
	 * @param inspectorLogueado
	 * @return ArrayList<MultaPatenteDTO> multas realizadas  
	 * @throws InspectorNoHabilitadoEnUbicacionException Si el inspector no está habilitado a realizar una multa en esa ubicación en esa fecha y hora
	 * @throws Exception Cuando ocurren situaciones no controladas
	 */
	public ArrayList<MultaPatenteDTO> generarMultas(ArrayList<String> listaPatentes, 
													UbicacionBean ubicacion, 
													InspectorBean inspectorLogueado) throws InspectorNoHabilitadoEnUbicacionException, Exception;

}
