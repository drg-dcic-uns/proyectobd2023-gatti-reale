package parquimetros.modelo.beans;

public interface UbicacionBean {

	/**
	 * @return La calle
	 */
	String getCalle();

	/**
	 * @param calle a asignar
	 */
	void setCalle(String calle);

	/**
	 * @return Altura
	 */
	int getAltura();

	/**
	 * @param altura de la calle, por ejemplo, 1251 (de Alem)
	 */
	void setAltura(int altura);
	
	/**
	 * @return Tarifa
	 */
	double getTarifa();

	/**
	 * @param tarifa the nroCliente to set
	 */
	void setTarifa(double tarifa);	
	
	/**
	 * Permite mostrar en los combos la propiedad deseada.
	 */
	@Override
	String toString();
}
