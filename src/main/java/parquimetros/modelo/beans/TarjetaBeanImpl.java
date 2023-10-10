package parquimetros.modelo.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TarjetaBeanImpl implements TarjetaBean {
	
	private static Logger logger = LoggerFactory.getLogger(TarjetaBeanImpl.class);
	
	private static final long serialVersionUID = 1L;

	private int id;
	private double saldo;
	private TipoTarjetaBean tipoTarjeta;
	private AutomovilBean automovil;

	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public void setId(int id) {
		this.id=id;
	}

	@Override
	public double getSaldo() {
		return this.saldo;
	}

	@Override
	public void setSaldo(double saldo) {
		this.saldo=saldo;
	}

	@Override
	public TipoTarjetaBean getTipoTarjeta() {
		return this.tipoTarjeta;
	}

	@Override
	public void setTipoTarjeta(TipoTarjetaBean tipo) {
		this.tipoTarjeta=tipo;
	}

	@Override
	public AutomovilBean getAutomovil() {
		return this.automovil;
	}

	@Override
	public void setAutomovil(AutomovilBean automovil) {
		this.automovil=automovil;
	}

	@Override
	public String toString() {
		String tarjeta = String.valueOf(id);
		if (this.tipoTarjeta != null) {
			tarjeta += " [" + this.tipoTarjeta.getTipo() + "]";
		}
		if (this.automovil != null) {
			tarjeta += " " + this.automovil.getPatente();
			if (this.automovil.getConductor() != null) {
				tarjeta += " de " + this.automovil.getConductor().getApellido() + 
							", " + this.automovil.getConductor().getNombre();
			}
		}		
		return tarjeta;
	}
}
