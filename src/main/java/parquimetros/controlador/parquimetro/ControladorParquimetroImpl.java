package parquimetros.controlador.parquimetro;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.controlador.login.ControladorLogin;
import parquimetros.controlador.login.ControladorLoginImpl;
import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.TarjetaBean;
import parquimetros.modelo.beans.UbicacionBean;
import parquimetros.modelo.login.ModeloLogin;
import parquimetros.modelo.login.ModeloLoginImpl;
import parquimetros.modelo.parquimetro.ModeloParquimetro;
import parquimetros.modelo.parquimetro.dto.EntradaEstacionamientoDTO;
import parquimetros.modelo.parquimetro.dto.EstacionamientoDTO;
import parquimetros.modelo.parquimetro.dto.SalidaEstacionamientoDTO;
import parquimetros.modelo.parquimetro.exception.ParquimetroNoExisteException;
import parquimetros.modelo.parquimetro.exception.SinSaldoSuficienteException;
import parquimetros.modelo.parquimetro.exception.TarjetaNoExisteException;
import parquimetros.utils.Mensajes;
import parquimetros.vista.login.VentanaLogin;
import parquimetros.vista.login.VentanaLoginImpl;
import parquimetros.vista.parquimetro.VentanaParquimetro;

public class ControladorParquimetroImpl implements ControladorParquimetro {

	private static Logger logger = LoggerFactory.getLogger(ControladorParquimetroImpl.class);
	
	public ControladorParquimetroImpl(VentanaParquimetro ventana, ModeloParquimetro modelo) {
		logger.debug(Mensajes.getMessage("ControladorParquimetroImpl.constructor.logger"));
		this.ventana = ventana;
		this.modelo = modelo;
		this.ventana.registrarControlador(this);
	}

	private VentanaParquimetro ventana;
	private ModeloParquimetro modelo;	
	
	@Override
	public void ejecutar() {
		try {
			this.ventana.mostrarVentana();
			this.precargarDatos();
		} catch (Exception e) {
			logger.error(e.getMessage());
			this.ventana.informar(e.getMessage());
		}
	}

	@Override
	public void salirAplicacion() {
		logger.info(Mensajes.getMessage("ControladorParquimetroImpl.salirAplicacion.logger"));
		this.modelo.desconectar();
		this.ventana.eliminarVentana();
		System.exit(0);
	}

	@Override
	public void cerrarSesion() {
		logger.info(Mensajes.getMessage("ControladorParquimetroImpl.cerrarSesion.logger"));
		this.modelo.desconectar();
		this.ventana.eliminarVentana();

		logger.info(Mensajes.getMessage("ControladorParquimetroImpl.cerrarSesion.loggerLogin"));
		ModeloLogin modeloLogin = new ModeloLoginImpl();  
		VentanaLogin ventanaLogin = new VentanaLoginImpl();
		@SuppressWarnings("unused")
		ControladorLogin controlador = new ControladorLoginImpl(ventanaLogin, modeloLogin);
	}
	
	/*
	 * Muestra la ventana que permite seleccionar una ubicación para conectarse.
	 */
	private void precargarDatos() {
		logger.info(Mensajes.getMessage("ControladorParquimetroImpl.mostrarCargaPatentesEnUbicacion.logger"));
		try {
			ArrayList<UbicacionBean> ubicaciones = this.modelo.recuperarUbicaciones();

			logger.info(Mensajes.getMessage("ControladorParquimetroImpl.mostrarCargaPatentesEnUbicacion.loggerPoblarUbicaciones"));
			this.ventana.poblarUbicaciones(ubicaciones);

			logger.info(Mensajes.getMessage("ControladorParquimetroImpl.mostrarCargaPatentesEnUbicacion.loggerRecuperarTarjetas"));
			ArrayList<TarjetaBean> tarjetas = this.modelo.recuperarTarjetas();

			logger.info(Mensajes.getMessage("ControladorParquimetroImpl.mostrarCargaPatentesEnUbicacion.loggerPoblarTarjetas"));
			this.ventana.poblarTarjetas(tarjetas);

		} catch (Exception e) {
			logger.warn(Mensajes.getMessage("ControladorParquimetroImpl.mostrarCargaPatentesEnUbicacion.loggerWarn"), e.getMessage());
			this.ventana.informar(Mensajes.getMessage("ControladorParquimetroImpl.mostrarCargaPatentesEnUbicacion.informar"));
		}
	}
	
