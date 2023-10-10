package parquimetros.vista.parquimetro;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.controlador.Controlador;
import parquimetros.controlador.parquimetro.ControladorParquimetro;
import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.TarjetaBean;
import parquimetros.modelo.beans.UbicacionBean;
import parquimetros.modelo.parquimetro.dto.EntradaEstacionamientoDTO;
import parquimetros.modelo.parquimetro.dto.SalidaEstacionamientoDTO;
import parquimetros.utils.Mensajes;

//CLASE IMPLEMENTADA PROVISTA POR LA CATEDRA
public class VentanaParquimetroImpl extends JFrame implements VentanaParquimetro {

	private static Logger logger = LoggerFactory.getLogger(VentanaParquimetroImpl.class);

	private static final long serialVersionUID = 1L;
	
	/*
	 * Propiedades 
	 */	
	protected ControladorParquimetro controlador;
	
	protected VentanaParquimetro ventana;
	protected JFrame frame;
	
	protected JMenuItem mntmCerrarSesion;
	protected JMenuItem mntmSalir;

	protected JPanel panelInterno;
	
	protected JTextField txtPatente;	

	protected JLabel lblParquimetroUbicacion;	
	protected JComboBox<UbicacionBean> cmbUbicacion;
	protected JComboBox<ParquimetroBean> cmbParquimetro;
	protected JComboBox<TarjetaBean> cmbTarjeta;
	protected JButton btnConectarParquimetro;
	
	protected JLabel lblTarjetaInformacion;
	protected JLabel lblConexionResultado;
	
	/**
	 * Constructor
	 */
	public VentanaParquimetroImpl() {		
		inicializar();
		this.frame.setVisible(true);
	}
	
	/*
	 * Métodos publicos
	 */
	@Override
	public void registrarControlador(Controlador c) {
        if (c instanceof ControladorParquimetro) {
            this.controlador = (ControladorParquimetro) c;
        } else {
            throw new IllegalArgumentException("Se esperaba un ControladorParquimetro");
        }	
    }
	
	@Override
	public void eliminarVentana() {
		logger.info(Mensajes.getMessage("VentanaParquimetroImpl.eliminarVentana.logger"));
		this.frame.dispose();
	}	

	@Override
	public void informar(String mensaje) {
		logger.info(Mensajes.getMessage("VentanaParquimetroImpl.informar.logger"),mensaje);		
		JOptionPane.showMessageDialog(this.frame,mensaje);
	}
	
	@Override
	public void mostrarVentana() throws Exception {
		if (this.frame != null) {
			this.frame.setVisible(true);
		}
		else 
		{
			throw new Exception("Error la ventana no está disponible");			
		}		
	}
	
	@Override
	public void poblarTarjetas(ArrayList<TarjetaBean> tarjetas) {
		logger.info(Mensajes.getMessage("VentanaParquimetroImpl.poblarTarjetas.logger"), tarjetas.size());

		this.cmbTarjeta.removeAllItems();
		
		for (TarjetaBean tarjeta : tarjetas) {
			this.cmbTarjeta.addItem(tarjeta);
		}
	}
	
	@Override
	public void poblarParquimetros(ArrayList<ParquimetroBean> parquimetros) {
		logger.info(Mensajes.getMessage("VentanaParquimetroImpl.poblarParquimetros.logger"), parquimetros.size());

		this.cmbParquimetro.removeAllItems();
		
		for (ParquimetroBean parquimetro : parquimetros) {
			this.cmbParquimetro.addItem(parquimetro);
		}
	}
	
	@Override
	public void poblarUbicaciones(ArrayList<UbicacionBean> ubicaciones) {
		logger.info(Mensajes.getMessage("VentanaParquimetroImpl.poblarUbicaciones.logger"), ubicaciones.size());

		this.cmbUbicacion.removeAllItems();
		
		for (UbicacionBean ubicacion : ubicaciones) {
			this.cmbUbicacion.addItem(ubicacion);
		}
	}	
	
	@Override
	public void mostrarInformacionTarjeta(TarjetaBean tarjeta) {
		logger.info(Mensajes.getMessage("VentanaParquimetroImpl.mostrarInformacionTarjeta.logger"), tarjeta.getId());
		this.getLblTarjetaInformacion().setText(this.setTextLblTarjetaInformacion(tarjeta));		
	}

	@Override
	public void mostrarIngresoEstacionamiento(EntradaEstacionamientoDTO entrada) {
		logger.info(Mensajes.getMessage("VentanaParquimetroImpl.mostrarIngresoEstacionamiento.logger"));
		this.getLblConexionResultado().setText(this.setTextLblConexionResultadoEntrada(entrada)); 
	}

