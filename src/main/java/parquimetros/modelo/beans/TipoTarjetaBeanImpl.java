package parquimetros.modelo.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TipoTarjetaBeanImpl implements TipoTarjetaBean {

	private static Logger logger = LoggerFactory.getLogger(TipoTarjetaBeanImpl.class);
	
	private static final long serialVersionUID = 1L;

	private String tipo;
	private double descuento;
	
	@Override
	public String getTipo() {
		return this.tipo;
	}
	@Override
	public void setTipo(String tipo) {
		this.tipo=tipo;		
	}
	@Override
	public double getDescuento() {
		return this.descuento;
	}
	@Override
	public void setDescuento(double descuento) {
		this.descuento=descuento;		
	}
	
}