	@Override
	public void cambiarTarjeta(TarjetaBean tarjeta) {
		logger.info(Mensajes.getMessage("ControladorParquimetroImpl.cambiarTarjeta.logger"));
		this.ventana.mostrarInformacionTarjeta(tarjeta);
		this.ventana.limpiarEstacionamientoResultado();
	}
	
	@Override
	public void cambiarUbicacion(UbicacionBean ubicacion) {
		logger.info(Mensajes.getMessage("ControladorParquimetroImpl.cambiarUbicacion.logger"));

		try {
			logger.info(Mensajes.getMessage("ControladorParquimetroImpl.cambiarUbicacion.logger2"));
			
			ArrayList<ParquimetroBean> parquimetros = this.modelo.recuperarParquimetros(ubicacion);
			logger.info(Mensajes.getMessage("ControladorParquimetroImpl.cambiarUbicacion.loggerRecuperarParquimetros"), parquimetros.size());

			this.ventana.poblarParquimetros(parquimetros);

		} catch (Exception e) {
			logger.warn(Mensajes.getMessage("ControladorParquimetroImpl.cambiarUbicacion.warn"), e.getMessage());
			this.ventana.informar(e.getMessage());
		}	
		
	}

	@Override
	public void conectarParquimetro(ParquimetroBean parquimetro, TarjetaBean tarjeta) {

		if (parquimetro == null) {
			this.ventana.informar(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.parquimetroNull"));
			return;
		}
		if (tarjeta == null) {
			this.ventana.informar(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.tarjetaNull"));
			return;
		}

		logger.info(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.logger"), parquimetro.getNumero());

		this.ventana.limpiarEstacionamientoResultado();
		
		try 
		{
			EstacionamientoDTO estacionamiento = this.modelo.conectarParquimetro(parquimetro, tarjeta);
			logger.info(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.loggerConectarParquimetro"));
						
			if (estacionamiento instanceof EntradaEstacionamientoDTO) {
				
				logger.info(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.loggerMostrarIngresoEstacionamiento"));				
				this.ventana.mostrarIngresoEstacionamiento((EntradaEstacionamientoDTO) estacionamiento);
				
			} else if (estacionamiento instanceof SalidaEstacionamientoDTO) {
				
				logger.info(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.loggerMostrarSalidaEstacionamiento"));				
				this.ventana.mostrarSalidaEstacionamiento((SalidaEstacionamientoDTO) estacionamiento);
				
			} else {
				logger.warn("Houston we are in trouble!");
				this.ventana.informar(":-(");
			}
			
		} catch (SinSaldoSuficienteException e) {

			logger.warn(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.loggerSinSaldoSuficienteException"));
			this.ventana.informar(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.SinSaldoSuficienteException"));

		} catch (ParquimetroNoExisteException e) {

			logger.warn(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.loggerParquimetroNoExisteException"));
			this.ventana.informar(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.ParquimetroNoExisteException"));

		} catch (TarjetaNoExisteException e) {

			logger.warn(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.loggerTarjetaNoExisteException"));
			this.ventana.informar(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.TarjetaNoExisteException"));

		} catch (Exception e) {

			logger.warn(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.loggerException"));
			this.ventana.informar(Mensajes.getMessage("ControladorParquimetroImpl.conectarParquimetro.Exception") + e.getMessage());

		} 
	}



}
