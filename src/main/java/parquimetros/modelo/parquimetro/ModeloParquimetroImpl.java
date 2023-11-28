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

		String sql = "SELECT tarjetas.id_tarjeta, tarjetas.saldo, tarjetas.tipo, " +
				"Automoviles.modelo, Automoviles.color, Automoviles.marca, Automoviles.patente, " +
				"Conductores.apellido, Conductores.direccion, Conductores.nombre, Conductores.registro, Conductores.dni, Conductores.telefono, " +
				"tipos_tarjeta.tipo, tipos_tarjeta.descuento " +
				"FROM tarjetas " +
				"NATURAL JOIN Automoviles " +
				"NATURAL JOIN Conductores " +
				"NATURAL JOIN tipos_tarjeta";
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = this.conexion.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {
				TarjetaBeanImpl t = new TarjetaBeanImpl();
				t.setId(rs.getInt("id_tarjeta"));
				t.setSaldo(rs.getDouble("saldo"));

				TipoTarjetaBean tipoTarjeta = new TipoTarjetaBeanImpl();
				tipoTarjeta.setTipo(rs.getString("tipo"));
				tipoTarjeta.setDescuento(rs.getDouble("descuento"));
				t.setTipoTarjeta(tipoTarjeta);

				AutomovilBean a = new AutomovilBeanImpl();
				a.setModelo(rs.getString("modelo"));
				a.setColor(rs.getString("color"));
				a.setMarca(rs.getString("marca"));
				a.setPatente(rs.getString("patente"));

				ConductorBean c = new ConductorBeanImpl();
				c.setApellido(rs.getString("apellido"));
				c.setDireccion(rs.getString("direccion"));
				c.setNombre(rs.getString("nombre"));
				c.setRegistro(rs.getInt("registro"));
				c.setNroDocumento(rs.getInt("dni"));
				c.setTelefono(rs.getString("telefono"));

				a.setConductor(c);
				t.setAutomovil(a);

				tarjetas.add(t);
			}
		} catch (SQLException e) {
			logger.error(Mensajes.getMessage("ModeloParquimetroImpl.recuperarTarjetas.error"), e);
		} finally {
			try {
				if (rs != null) rs.close();
				if (statement != null) statement.close();
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

		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM ubicaciones";
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

		PreparedStatement preparedStatement = null;
		ArrayList<ParquimetroBean> parquimetros = new ArrayList<ParquimetroBean>();
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM Parquimetros WHERE calle = ? AND altura = ?";
			preparedStatement = conexion.prepareStatement(sql);
			preparedStatement.setString(1, ubicacion.getCalle());
			preparedStatement.setInt(2, ubicacion.getAltura());
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
					if (preparedStatement != null) preparedStatement.close();
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
		/*
		 Hemos visto la correcion de la parte 2: "La apertura debe devolver la fecha y hora de entrada,
		 en conectarParquimetro() la única consulta SQL debe ser la llamada al S.P."
		 pero segun el enunciado el s.p al hacer una apertura no devuelve fecha u hora de entrada.
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

			}
			String sql = "{CALL conectar(?, ?)}";

				ResultSet rs = null;
			CallableStatement cs = null;
			try {
				cs = conexion.prepareCall(sql);
				cs.setInt(1, tarjeta.getId());
				cs.setInt(2, parquimetro.getId());
				rs = cs.executeQuery();

				try {
					if (rs.next()) {
						try {
							System.out.println(rs.getString(1));
							System.out.println(rs.getInt("tiempoRestante"));
						}catch (SQLException e) {
							System.out.println("No hay tiempo restante");
						}
						try {
							System.out.println(rs.getString(1));
							System.out.println(rs.getTime("saldo"));
						}catch (SQLException e) {
							System.out.println("No hay saldo");
						}
						String tipoOperacion = rs.getString("operacion");
						Time horaActual = null;
						Date fechaActual = null;

						int tiempoRestante = 0;
						if (tipoOperacion.equals("Apertura") &&  rs.getString("resultado").equals("La operacion se realizo con exito")) {
							tiempoRestante = rs.getInt("tiempoRestante");
							String sql2 = "SELECT CURTIME() AS horaActual, CURDATE() AS fechaActual";
							PreparedStatement statement = conexion.prepareStatement(sql2);
							ResultSet fechaHora = statement.executeQuery();
							if (fechaHora.next()) {
								horaActual = fechaHora.getTime("horaActual");
								fechaActual = fechaHora.getDate("fechaActual");
							}

							return new EntradaEstacionamientoDTOImpl("" + tiempoRestante, "" + fechaActual, "" + horaActual);
						} else if (tipoOperacion.equals("Cierre")) {


							Time horaCierre = rs.getTime("horaSalida");
							int tiempoTranscurrido = rs.getInt("tiempoTranscurrido");
							BigDecimal saldoActualizado = rs.getBigDecimal("saldo");

							Date fechaCierre = rs.getDate("Fecha_cierre");

							return new SalidaEstacionamientoDTOImpl("" + tiempoTranscurrido, "" + saldoActualizado, "" + fechaApertura,
									"" + horaApertura2, "" + fechaCierre, "" + horaCierre);
						} else if ( rs.getString("resultado").equals("Saldo de la tarjeta insuficiente")){
							throw new SinSaldoSuficienteException("Sin Saldo suficiente");
							} else if (rs.getString("resultado").equals("Error tarjeta inexistente")) {
								throw new TarjetaNoExisteException();
								} else throw new ParquimetroNoExisteException();
					}
				} catch (SQLException e) {
					// Manejar las excepciones de SQL según sea necesario
					e.printStackTrace();
					throw new Exception("  espere unos segundos antes de reintentar", e);
				}

			} catch (SQLException e) {
				// Manejar las excepciones de SQL según sea necesario
				e.printStackTrace();
				throw new Exception("Error al obtener la conexión a la base de datos", e);
			} finally {
				pS.close();
				if (cs != null) cs.close();
				rs1.close();
				if (rs != null) rs.close();
			}

		} catch (SQLException e) {
			// En caso de algún problema inesperado, lanzar una excepción general
			throw new Exception("Error inesperado al conectar el parquímetro");
		}

		return null;
	}
}

