package parquimetros.modelo.beans;

public interface AutomovilBean {

	ConductorBean getConductor();

	void setConductor(ConductorBean conductor);

	String getPatente();
	
	void setPatente(String patente);
	
	String getMarca();
	
	void setMarca(String marca);

	String getModelo();
	
	void setModelo(String modelo);

	String getColor();
	
	void setColor(String color);

}
