package parquimetros.modelo.beans;

//CLASE IMPLEMENTADA PROVISTA POR LA CATEDRA
//Representa como un objeto una tupla de la tabla "parquimetros"
public class ParquimetroBeanImpl implements ParquimetroBean {

	private int id;
	private int numero;
	private UbicacionBean ubicacion;

	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public int getNumero() {
		return this.numero;
	}	
	@Override
	public void setNumero(int numero) {
		this.numero = numero;		
	} 
	@Override
	public UbicacionBean getUbicacion() {
		return ubicacion;
	}
	@Override
	public void setUbicacion(UbicacionBean ubicacion) {
		this.ubicacion = ubicacion;
	}

	@Override
	public String toString() {
		return Integer.toString(getNumero());
	}
}
