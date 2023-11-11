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

		ArrayList<TarjetaBean> tarjetas = new ArrayList<TarjetaBean>();

		String sql = "SELECT * FROM tarjetas";
		String patente, tipo;
		int id_tarjeta;
		double saldo;
		String sqlTemp;
		int dni;
		PreparedStatement statement = null, statement1 = null, statement2 = null, statement3 = null;
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null;
		try  {
			statement = this.conexion.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {

				patente = rs.getString("patente");
				id_tarjeta = rs.getInt("id_tarjeta");
				saldo = rs.getDouble("saldo");
				tipo = rs.getString("tipo");
				System.out.println("patente igual a : " + patente);
				sqlTemp = "SELECT * FROM Automoviles where patente = ?";
				statement1 = this.conexion.prepareStatement(sqlTemp);
				statement1.setString(1, patente);

				rs1 = statement1.executeQuery();

				AutomovilBean a = new AutomovilBeanImpl();
				if (rs1.next()) {
					dni = rs1.getInt("dni");
					sqlTemp = "SELECT * FROM Conductores where dni = ?";

					statement2 = this.conexion.prepareStatement(sqlTemp);
					statement2.setInt(1, dni);


					rs2 = statement2.executeQuery();
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

				sqlTemp = "SELECT * FROM tipos_tarjeta where tipo = ?";
				statement3 = this.conexion.prepareStatement(sqlTemp);
				statement3.setString(1, tipo);
				rs3 = statement3.executeQuery();
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
				System.out.println("llegue hasta aca y numero de tarjetas = " + tarjetas.size());
			}


		} catch (SQLException e) {
			// Manejar la excepción, por ejemplo, imprimir el stack trace
			e.printStackTrace();
		}
		finally {
			try {
				if (rs != null) rs.close();
				if (statement != null) statement.close();
				if (rs1 != null) rs1.close();
				if (statement1 != null) statement1.close();
				if (rs2 != null) rs2.close();
				if (statement2 != null) statement2.close();
				if (rs3 != null) rs3.close();
				if (statement3 != null) statement3.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tarjetas;
	}

	/*
	 * Atención: Este codigo de recuperarUbicaciones (como el de recuperarParquimetros) es igual en el modeloParquimetro 
	 *           y en modeloInspector. Se podría haber unificado en un DAO compartido. Pero se optó por dejarlo duplicado
	 *           porque tienen diferentes permisos ambos usuarios y quizas uno estaría tentado a seguir agregando metodos
	 *           que van a resultar disponibles para ambos cuando los permisos de la BD no lo permiten.
	 */
	@Override
	public ArrayList<UbicacionBean> recuperarUbicaciones() throws Exception {
		logger.info(Mensajes.getMessage("ModeloInspectorImpl.recuperarUbicaciones.logger"));

		ArrayList<UbicacionBean> ubicaciones = new ArrayList<UbicacionBean>();
		Statement statement = this.conexion.createStatement();
		String sql = "SELECT * FROM ubicaciones";
		java.sql.ResultSet rs = statement.executeQuery(sql);
		while (rs.next()) {
			String calle = rs.getString("calle");
			String altura = rs.getString("altura");
			String tarifa = rs.getString("tarifa");
			UbicacionBeanImpl ubi = new UbicacionBeanImpl();
			ubi.setCalle(calle);
			ubi.setAltura(Integer.parseInt(altura));
			ubi.setTarifa(Double.parseDouble(tarifa));
			ubicaciones.add(ubi);
		}
		return ubicaciones;

	}

	@Override
	public ArrayList<ParquimetroBean> recuperarParquimetros(UbicacionBean ubicacion) throws Exception {
		logger.info(Mensajes.getMessage("ModeloParquimetroImpl.recuperarParquimetros.logger"));
		logger.info(Mensajes.getMessage("ModeloInspectorImpl.recuperarParquimetros.logger"),ubicacion.toString());

		String sql = "SELECT * FROM Parquimetros WHERE calle = ? AND altura = ?";
		PreparedStatement preparedStatement = conexion.prepareStatement(sql);
		preparedStatement.setString(1, ubicacion.getCalle());
		preparedStatement.setInt(2, ubicacion.getAltura());

		ResultSet rs = preparedStatement.executeQuery();

		ArrayList<ParquimetroBean> parquimetros = new ArrayList<ParquimetroBean>();
		while (rs.next()) {
			ParquimetroBeanImpl parq = new ParquimetroBeanImpl();
			parq.setUbicacion(ubicacion);
			parq.setId(rs.getInt("id_parq"));
			parq.setNumero(rs.getInt("numero"));
			parquimetros.add(parq);
		}

		return parquimetros;

	}

	@Override
	public EstacionamientoDTO conectarParquimetro(ParquimetroBean parquimetro, TarjetaBean tarjeta)
			throws SinSaldoSuficienteException, ParquimetroNoExisteException, TarjetaNoExisteException, Exception {

		logger.info(Mensajes.getMessage("ModeloParquimetroImpl.conectarParquimetro.logger"),parquimetro.getId(),tarjeta.getId());

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
