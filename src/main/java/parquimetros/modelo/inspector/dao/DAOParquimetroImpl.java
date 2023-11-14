package parquimetros.modelo.inspector.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.modelo.beans.*;
import parquimetros.modelo.inspector.dao.datosprueba.DAOParquimetrosDatosPrueba;

public class DAOParquimetroImpl implements DAOParquimetro {

	private static Logger logger = LoggerFactory.getLogger(DAOParquimetroImpl.class);
	
	private Connection conexion;
	
	public DAOParquimetroImpl(Connection c) {
		this.conexion = c;
	}

	@Override
	public UbicacionBean recuperarUbicacion(ParquimetroBean parquimetro) throws Exception {


		String sql = "SELECT U.calle, U.altura, U.tarifa FROM Parquimetros P JOIN Ubicaciones U ON P.altura = U.altura " +
				"AND P.calle = U.calle WHERE P.id_parq = ?";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		UbicacionBean ubicacion = new UbicacionBeanImpl();

		try{
			preparedStatement = conexion.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			rs.next();

			ubicacion.setCalle(rs.getString("calle"));
			ubicacion.setAltura(rs.getInt("altura"));
			ubicacion.setTarifa(rs.getDouble("tarifa"));
		}catch (SQLException e) {
			logger.error("Error al recuperar la ubicación del parquimetro", e);
			throw new Exception("Error al recuperar la ubicación del parquimetro");
		} finally {
			if (preparedStatement != null)
				preparedStatement.close();
			if (rs != null)
				rs.close();
		}
		return ubicacion;
	}



}
