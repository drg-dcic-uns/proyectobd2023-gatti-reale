package parquimetros.vista.login;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.controlador.Controlador;
import parquimetros.controlador.login.ControladorLogin;
import parquimetros.utils.Mensajes;

//CLASE IMPLEMENTADA PROVISTA POR LA CATEDRA
public class VentanaLoginImpl extends JFrame implements VentanaLogin, ItemListener {

	private static Logger logger = LoggerFactory.getLogger(VentanaLoginImpl.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public VentanaLoginImpl()
	{
		logger.info(Mensajes.getMessage("VentanaLoginImpl.constructor.logger"));
				
		this.inicializar();
	}

	@Override
	public void eliminarVentana() {
		logger.info(Mensajes.getMessage("VentanaLoginImpl.constructor.logger"));
		this.dispose();
	}

	@Override
	public void informar(String mensaje) {
		logger.info(Mensajes.getMessage("VentanaLoginImpl.informar.logger"), mensaje);
		
		JOptionPane.showMessageDialog(null,mensaje);
	}

	@Override
	public void mostrarVentana() throws Exception {
		this.setVisible(true);		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		// Cuando cambia de usuario muestra un diferente layout
		
		String opcion = (String) e.getItem();
		
		if (opcion.equals(ControladorLogin.INSPECTOR))
		{
			this.loginLayout.show(this.panelLogin, ControladorLogin.INSPECTOR);	
		} 
		else if (opcion.equals(ControladorLogin.PARQUIMETRO)) 
		{
			this.loginLayout.show(this.panelLogin, ControladorLogin.PARQUIMETRO);	
		} 
	}
	
	@Override
	public void poblarComboTipoUsuario(List<String> nombresUsuarios) {
		this.getComboTipoUsuario().removeAllItems();		
		for (String item: nombresUsuarios) {
			this.getComboTipoUsuario().addItem(item);
		}
	}
	
	private String getUsuarioSeleccionado() {
		return (String) this.getComboTipoUsuario().getSelectedItem();
	}	

	private String getUserName() {

		String username = null;
		
		if (this.getUsuarioSeleccionado().equals(ControladorLogin.INSPECTOR)) 
		{
			username = (String) this.getCampoInspectorUsername().getText();
		} 
		
		return username;
	}

	
	private char[] getPassword() {		

		char[] password = null;
		
		if (this.getUsuarioSeleccionado().equals(ControladorLogin.INSPECTOR)) 
		{
			password = this.getCampoInspectorPassword().getPassword();
		} 
		
		return password;
		
	}
	
	/*
	 * Propiedades y metodos privados y protegidos
	 * 
	 * 
	 */
	protected ControladorLogin controlador;
	 
	
	protected JPanel mainPanel;	

	protected JComboBox<String> comboTipoUsuario;
	protected JPanel panelLogin;	
	protected CardLayout loginLayout;	

	// Card Inspector
	protected JTextField campoInspectorUsername;
	protected JPasswordField campoInspectorPassword;
	
	protected JButton btnAceptarLogin;	
	protected JButton btnCancelarLogin;
	
	/**
	  * Método encargado de inicializar todos los componentes de la ventana para logguearse
	  * 
	  * BorderLayout
	  * 
	  * +--------------------------------------+
	  * |               PAGE_START             |
	  * +--------------+----------+------------+
	  * |              |          |            |
	  * |  LINE_START  |  CENTER  |  LINE_END  |
	  * |              |          |            |
	  * +--------------+----------+------------+
	  * |               PAGE_END               |
	  * +--------------+----------+------------+
	  * 
	  */
	private void inicializar()
	{
		this.setType(Type.POPUP);
		this.setTitle("VentanaLoginImpl.inicializar.title");
		this.setResizable(false);
		this.setBounds(100, 100, 406, 250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BorderLayout());
		this.mainPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
				
		this.mainPanel.add(this.crearPanelTipoUsuario(), BorderLayout.PAGE_START);
		this.mainPanel.add(this.crearPanelLogin(), BorderLayout.CENTER);		
		this.mainPanel.add(this.crearPanelButtons(), BorderLayout.PAGE_END);

		logger.debug(Mensajes.getMessage("VentanaLoginImpl.inicializar.registrarListener"));
		this.registrarEventos();
		
		this.setContentPane(this.mainPanel);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Crea el panel de la botonera 
	 * 
	 * @return JPanel
	 */
	private JPanel crearPanelButtons() {
		
		JPanel panelButtons = new JPanel();
		
		btnAceptarLogin = new JButton(Mensajes.getMessage("VentanaLoginImpl.crearPanelButtons.btnAceptarLogin"));
		panelButtons.add(btnAceptarLogin);

		btnCancelarLogin = new JButton(Mensajes.getMessage("VentanaLoginImpl.crearPanelButtons.btnCancelarLogin"));
		panelButtons.add(btnCancelarLogin);
		
		return panelButtons;
	}

	/**
	 * Panel que permite seleccionar el tipo de usuario que se va a loguear
	 * 
	 * @return
	 */
	private JPanel crearPanelTipoUsuario() {
		
		JPanel panelTipoLogin = new JPanel();
		((FlowLayout) panelTipoLogin.getLayout()).setHgap(25);		
		
		JLabel lblRol = new JLabel(Mensajes.getMessage("VentanaLoginImpl.crearPanelTipoUsuario.lblRol"));
		comboTipoUsuario = new JComboBox<String>();
		panelTipoLogin.add(lblRol);
		panelTipoLogin.add(comboTipoUsuario);
		
		comboTipoUsuario.addItemListener(this);		
		
		return panelTipoLogin;
	}
	

	/**
	 * Panel que permite ingresar los datos según el tipo de usuario.
	 * 
	 * Si el usuario es
	 * Venta: Intenta ingresar a las operaciones publicas
	 * Inspector: Solicita el legajo y password.
	 * 
	 * @return
	 */
	private JPanel crearPanelLogin() {
		
		this.loginLayout = new CardLayout();
		
		this.panelLogin = new JPanel();
		this.panelLogin.setLayout(this.loginLayout);
		
		this.panelLogin.add(this.crearPanelLoginInspector(),ControladorLogin.INSPECTOR);
		this.panelLogin.add(this.crearPanelLoginVenta(),ControladorLogin.PARQUIMETRO);
		
		return this.panelLogin;
	}
		
	private JPanel crearPanelLoginVenta() {

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		return panel;
	}
	
	private JPanel crearPanelLoginInspector() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		JPanel panelFila1 = new JPanel();
		((FlowLayout) panelFila1.getLayout()).setHgap(25);		

		JLabel lblUsername = new JLabel(Mensajes.getMessage("VentanaLoginImpl.crearPanelLoginInspector.lblUsername"));
		this.campoInspectorUsername = new JTextField();
		this.campoInspectorUsername.setColumns(10);

		panelFila1.add(lblUsername);
		panelFila1.add(this.campoInspectorUsername);
		
		JPanel panelFila2 = new JPanel();
				
		JLabel lblPasswordLogin = new JLabel(Mensajes.getMessage("VentanaLoginImpl.crearPanelLoginInspector.lblPasswordLogin"));
				
		this.campoInspectorPassword = new JPasswordField();
		this.campoInspectorPassword.setColumns(10);

		panelFila2.add(lblPasswordLogin);		
		panelFila2.add(this.campoInspectorPassword);
		
		panel.add(panelFila1);
		panel.add(panelFila2);
		
		return panel;
	}
	
	/*
	 * Setters y Getters
	 * 
	 */

	protected JTextField getCampoInspectorUsername() {
		return campoInspectorUsername;
	}

	protected JPasswordField getCampoInspectorPassword() {
		return campoInspectorPassword;
	}

	protected JComboBox<String> getComboTipoUsuario() {
		return comboTipoUsuario;
	}

	protected void setComboTipoUsuario(JComboBox<String> comboTipoUsuario) {
		this.comboTipoUsuario = comboTipoUsuario;
	}
	
	protected JButton getBtnAceptarLogin() {
		return btnAceptarLogin;
	}

	protected JButton getBtnCancelarLogin() {
		return btnCancelarLogin;
	}

	/*
	 * Metodos para los listener
	 * 
	 * 
	 */
	protected void registrarEventos() {
		// Estos listeners permiten que si se presiona ENTER sobre los campos ingresados intenta Ingresar
		this.getCampoInspectorUsername().addActionListener(this.getIngresarListener());		
		this.getCampoInspectorPassword().addActionListener(this.getIngresarListener());

		this.getBtnAceptarLogin().addActionListener(this.getIngresarListener());		
		this.getBtnCancelarLogin().addActionListener(this.getCancelarListener());		
	}
	
	protected ActionListener getIngresarListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if (getUsuarioSeleccionado().equals(ControladorLogin.INSPECTOR)) 
				{
					logger.info(Mensajes.getMessage("VentanaLoginImpl.getIngresarListener.ingresarComoInspector"),ControladorLogin.INSPECTOR);
	            	controlador.ingresarComoInspector(getUserName(),getPassword());
				} 
				else if (getUsuarioSeleccionado().equals(ControladorLogin.PARQUIMETRO)) 
				{
					logger.info(Mensajes.getMessage("VentanaLoginImpl.getIngresarListener.ingresarComoParquimetro"),ControladorLogin.PARQUIMETRO);
	            	controlador.ingresarComoParquimetro();					
				} 
				else  
				{ 
					logger.error(Mensajes.getMessage("VentanaLoginImpl.getIngresarListener.error"));
				}	
            }
        };
	}

	protected ActionListener getCancelarListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e){
				System.exit(0);
            }
        };
	}

	@Override
	public void registrarControlador(Controlador c) {
        if (c instanceof ControladorLogin) {
            this.controlador = (ControladorLogin) c;
        } else {
            throw new IllegalArgumentException("Se esperaba un ControladorLogin");
        }		
	}	
}
