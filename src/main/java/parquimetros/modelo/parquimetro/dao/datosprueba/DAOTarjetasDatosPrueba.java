package parquimetros.modelo.parquimetro.dao.datosprueba;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.modelo.beans.TarjetaBean;
import parquimetros.modelo.beans.TarjetaBeanImpl;
import parquimetros.modelo.beans.TipoTarjetaBean;
import parquimetros.modelo.beans.TipoTarjetaBeanImpl;
import parquimetros.modelo.beans.AutomovilBean;
import parquimetros.modelo.beans.AutomovilBeanImpl;
import parquimetros.modelo.beans.ConductorBean;
import parquimetros.modelo.beans.ConductorBeanImpl;

/*CLASE IMPLEMENTADA PROVISTA POR LA CATEDRA
  Contiene datos estáticos de prueba para mostrar en la aplicación parcialemente implementada.
  Una vez completada la implementacion esta clase ya no se utilizará. */
public class DAOTarjetasDatosPrueba {

	private static Logger logger = LoggerFactory.getLogger(DAOTarjetasDatosPrueba.class);
	
	public static HashMap<String,TarjetaBean> datos = new HashMap<String,TarjetaBean>();
	
	public static void poblar() {
		
		datos.clear();
		
		TipoTarjetaBean t1 = new TipoTarjetaBeanImpl();
		t1.setTipo("Normal");
		t1.setDescuento(0);
		
		TipoTarjetaBean t2 = new TipoTarjetaBeanImpl();
		t2.setTipo("Especial");
		t2.setDescuento(0.25);

		TipoTarjetaBean t3 = new TipoTarjetaBeanImpl();
		t3.setTipo("Jubilado");
		t3.setDescuento(0.6);

		TipoTarjetaBean t4 = new TipoTarjetaBeanImpl();
		t4.setTipo("Liberada");
		t4.setDescuento(1);

		ConductorBean c1 = new ConductorBeanImpl();
		c1.setApellido("Apellido1");
		c1.setNombre("Nombre1");
		c1.setDireccion("Dir1");
		c1.setNroDocumento(11111111);
		c1.setRegistro(100001);
		
		AutomovilBean a1 = new AutomovilBeanImpl();
		a1.setPatente("XYZ101");
		a1.setMarca("VW");
		a1.setModelo("Gol");
		a1.setColor("Rojo");
		a1.setConductor(c1);
		
		ConductorBean c2 = new ConductorBeanImpl();
		c2.setApellido("Apellido2");
		c2.setNombre("Nombre2");
		c2.setDireccion("Dir2");
		c2.setNroDocumento(22222222);
		c2.setRegistro(200002);
		
		AutomovilBean a2 = new AutomovilBeanImpl();
		a2.setPatente("XYZ202");
		a2.setMarca("Peugeot");
		a2.setModelo("504");
		a2.setColor("Amarillo");
		a2.setConductor(c2);
		
		ConductorBean c3 = new ConductorBeanImpl();
		c3.setApellido("Apellido3");
		c3.setNombre("Nombre3");
		c3.setDireccion("Dir3");
		c3.setNroDocumento(33333333);
		c3.setRegistro(300003);
		
		AutomovilBean a3 = new AutomovilBeanImpl();
		a3.setPatente("XYZ303");
		a3.setMarca("Ford");
		a3.setModelo("Falcon");
		a3.setColor("Azul");
		a3.setConductor(c3);

		ConductorBean c4 = new ConductorBeanImpl();
		c4.setApellido("Apellido3");
		c4.setNombre("Nombre3");
		c4.setDireccion("Dir3");
		c4.setNroDocumento(44444444);
		c4.setRegistro(400004);
		
		AutomovilBean a4 = new AutomovilBeanImpl();
		a4.setPatente("XYZ404");
		a4.setMarca("Fiat");
		a4.setModelo("600");
		a4.setColor("Blanco");
		a4.setConductor(c4);
		
		TarjetaBean k1 = new TarjetaBeanImpl();
		k1.setId(1);
		k1.setSaldo(-10);
		k1.setAutomovil(a1);
		k1.setTipoTarjeta(t1);
		datos.put("k1", k1);		
		
		TarjetaBean k2 = new TarjetaBeanImpl();
		k2.setId(2);
		k2.setSaldo(120.40);
		k2.setAutomovil(a2);
		k2.setTipoTarjeta(t2);
		datos.put("k2", k2);		

		TarjetaBean k3 = new TarjetaBeanImpl();
		k3.setId(3);
		k3.setSaldo(70.50);
		k3.setAutomovil(a3);
		k3.setTipoTarjeta(t3);
		datos.put("k3", k3);		

		TarjetaBean k4 = new TarjetaBeanImpl();
		k4.setId(4);
		k4.setSaldo(-10);
		k4.setAutomovil(a4);
		k4.setTipoTarjeta(t4);
		datos.put("k4", k4);		

	}
	

}
