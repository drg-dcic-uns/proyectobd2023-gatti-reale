package parquimetros.controlador.inspector;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.controlador.login.ControladorLogin;
import parquimetros.controlador.login.ControladorLoginImpl;
import parquimetros.modelo.beans.InspectorBean;
import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.UbicacionBean;
import parquimetros.modelo.inspector.ModeloInspector;
import parquimetros.modelo.inspector.dto.EstacionamientoPatenteDTO;
import parquimetros.modelo.inspector.dto.MultaPatenteDTO;
import parquimetros.modelo.inspector.exception.AutomovilNoEncontradoException;
import parquimetros.modelo.inspector.exception.ConexionParquimetroException;
import parquimetros.modelo.inspector.exception.InspectorNoHabilitadoEnUbicacionException;
import parquimetros.modelo.login.ModeloLogin;
import parquimetros.modelo.login.ModeloLoginImpl;
import parquimetros.utils.Mensajes;
import parquimetros.vista.inspector.VentanaInspector;
import parquimetros.vista.login.VentanaLogin;
import parquimetros.vista.login.VentanaLoginImpl;

public class ControladorInspectorImpl implements ControladorInspector {

	private static Logger logger = LoggerFactory.getLogger(ControladorInspectorImpl.class);
	
	public ControladorInspectorImpl(VentanaInspector ventana, ModeloInspector modelo) {
		logger.debug(Mensajes.getMessage("ControladorInspectorImpl.constructor.logger"));		
		this.ventana = ventana;
		this.modelo = modelo;
		this.ventana.registrarControlador(this);
		this.listaPatentesEstacionadas = new ArrayList<EstacionamientoPatenteDTO>();
	}

	private VentanaInspector ventana;
	private ModeloInspector modelo;	
	
	private InspectorBean inspectorLogueado;
	private ParquimetroBean parquimetroConectado;
	private ArrayList<EstacionamientoPatenteDTO> listaPatentesEstacionadas;
	
	@Override
	public void ejecutar() {
		try {
			this.ventana.mostrarInspectorLogueado(inspectorLogueado);
			this.ventana.mostrarVentana();
			this.mostrarCargaPatentesEnUbicacion();
		} catch (Exception e) {
			logger.error(e.getMessage());
			this.ventana.informar(e.getMessage());
		}
	}

	@Override
	public void salirAplicacion() {
		logger.info(Mensajes.getMessage("ControladorInspectorImpl.salirAplicacion.logger"));
		this.modelo.desconectar();
		this.ventana.eliminarVentana();
		System.exit(0);
	}

	@Override
	public void cerrarSesion() {
		logger.info(Mensajes.getMessage("ControladorInspectorImpl.cerrarSesion.logger"));
		this.modelo.desconectar();
		this.ventana.eliminarVentana();

		logger.info(Mensajes.getMessage("ControladorInspectorImpl.cerrarSesion.loggerLogin"));
		ModeloLogin modeloLogin = new ModeloLoginImpl();  
		VentanaLogin ventanaLogin = new VentanaLoginImpl();
		@SuppressWarnings("unused")
		ControladorLogin controlador = new ControladorLoginImpl(ventanaLogin, modeloLogin);
	}
	
	/*
	 * Muestra la ventana que permite seleccionar una ubicación para conectarse.
	 */
	private void mostrarCargaPatentesEnUbicacion() {
		logger.info(Mensajes.getMessage("ControladorInspectorImpl.mostrarCargaPatentesEnUbicacion.logger"));
		try {
			ArrayList<UbicacionBean> ubicaciones = this.modelo.recuperarUbicaciones();

			logger.info(Mensajes.getMessage("ControladorInspectorImpl.mostrarCargaPatentesEnUbicacion.loggerPoblarUbicaciones"));
			this.ventana.poblarUbicaciones(ubicaciones);
			
			logger.info(Mensajes.getMessage("ControladorInspectorImpl.mostrarCargaPatentesEnUbicacion.loggerMostrarPanelPrincipal"));
			this.ventana.mostrarPanelPrincipal(VentanaInspector.ESTACIONAMIENTOS_Y_MULTAS);
			this.ventana.mostrarPanelDeEstacionamientosMultas(VentanaInspector.MULTAS_SELECCIONA_UBICACION);

		} catch (Exception e) {
			logger.warn(Mensajes.getMessage("ControladorInspectorImpl.mostrarCargaPatentesEnUbicacion.loggerWarn"), e.getMessage());
			this.ventana.informar(Mensajes.getMessage("ControladorInspectorImpl.mostrarCargaPatentesEnUbicacion.informar"));
		}
	}
	
	@Override
	public void registrarInspectorLogueado(InspectorBean inspectorLogueado) {
		logger.info(Mensajes.getMessage("ControladorInspectorImpl.registrarInspectorLogueado.logger"),inspectorLogueado.getLegajo());
		this.inspectorLogueado = inspectorLogueado;
	}

	@Override
	public void cambiarUbicacion(UbicacionBean ubicacion) {
		logger.info(Mensajes.getMessage("ControladorInspectorImpl.cambiarUbicacion.logger"));

		try {
			logger.info(Mensajes.getMessage("ControladorInspectorImpl.cambiarUbicacion.logger2"));
			
			ArrayList<ParquimetroBean> parquimetros = this.modelo.recuperarParquimetros(ubicacion);
			logger.info(Mensajes.getMessage("ControladorInspectorImpl.cambiarUbicacion.loggerRecuperarParquimetros"), parquimetros.size());

			this.ventana.poblarParquimetros(parquimetros);

		} catch (Exception e) {
			logger.warn(Mensajes.getMessage("ControladorInspectorImpl.cambiarUbicacion.warn"), e.getMessage());
			this.ventana.informar(e.getMessage());
		}	
		
	}
	
