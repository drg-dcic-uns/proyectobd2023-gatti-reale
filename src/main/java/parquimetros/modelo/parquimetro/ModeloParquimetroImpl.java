package parquimetros.modelo.parquimetro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.modelo.ModeloImpl;
import parquimetros.modelo.beans.*;
import parquimetros.modelo.inspector.dao.datosprueba.DAOParquimetrosDatosPrueba;
import parquimetros.modelo.inspector.dao.datosprueba.DAOUbicacionesDatosPrueba;
import parquimetros.modelo.parquimetro.dao.datosprueba.DAOTarjetasDatosPrueba;
import parquimetros.modelo.parquimetro.dto.EntradaEstacionamientoDTOImpl;
import parquimetros.modelo.parquimetro.dto.EstacionamientoDTO;
import parquimetros.modelo.parquimetro.dto.SalidaEstacionamientoDTOImpl;
import parquimetros.modelo.parquimetro.exception.ParquimetroNoExisteException;
import parquimetros.modelo.parquimetro.exception.SinSaldoSuficienteException;
import parquimetros.modelo.parquimetro.exception.TarjetaNoExisteException;
import parquimetros.utils.Mensajes;

public class ModeloParquimetroImpl extends ModeloImpl implements ModeloParquimetro {

	private static Logger logger = LoggerFactory.getLogger(ModeloParquimetroImpl.class);
	
