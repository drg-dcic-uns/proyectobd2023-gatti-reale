package parquimetros.modelo.inspector.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.modelo.beans.InspectorBean;
import parquimetros.modelo.beans.InspectorBeanImpl;
import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.UbicacionBean;
import parquimetros.modelo.inspector.dao.datosprueba.DAOParquimetrosDatosPrueba;

public class DAOParquimetroImpl implements DAOParquimetro {

	private static Logger logger = LoggerFactory.getLogger(DAOParquimetroImpl.class);
	
	private Connection conexion;
	
	public DAOParquimetroImpl(Connection c) {
		this.conexion = c;
	}

	@Override
	public UbicacionBean recuperarUbicacion(ParquimetroBean parquimetro) throws Exception {
		/**
		 * TODO Recuperar  de la B.D. la ubicación de un parquimetro a patir de su ID
		 * 
		 *      Importante: Para acceder a la B.D. utilice la propiedad this.conexion (de clase Connection) 
		 *      que se inicializa en el constructor.   
		 */		

		//Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.
		UbicacionBean ubicacion = DAOParquimetrosDatosPrueba.obtenerUbicacion(parquimetro.getId());
		
		return ubicacion;
		//Fin datos de prueba 
		
	}



}
