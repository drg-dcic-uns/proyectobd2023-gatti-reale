package parquimetros.modelo.inspector.dao;

import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.modelo.inspector.exception.AutomovilNoEncontradoException;
import parquimetros.utils.Mensajes;

public class DAOAutomovilImpl implements DAOAutomovil {

	private static Logger logger = LoggerFactory.getLogger(DAOAutomovilImpl.class);
	
	private Connection conexion;
	
	public DAOAutomovilImpl(Connection conexion) {
		this.conexion = conexion;
	}

	@Override
	public void verificarPatente(String patente) throws AutomovilNoEncontradoException, Exception {
		/** 
		 * TODO Debe verificar que exista la patente en la tabla automoviles. 
		 * 		Deberá generar una excepción AutomovilNoEncontradoException en caso de no encontrarlo. 
		 *      Si hay algún error en la consulta o en la conexión deberá propagar la excepción.    
		 *       
		 *      Importante: Para acceder a la B.D. utilice la propiedad this.conexion (de clase Connection) 
		 *      que se inicializa en el constructor.     
		 */
		//Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.
		//
		// Diseño de datos de prueba: Las patentes que terminan en 
		//  * 1 al 8 retorna exitosamente
		//  * 9 produce una excepción de AutomovilNoEncontradoException
		//  * 0 propaga la excepción recibida (produce una Exception)
 		// 
	
		int ultimo = Integer.parseInt(patente.substring(patente.length()-1));
		
		if (ultimo == 0) {
			throw new Exception("Hubo un error en la conexión.");
		} else if (ultimo == 9) {
			throw new AutomovilNoEncontradoException(Mensajes.getMessage("DAOAutomovilImpl.recuperarAutomovilPorPatente.AutomovilNoEncontradoException"));			
		} 
		// Fin datos estáticos de prueba.		
	}

}
