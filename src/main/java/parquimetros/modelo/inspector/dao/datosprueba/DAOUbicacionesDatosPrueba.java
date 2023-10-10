package parquimetros.modelo.inspector.dao.datosprueba;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.modelo.beans.UbicacionBean;
import parquimetros.modelo.beans.UbicacionBeanImpl;

/*CLASE IMPLEMENTADA PROVISTA POR LA CATEDRA
  Contiene datos estáticos de prueba para mostrar en la aplicación parcialemente implementada.
  Una vez completada la implementacion esta clase ya no se utilizará. */
public class DAOUbicacionesDatosPrueba {

	private static Logger logger = LoggerFactory.getLogger(DAOUbicacionesDatosPrueba.class);
	
	public static HashMap<String,UbicacionBean> datos = new HashMap<String,UbicacionBean>();
	
	public static void poblar() {
		UbicacionBean alem1 = new UbicacionBeanImpl();
		alem1.setCalle("Av. Alem");
		alem1.setAltura(100);
		alem1.setTarifa(85);

		UbicacionBean alem2 = new UbicacionBeanImpl();
		alem2.setCalle("Av. Alem");
		alem2.setAltura(200);
		alem2.setTarifa(85);

		UbicacionBean alem3 = new UbicacionBeanImpl();
		alem3.setCalle("Av. Alem");
		alem3.setAltura(500);
		alem3.setTarifa(85);

		UbicacionBean alsina1 = new UbicacionBeanImpl();
		alsina1.setCalle("Alsina");
		alsina1.setAltura(200);
		alsina1.setTarifa(110);

		UbicacionBean estomba1 = new UbicacionBeanImpl();
		estomba1.setCalle("Estomba");
		estomba1.setAltura(400);
		estomba1.setTarifa(105.50);

		datos.clear();
		datos.put("alem1", alem1);
		datos.put("alem2", alem2);
		datos.put("alem3", alem3);	
		datos.put("alsina1", alsina1);
		datos.put("estomba1", estomba1);
	}
	
	public static UbicacionBean obtenerUbicacion(String key) {
		return datos.get(key);
	}
	
	
	
}
