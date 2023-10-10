package parquimetros.modelo.beans;

public interface InspectorBean {
	
	/**
	 * @return el legajo
	 */
	int getLegajo();

	/**
	 * @param Legajo
	 */
	void setLegajo(int legajo);

	/**
	 * @return the dni
	 */
	int getDNI();

	/**
	 * @param dni the dni to set
	 */
	void setDNI(int dni);
	
	/**
	 * @return the apellido
	 */
	String getApellido();

	/**
	 * @param apellido the apellido to set
	 */
	void setApellido(String apellido);

	/**
	 * @return the nombre
	 */
	String getNombre();

	/**
	 * @param nombre the nombre to set
	 */
	void setNombre(String nombre);

	/**
	 * @return Password
	 */
	String getPassword();

	/**
	 * @param Password
	 */
	void setPassword(String password);
	
}
