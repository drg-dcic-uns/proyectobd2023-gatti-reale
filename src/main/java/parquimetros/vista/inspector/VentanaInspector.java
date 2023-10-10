package parquimetros.vista.inspector;

import java.util.ArrayList;

import parquimetros.modelo.beans.InspectorBean;
import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.UbicacionBean;
import parquimetros.modelo.inspector.dto.EstacionamientoPatenteDTO;
import parquimetros.modelo.inspector.dto.MultaPatenteDTO;
import parquimetros.utils.Mensajes;
import parquimetros.vista.Ventana;

public interface VentanaInspector extends Ventana {

	/**
	 * Constante que identifica la opcion del menu para registrar multas. 
	 */
	public final static String CARGA_PATENTES_POR_UBICACION = "patentesPorUbicacion";
	/**
	 * Constante que identifica la opcion del menu para listar las multas según diferentes parametros
	 */
	// public final static String LISTADO_MULTAS = "listadoMultas";
	
	/**
	 * Constantes que identifican las pantallas de carga de patentes y multas
	 */
	// 
	// Panel principal
	public final static String VACIO = "vacio";   // Panel Vacío
	public final static String ESTACIONAMIENTOS_Y_MULTAS = "estacionamientosMultas"; //opcion del inspector

	// Panel secundario - Asociado internamente a un panel principal
	// en este caso a ESTACIONAMIENTOS_Y_MULTAS
	public final static String MULTAS_SELECCIONA_UBICACION = "seleccionUbicacion";
	public final static String MULTAS_CARGA_PATENTES = "cargaPatentes";
	
	/**
	 * Columnas de las tablas de multas
	 */
	public final static String TABLA_COMPROBAR_ESTACIONAMIENTO_PATENTE = Mensajes.getMessage("VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_PATENTE");
	public final static String TABLA_COMPROBAR_ESTACIONAMIENTO_CALLE = Mensajes.getMessage("VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_CALLE");
	public final static String TABLA_COMPROBAR_ESTACIONAMIENTO_ALTURA = Mensajes.getMessage("VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_ALTURA");
	public final static String TABLA_COMPROBAR_ESTACIONAMIENTO_FECHA_ENTRADA = Mensajes.getMessage("VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_FECHA_ENTRADA");
	public final static String TABLA_COMPROBAR_ESTACIONAMIENTO_HORA_ENTRADA = Mensajes.getMessage("VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_HORA_ENTRADA");
	public final static String TABLA_COMPROBAR_ESTACIONAMIENTO_ESTADO = Mensajes.getMessage("VentanaInspector.TABLA_COMPROBAR_ESTACIONAMIENTO_ESTADO");
	
	public final static String TABLA_GENERAR_MULTA_PATENTE = Mensajes.getMessage("VentanaInspector.TABLA_GENERAR_MULTA_PATENTE");
	public final static String TABLA_GENERAR_MULTA_CALLE = Mensajes.getMessage("VentanaInspector.TABLA_GENERAR_MULTA_CALLE");
	public final static String TABLA_GENERAR_MULTA_ALTURA = Mensajes.getMessage("VentanaInspector.TABLA_GENERAR_MULTA_ALTURA");
	public final static String TABLA_GENERAR_MULTA_NRO_MULTA = Mensajes.getMessage("VentanaInspector.TABLA_GENERAR_MULTA_NRO_MULTA");
	public final static String TABLA_GENERAR_MULTA_FECHA_MULTA = Mensajes.getMessage("VentanaInspector.TABLA_GENERAR_MULTA_FECHA_MULTA");
	public final static String TABLA_GENERAR_MULTA_HORA_MULTA = Mensajes.getMessage("VentanaInspector.TABLA_GENERAR_MULTA_HORA_MULTA");
	public final static String TABLA_GENERAR_MULTA_LEGAJO = Mensajes.getMessage("VentanaInspector.TABLA_GENERAR_MULTA_LEGAJO");
	
	/**
	 * Muestra el panel (ventana) que corresponda a alguna de las opciones del menu principal
	 * 
	 * @param panel
	 */
	public void mostrarPanelPrincipal(String panel);
	
	/**
	 * Muestra uno de los paneles secundarios que corresponden a ESTACIONAMIENTOS_Y_MULTAS
	 * 
	 * @param panel se espera que panel se MULTAS_SELECCIONA_UBICACION o MULTAS_CARGA_PATENTES
	 */	
	public void mostrarPanelDeEstacionamientosMultas(String panel);
	
	/**
	 * Muestra en la ventana la información del inspector que está logueado.
	 * 
	 * @param inspector objeto de tipo InspectorBean 
	 */
	public void mostrarInspectorLogueado(InspectorBean inspector);	
	
	/**
	 * Carga el componente que permite seleccionar una calle y altura
	 * 
	 * @param List<UbicacionesBean> ubicaciones  
	 */
	public void poblarUbicaciones(ArrayList<UbicacionBean> ubicaciones);
	
	/**
	 * Carga el componente que permite seleccionar un parquimetro presente en una calle y altura previamente dado.
	 * 
	 * @param List<ParquimetrosBean> parquimetros  
	 */
	public void poblarParquimetros(ArrayList<ParquimetroBean> parquimetros);

	
	/**
	 * Muestra en la ventana la información del parquimetro y la ubicación a la que está conectado el inspector.
	 * 
	 */
	public void mostrarParquimetroConectado(ParquimetroBean parquimetro);

	/**
	 * Prepara las componentes (las limpia si es necesario) y muestra el panel donde se carga las patentes 
	 */
//	public void mostrarCargaPatente();

	/**
	 * Solicita a la ventana que actualice la lista de Patentes Estacionadas
	 * 
	 * @param listaPatentesEstacionadas
	 */
	public void actualizarListaPatentesEstacionadas(ArrayList<EstacionamientoPatenteDTO> listaPatentesEstacionadas);

	/**
	 * Solicita a la ventana que actualice la lista de Multas
	 * 
	 * @param multas
	 */
	public void actualizarListaMultas(ArrayList<MultaPatenteDTO> multas);
	
}
