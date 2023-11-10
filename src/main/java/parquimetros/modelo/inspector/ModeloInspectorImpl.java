package parquimetros.modelo.inspector;

import java.sql.*;
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
	public void conectarParquimetro(ParquimetroBean parquimetro, InspectorBean inspectorLogueado) throws ConexionParquimetroException, Exception {

		logger.info(Mensajes.getMessage("ModeloInspectorImpl.conectarParquimetro.logger"), parquimetro.toString());

		int altura = parquimetro.getUbicacion().getAltura();
		String calle = parquimetro.getUbicacion().getCalle();
		Time hora = Time.valueOf(LocalDateTime.now().toLocalTime());
		Date fecha = Date.valueOf(LocalDateTime.now().toLocalDate());
		int legajo = inspectorLogueado.getLegajo();

		Statement statement = this.conexion.createStatement();
		String sql = "SELECT * FROM Asociado_con WHERE legajo = " + legajo + " and calle = '" + calle + "' AND altura = " + altura;

		java.sql.ResultSet rs = statement.executeQuery(sql);


		Time horaInicioManiana = Time.valueOf(LocalTime.of(8, 0));  // 8:00 AM
		Time horaFinManiana = Time.valueOf(LocalTime.of(14, 0));   // 2:00 PM

		Time horaInicioTarde = Time.valueOf(LocalTime.of(8, 0));  // 14:00 PM
		Time horaFinTarde = Time.valueOf(LocalTime.of(20, 0));   // 20:00 PM


		while (rs.next()) {
			if ((rs.getString("dia").equals(getDia())) &&
					((rs.getString("turno").equals("m") &&
							hora.after(horaInicioManiana) && hora.before(horaFinManiana)) ||

							(rs.getString("turno").equals("t") &&
									hora.after(horaInicioTarde) && hora.before(horaFinTarde)))) {

				insertar(fecha, hora, rs.getString("legajo"), parquimetro.getId());
				return;
			} else
				throw new ConexionParquimetroException(Mensajes.getMessage("InspectorNoHabilitadoEnUbicacionException"));
		}
	}


/*
* En conectarParquimetro al verificar el acceso e insertar, debería retornar.
* De esta forma cuando valida inserta en la tabla pero sigue ciclando
*  y podría arrojar excepciones en un próximo ciclo.
* Además se podría factorizar el throw y el insert
* */
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
					case "2":
						dia = "lu";
						break;
					case "3":
						dia = "ma";
						break;
					case "4":
						dia = "mi";
						break;
					case "5":
						dia = "ju";
						break;
					case "6":
						dia = "lu";
						break;
					case "7":
						dia = "sa";
						break;
					case "1":
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

		String fechaEntrada=null, horaEntrada=null, estado=null;

		Statement statement = this.conexion.createStatement();

		String sql = "SELECT * FROM Estacionados WHERE patente = '" + patente + "' AND calle = '" + ubicacion.getCalle() + "' AND altura = " + ubicacion.getAltura() + "";
		ResultSet rs = statement.executeQuery(sql);
		if(rs.next()){
			estado = EstacionamientoPatenteDTO.ESTADO_REGISTRADO;
			fechaEntrada = rs.getString("fecha_ent");
			horaEntrada = rs.getString("hora_ent");
		}
		else{
			estado = EstacionamientoPatenteDTO.ESTADO_NO_REGISTRADO;
	        fechaEntrada = "";
	        horaEntrada = "";
		}

		return new EstacionamientoPatenteDTOImpl(patente, ubicacion.getCalle(), String.valueOf(ubicacion.getAltura()), fechaEntrada, horaEntrada, estado);
	}
	

	@Override
	public ArrayList<MultaPatenteDTO> generarMultas(ArrayList<String> listaPatentes, 
													UbicacionBean ubicacion, 
													InspectorBean inspectorLogueado) 
									throws InspectorNoHabilitadoEnUbicacionException, Exception {

		logger.info(Mensajes.getMessage("ModeloInspectorImpl.generarMultas.logger"),listaPatentes.size());		
		

		int legajo = inspectorLogueado.getLegajo();
		Statement statement = this.conexion.createStatement();
		String sql = "SELECT * FROM Asociado_con WHERE legajo = " + legajo;
		java.sql.ResultSet rs = statement.executeQuery(sql);
		int altura = ubicacion.getAltura();
		String calle = ubicacion.getCalle();
		Time hora = Time.valueOf(LocalDateTime.now().toLocalTime());
		Date fecha = Date.valueOf(LocalDateTime.now().toLocalDate());

		Time horaInicioManiana = Time.valueOf(LocalTime.of(8, 0));  // 8:00 AM
		Time horaFinManiana = Time.valueOf(LocalTime.of(14, 0));   // 2:00 PM

		Time horaInicioTarde = Time.valueOf(LocalTime.of(14, 0));  // 14:00 PM
		Time horaFinTarde = Time.valueOf(LocalTime.of(20, 0));   // 20:00 PM
		boolean habilitado = false;
		while (!habilitado && rs.next() ) {
			if (rs.getString("calle").equals(calle) &&
					rs.getInt("altura") == altura
					&& rs.getString("dia").equals(getDia())) {

				if (rs.getString("turno").equals("m") &&
						hora.after(horaInicioManiana) &&
						hora.before(horaFinManiana)) {
					habilitado = true;

				} else {
					if (rs.getString("turno").equals("t") &&
							hora.after(horaInicioTarde) &&
							hora.before(horaFinTarde)) {
						habilitado = true;
					}
				}

			}

		}
		if(!habilitado) {
			throw new InspectorNoHabilitadoEnUbicacionException();
		}

		ArrayList<MultaPatenteDTO> multas = new ArrayList<MultaPatenteDTO>();

		int nroMulta = 2;
		LocalDateTime currentDateTime = LocalDateTime.now();
        // Definir formatos para la fecha y la hora
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Formatear la fecha y la hora como cadenas separadas
        String fechaMulta = currentDateTime.format(dateFormatter);
        String horaMulta = currentDateTime.format(timeFormatter);

		Time horaMultaTime = Time.valueOf(LocalDateTime.now().toLocalTime());
		Date fechaMultaDate = Date.valueOf(LocalDateTime.now().toLocalDate());

		for (String patente : listaPatentes) {
			
			EstacionamientoPatenteDTO estacionamiento = this.recuperarEstacionamiento(patente,ubicacion);
			if (estacionamiento.getEstado() == EstacionamientoPatenteDTO.ESTADO_NO_REGISTRADO) {

				String sql1 = "INSERT INTO Multa (fecha, hora, id_asociado_con ,patente ) VALUES (?, ?, ?, ?)";
				PreparedStatement statement1 = conexion.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);

				statement1.setString(4, patente);
				statement1.setDate(1, fechaMultaDate);
				statement1.setTime(2, horaMultaTime);
				statement1.setInt(3, (rs.getInt("id_asociado_con")));
				int idMultaGenerada = 0;
				try {
					statement1.executeUpdate();
					ResultSet generatedKeys = statement1.getGeneratedKeys();

					if (generatedKeys.next()) {
						idMultaGenerada = generatedKeys.getInt(1);
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}

				System.out.println("antes del rs");


				MultaPatenteDTO multa = new MultaPatenteDTOImpl(String.valueOf(idMultaGenerada),
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
		return multas;		
	}
}
