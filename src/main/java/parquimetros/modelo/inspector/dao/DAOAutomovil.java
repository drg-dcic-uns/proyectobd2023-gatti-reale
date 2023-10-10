package parquimetros.modelo.inspector.dao;

import parquimetros.modelo.inspector.exception.AutomovilNoEncontradoException;

public interface DAOAutomovil {

	public void verificarPatente(String patente) throws AutomovilNoEncontradoException, Exception;

}