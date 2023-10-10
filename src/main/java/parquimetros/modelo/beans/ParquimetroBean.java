package parquimetros.modelo.beans;

public interface ParquimetroBean {

	int getId();

	void setId(int id);

	int getNumero();

	void setNumero(int numero);
	
	UbicacionBean getUbicacion();

	void setUbicacion(UbicacionBean ubicacion);


}