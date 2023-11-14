package parquimetros.modelo.inspector.dao;

import java.sql.*;

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
		PreparedStatement statement = null;
		String sql = "SELECT * FROM automoviles WHERE patente = ?";
		ResultSet rs = null;
		boolean existe = false;
		try {
			statement = this.conexion.prepareStatement(sql);
			statement.setString(1,patente);
			rs = statement.executeQuery();
			if (rs.next()) {
				if (rs.getString("patente").equals(patente)) {
					existe = true;
				}
			}
			if (!existe) {
				throw new AutomovilNoEncontradoException(Mensajes.getMessage("DAOAutomovilImpl.recuperarAutomovilPorPatente.AutomovilNoEncontradoException"));
			}
		}catch (Exception e) {
			logger.error(Mensajes.getMessage("DAOAutomovilImpl.recuperarAutomovilPorPatente.error"), e);
			throw new Exception(Mensajes.getMessage("DAOAutomovilImpl.recuperarAutomovilPorPatente.error"));
		}
		finally {
			if (statement != null)
				statement.close();
			if (rs != null)
				rs.close();
		}

	}


	}


