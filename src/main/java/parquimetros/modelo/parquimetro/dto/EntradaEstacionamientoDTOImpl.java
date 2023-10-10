package parquimetros.modelo.parquimetro.dto;

public class EntradaEstacionamientoDTOImpl implements EntradaEstacionamientoDTO {

	public EntradaEstacionamientoDTOImpl(String tiempoDisponible, String fechaEntrada, String horaEntrada) {
		super();
		this.tiempoDisponible = tiempoDisponible;
		this.fechaEntrada = fechaEntrada;
		this.horaEntrada = horaEntrada;
	}

	private String tiempoDisponible;
	private String fechaEntrada;
	private String horaEntrada;
	
	@Override	
	public String getTiempoDisponible() {
		return this.tiempoDisponible;
	}
	
	@Override	
	public String getFechaEntrada() {
		return this.fechaEntrada;
	}
	
	@Override	
	public String getHoraEntrada() {
		return this.horaEntrada;
	}

	@Override
	public TipoOperacion getOperacion() {
		return EstacionamientoDTO.TipoOperacion.APERTURA;
	}	

}
