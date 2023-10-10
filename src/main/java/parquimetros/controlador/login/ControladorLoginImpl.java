package parquimetros.controlador.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.controlador.inspector.ControladorInspector;
import parquimetros.controlador.inspector.ControladorInspectorImpl;
import parquimetros.modelo.inspector.ModeloInspectorImpl;
import parquimetros.modelo.inspector.exception.InspectorNoAutenticadoException;
import parquimetros.modelo.beans.InspectorBean;
import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.inspector.ModeloInspector;
import parquimetros.modelo.parquimetro.ModeloParquimetro;
import parquimetros.modelo.parquimetro.ModeloParquimetroImpl;
import parquimetros.utils.Mensajes;
import parquimetros.controlador.parquimetro.ControladorParquimetro;
import parquimetros.controlador.parquimetro.ControladorParquimetroImpl;
import parquimetros.modelo.login.ModeloLogin;
import parquimetros.modelo.login.beans.UsuarioBean;
import parquimetros.vista.inspector.VentanaInspector;
import parquimetros.vista.inspector.VentanaInspectorImpl;
import parquimetros.vista.parquimetro.VentanaParquimetro;
import parquimetros.vista.parquimetro.VentanaParquimetroImpl;
import parquimetros.vista.login.VentanaLogin;

//CLASE IMPLEMENTADA PROVISTA POR LA CATEDRA 
public class ControladorLoginImpl implements ControladorLogin { 
	
	private static Logger logger = LoggerFactory.getLogger(ControladorLoginImpl.class);	
	
	private VentanaLogin ventana; 
	private ModeloLogin modelo;	
	
	public ControladorLoginImpl(VentanaLogin ventana, ModeloLogin modelo) {
		this.ventana = ventana;	
		this.modelo = modelo;
		
		try {
			logger.info(Mensajes.getMessage("ControladorLoginImpl.constructor.iniciarConexion"));
			this.modelo.iniciarConexion();
			
			logger.debug(Mensajes.getMessage("ControladorLoginImpl.constructor.registrarControlador"));
			this.ventana.registrarControlador(this);
			
			logger.debug(Mensajes.getMessage("ControladorLoginImpl.constructor.poblarComboTipoUsuario"));
			this.ventana.poblarComboTipoUsuario(this.modelo.obtenerNombresUsuarios());
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
	public void ejecutar() {
		logger.debug(Mensajes.getMessage("ControladorLoginImpl.ejecutar.logger"));
		try {
			this.ventana.mostrarVentana();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void ingresarComoInspector(String legajo, char[] password) {
		logger.debug(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.logger"), INSPECTOR, legajo, String.valueOf(password));
		
		logger.debug(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.obtenerUsuario"),INSPECTOR);
		UsuarioBean usuario = this.modelo.obtenerUsuario(INSPECTOR);

		if (usuario != null) {
			
			ModeloInspector modeloInspector = new ModeloInspectorImpl();
			
			if (modeloInspector.conectar(usuario.getUsername(), usuario.getPassword())) {
			
				try {
					
					InspectorBean inspectorLogueado = modeloInspector.autenticar(legajo, String.valueOf(password));
					logger.info(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.autenticar"),INSPECTOR, legajo);
						
					VentanaInspector ventanaInspector = new VentanaInspectorImpl();
					ControladorInspector controladorInspector = new ControladorInspectorImpl(ventanaInspector, modeloInspector);
						
					logger.info(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.registrarInspectorLogueado"));
					controladorInspector.registrarInspectorLogueado(inspectorLogueado);
					
					logger.info(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.ejecutar"));
					controladorInspector.ejecutar();
						
					logger.info(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.eliminarVentana"));					
					this.ventana.eliminarVentana();

				} catch (InspectorNoAutenticadoException e) {

					logger.error(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.InspectorNoAutenticadoException"));
					this.ventana.informar(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.InspectorNoAutenticadoExceptionInformar"));

				} catch (Exception e) {
					
					logger.error(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.Exception"));
					this.ventana.informar(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.Exception") + e.getMessage());
					
				}
			}
			else
			{
				logger.error(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.errorConectar"));
				this.ventana.informar(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.errorConectar"));
			}			
		}
		else
		{
			logger.error(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.errorObtenerUsuarioLogger"), INSPECTOR);
			this.ventana.informar(String.format(Mensajes.getMessage("ControladorLoginImpl.ingresarComoInspector.errorObtenerUsuario"),((String) INSPECTOR).toLowerCase()));
		}		
	}


	@Override
	public void ingresarComoParquimetro() {
		logger.debug(Mensajes.getMessage("ControladorLoginImpl.ingresarComoParquimetro.logger"), PARQUIMETRO);
	
		UsuarioBean usuario = this.modelo.obtenerUsuario(PARQUIMETRO);

		if (usuario != null) {
			
			logger.debug(Mensajes.getMessage("ControladorLoginImpl.ingresarComoParquimetro.usuarioNotNull"),usuario.getUsername(), usuario.getPassword());
			
			ModeloParquimetro modeloParquimetro = new ModeloParquimetroImpl();
			VentanaParquimetro ventanaParquimetro = new VentanaParquimetroImpl();

			ControladorParquimetro controladorParquimetro = new ControladorParquimetroImpl(ventanaParquimetro, modeloParquimetro);
						
			logger.info(Mensajes.getMessage("ControladorLoginImpl.ingresarComoParquimetro.ejecutar"));
			controladorParquimetro.ejecutar();
						
			logger.info(Mensajes.getMessage("ControladorLoginImpl.ingresarComoParquimetro.eliminarVentana"));					
			this.ventana.eliminarVentana();

		} else {
			
			logger.error(Mensajes.getMessage("ControladorLoginImpl.ingresarComoParquimetro.errorObtenerUsuarioLogger"), PARQUIMETRO);
			this.ventana.informar(String.format(Mensajes.getMessage("ControladorLoginImpl.ingresarComoParquimetro.errorObtenerUsuario"),((String) PARQUIMETRO).toLowerCase()));
		}		
	}
	
}