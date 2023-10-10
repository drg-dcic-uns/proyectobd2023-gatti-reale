package parquimetros.modelo.inspector.dao;

import parquimetros.modelo.beans.InspectorBean;
import parquimetros.modelo.inspector.exception.InspectorNoAutenticadoException;

public interface DAOInspector {

	/**
	 * Verifica que el usuario con el que intenta realizar el login corresponde a un inspector de la aplicación parquímetros.
	 *  
	 * @param legajo
	 * @param password
	 * @return InspectorBean inspector logueado
	 * @throws InspectorNoAutenticadoException
	 * @throws Exception Produce una excepción si hay algún problema con los parámetros o de conexión.
	 */
	public InspectorBean autenticar(String legajo, String password) throws InspectorNoAutenticadoException, Exception;
		
	
}
