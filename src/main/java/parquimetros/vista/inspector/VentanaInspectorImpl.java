package parquimetros.vista.inspector;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.controlador.Controlador;
import parquimetros.controlador.inspector.ControladorInspector;
import parquimetros.modelo.beans.InspectorBean;
import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.UbicacionBean;
import parquimetros.modelo.inspector.dto.EstacionamientoPatenteDTO;
import parquimetros.modelo.inspector.dto.MultaPatenteDTO;
import parquimetros.utils.Mensajes;

//CLASE IMPLEMENTADA PROVISTA POR LA CATEDRA
public class VentanaInspectorImpl extends JFrame implements VentanaInspector {

	private static Logger logger = LoggerFactory.getLogger(VentanaInspectorImpl.class);

	private static final long serialVersionUID = 1L;
	
	/*
	 * Propiedades 
	 */	
	protected ControladorInspector controlador;
	
	protected VentanaInspector ventana;
	protected JFrame frame;
	
	protected CardLayout frameLayout;
	/** 
	 * Panel que contiene las diferentes pantallas a mostrar segun la opcion elegida
	 */
	protected JPanel panelInterno;
	protected JMenuItem mntmCerrarSesion;

	protected JMenuItem mntmSalir;
	protected JLabel lblInspector;	

	protected JLabel lblParquimetroUbicacion;	
	/*
	 * Componentes
	 */
	protected JPanel panelEstacionamientosMultas;

	protected JTextField txtPatente;	
	protected JComboBox<UbicacionBean> cmbUbicacion;
	protected JComboBox<ParquimetroBean> cmbParquimetro;
	protected JButton btnConectarParquimetro;
	protected JButton btnAgregarPatente;
	protected JButton btnQuitarPatente;
	protected JButton btnLimpiarLista;	
	protected JButton btnRegistrarMultas;
	protected JButton btnVolverMenuOpciones;
	
	protected JTable tablaPatentesEstacionadas;
	protected JTable tablaMultasGeneradas;
	
	/**
	 * Modelos para las tablas de Comprobar Estacionamiento y Multas
	 */
	protected DefaultTableModel modeloTablaMultaComprobarEstacionamiento;
	
	protected DefaultTableModel modeloTablaMultaGenerarMultas;	

	/**
	 * Constructor
	 */
	public VentanaInspectorImpl() {		
		inicializar();
		this.frame.setVisible(true);
	}
	
	/*
	 * Métodos publicos
	 */
	@Override
	public void registrarControlador(Controlador c) {
        if (c instanceof ControladorInspector) {
            this.controlador = (ControladorInspector) c;
        } else {
            throw new IllegalArgumentException("Se esperaba un ControladorInspector");
        }	
    }
	
	@Override
	public void eliminarVentana() {
		logger.info(Mensajes.getMessage("VentanaInspectorImpl.eliminarVentana.logger"));
		this.frame.dispose();
	}	

	@Override
	public void informar(String mensaje) {
		logger.info(Mensajes.getMessage("VentanaInspectorImpl.informar.logger"),mensaje);		
		JOptionPane.showMessageDialog(this.frame,mensaje);
	}

	@Override
	public void mostrarInspectorLogueado(InspectorBean inspector) {
		logger.info(Mensajes.getMessage("VentanaInspectorImpl.mostrarInspectorLogueado.logger"), inspector.getApellido(), inspector.getNombre(), inspector.getLegajo());
		
		this.lblInspector.setText(Mensajes.getMessage("VentanaInspectorImpl.mostrarInspectorLogueado.lblInspector") + 
					String.valueOf(inspector.getLegajo()) + " - " +	inspector.getApellido() + ", " + inspector.getNombre());
	}	

	@Override
	public void mostrarPanelDeEstacionamientosMultas(String panel) { 
		((CardLayout) this.panelEstacionamientosMultas.getLayout()).show(this.panelEstacionamientosMultas, panel);
		if (panel.equals(VentanaInspector.MULTAS_SELECCIONA_UBICACION)) {
			this.getBtnVolverMenuOpciones().setVisible(false);
		} else {
			this.getBtnVolverMenuOpciones().setVisible(true);   // muestra el boton de volver
			this.habilitarBotones();
		}
	}	
	
	@Override
	public void mostrarPanelPrincipal(String panel) {
		this.frameLayout.show(this.panelInterno, panel);
	}
	
