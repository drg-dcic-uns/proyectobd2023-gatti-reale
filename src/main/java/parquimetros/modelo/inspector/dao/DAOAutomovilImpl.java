package parquimetros.modelo.inspector.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
		Statement statement = this.conexion.createStatement();
		String sql = "SELECT * FROM automoviles WHERE patente = '" + patente + "'";
		java.sql.ResultSet rs = statement.executeQuery(sql);
		boolean existe = false;
		while (rs.next()) {
			if (rs.getString("patente").equals(patente)){
				existe = true;
				break;
			}
		}
		if (!existe){
			throw new AutomovilNoEncontradoException(Mensajes.getMessage("DAOAutomovilImpl.verificarPatente.automovilNoEncontradoException"));
		}

		/*
		* La recuperacion y la mostrada de datos de si un estacionamiento es abierto
		* esta hecha en ModeloInspectorImpl
		*
		* */
	}


	}