	@Override
	public void conectarParquimetro(ParquimetroBean parquimetro) {
		logger.info(Mensajes.getMessage("ControladorInspectorImpl.conectarParquimetro.logger"), parquimetro.getNumero());

		try 
		{
			this.modelo.conectarParquimetro(parquimetro, this.inspectorLogueado);
			logger.info(Mensajes.getMessage("ControladorInspectorImpl.conectarParquimetro.logger.conectarParquimetro"));
						
			this.parquimetroConectado = parquimetro;
			
			logger.info(Mensajes.getMessage("ControladorInspectorImpl.conectarParquimetro.logger.actualizaUbicacion"));
			this.parquimetroConectado.setUbicacion(this.modelo.recuperarUbicacion(parquimetro));
			
			logger.info(Mensajes.getMessage("ControladorInspectorImpl.conectarParquimetro.logger.muestraParquimetro"));
			this.ventana.mostrarParquimetroConectado(this.parquimetroConectado);

			logger.info(Mensajes.getMessage("ControladorInspectorImpl.conectarParquimetro.logger.limpiaListaPatentes"));
			listaPatentesEstacionadas.clear();
			
			this.mostrarVentanaCargaPatentes();
			
		} catch (ConexionParquimetroException e) {
			logger.warn(Mensajes.getMessage("ControladorInspectorImpl.conectarParquimetro.logger.conexionParquimetroException"));
			
			this.ventana.informar(e.getMessage());

		} catch (Exception e) {
			logger.warn(Mensajes.getMessage("ControladorInspectorImpl.conectarParquimetro.logger.Exception"), e.getMessage());
			this.ventana.informar(e.getMessage());
		}
	}
	
	/*
	 * Acciones necesarias para mostrar la ventana que carga patentes.
	 */
	private void mostrarVentanaCargaPatentes() {
		logger.info(Mensajes.getMessage("ControladorInspectorImpl.mostrarVentanaCargaPatentes.logger"));			
		this.ventana.mostrarPanelDeEstacionamientosMultas(VentanaInspector.MULTAS_CARGA_PATENTES);
	}

	@Override
	public void agregarPatente(String patente) {
		
		logger.info(Mensajes.getMessage("ControladorInspectorImpl.agregarPatente.logger"), patente);
		
		try {

			logger.info(Mensajes.getMessage("ControladorInspectorImpl.agregarPatente.logger.patenteRepetida"));
			for (EstacionamientoPatenteDTO auto : this.listaPatentesEstacionadas) {
				if (auto.getPatente().equals(patente)) {
					this.ventana.informar(Mensajes.getMessage("ControladorInspectorImpl.agregarPatente.patenteRepetida"));
					return;
				}
			}
			
			this.modelo.verificarPatente(patente);
			
			EstacionamientoPatenteDTO estacionamiento = this.modelo.recuperarEstacionamiento(patente,this.parquimetroConectado.getUbicacion());

			this.listaPatentesEstacionadas.add(estacionamiento);

			this.ventana.actualizarListaPatentesEstacionadas(this.listaPatentesEstacionadas);
			
		} catch (AutomovilNoEncontradoException e) {
			this.ventana.informar(Mensajes.getMessage("ControladorInspectorImpl.agregarPatente.patenteNoExiste"));
			return;
		} catch (Exception e) {
			this.ventana.informar(Mensajes.getMessage("ControladorInspectorImpl.agregarPatente.errorConexion"));
			return;
		}
		
	}

	@Override
	public void quitarPatentesEstacionadas(ArrayList<String> listaPatentesQuitar) {
		for (String patenteAQuitar : listaPatentesQuitar) {
			this.listaPatentesEstacionadas.removeIf(a -> a.getPatente().equals(patenteAQuitar));
		}
		this.ventana.actualizarListaPatentesEstacionadas(this.listaPatentesEstacionadas);
	}
	
	@Override
	public void limpiarListaPatentesEstacionadas() {
		this.listaPatentesEstacionadas.clear();
		this.ventana.actualizarListaPatentesEstacionadas(this.listaPatentesEstacionadas);		
	}

	@Override
	public void registraMultas(ArrayList<String> listaPatentes) {
		
		logger.info(Mensajes.getMessage("ControladorInspectorImpl.registraMultas.logger"), parquimetroConectado.getUbicacion());
		try {
			
			ArrayList<MultaPatenteDTO> multas = this.modelo.generarMultas(listaPatentes, parquimetroConectado.getUbicacion(), inspectorLogueado);
			logger.info(Mensajes.getMessage("ControladorInspectorImpl.registraMultas.logger.multas"), multas.size());
			
			this.ventana.actualizarListaMultas(multas);
			
		} catch (InspectorNoHabilitadoEnUbicacionException e) {
			this.ventana.informar(Mensajes.getMessage("ControladorInspectorImpl.registraMultas.inspectorNoHabilitado"));
			return;
		} catch (Exception e) {
			this.ventana.informar(Mensajes.getMessage("ControladorInspectorImpl.registraMultas.errorConexion"));
			return;
		}
		
	}


}
