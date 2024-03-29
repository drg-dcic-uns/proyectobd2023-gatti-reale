package parquimetros.modelo.inspector.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.modelo.beans.InspectorBean;
import parquimetros.modelo.beans.InspectorBeanImpl;
import parquimetros.modelo.inspector.exception.InspectorNoAutenticadoException;
import parquimetros.utils.Mensajes;

public class DAOInspectorImpl implements DAOInspector {

	private static Logger logger = LoggerFactory.getLogger(DAOInspectorImpl.class);

	private Connection conexion;

	public DAOInspectorImpl(Connection c) {
		this.conexion = c;
	}

	@Override
	public InspectorBean autenticar(String legajo, String password) throws InspectorNoAutenticadoException, Exception {

		String sql = "SELECT * FROM inspectores WHERE legajo = ? AND password = md5(?)";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		InspectorBean inspectorAutenticado = new InspectorBeanImpl();
		try {
			preparedStatement = conexion.prepareStatement(sql);
			preparedStatement.setString(1, legajo);
			preparedStatement.setString(2, password);
			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				inspectorAutenticado.setLegajo(Integer.parseInt(rs.getString("legajo")));
				inspectorAutenticado.setApellido(rs.getString("apellido"));
				inspectorAutenticado.setNombre(rs.getString("nombre"));
				inspectorAutenticado.setDNI(Integer.parseInt(rs.getString("dni")));
				inspectorAutenticado.setPassword(rs.getString("password"));
			} else
				throw new InspectorNoAutenticadoException(Mensajes.getMessage("DAOInspectorImpl.autenticar.inspectorNoAutenticado"));
			}catch (Exception e) {
				logger.error(Mensajes.getMessage("DAOInspectorImpl.autenticar.error"), e);
				throw new Exception(Mensajes.getMessage("DAOInspectorImpl.autenticar.error"));
		}
		finally {
			if (preparedStatement != null)
				preparedStatement.close();
			if (rs != null)
				rs.close();
		}
		return inspectorAutenticado;
	}
}