	@Override
	public void mostrarSalidaEstacionamiento(SalidaEstacionamientoDTO salida) {
		logger.info(Mensajes.getMessage("VentanaParquimetroImpl.mostrarSalidaEstacionamiento.logger"));
		this.getLblConexionResultado().setText(this.setTextLblConexionResultadoSalida(salida)); 
	}

	/*
	 * Metodo encargado de iniciar la ventana y sus componentes
	 */
	private void inicializar()
	{ 
		logger.debug(Mensajes.getMessage("VentanaParquimetroImpl.inicializar.logger"));
		
		this.frame = new JFrame();
		this.frame.setResizable(false);
		this.frame.setTitle(Mensajes.getMessage("VentanaParquimetroImpl.inicializar.frameTitle"));
		this.frame.setBounds(20, 20, 850, 400);
		/*
		 * Se evita el comportamiento por defecto que hace un dispose de la ventana y cierra, porque 
		 * se requiere que el controlador se haga cargo de cerrar todo, para cerrar las conexiones a la BD y luego
		 * las ventanas.
		 */
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.addWindowListener(new WindowAdapter() {
			@Override
		    public void windowClosing(WindowEvent we){
				controlador.salirAplicacion();
		    }
		});
	
		this.frame.getContentPane().setLayout(new BorderLayout(1,0));
		this.frame.getContentPane().add(this.crearPanelConectarParquimetro(),BorderLayout.CENTER);
		this.frame.setJMenuBar(this.crearMenuOpciones());
		
		this.registrarEventos();
	}	
	
	private JMenuBar crearMenuOpciones() {
		JMenuBar barraDeMenu = new JMenuBar();
		JMenu menuOpciones=new JMenu(Mensajes.getMessage("VentanaParquimetroImpl.crearMenuOpciones.menuOpciones"));
		menuOpciones.setFont(new Font("Segoe UI", Font.BOLD, 14));
		barraDeMenu.add(menuOpciones);

		this.mntmCerrarSesion = new JMenuItem(Mensajes.getMessage("VentanaParquimetroImpl.crearMenuOpciones.mntmCerrarSesion"));
		this.mntmCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuOpciones.add(this.mntmCerrarSesion);

		this.mntmSalir = new JMenuItem(Mensajes.getMessage("VentanaParquimetroImpl.crearMenuOpciones.mntmSalir"));
		this.mntmSalir.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuOpciones.add(this.mntmSalir);
		
		return barraDeMenu;		
	}

	/**
	 * Crea el panel para la conexión al parquímetro
	 * 
	 * @return Panel
	 */
	private Component crearPanelConectarParquimetro() {
		
		logger.info(Mensajes.getMessage("VentanaParquimetroImpl.crearPanelConectarParquimetro.logger"));		
		
		this.panelInterno = new JPanel();
		this.panelInterno.setLayout(null);
		
		/*
		 * Combo de tarjeta y luego su info
		 */
		JLabel lblTarjeta = new JLabel(Mensajes.getMessage("VentanaParquimetroImpl.crearPanelSeleccionaUbicacion.lblTarjeta"));
		lblTarjeta.setBounds(20, 30, 70, 16);
		this.panelInterno.add(lblTarjeta);
		
		this.cmbTarjeta = new JComboBox<TarjetaBean>();
		this.cmbTarjeta.setBounds(100, 30, 240, 20);
		this.panelInterno.add(this.cmbTarjeta);
	
		/*
		 * Combo de Ubicacion y Parquimetro
		 * Boton de Conexion
		 */
		JLabel lblUbicacion = new JLabel(Mensajes.getMessage("VentanaParquimetroImpl.crearPanelConectarParquimetro.lblUbicacion"));
		lblUbicacion.setBounds(20, 60, 70, 16);
		this.panelInterno.add(lblUbicacion);
		
		this.cmbUbicacion = new JComboBox<UbicacionBean>();
		this.cmbUbicacion.setBounds(100, 60, 240, 20);
		this.panelInterno.add(this.cmbUbicacion);

		JLabel lblParquimetro = new JLabel(Mensajes.getMessage("VentanaParquimetroImpl.crearPanelConectarParquimetro.lblParquimetro"));
		lblParquimetro.setBounds(20, 90, 80, 16);
		this.panelInterno.add(lblParquimetro);

		this.cmbParquimetro = new JComboBox<ParquimetroBean>();
		this.cmbParquimetro.setBounds(100, 90, 240, 20);
		this.panelInterno.add(this.cmbParquimetro);

		this.btnConectarParquimetro = new JButton(Mensajes.getMessage("VentanaParquimetroImpl.crearPanelConectarParquimetro.btnConectarParquimetro"));
		this.btnConectarParquimetro.setBounds(100, 140, 210, 20);
		this.panelInterno.add(this.btnConectarParquimetro);

		/*		  
		 * Info de Resultado
		 * 
		 */ 
		this.lblTarjetaInformacion = new JLabel(setTextLblTarjetaInformacion(null),JLabel.LEFT);
		this.lblTarjetaInformacion.setVerticalAlignment(JLabel.TOP);
		this.lblTarjetaInformacion.setBounds(370, 30, 480, 300);
		this.panelInterno.add(this.lblTarjetaInformacion);

		this.lblConexionResultado = new JLabel("",JLabel.LEFT);
		this.lblConexionResultado.setVerticalAlignment(JLabel.TOP);
		this.lblConexionResultado.setBounds(370, 160, 480, 300);
		this.panelInterno.add(this.lblConexionResultado);
		
		return this.panelInterno;
	} 

