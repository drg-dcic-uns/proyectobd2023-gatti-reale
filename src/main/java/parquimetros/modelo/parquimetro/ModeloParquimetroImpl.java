package parquimetros.modelo.parquimetro;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.modelo.ModeloImpl;
import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.TarjetaBean;
import parquimetros.modelo.beans.UbicacionBean;
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

		// Datos estáticos de prueba. Quitar y reemplazar por código que recupera las ubicaciones de la B.D. en una lista de UbicacionesBean		 
		DAOTarjetasDatosPrueba.poblar();
		
		for (TarjetaBean ubicacion : DAOTarjetasDatosPrueba.datos.values()) {
			tarjetas.add(ubicacion);	
		}
		// Fin datos estáticos de prueba.
	
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
		
		/**
		 * TODO Invoca al stored procedure conectar(...) que se encarga de realizar en una transacción la apertura o cierre 
		 *      de estacionamiento segun corresponda.
		 *      
		 *      Segun la infromacion devuelta por el stored procedure se retorna un objeto EstacionamientoDTO o
		 *      dependiendo del error se produce la excepción correspondiente:
		 *       SinSaldoSuficienteException, ParquimetroNoExisteException, TarjetaNoExisteException     
		 *  
		 */
		
		//Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.
		if ((tarjeta.getSaldo() < 0) && (tarjeta.getTipoTarjeta().getDescuento() < 1)) {  // tarjeta k1
			throw new SinSaldoSuficienteException();
		}
		EstacionamientoDTO estacionamiento;

		LocalDateTime currentDateTime = LocalDateTime.now();
        // Definir formatos para la fecha y la hora
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Formatear la fecha y la hora como cadenas separadas
        String fechaAhora = currentDateTime.format(dateFormatter);
        String horaAhora = currentDateTime.format(timeFormatter);
		
		if (tarjeta.getId() == 2) { 		//EntradaEstacionamientoDTO(String tiempoDisponible, String fechaEntrada, String horaEntrada)			
			estacionamiento = new EntradaEstacionamientoDTOImpl("01:40:00",
																fechaAhora,
																horaAhora);
		} else if (tarjeta.getId() == 3) {  		//SalidaEstacionamientoDTO(String tiempoTranscurrido, String saldoTarjeta, String fechaEntrada,	String horaEntrada, String fechaSalida, String horaSalida)
			
			LocalDateTime antes = currentDateTime.minusMinutes(45); // hora actual menos 45 minutos
			
			estacionamiento = new SalidaEstacionamientoDTOImpl("00:45:00", // tiempoTranscurrido
																"10.20", // saldoTarjeta
																fechaAhora, // fechaEntrada
																antes.format(timeFormatter), // horaEntrada
																fechaAhora, // fechaSalida
																horaAhora); // horaSalida
		} else if (tarjeta.getId() == 4) { 

			LocalDateTime antes = currentDateTime.minusMinutes(90); // hora actual menos 45 minutos
			
			estacionamiento = new SalidaEstacionamientoDTOImpl("01:30:00", // tiempoTranscurrido
																"-85", // saldoTarjeta
																fechaAhora, // fechaEntrada
																antes.format(timeFormatter), // horaEntrada
																fechaAhora, // fechaSalida
																horaAhora); // horaSalida
			
		} else {
			throw new Exception();
		}
	
		return estacionamiento;
		//Fin datos estáticos de prueba
		
	}

}
