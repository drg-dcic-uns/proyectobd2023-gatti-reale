package parquimetros.modelo.inspector;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.modelo.ModeloImpl;
import parquimetros.modelo.beans.*;
import parquimetros.modelo.inspector.dao.DAOParquimetro;
import parquimetros.modelo.inspector.dao.DAOParquimetroImpl;
import parquimetros.modelo.inspector.dao.DAOInspector;
import parquimetros.modelo.inspector.dao.DAOInspectorImpl;
import parquimetros.modelo.inspector.dao.DAOAutomovil;
import parquimetros.modelo.inspector.dao.DAOAutomovilImpl;
import parquimetros.modelo.inspector.dao.datosprueba.DAOParquimetrosDatosPrueba;
import parquimetros.modelo.inspector.dao.datosprueba.DAOUbicacionesDatosPrueba;
import parquimetros.modelo.inspector.dto.EstacionamientoPatenteDTO;
import parquimetros.modelo.inspector.dto.EstacionamientoPatenteDTOImpl;
import parquimetros.modelo.inspector.dto.MultaPatenteDTO;
import parquimetros.modelo.inspector.dto.MultaPatenteDTOImpl;
import parquimetros.modelo.inspector.exception.AutomovilNoEncontradoException;
import parquimetros.modelo.inspector.exception.ConexionParquimetroException;
import parquimetros.modelo.inspector.exception.InspectorNoAutenticadoException;
import parquimetros.modelo.inspector.exception.InspectorNoHabilitadoEnUbicacionException;
import parquimetros.utils.Mensajes;

public class ModeloInspectorImpl extends ModeloImpl implements ModeloInspector {

	private static Logger logger = LoggerFactory.getLogger(ModeloInspectorImpl.class);	
	
	public ModeloInspectorImpl() {
		logger.debug(Mensajes.getMessage("ModeloInspectorImpl.constructor.logger"));
	}

	@Override
	public InspectorBean autenticar(String legajo, String password) throws InspectorNoAutenticadoException, Exception {
		logger.info(Mensajes.getMessage("ModeloInspectorImpl.autenticar.logger"), legajo, password);

		if (legajo==null || legajo.isEmpty() || password==null || password.isEmpty()) {
			throw new InspectorNoAutenticadoException(Mensajes.getMessage("ModeloInspectorImpl.autenticar.parametrosVacios"));
		}
		DAOInspector dao = new DAOInspectorImpl(this.conexion);
		return dao.autenticar(legajo, password);		
	}
	
	@Override
	public ArrayList<UbicacionBean> recuperarUbicaciones() throws Exception {
		
		logger.info(Mensajes.getMessage("ModeloInspectorImpl.recuperarUbicaciones.logger"));
		/** 
		 * TODO Debe retornar una lista de UbicacionesBean con todas las ubicaciones almacenadas en la B.D. 
		 *      Debería propagar una excepción si hay algún error en la consulta. 
		 *      
		 *      Importante: Para acceder a la B.D. utilice la propiedad this.conexion (de clase Connection) 
		 *      que se hereda al extender la clase ModeloImpl.       
		 *      
		 */

		 //codigo hecho por guido, pero el user inspector no tiene acceso a las ubicaciones de toda la bdd..¿como lo hacemos?

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

		/*
		ArrayList<UbicacionBean> ubicaciones = new ArrayList<UbicacionBean>();

		// Datos estáticos de prueba. Quitar y reemplazar por código que recupera las ubicaciones de la B.D. en una lista de UbicacionesBean		 
		DAOUbicacionesDatosPrueba.poblar();
		
		for (UbicacionBean ubicacion : DAOUbicacionesDatosPrueba.datos.values()) {
			ubicaciones.add(ubicacion);	
		}
		// Fin datos estáticos de prueba.
		*/
		return ubicaciones;


	}