	@Override
	public void mostrarParquimetroConectado(ParquimetroBean parquimetro) {
		logger.info(Mensajes.getMessage("VentanaInspectorImpl.mostrarParquimetroConectado.logger"));
		this.lblParquimetroUbicacion.setText(
				Mensajes.getMessage("VentanaInspectorImpl.mostrarParquimetroConectado.lblParquimetroUbicacion1") + 
				" " + String.valueOf(parquimetro.getNumero()) + 
				" " + Mensajes.getMessage("VentanaInspectorImpl.mostrarParquimetroConectado.lblParquimetroUbicacion2") +
				" " + parquimetro.getUbicacion().toString());
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
	public void poblarParquimetros(ArrayList<ParquimetroBean> parquimetros) {
		logger.info(Mensajes.getMessage("VentanaInspectorImpl.poblarParquimetros.logger"), parquimetros.size());

		this.cmbParquimetro.removeAllItems();
		
		for (ParquimetroBean parquimetro : parquimetros) {
			this.cmbParquimetro.addItem(parquimetro);
		}
	}
	
	@Override
	public void poblarUbicaciones(ArrayList<UbicacionBean> ubicaciones) {
		logger.info(Mensajes.getMessage("VentanaInspectorImpl.poblarUbicaciones.logger"), ubicaciones.size());

		this.cmbUbicacion.removeAllItems();
		
		for (UbicacionBean ubicacion : ubicaciones) {
			this.cmbUbicacion.addItem(ubicacion);
		}
	}	
	
	@Override
	public void actualizarListaPatentesEstacionadas(ArrayList<EstacionamientoPatenteDTO> listaPatentesEstacionadas) {

		logger.info(Mensajes.getMessage("VentanaInspectorImpl.actualizarListaPatentesEstacionadas.logger"), listaPatentesEstacionadas.size());
		
		this.poblarTablaPatentesEstacionadas(this.modeloTablaMultaComprobarEstacionamiento, listaPatentesEstacionadas);

		this.txtPatente.setText("");
	}
	

	@Override
	public void actualizarListaMultas(ArrayList<MultaPatenteDTO> multas) {
		
		logger.info(Mensajes.getMessage("VentanaInspectorImpl.actualizarListaMultas.logger"), multas.size());
		
		this.poblarTablaMultaGenerarMultas(this.modeloTablaMultaGenerarMultas, multas);

		this.dehabilitarBotones();
	}	
	
	
	/*
	 * Metodo encargado de iniciar la ventana y sus componentes
	 */
	private void inicializar()
	{ 
		logger.debug(Mensajes.getMessage("VentanaInspectorImpl.inicializar.logger"));
		
		this.frame = new JFrame();
		this.frame.setResizable(false);
		this.frame.setTitle(Mensajes.getMessage("VentanaInspectorImpl.inicializar.frameTitle"));
		this.frame.setBounds(20, 20, 1200, 600);
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
		
		this.frameLayout = new CardLayout();

		this.panelInterno = new JPanel();
		this.panelInterno.setLayout(this.frameLayout);
		this.panelInterno.add(this.crearPanelVacio(),VentanaInspector.VACIO);   // Por si se desea que inicialmente aparezca un panel vacío y que tenga que seleccionar una opcion para ingresar datos
		this.panelInterno.add(this.crearPanelEstacionamientosMultas(),VentanaInspector.ESTACIONAMIENTOS_Y_MULTAS);
		
		this.frame.getContentPane().setLayout(new BorderLayout(1,0));
		this.frame.getContentPane().add(panelInterno,BorderLayout.CENTER);
		this.frame.getContentPane().add(this.crearEncabezado(),BorderLayout.NORTH);
		this.frame.setJMenuBar(this.crearMenuOpciones());
		
		this.registrarEventos();
		
		// Mostrar el panel por defecto
	}	
	
	private Component crearEncabezado() {
		JPanel panelEncabezado = new JPanel();		
		panelEncabezado.setLayout(new BorderLayout());
		panelEncabezado.setBorder(new EmptyBorder(5, 15, 5, 15));
		
		this.lblInspector = new JLabel("");
		this.lblInspector.setHorizontalAlignment(SwingConstants.RIGHT);

		this.lblParquimetroUbicacion = new JLabel("");
		this.lblParquimetroUbicacion.setHorizontalAlignment(SwingConstants.LEFT);

		panelEncabezado.add(this.lblParquimetroUbicacion, BorderLayout.WEST);
		panelEncabezado.add(this.lblInspector, BorderLayout.EAST);
		
		return panelEncabezado;
	}

	private JMenuBar crearMenuOpciones() {
		JMenuBar barraDeMenu = new JMenuBar();
		JMenu menuOpciones=new JMenu(Mensajes.getMessage("VentanaInspectorImpl.crearMenuOpciones.menuOpciones"));
		menuOpciones.setFont(new Font("Segoe UI", Font.BOLD, 14));
		barraDeMenu.add(menuOpciones);

		this.mntmCerrarSesion = new JMenuItem(Mensajes.getMessage("VentanaInspectorImpl.crearMenuOpciones.mntmCerrarSesion"));
		this.mntmCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuOpciones.add(this.mntmCerrarSesion);

		this.mntmSalir = new JMenuItem(Mensajes.getMessage("VentanaInspectorImpl.crearMenuOpciones.mntmSalir"));
		this.mntmSalir.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuOpciones.add(this.mntmSalir);
		
		return barraDeMenu;		
	}

	/** 
	 * Este panel tiene dos paneles internos, 
	 * uno para seleccionar la ubicación y el parquimetro donde se conecta el inspector llamado MULTAS_SELECCIONA_UBICACION
	 * y otro para cargar las patentes llamado MULTAS_CARGA_PATENTES
	 * 
	 * @return
	 */
	private Component crearPanelEstacionamientosMultas() {
		
		this.modeloTablaMultaComprobarEstacionamiento = this.inicializarModelo(this.getColumnasTablaMultaComprobarEstacionamiento());
		this.modeloTablaMultaGenerarMultas = this.inicializarModelo(this.getColumnasTablaMultaGenerarMultas());

		/*
		 * Contenedor de las distintas ventanas
		 */
		this.panelEstacionamientosMultas = new JPanel();
		this.panelEstacionamientosMultas.setLayout(new CardLayout());
		this.panelEstacionamientosMultas.add(this.crearPanelSeleccionaUbicacion(), VentanaInspector.MULTAS_SELECCIONA_UBICACION);
		this.panelEstacionamientosMultas.add(this.crearPanelCargaPatentes(), VentanaInspector.MULTAS_CARGA_PATENTES);
		
		/*
		 * Contenedor principal que tiene las ventanas y una botonera
		 */
		JPanel panelPpal = new JPanel();
		panelPpal.setLayout(new BorderLayout(0, 0));
		panelPpal.add(this.panelEstacionamientosMultas, BorderLayout.CENTER);
		panelPpal.add(this.crearPanelBotoneraVolverMenuPrincipal(), BorderLayout.SOUTH);		
		panelPpal.setVisible(false);		
		
		return panelPpal;
	}	

	private JPanel crearPanelBotoneraVolverMenuPrincipal() {
		JPanel panelBotonera = new JPanel();		
		panelBotonera.setLayout(new FlowLayout(FlowLayout.RIGHT));
	
		this.btnVolverMenuOpciones = new JButton(Mensajes.getMessage("VentanaInspectorImpl.crearPanelBotoneraVolverMenuPrincipal.btnVolverMenuOpciones"));
		panelBotonera.add(this.btnVolverMenuOpciones);
		
		return panelBotonera;
	}
				
	/**
	 * Crea el panel para cargar las patentes y verificar su estado
	 * 
	 * @return Panel
	 */
	private Component crearPanelCargaPatentes() {
		/*
		 * Panel principal
		 */
		JPanel panelCargaPatentes = new JPanel();
		panelCargaPatentes.setLayout(null);

		/*
		 * Titulo
		 */
		JLabel lblTitulo = new JLabel(Mensajes.getMessage("VentanaInspectorImpl.crearPanelCargaPatentes.lblTitulo"));
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
		lblTitulo.setBounds(470, 10, 380, 16);
		panelCargaPatentes.add(lblTitulo);
		
		/*
		 * Componentes
		 */
		JLabel lblPatente = new JLabel(Mensajes.getMessage("VentanaInspectorImpl.crearPanelCargaPatentes.lblPatente"));
		lblPatente.setBounds(25, 450, 60, 16);
		panelCargaPatentes.add(lblPatente);

		this.txtPatente = crearTextFieldPatente();
		this.txtPatente.setBounds(80, 450, 100, 20);
		panelCargaPatentes.add(this.txtPatente);

		this.btnAgregarPatente = new JButton(Mensajes.getMessage("VentanaInspectorImpl.crearPanelCargaPatentes.btnAgregarPatente"));
		this.btnAgregarPatente.setBounds(185, 450, 130, 20);
		panelCargaPatentes.add(this.btnAgregarPatente);

		this.btnQuitarPatente = new JButton(Mensajes.getMessage("VentanaInspectorImpl.crearPanelCargaPatentes.btnQuitarPatente"));
		this.btnQuitarPatente.setBounds(70, 390, 130, 20);
		panelCargaPatentes.add(this.btnQuitarPatente);

		this.btnLimpiarLista = new JButton(Mensajes.getMessage("VentanaInspectorImpl.crearPanelCargaPatentes.btnLimpiarLista"));
		this.btnLimpiarLista.setBounds(270, 390, 130, 20);
		panelCargaPatentes.add(this.btnLimpiarLista);

		this.btnRegistrarMultas = new JButton(Mensajes.getMessage("VentanaInspectorImpl.crearPanelCargaPatentes.btnRegistrarMultas"));
		this.btnRegistrarMultas.setBounds(600, 390, 130, 20);
		panelCargaPatentes.add(this.btnRegistrarMultas);
		
		JLabel lblListaPatentes = new JLabel(Mensajes.getMessage("VentanaInspectorImpl.crearPanelCargaPatentes.lblListaPatentes"));
		lblListaPatentes.setBounds(20, 50, 200, 16);
		panelCargaPatentes.add(lblListaPatentes);
		panelCargaPatentes.add(this.crearPanelTablaPatentesEstacionadas());
		
		JLabel lblListaMultas = new JLabel(Mensajes.getMessage("VentanaInspectorImpl.crearPanelCargaPatentes.lblListaMultas"));
		lblListaMultas.setBounds(608, 50, 200, 16);
		panelCargaPatentes.add(lblListaMultas);
		panelCargaPatentes.add(this.crearPanelTablaMultas());
		
		return panelCargaPatentes;
	}
	
	private void dehabilitarBotones() {
		this.getBtnAgregarPatente().setEnabled(false);
		this.getBtnLimpiarLista().setEnabled(false);
		this.getBtnQuitarPatente().setEnabled(false);
		this.getBtnRegistrarMultas().setEnabled(false);
	}

	private void habilitarBotones() {
		this.getBtnAgregarPatente().setEnabled(true);
		this.getBtnLimpiarLista().setEnabled(true);
		this.getBtnQuitarPatente().setEnabled(true);
		this.getBtnRegistrarMultas().setEnabled(true);
	}
	
	/*
	 * Se crea este metodo porque la patente no es un componente tipico sino que se le agrega la verificacion
	 * por expresión regular en su ingreso de datos.
	 * 
	 */
	private JTextField crearTextFieldPatente() {
		JTextField patenteTextField = new JTextField(6);
		patenteTextField.setDocument(new PatenteVistaComponente(6));
		return patenteTextField;
	}

	private Component crearPanelSeleccionaUbicacion() {
		
		logger.info(Mensajes.getMessage("VentanaInspectorImpl.crearPanelSeleccionaUbicacion.logger"));
		
		JPanel panelSeleccionUbicacion = new JPanel();
		panelSeleccionUbicacion.setLayout(null);

		JLabel lblUbicacion = new JLabel(Mensajes.getMessage("VentanaInspectorImpl.crearPanelSeleccionaUbicacion.lblUbicacion"));
		lblUbicacion.setBounds(120, 160, 70, 16);
		panelSeleccionUbicacion.add(lblUbicacion);
		
		this.cmbUbicacion = new JComboBox<UbicacionBean>();
		this.cmbUbicacion.setBounds(200, 160, 140, 20);
		panelSeleccionUbicacion.add(this.cmbUbicacion);

		JLabel lblParquimetro = new JLabel(Mensajes.getMessage("VentanaInspectorImpl.crearPanelSeleccionaUbicacion.lblParquimetro"));
		lblParquimetro.setBounds(120, 190, 80, 16);
		panelSeleccionUbicacion.add(lblParquimetro);

		this.cmbParquimetro = new JComboBox<ParquimetroBean>();
		this.cmbParquimetro.setBounds(200, 190, 140, 20);
		panelSeleccionUbicacion.add(this.cmbParquimetro);

		this.btnConectarParquimetro = new JButton(Mensajes.getMessage("VentanaInspectorImpl.crearPanelSeleccionaUbicacion.btnConectarParquimetro"));
		this.btnConectarParquimetro.setBounds(120, 240, 210, 20);
		panelSeleccionUbicacion.add(this.btnConectarParquimetro);
		
		return panelSeleccionUbicacion;
	}
	
	/**
	 * Crea un panel con la tabla de patentes que carga el inspector
	 */
	private Component crearPanelTablaMultas() {
		
		this.tablaMultasGeneradas = this.crearTabla(this.modeloTablaMultaGenerarMultas);
/* SACAR?
		tablaMultasGeneradas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (! e.getValueIsAdjusting()){   // Porque sino se dispara multiples veces
					logger.debug("Se ha seleccionado una fila de la tabla. ");
					
					// if (tablaMultasGeneradas.getSelectedRow() != -1) { }
				}
			}
		});
*/
		JPanel panelMultasGeneradas = new JPanel();
		panelMultasGeneradas.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panelMultasGeneradas.setLayout(new GridLayout(1, 1, 0, 0));
		panelMultasGeneradas.add(new JScrollPane(tablaMultasGeneradas));
		panelMultasGeneradas.setBounds(600, 80, 560, 300);
		
		return panelMultasGeneradas;		
	}	
	
	/**
	 * Crea un panel con la tabla de patentes que carga el inspector
	 */
	private Component crearPanelTablaPatentesEstacionadas() {
		
		this.tablaPatentesEstacionadas = this.crearTabla(this.modeloTablaMultaComprobarEstacionamiento);
/*
		tablaPatentesEstacionadas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (! e.getValueIsAdjusting()){   // Porque sino se dispara multiples veces
					logger.debug("Se ha seleccionado una fila de la tabla. ");
					
					// if (tablaPatentesEstacionadas.getSelectedRow() != -1) { }
				}
			}
		});
*/
		JPanel panelPatentesEstacionadas = new JPanel();
		panelPatentesEstacionadas.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panelPatentesEstacionadas.setLayout(new GridLayout(1, 1, 0, 0));
		panelPatentesEstacionadas.add(new JScrollPane(this.tablaPatentesEstacionadas));
		panelPatentesEstacionadas.setBounds(12, 80, 560, 300);
		
		return panelPatentesEstacionadas;		
	}

	private Component crearPanelVacio() {
		JPanel panelPpal = new JPanel();
		panelPpal.setLayout(new BorderLayout(0, 0));
		panelPpal.setVisible(false);		
		
		return panelPpal;			
	}
		
	private JTable crearTabla(DefaultTableModel model) {
		JTable tabla = new JTable(model) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);
				Color foreground, background;
				Color color = new Color(255, 232, 219);
				Color seleccion = new Color(219, 219, 219);

				if (isRowSelected(row)) {
					foreground = Color.black;
					background = seleccion;
				} else {
					if (row % 2 != 0) {
		               foreground = Color.black;
		               background = Color.white;
					} else {
		               foreground = Color.black;
		               background = color;
					}
				}
		        c.setForeground(foreground);
		        c.setBackground(background);
		            
		        return c;
			}
		};	
		
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabla.setAutoCreateRowSorter(true);		
		tabla.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return tabla;
	}
	
	private JTextField getTxtPatente() {
		return this.txtPatente;
	}
	
	private AbstractButton getBtnVolverMenuOpciones() {
		return this.btnVolverMenuOpciones;
	}
	
	private AbstractButton getBtnConectarParquimetro() {
		return this.btnConectarParquimetro;
	}

	private AbstractButton getBtnAgregarPatente() {
		return this.btnAgregarPatente;
	}

	private AbstractButton getBtnQuitarPatente() {
		return this.btnQuitarPatente;
	}

	private AbstractButton getBtnLimpiarLista() {
		return this.btnLimpiarLista;
	}

	private AbstractButton getBtnRegistrarMultas() {
		return this.btnRegistrarMultas;
	}
	
	private JComboBox<ParquimetroBean> getCmbParquimetro(){
		return this.cmbParquimetro;
	}
	
	private JComboBox<UbicacionBean> getCmbUbicacion(){
		return this.cmbUbicacion;
	}		
	
	/** 
	 * 
	 * @return La lista de columnas con la que se inicializa la tabla donde se muestran los estados de las patentes ingresadas
	 * 
	 *  Estados: Registrado, No registrado (no tiene estacionamiento abierto), Registrado en otra ubicación
	 */
	private ArrayList<String> getColumnasTablaMultaComprobarEstacionamiento() {
		ArrayList<String> columnas = new ArrayList<String>();
		columnas.add(VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_PATENTE);
		columnas.add(VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_CALLE);
		columnas.add(VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_ALTURA);
		columnas.add(VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_FECHA_ENTRADA);
		columnas.add(VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_HORA_ENTRADA);
		columnas.add(VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_ESTADO);
		return columnas;
	}	

	private ArrayList<String> getColumnasTablaMultaGenerarMultas() {
		ArrayList<String> columnas = new ArrayList<String>();
		columnas.add(VentanaInspector.TABLA_GENERAR_MULTA_PATENTE);
		columnas.add(VentanaInspector.TABLA_GENERAR_MULTA_CALLE);
		columnas.add(VentanaInspector.TABLA_GENERAR_MULTA_ALTURA);
		columnas.add(VentanaInspector.TABLA_GENERAR_MULTA_NRO_MULTA);
		columnas.add(VentanaInspector.TABLA_GENERAR_MULTA_FECHA_MULTA);
		columnas.add(VentanaInspector.TABLA_GENERAR_MULTA_HORA_MULTA);
		columnas.add(VentanaInspector.TABLA_GENERAR_MULTA_LEGAJO);
		return columnas;
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

	private DefaultTableModel inicializarModelo(ArrayList<String> column) {
		DefaultTableModel modelo = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		// Inicializa las columnas
		for (String col : column ) {
			modelo.addColumn(col);
		}
		return modelo;
	}
	
	private void limpiarPanelCargaPatentes() {
		this.limpiarTablas();
		this.lblParquimetroUbicacion.setText("");
	}
	
	private void limpiarTablas() {
		this.modeloTablaMultaGenerarMultas.setRowCount(0);
		this.modeloTablaMultaComprobarEstacionamiento.setRowCount(0);
	}

	private void poblarTablaPatentesEstacionadas(DefaultTableModel modelo, ArrayList<EstacionamientoPatenteDTO> estacionamientos) {
		logger.info(Mensajes.getMessage("VentanaInspectorImpl.poblarTablaPatentesEstacionadas.logger"));

		modelo.setRowCount(0);
		
		for (EstacionamientoPatenteDTO unEstacionamiento: estacionamientos) {
			
			String[] fila = new String[modelo.getColumnCount()];
			
			fila[modelo.findColumn(VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_PATENTE)] = unEstacionamiento.getPatente();
			fila[modelo.findColumn(VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_CALLE)] = unEstacionamiento.getCalle();
			fila[modelo.findColumn(VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_ALTURA)] = unEstacionamiento.getAltura();
			fila[modelo.findColumn(VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_FECHA_ENTRADA)] = unEstacionamiento.getFechaEntrada();
			fila[modelo.findColumn(VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_HORA_ENTRADA)] = unEstacionamiento.getHoraEntrada();
			fila[modelo.findColumn(VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_ESTADO)] = unEstacionamiento.getEstado();
			
			modelo.addRow(fila); 		
		}
	}
	
	
	private void poblarTablaMultaGenerarMultas(DefaultTableModel modelo, ArrayList<MultaPatenteDTO> multas) {
		logger.info(Mensajes.getMessage("VentanaInspectorImpl.poblarTablaMultaGenerarMultas.logger"));
		
		modelo.setRowCount(0);
		
		for (MultaPatenteDTO unaMulta: multas) {
			
			String[] fila = new String[modelo.getColumnCount()];
			
			fila[modelo.findColumn(VentanaInspector.TABLA_GENERAR_MULTA_NRO_MULTA)] = unaMulta.getNroMulta();
			fila[modelo.findColumn(VentanaInspector.TABLA_GENERAR_MULTA_PATENTE)] = unaMulta.getPatente();
			fila[modelo.findColumn(VentanaInspector.TABLA_GENERAR_MULTA_CALLE)] = unaMulta.getCalle();
			fila[modelo.findColumn(VentanaInspector.TABLA_GENERAR_MULTA_ALTURA)] = unaMulta.getAltura();
			fila[modelo.findColumn(VentanaInspector.TABLA_GENERAR_MULTA_FECHA_MULTA)] = unaMulta.getFechaMulta();
			fila[modelo.findColumn(VentanaInspector.TABLA_GENERAR_MULTA_HORA_MULTA)] = unaMulta.getHoraMulta();
			fila[modelo.findColumn(VentanaInspector.TABLA_GENERAR_MULTA_LEGAJO)] = unaMulta.getLegajo();
			
			modelo.addRow(fila); 		
		}
		
	}


	private void registrarEventos() {
		
		logger.debug(Mensajes.getMessage("VentanaInspectorImpl.registrarEventos.logger"));
		
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
					logger.info(Mensajes.getMessage("VentanaInspectorImpl.registrarEventos.cmbUbicacion.listener"),getCmbUbicacion().getSelectedItem().toString());
					controlador.cambiarUbicacion((UbicacionBean) getCmbUbicacion().getSelectedItem());
				}
			};
		});		
		
		/*
		 * Eventos de botones
		 */
			
		this.getBtnConectarParquimetro().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info(Mensajes.getMessage("VentanaInspectorImpl.registrarEventos.getBtnConectarParquimetro.listener"));
			
				controlador.conectarParquimetro((ParquimetroBean) getCmbParquimetro().getSelectedItem());				
			};
		});
		
		this.getTxtPatente().addActionListener(getAgregarPatenteListener());
		this.getBtnAgregarPatente().addActionListener(getAgregarPatenteListener());
		
		this.getBtnQuitarPatente().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info(Mensajes.getMessage("VentanaInspectorImpl.registrarEventos.getBtnQuitarPatente.listener"));
							
				if (tablaPatentesEstacionadas.getSelectedRowCount() > 0) {
					ArrayList<String> listaPatentesQuitar = new ArrayList<String>(); 					
					for (int indice : tablaPatentesEstacionadas.getSelectedRows()) {
						String patente = (String) tablaPatentesEstacionadas.getValueAt(indice,modeloTablaMultaComprobarEstacionamiento.findColumn(TABLA_COMPROBAR_ESTACIONAMIENTO_PATENTE));						
						listaPatentesQuitar.add(patente);						
					}
					controlador.quitarPatentesEstacionadas(listaPatentesQuitar);
				} else {
					informar(Mensajes.getMessage("VentanaInspectorImpl.registrarEventos.getBtnQuitarPatente.informar"));					
				}				
			};
		});

		this.getBtnLimpiarLista().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info(Mensajes.getMessage("VentanaInspectorImpl.registrarEventos.getBtnLimpiarLista.listener"));
				controlador.limpiarListaPatentesEstacionadas();				
			};
		});

		this.getBtnRegistrarMultas().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info(Mensajes.getMessage("VentanaInspectorImpl.registrarEventos.getBtnRegistrarMultas.listener"));
				
				if (tablaPatentesEstacionadas.getRowCount() > 0) {
					ArrayList<String> listaPatentes = new ArrayList<String>(); 					
					for (int indice = 0; indice < tablaPatentesEstacionadas.getRowCount(); indice++) {
						String patente = (String) tablaPatentesEstacionadas.getValueAt(indice,modeloTablaMultaComprobarEstacionamiento.findColumn(TABLA_COMPROBAR_ESTACIONAMIENTO_PATENTE));						
						listaPatentes.add(patente);						
					}
					controlador.registraMultas(listaPatentes);
				} else {
					informar(Mensajes.getMessage("VentanaInspectorImpl.registrarEventos.getBtnRegistrarMultas.informar"));
				}
			}
		});		
		
		this.getBtnVolverMenuOpciones().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				limpiarPanelCargaPatentes();
				mostrarPanelDeEstacionamientosMultas(VentanaInspector.MULTAS_SELECCIONA_UBICACION);
			}
		});
		
	}
	
	private ActionListener getAgregarPatenteListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info(Mensajes.getMessage("VentanaInspectorImpl.registrarEventos.getBtnAgregarPatente.listener"));
				
				String patente = txtPatente.getText();
				
				//if (patente.matches("[A-Z]{3}[0-9]{3}")) { solo mayúsculas
				if (patente.matches("[A-Za-z]{3}[0-9]{3}")) {
					controlador.agregarPatente(patente.toUpperCase());
				} else {
					informar(Mensajes.getMessage("VentanaInspectorImpl.registrarEventos.getBtnAgregarPatente.informar"));
				}
			};
        };
	}

}
