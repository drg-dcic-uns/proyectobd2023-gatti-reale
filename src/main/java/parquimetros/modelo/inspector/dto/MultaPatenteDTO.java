package parquimetros.modelo.inspector.dto;

public interface MultaPatenteDTO {
	
	/*
	 * DTO: Data Transfer Object
	 * 
	 * El propósito de esta clase es transferir información entre las distintas capas de la aplicación
	 * enviando solo la información necesaria, sin incorporar detalles de como está almacenada dicha información
	 * en el modelo. Puede integrar información de diferentes Beans y transformar el contenido para su visualización
	 * como sucede en el caso de las fechas.  
	 * 
	 * De alguna forma, existe una analogía con el concepto de vista en BD, donde se oculta la representación interna.
	 * Obviamente este tipo de objetos tiene algunas otras particularidades y restricciones que no entraremos en detalle.
	 */
	
	String getNroMulta();
	String getPatente();
	String getCalle();
	String getAltura();
	String getFechaMulta();
	String getHoraMulta();
	String getLegajo();
}
