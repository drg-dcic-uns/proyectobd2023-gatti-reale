package parquimetros.modelo.inspector.dao.datosprueba;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.ParquimetroBeanImpl;
import parquimetros.modelo.beans.UbicacionBean;
import parquimetros.modelo.beans.UbicacionBeanImpl;

/*CLASE IMPLEMENTADA PROVISTA POR LA CATEDRA
  Contiene datos estáticos de prueba para mostrar en la aplicación parcialemente implementada.
  Una vez completada la implementacion esta clase ya no se utilizará. */
public class DAOParquimetrosDatosPrueba {

	private static Logger logger = LoggerFactory.getLogger(DAOParquimetrosDatosPrueba.class);
	
	public static HashMap<String,ParquimetroBean> datos = new HashMap<String,ParquimetroBean>();
	
	public static void poblar(UbicacionBean ubicacion) {
		
		datos.clear();
		
		int altura = ubicacion.getAltura();
		
		if ((altura >= 100) && (altura < 200)) {
			// Genera un solo parquimetro
			ParquimetroBean p1 = new ParquimetroBeanImpl();
			p1.setUbicacion(ubicacion);
			p1.setNumero(101);
			p1.setId(101);
			datos.put("p1", p1);
			
		} else if ((altura >= 200) && (altura < 300)) {
			// Genera dos parquimetros
			ParquimetroBean p1 = new ParquimetroBeanImpl();
			p1.setUbicacion(ubicacion);
			p1.setNumero(201);
			p1.setId(201);
			datos.put("p1", p1);

			ParquimetroBean p2 = new ParquimetroBeanImpl();
			p2.setUbicacion(ubicacion);
			p2.setNumero(202);
			p2.setId(202);
			datos.put("p2", p2);
			
		} else if ((altura >= 300) && (altura < 400)) {
			// Genera tres parquimetros
			ParquimetroBean p1 = new ParquimetroBeanImpl();
			p1.setUbicacion(ubicacion);
			p1.setNumero(301);
			p1.setId(301);
			datos.put("p1", p1);

			ParquimetroBean p2 = new ParquimetroBeanImpl();
			p2.setUbicacion(ubicacion);
			p2.setNumero(302);
			p2.setId(302);
			datos.put("p2", p2);

			ParquimetroBean p3 = new ParquimetroBeanImpl();
			p3.setUbicacion(ubicacion);
			p3.setNumero(303);
			p3.setId(303);
			datos.put("p3", p3);
			
		} else {
			// Genera un solo parquimetro
			ParquimetroBean p1 = new ParquimetroBeanImpl();
			p1.setUbicacion(ubicacion);
			p1.setNumero(555);
			p1.setId(401);
			datos.put("p1", p1);			
		} 
		
	}

	public static UbicacionBean obtenerUbicacion(int id) {

		UbicacionBean unaUbicacion = new UbicacionBeanImpl();
		unaUbicacion.setCalle("Caronti");
		unaUbicacion.setAltura(508);
		unaUbicacion.setTarifa(200);
		
		return unaUbicacion;
	}

	
}
