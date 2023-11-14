package parquimetros.modelo;

import java.sql.*;

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

		ResultSet rs = null;

		try {
			Statement statement = this.conexion.createStatement();
			rs = statement.executeQuery(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return rs;
	}	
	
	@Override
	public void actualizacion (String sql) {

		try {
			PreparedStatement statement = this.conexion.prepareStatement(sql);
			statement.executeUpdate();
			statement.close();
		}
		catch (SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLState: " + ex.getSQLState());
			logger.error("VendorError: " + ex.getErrorCode());
		}

	}	
}
