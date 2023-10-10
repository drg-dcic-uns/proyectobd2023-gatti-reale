package parquimetros.modelo.parquimetro.dto;

public class SalidaEstacionamientoDTOImpl implements SalidaEstacionamientoDTO {

	public SalidaEstacionamientoDTOImpl(String tiempoTranscurrido, String saldoTarjeta, String fechaEntrada,
			String horaEntrada, String fechaSalida, String horaSalida) {
		super();
		this.tiempoTranscurrido = tiempoTranscurrido;
		this.saldoTarjeta = saldoTarjeta;
		this.fechaEntrada = fechaEntrada;
		this.horaEntrada = horaEntrada;
		this.fechaSalida = fechaSalida;
		this.horaSalida = horaSalida;
	}

	private String tiempoTranscurrido;
	private String saldoTarjeta;
	private String fechaEntrada;
	private String horaEntrada;
	private String fechaSalida;
	private String horaSalida;
	

	@Override
	public String getTiempoTranscurrido() {
		return this.tiempoTranscurrido;
	}

	@Override
	public String getSaldoTarjeta() {
		return this.saldoTarjeta;
	}

	@Override
	public String getFechaSalida() {
		return this.fechaSalida;
	}

	@Override
	public String getHoraSalida() {
		return this.horaSalida;
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
		return EstacionamientoDTO.TipoOperacion.CIERRE;
	}
}
