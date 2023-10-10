package parquimetros.modelo.inspector.dto;

public class MultaPatenteDTOImpl implements MultaPatenteDTO {

	public MultaPatenteDTOImpl(String nroMulta, String patente, String calle, String altura, String fechaMulta,
			String horaMulta, String legajo) {
		super();
		this.nroMulta = nroMulta;
		this.patente = patente;
		this.calle = calle;
		this.altura = altura;
		this.fechaMulta = fechaMulta;
		this.horaMulta = horaMulta;
		this.legajo = legajo;
	}

	private String nroMulta;
    private String patente;
    private String calle;
    private String altura;
    private String fechaMulta;
    private String horaMulta;
    private String legajo;
	
	@Override
	public String getNroMulta() {
		return this.nroMulta;
	}

	@Override
	public String getPatente() {
		return this.patente;
	}

	@Override
	public String getCalle() {
		return this.calle;
	}

	@Override
	public String getAltura() {
		return this.altura;
	}

	@Override
	public String getFechaMulta() {
		return this.fechaMulta;
	}

	@Override
	public String getHoraMulta() {
		return this.horaMulta;
	}

	@Override
	public String getLegajo() {
		return this.legajo;
	}

}
