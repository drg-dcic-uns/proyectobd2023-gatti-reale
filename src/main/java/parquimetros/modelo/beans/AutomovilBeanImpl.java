package parquimetros.modelo.beans;

//CLASE IMPLEMENTADA PROVISTA POR LA CATEDRA
//
public class AutomovilBeanImpl implements AutomovilBean {
	
	private ConductorBean conductor;
	private String patente;
	private String marca;
	private String modelo;
	private String color;

	@Override
	public ConductorBean getConductor() {
		return this.conductor;
	}

	@Override
	public void setConductor(ConductorBean conductor) {
		this.conductor = conductor;	
	}

	@Override
	public String getPatente() {
		return this.patente;
	}

	@Override
	public void setPatente(String patente) {
		this.patente = patente;
	}

	@Override
	public String getMarca() {
		return this.marca;
	}

	@Override
	public void setMarca(String marca) {
		this.marca = marca;
	}

	@Override
	public String getModelo() {
		return this.modelo;
	}

	@Override
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	@Override
	public String getColor() {
		return this.color;
	}

	@Override
	public void setColor(String color) {
		this.color = color;
	}
	
}
