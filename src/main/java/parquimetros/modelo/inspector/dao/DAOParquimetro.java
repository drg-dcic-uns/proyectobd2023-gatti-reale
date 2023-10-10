package parquimetros.modelo.inspector.dao;

import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.UbicacionBean;

public interface DAOParquimetro {

	/**
	 * @param parquimetro
	 * @return
	 * @throws Exception
	 */
	public UbicacionBean recuperarUbicacion(ParquimetroBean parquimetro) throws Exception;
		
	
}