	@Override
	public ArrayList<ParquimetroBean> recuperarParquimetros(UbicacionBean ubicacion) throws Exception {
		
		logger.info(Mensajes.getMessage("ModeloInspectorImpl.recuperarParquimetros.logger"),ubicacion.toString());
		
		/** 
		 * TODO Debe retornar una lista de ParquimetroBean con todos los parquimetros que corresponden a una ubicación.
		 * 		Debería propagar una excepción si hay algún error en la consulta.
		 *            
		 *      Importante: Para acceder a la B.D. utilice la propiedad this.conexion (de clase Connection) 
		 *      que se hereda al extender la clase ModeloImpl.      
		 *      
		 */

		Statement statement = this.conexion.createStatement();
		String sql = "SELECT * FROM Parquimetros WHERE calle = ? AND altura = ?";
		PreparedStatement preparedStatement = conexion.prepareStatement(sql);
		preparedStatement.setString(1, ubicacion.getCalle());
		preparedStatement.setInt(2, ubicacion.getAltura());

		ResultSet rs = preparedStatement.executeQuery();


		//java.sql.ResultSet rs = statement.executeQuery(sql);
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
	public void conectarParquimetro(ParquimetroBean parquimetro, InspectorBean inspectorLogueado) throws ConexionParquimetroException, Exception {
		// es llamado desde Controlador.conectarParquimetro

		logger.info(Mensajes.getMessage("ModeloInspectorImpl.conectarParquimetro.logger"), parquimetro.toString());

		/** TODO Simula la conexión al parquímetro con el inspector que se encuentra logueado en el momento 
		 *       en que se ejecuta la acción. 
		 *
		 *       Debe verificar si el inspector está habilitado a acceder a la ubicación del parquímetro 
		 *       en el dia y hora actual, segun la tabla asociado_con. 
		 *       Sino puede deberá producir una excepción ConexionParquimetroException.     
		 *       En caso exitoso se registra su acceso en la tabla ACCEDE y retorna exitosamente.		         
		 *
		 *       Si hay un error no controlado se produce una Exception genérica.
		 *
		 *       Importante: Para acceder a la B.D. utilice la propiedad this.conexion (de clase Connection) 
		 *       que se hereda al extender la clase ModeloImpl.
		 *
		 * @param parquimetro
		 * @throws ConexionParquimetroException
		 * @throws Exception
		 */
		int legajo = inspectorLogueado.getLegajo();
		Statement statement = this.conexion.createStatement();
		String sql = "SELECT * FROM Asociado_con WHERE legajo = " + legajo;

		java.sql.ResultSet rs = statement.executeQuery(sql);
		int altura = parquimetro.getUbicacion().getAltura();
		String calle = parquimetro.getUbicacion().getCalle();
		Time hora = Time.valueOf(LocalDateTime.now().toLocalTime());
		Date fecha = Date.valueOf(LocalDateTime.now().toLocalDate());


		Time horaInicioManiana = Time.valueOf(LocalTime.of(8, 0));  // 8:00 AM
		Time horaFinManiana = Time.valueOf(LocalTime.of(14, 0));   // 2:00 PM

		Time horaInicioTarde = Time.valueOf(LocalTime.of(14, 0));  // 14:00 pM
		Time horaFinTarde = Time.valueOf(LocalTime.of(20, 0));   // 20:00 PM


		while (rs.next()) {
			if (rs.getString("calle").equals(calle) &&
					rs.getInt("altura") == altura
					&& rs.getString("dia").equals(getDia())) {

				if (rs.getString("turno").equals("m") &&
						hora.after(horaInicioManiana) &&
						hora.before(horaFinManiana)) {
					insertar(fecha, hora, rs.getString("legajo"), parquimetro.getId());
				} else {
					if (rs.getString("turno").equals("t") &&
							hora.after(horaInicioTarde) &&
							hora.before(horaFinTarde)) {
						insertar(fecha, hora, rs.getString("legajo"), parquimetro.getId());
					} else
						throw new ConexionParquimetroException("no se encontró");
				}

			}
		}
	}


	private void insertar(Date fecha,Time hora, String legajo, int idParq) throws SQLException {
		String insercion = "INSERT INTO Accede (fecha, hora, legajo, id_parq) VALUES (?, ?, ?, ?)";
		PreparedStatement preparedStatement = conexion.prepareStatement(insercion);
		preparedStatement.setTime(2, Time.valueOf(LocalDateTime.now().toLocalTime()));
		preparedStatement.setDate(1, Date.valueOf(LocalDateTime.now().toLocalDate()));
		preparedStatement.setString(3, legajo);
		preparedStatement.setInt(4, idParq);
		preparedStatement.executeUpdate();
	}

	 private static String getDia() {
				String dia = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + "";
				switch (dia) {
					case "Monday":
						dia = "lu";
						break;
					case "Tuesday":
						dia = "ma";
						break;
					case "Wednesday":
						dia = "mi";
						break;
					case "Thursday":
						dia = "ju";
						break;
					case "Friday":
						dia = "vi";
						break;
					case "Saturday":
						dia = "sa";
						break;
					case "Sunday":
						dia = "do";
						break;
					default:
						dia = "Día no válido";
				}
				return dia;
			}


	@Override
	public UbicacionBean recuperarUbicacion(ParquimetroBean parquimetro) throws Exception {
		logger.info(Mensajes.getMessage("ModeloInspectorImpl.recuperarUbicacion.logger"),parquimetro.getId());
		UbicacionBean ubicacion = parquimetro.getUbicacion();
		if (Objects.isNull(ubicacion)) {
			DAOParquimetro dao = new DAOParquimetroImpl(this.conexion);
			ubicacion = dao.recuperarUbicacion(parquimetro);
		}			
		return ubicacion; 
	}

	@Override
	public void verificarPatente(String patente) throws AutomovilNoEncontradoException, Exception {
		logger.info(Mensajes.getMessage("ModeloInspectorImpl.verificarPatente.logger"),patente);
		DAOAutomovil dao = new DAOAutomovilImpl(this.conexion);
		dao.verificarPatente(patente); 
	}	
	
	@Override
	public EstacionamientoPatenteDTO recuperarEstacionamiento(String patente, UbicacionBean ubicacion) throws Exception {

		logger.info(Mensajes.getMessage("ModeloInspectorImpl.recuperarEstacionamiento.logger"),patente,ubicacion.getCalle(),ubicacion.getAltura());
		/**
		 * TODO Verifica si existe un estacionamiento abierto registrado la patente en la ubicación, y
		 *	    de ser asi retorna un EstacionamientoPatenteDTO con estado Registrado (EstacionamientoPatenteDTO.ESTADO_REGISTRADO), 
		 * 		y caso contrario sale con estado No Registrado (EstacionamientoPatenteDTO.ESTADO_NO_REGISTRADO).
		 * 
		 *      Importante: Para acceder a la B.D. utilice la propiedad this.conexion (de clase Connection) 
		 *      que se hereda al extender la clase ModeloImpl.
		 */
		//
		// Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reale de la BD.
		//
		// Diseño de datos de prueba: Las patentes que terminan en 1 al 8 fueron verificados como existentes en la tabla automovil,
		//                            las terminadas en 9 y 0 produjeron una excepción de AutomovilNoEncontradoException y Exception.
		//                            entonces solo consideramos los casos terminados de 1 a 8
 		// 
		// Utilizaremos el criterio que si es par el último digito de patente entonces está registrado correctamente el estacionamiento.
		//
		String fechaEntrada, horaEntrada, estado;
		
		if (Integer.parseInt(patente.substring(patente.length()-1)) % 2 == 0) {
			estado = EstacionamientoPatenteDTO.ESTADO_REGISTRADO;

			LocalDateTime currentDateTime = LocalDateTime.now();
	        // Definir formatos para la fecha y la hora
	        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	        // Formatear la fecha y la hora como cadenas separadas
	        fechaEntrada = currentDateTime.format(dateFormatter);
	        horaEntrada = currentDateTime.format(timeFormatter);
			
		} else {
			estado = EstacionamientoPatenteDTO.ESTADO_NO_REGISTRADO;
	        fechaEntrada = "";
	        horaEntrada = "";
		}

		return new EstacionamientoPatenteDTOImpl(patente, ubicacion.getCalle(), String.valueOf(ubicacion.getAltura()), fechaEntrada, horaEntrada, estado);
		// Fin de datos de prueba
	}
	

	@Override
	public ArrayList<MultaPatenteDTO> generarMultas(ArrayList<String> listaPatentes, 
													UbicacionBean ubicacion, 
													InspectorBean inspectorLogueado) 
									throws InspectorNoHabilitadoEnUbicacionException, Exception {

		logger.info(Mensajes.getMessage("ModeloInspectorImpl.generarMultas.logger"),listaPatentes.size());		
		
		/** 
		 * TODO Primero verificar si el inspector puede realizar una multa en esa ubicacion el dia y hora actual 
		 *      segun la tabla asociado_con. Sino puede deberá producir una excepción de 
		 *      InspectorNoHabilitadoEnUbicacionException. 
		 *            
		 * 		Luego para cada una de las patentes suministradas, si no tiene un estacionamiento abierto en dicha 
		 *      ubicación, se deberá cargar una multa en la B.D. 
		 *      
		 *      Debe retornar una lista de las multas realizadas (lista de objetos MultaPatenteDTO).
		 *      
		 *      Importante: Para acceder a la B.D. utilice la propiedad this.conexion (de clase Connection) 
		 *      que se hereda al extender la clase ModeloImpl.      
		 */
		
		//Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.
		//
		// 1) throw InspectorNoHabilitadoEnUbicacionException
		//
		ArrayList<MultaPatenteDTO> multas = new ArrayList<MultaPatenteDTO>();
		int nroMulta = 1;
		
		LocalDateTime currentDateTime = LocalDateTime.now();
        // Definir formatos para la fecha y la hora
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Formatear la fecha y la hora como cadenas separadas
        String fechaMulta = currentDateTime.format(dateFormatter);
        String horaMulta = currentDateTime.format(timeFormatter);
		
		for (String patente : listaPatentes) {
			
			EstacionamientoPatenteDTO estacionamiento = this.recuperarEstacionamiento(patente,ubicacion);
			if (estacionamiento.getEstado() == EstacionamientoPatenteDTO.ESTADO_NO_REGISTRADO) {
				
				MultaPatenteDTO multa = new MultaPatenteDTOImpl(String.valueOf(nroMulta), 
																patente, 
																ubicacion.getCalle(), 
																String.valueOf(ubicacion.getAltura()), 
																fechaMulta, 
																horaMulta, 
																String.valueOf(inspectorLogueado.getLegajo()));
				multas.add(multa);
				nroMulta++;
			}
		}
		// Fin datos prueba
		return multas;		
	}
}