	@Override
	public ArrayList<TarjetaBean> recuperarTarjetas() throws Exception {
		logger.info(Mensajes.getMessage("ModeloParquimetroImpl.recuperarTarjetas.logger"));
		/**
		 * TODO Debe retornar una lista de UbicacionesBean con todas las tarjetas almacenadas en la B.D. 
		 *      Deberia propagar una excepción si hay algún error en la consulta.
		 *
		 *      Importante: Para acceder a la B.D. utilice la propiedad this.conexion (de clase Connection) 
		 *      que se hereda al extender la clase ModeloImpl. 
		 */

		ArrayList<TarjetaBean> tarjetas = new ArrayList<TarjetaBean>();

		String sql = "SELECT * FROM tarjetas";
		String patente, tipo;
		int id_tarjeta;
		double saldo;
		String sqlTemp;
		try (PreparedStatement statement = this.conexion.prepareStatement(sql)) {
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				patente = rs.getString("patente");
				id_tarjeta = rs.getInt("id_tarjeta");
				saldo = rs.getDouble("saldo");
				tipo = rs.getString("tipo");

				sqlTemp = "SELECT * FROM Automoviles where patente = " + patente + "";
				PreparedStatement statement1 = this.conexion.prepareStatement(sqlTemp);
				ResultSet rs1 = statement1.executeQuery();
				int dni = rs1.getInt("dni");
				AutomovilBean a = new AutomovilBeanImpl();
				if (rs.next()) {

					sqlTemp = "SELECT * FROM Conductores where dni = " + dni + "";
					PreparedStatement statement2 = this.conexion.prepareStatement(sqlTemp);
					ResultSet rs2 = statement1.executeQuery();
					ConductorBean c = new ConductorBeanImpl();
					if (rs2.next()) {
						c.setApellido(rs2.getString("apellido"));
						c.setDireccion(rs2.getString("direccion"));
						c.setNombre(rs2.getString("nombre"));
						c.setRegistro(rs2.getInt("registro"));
						c.setNroDocumento(rs2.getInt("dni"));
						c.setTelefono(rs2.getString("telefono"));
					}

					a.setModelo(rs1.getString("modelo"));
					a.setColor(rs1.getString("color"));
					a.setMarca(rs1.getString("marca"));
					a.setPatente(patente);
					a.setConductor(c);
				}

				sqlTemp = "SELECT * FROM tipos_tarjeta where tipo = " + tipo + "";
				PreparedStatement statement3 = this.conexion.prepareStatement(sqlTemp);
				ResultSet rs3 = statement3.executeQuery();
				TipoTarjetaBean tipoTarjeta = new TipoTarjetaBeanImpl();
				if (rs3.next()) {
					tipoTarjeta.setTipo(rs3.getString("tipo"));
					tipoTarjeta.setDescuento(rs3.getDouble("descuento"));
				}

				TarjetaBeanImpl t = new TarjetaBeanImpl();
				t.setId(id_tarjeta);
				t.setSaldo(saldo);
				t.setTipoTarjeta(tipoTarjeta);
				t.setAutomovil(a);
				tarjetas.add(t);
			}

		} catch (SQLException e) {
			// Manejar la excepción, por ejemplo, imprimir el stack trace
			e.printStackTrace();
		}
		return tarjetas;
	}

		/*
		// Datos estáticos de prueba. Quitar y reemplazar por código que recupera las ubicaciones de la B.D. en una lista de UbicacionesBean		 
		DAOTarjetasDatosPrueba.poblar();
		
		for (TarjetaBean ubicacion : DAOTarjetasDatosPrueba.datos.values()) {
			tarjetas.add(ubicacion);	
		}
		// Fin datos estáticos de prueba.
	
		return tarjetas;
	}
	*/
	/*
	 * Atención: Este codigo de recuperarUbicaciones (como el de recuperarParquimetros) es igual en el modeloParquimetro 
	 *           y en modeloInspector. Se podría haber unificado en un DAO compartido. Pero se optó por dejarlo duplicado
	 *           porque tienen diferentes permisos ambos usuarios y quizas uno estaría tentado a seguir agregando metodos
	 *           que van a resultar disponibles para ambos cuando los permisos de la BD no lo permiten.
	 */
	@Override
	public ArrayList<UbicacionBean> recuperarUbicaciones() throws Exception {
		
		logger.info(Mensajes.getMessage("ModeloParquimetroImpl.recuperarUbicaciones.logger"));
		
		/** 
		 * TODO Debe retornar una lista de UbicacionesBean con todas las ubicaciones almacenadas en la B.D. 
		 *      Deberia propagar una excepción si hay algún error en la consulta.
		 *      
		 *      Importante: Para acceder a la B.D. utilice la propiedad this.conexion (de clase Connection) 
		 *      que se hereda al extender la clase ModeloImpl. 
		 */
		ArrayList<UbicacionBean> ubicaciones = new ArrayList<UbicacionBean>();

		// Datos estáticos de prueba. Quitar y reemplazar por código que recupera las ubicaciones de la B.D. en una lista de UbicacionesBean		 
		DAOUbicacionesDatosPrueba.poblar();
		
		for (UbicacionBean ubicacion : DAOUbicacionesDatosPrueba.datos.values()) {
			ubicaciones.add(ubicacion);	
		}
		// Fin datos estáticos de prueba.
	
		return ubicaciones;
	}

	@Override
	public ArrayList<ParquimetroBean> recuperarParquimetros(UbicacionBean ubicacion) throws Exception {
		logger.info(Mensajes.getMessage("ModeloParquimetroImpl.recuperarParquimetros.logger"));
		
		/** 
		 * TODO Debe retornar una lista de ParquimetroBean con todos los parquimetros que corresponden a una ubicación.
		 * 		 
		 *      Debería propagar una excepción si hay algún error en la consulta.
		 *      
		 *      Importante: Para acceder a la B.D. utilice la propiedad this.conexion (de clase Connection) 
		 *      que se hereda al extender la clase ModeloImpl. 
		 */

		ArrayList<ParquimetroBean> parquimetros = new ArrayList<ParquimetroBean>();

		// datos de prueba
		DAOParquimetrosDatosPrueba.poblar(ubicacion);
		
		for (ParquimetroBean parquimetro : DAOParquimetrosDatosPrueba.datos.values()) {
			parquimetros.add(parquimetro);	
		}
		// Fin datos estáticos de prueba.
	
		return parquimetros;
	}

	@Override
	public EstacionamientoDTO conectarParquimetro(ParquimetroBean parquimetro, TarjetaBean tarjeta)
			throws SinSaldoSuficienteException, ParquimetroNoExisteException, TarjetaNoExisteException, Exception {

		logger.info(Mensajes.getMessage("ModeloParquimetroImpl.conectarParquimetro.logger"),parquimetro.getId(),tarjeta.getId());
		Statement statement = this.conexion.createStatement();
		String sql = "SELECT * FROM tarjetas WHERE id_tarjeta = " + tarjeta.getId();
		java.sql.ResultSet rs = statement.executeQuery(sql);

		while(rs.next()){

		}
		/**
		 * TODO Invoca al stored procedure conectar(...) que se encarga de realizar en una transacción la apertura o cierre 
		 *      de estacionamiento segun corresponda.
		 *      
		 *      Segun la infromacion devuelta por el stored procedure se retorna un objeto EstacionamientoDTO o
		 *      dependiendo del error se produce la excepción correspondiente:
		 *       SinSaldoSuficienteException, ParquimetroNoExisteException, TarjetaNoExisteException     
		 *  
		 */
		




		String fechaAhora = null;
		String horaAhora = null;
		String antes = null;
		String timeFormatter = null;
		SalidaEstacionamientoDTOImpl estacionamiento = new SalidaEstacionamientoDTOImpl("01:30:00", // tiempoTranscurrido
				"-85", // saldoTarjeta
				fechaAhora, // fechaEntrada
				antes.format(timeFormatter), // horaEntrada
				fechaAhora, // fechaSalida
				horaAhora); // horaSalida



		return estacionamiento;
		//Fin datos estáticos de prueba

	}

}
