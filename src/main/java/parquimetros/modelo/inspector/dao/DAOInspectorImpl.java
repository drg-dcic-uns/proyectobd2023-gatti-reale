package parquimetros.modelo.inspector.dao;

import java.sql.Connection;
import java.sql.Statement;

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

		Statement statement = this.conexion.createStatement();
		String sql = "SELECT * FROM inspectores WHERE legajo = " + legajo + " AND password = md5('" + password + "')";
		java.sql.ResultSet rs = statement.executeQuery(sql);
		InspectorBean inspectorAutenticado = new InspectorBeanImpl();
		rs.next();
		if (rs != null){
			inspectorAutenticado.setLegajo(Integer.parseInt(rs.getString("legajo")));
			inspectorAutenticado.setApellido(rs.getString("apellido"));
			inspectorAutenticado.setNombre(rs.getString("nombre"));
			inspectorAutenticado.setDNI(Integer.parseInt(rs.getString("dni")));
			inspectorAutenticado.setPassword(rs.getString("password"));
		}
		else throw new InspectorNoAutenticadoException(Mensajes.getMessage("DAOInspectorImpl.autenticar.inspectorNoAutenticado"));

		return inspectorAutenticado;

	}	


}