	private String setTextLblTarjetaInformacion(TarjetaBean tarjeta) {
		String s = "";
		if (tarjeta == null) {
			s = "<html><strong>" + Mensajes.getMessage("VentanaParquimetroImpl.setTextLblTarjetaInformacion.null") + "</strong></html>";
		} else {
			s = "<html>" + "<strong><span style='color:blue;'>" + " Id: "+ String.valueOf(tarjeta.getId()) + "</span></strong><br>";
			
			String color = (tarjeta.getSaldo() < 0)? "red":"blue";
			s += "<strong><span style='color:" + color + ";'>" + Mensajes.getMessage("VentanaParquimetroImpl.setTextLblTarjetaInformacion.saldo") + " $ "+ tarjeta.getSaldo() + "</span></strong><br>"; 
			
			if (tarjeta.getTipoTarjeta() != null) {
				s += Mensajes.getMessage("VentanaParquimetroImpl.setTextLblTarjetaInformacion.tipoTarjeta") + tarjeta.getTipoTarjeta().getTipo() + "<br>";
				s += Mensajes.getMessage("VentanaParquimetroImpl.setTextLblTarjetaInformacion.tipoTarjeta.descuento") + String.valueOf(tarjeta.getTipoTarjeta().getDescuento() * 100) + "%<br>";
			}

			if (tarjeta.getAutomovil() != null) {
				s += Mensajes.getMessage("VentanaParquimetroImpl.setTextLblTarjetaInformacion.automovil.patente") + tarjeta.getAutomovil().getPatente() + " - ";
				s += Mensajes.getMessage("VentanaParquimetroImpl.setTextLblTarjetaInformacion.automovil.marca") + tarjeta.getAutomovil().getMarca() + " - ";
				s += Mensajes.getMessage("VentanaParquimetroImpl.setTextLblTarjetaInformacion.automovil.modelo") + tarjeta.getAutomovil().getModelo() + " - ";
				s += Mensajes.getMessage("VentanaParquimetroImpl.setTextLblTarjetaInformacion.automovil.color") + tarjeta.getAutomovil().getColor() + "<br>";

				if (tarjeta.getAutomovil().getConductor() != null) {
					s += Mensajes.getMessage("VentanaParquimetroImpl.setTextLblTarjetaInformacion.conductor.apellidoYnombre") +  
							tarjeta.getAutomovil().getConductor().getApellido() + ", " + tarjeta.getAutomovil().getConductor().getNombre() + "<br>";
				}				
			}

			s += "</html>";
		}
		return s;
	}	
	
	private String setTextLblConexionResultadoEntrada(EntradaEstacionamientoDTO entrada) {
		String s = "";
		if (entrada != null) {
			s = "<html>";
			s += "<strong><span style='color:green;'>" + Mensajes.getMessage("VentanaParquimetroImpl.setTextLblConexionResultadoEntrada.ingreso") + "</span></strong><br><br>";
			s += "<strong><span style='color:blue;'>" + String.format(Mensajes.getMessage("VentanaParquimetroImpl.setTextLblConexionResultadoEntrada.tiempoDisponible"),entrada.getTiempoDisponible()) + "</span></strong><br><br>";
			s += Mensajes.getMessage("VentanaParquimetroImpl.setTextLblConexionResultadoEntrada.fechaIngreso") + " " + entrada.getFechaEntrada() + "<br>";
			s += Mensajes.getMessage("VentanaParquimetroImpl.setTextLblConexionResultadoEntrada.horaIngreso") + " " + entrada.getHoraEntrada();
			s += "</html>";
		}
		return s;
	}

