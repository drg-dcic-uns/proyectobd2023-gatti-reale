package parquimetros.modelo.parquimetro;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import parquimetros.modelo.parquimetro.dto.EstacionamientoDTOImpl;
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
		try {
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
			}


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
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
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
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
		}catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (statement != null) statement.close();
				if (rs != null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ubicaciones;

	}

	@Override
	public ArrayList<ParquimetroBean> recuperarParquimetros(UbicacionBean ubicacion) throws Exception {
		logger.info(Mensajes.getMessage("ModeloParquimetroImpl.recuperarParquimetros.logger"));
		logger.info(Mensajes.getMessage("ModeloInspectorImpl.recuperarParquimetros.logger"), ubicacion.toString());

		String sql = "SELECT * FROM Parquimetros WHERE calle = ? AND altura = ?";
		PreparedStatement preparedStatement = conexion.prepareStatement(sql);
		preparedStatement.setString(1, ubicacion.getCalle());
		preparedStatement.setInt(2, ubicacion.getAltura());
		ArrayList<ParquimetroBean> parquimetros = new ArrayList<ParquimetroBean>();
		ResultSet rs = null;
		try {
			rs = preparedStatement.executeQuery();


			while (rs.next()) {
				ParquimetroBeanImpl parq = new ParquimetroBeanImpl();
				parq.setUbicacion(ubicacion);
				parq.setId(rs.getInt("id_parq"));
				parq.setNumero(rs.getInt("numero"));
				parquimetros.add(parq);
			}
		}
		catch (SQLException e) {
				e.printStackTrace();
			}
		finally {
				try {
					preparedStatement.close();
					if (rs != null) rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		return parquimetros;

	}

	@Override
	public EstacionamientoDTO conectarParquimetro(ParquimetroBean parquimetro, TarjetaBean tarjeta)
			throws SinSaldoSuficienteException, ParquimetroNoExisteException, TarjetaNoExisteException, Exception {

		logger.info(Mensajes.getMessage("ModeloParquimetroImpl.conectarParquimetro.logger"), parquimetro.getId(), tarjeta.getId());

		/**
		 * TODO Invoca al stored procedure conectar(...) que se encarga de realizar en una transacción la apertura o cierre 
		 *      de estacionamiento segun corresponda.
		 *
		 *      Segun la infromacion devuelta por el stored procedure se retorna un objeto EstacionamientoDTO o
		 *      dependiendo del error se produce la excepción correspondiente:
		 *       SinSaldoSuficienteException, ParquimetroNoExisteException, TarjetaNoExisteException     
		 *
		 */

		try {
			String sql1 = "Select hora_ent, fecha_ent from Estacionamientos where id_tarjeta = ? AND fecha_sal IS NULL AND hora_sal IS NULL";
			PreparedStatement pS = conexion.prepareStatement(sql1);
			pS.setInt(1, tarjeta.getId());

			ResultSet rs1 = pS.executeQuery();
			Date fechaApertura = null;
			Time horaApertura2 = null;

			if (rs1.next()) {
				fechaApertura = rs1.getDate("fecha_ent");
				horaApertura2 = rs1.getTime("hora_ent");
				System.out.println(rs1.getTimestamp("hora_ent").toLocalDateTime());

			}
			String sql = "{CALL conectar(?, ?)}";

			ResultSet rs = null;
			try (CallableStatement cs = conexion.prepareCall(sql)) {
				cs.setInt(1, tarjeta.getId());
				cs.setInt(2, parquimetro.getId());
				cs.execute();

				try {
					rs = cs.getResultSet();
					if (rs.next()) {
						String operacionExitosa = rs.getString("resultado");
						String tipoOperacion = rs.getString("operacion");
						Time horaActual = null;
						Date fechaActual = null;

						int tiempoRestante = 0;
						if (tipoOperacion.equals("Apertura") && operacionExitosa.equals("La operacion se realizo con exito")) {
							tiempoRestante = rs.getInt("tiempoRestante");
							String sql2 = "SELECT CURTIME() AS horaActual, CURDATE() AS fechaActual";
							PreparedStatement statement = conexion.prepareStatement(sql2);
							ResultSet fechaHora = statement.executeQuery();
							if (fechaHora.next()) {
								horaActual = fechaHora.getTime("horaActual");
								fechaActual = fechaHora.getDate("fechaActual");
								System.out.println(fechaHora.getTime("horaActual"));
							}

							return new EntradaEstacionamientoDTOImpl("" + tiempoRestante, "" + fechaActual, "" + horaActual);
						} else if (tipoOperacion.equals("Cierre")) {


							Time horaCierre = rs.getTime("horaSalida");
							int tiempoTranscurrido = rs.getInt("tiempoTranscurrido");
							BigDecimal saldoActualizado = rs.getBigDecimal("saldo");

							Date fechaCierre = rs.getDate("Fecha_cierre");

							return new SalidaEstacionamientoDTOImpl("" + tiempoTranscurrido, "" + saldoActualizado, "" + fechaApertura,
									"" + horaApertura2, "" + fechaCierre, "" + horaCierre);
						} else if (operacionExitosa.equals("Saldo de la tarjeta insuficiente") || operacionExitosa.equals("Error tarjeta o parquimetro inexistente")) {
							throw new SinSaldoSuficienteException("Sin Saldo suficiente");
						}
					}
				} catch (SQLException e) {
					// Manejar las excepciones de SQL según sea necesario
					e.printStackTrace();
					throw new Exception("Error al ejecutar el procedimiento almacenado conectar", e);
				}

			} catch (SQLException e) {
				// Manejar las excepciones de SQL según sea necesario
				e.printStackTrace();
				throw new Exception("Error al obtener la conexión a la base de datos", e);
			} finally {
				rs1.close();
				rs.close();
			}

		} catch (SQLException e) {
			// En caso de algún problema inesperado, lanzar una excepción general
			throw new Exception("Error inesperado al conectar el parquímetro");
		}
		return null;
	}
}

