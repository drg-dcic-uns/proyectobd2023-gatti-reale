package parquimetros.modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.utils.Conexion;

public class ModeloImpl implements Modelo {
	
	private static Logger logger = LoggerFactory.getLogger(ModeloImpl.class);	

	protected Connection conexion = null;

	@Override
	public boolean conectar(String username, String password) {
		logger.info("Se establece la conexión con la BD.");
        this.conexion = Conexion.getConnection(username, password);        
    	return (this.conexion != null);	
	}

	@Override
	public void desconectar() {
		logger.info("Se desconecta la conexión a la BD.");
		Conexion.closeConnection(this.conexion);		
	}

	@Override
	public ResultSet consulta(String sql)	       		
	{
		logger.info("Se intenta realizar la siguiente consulta {}",sql);
		
		/** TODO: ejecutar la consulta sql recibida como parámetro utilizando 
		*         la propiedad conexion y devolver el resultado en un ResulSet
		*/
		/* 
		try
		{ 
		 
		}
		catch (SQLException ex){
		   logger.error("SQLException: " + ex.getMessage());
		   logger.error("SQLState: " + ex.getSQLState());
		   logger.error("VendorError: " + ex.getErrorCode());
		}
		*/
		return null;
	}	
	
	@Override
	public void actualizacion (String sql)
	{  /** TODO: ejecutar la consulta de actualizacion sql recibida como 
 		*       parámetro utilizando la propiedad conexion 
		*/  
		/*
		try
		{ 	
		}
		catch (SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLState: " + ex.getSQLState());
			logger.error("VendorError: " + ex.getErrorCode());
		}
		*/
	}	
}