	private String setTextLblConexionResultadoSalida(SalidaEstacionamientoDTO salida) {
		String s = "";
		if (salida != null) {
			s = "<html>";
			s += "<strong><span style='color:green;'>" + Mensajes.getMessage("VentanaParquimetroImpl.setTextLblConexionResultadoSalida.salida") + "</span></strong><br><br>";
			s += "<strong><span style='color:blue;'>" + String.format(Mensajes.getMessage("VentanaParquimetroImpl.setTextLblConexionResultadoSalida.tiempoTranscurrido"),salida.getTiempoTranscurrido()) + "</span></strong><br>";
			s += "<strong><span style='color:blue;'>" + Mensajes.getMessage("VentanaParquimetroImpl.setTextLblConexionResultadoSalida.saldo") + salida.getSaldoTarjeta() + "</span></strong><br><br>";
			s += Mensajes.getMessage("VentanaParquimetroImpl.setTextLblConexionResultadoSalida.fechaIngreso") + " " + salida.getFechaEntrada() + "<br>";
			s += Mensajes.getMessage("VentanaParquimetroImpl.setTextLblConexionResultadoSalida.horaIngreso") + " " + salida.getHoraEntrada()+ "<br>";
			s += Mensajes.getMessage("VentanaParquimetroImpl.setTextLblConexionResultadoSalida.fechaSalida") + " " + salida.getFechaSalida() + "<br>";
			s += Mensajes.getMessage("VentanaParquimetroImpl.setTextLblConexionResultadoSalida.horaSalida") + " " + salida.getHoraSalida();
			s += "</html>";
		}
		return s;
	}
	
	/*
	 * Getters de los componentes
	 * 
	 */
	private AbstractButton getMenuItemCerrarSesion() {
		return this.mntmCerrarSesion;
	}
	
	private AbstractButton getMenuItemSalir() {
		return this.mntmSalir;
	}	

	private AbstractButton getBtnConectarParquimetro() {
		return this.btnConectarParquimetro;
	}

	private JComboBox<TarjetaBean> getCmbTarjeta(){
		return this.cmbTarjeta; 
	}		
	
	private JComboBox<ParquimetroBean> getCmbParquimetro(){
		return this.cmbParquimetro;
	}
	
	private JComboBox<UbicacionBean> getCmbUbicacion(){
		return this.cmbUbicacion; 
	}		
	
	private JLabel getLblTarjetaInformacion(){
		return this.lblTarjetaInformacion;
	}

	private JLabel getLblConexionResultado(){
		return this.lblConexionResultado;
	}

	private void registrarEventos() {
		
		logger.debug(Mensajes.getMessage("VentanaParquimetroImpl.registrarEventos.logger"));
		
		this.getMenuItemCerrarSesion().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controlador.cerrarSesion();
			}
		});
			
		this.getMenuItemSalir().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controlador.salirAplicacion();
			}
		});

		this.cmbUbicacion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getCmbUbicacion().getSelectedItem() != null) {
					logger.info(Mensajes.getMessage("VentanaParquimetroImpl.registrarEventos.cmbUbicacion.listener"),getCmbUbicacion().getSelectedItem().toString());
					controlador.cambiarUbicacion((UbicacionBean) getCmbUbicacion().getSelectedItem());
				}
			};
		});
		
		this.cmbTarjeta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getCmbTarjeta().getSelectedItem() != null) {
					logger.info(Mensajes.getMessage("VentanaParquimetroImpl.registrarEventos.cmbTarjeta.listener"),getCmbTarjeta().getSelectedItem().toString());
					controlador.cambiarTarjeta((TarjetaBean) getCmbTarjeta().getSelectedItem());
				}
			};
		});		
		
		
		/*
		 * Eventos de botones
		 */
			
		this.getBtnConectarParquimetro().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info(Mensajes.getMessage("VentanaParquimetroImpl.registrarEventos.getBtnConectarParquimetro.listener"));
				
				controlador.conectarParquimetro((ParquimetroBean) getCmbParquimetro().getSelectedItem(),
												(TarjetaBean) getCmbTarjeta().getSelectedItem());				
			};
		});
			
	}

	@Override
	public void limpiarEstacionamientoResultado() {
		this.getLblConexionResultado().setText("");		
	}

}
