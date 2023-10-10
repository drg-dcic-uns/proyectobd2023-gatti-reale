package parquimetros.modelo.beans;

public interface TarjetaBean {

	public int getId();

	public void setId(int id);
	
	public double getSaldo();
	
	public void setSaldo(double saldo);
	
	public TipoTarjetaBean getTipoTarjeta();
	
	public void setTipoTarjeta(TipoTarjetaBean tipo);
	
	public AutomovilBean getAutomovil();
	
	public void setAutomovil(AutomovilBean automovil);
	
}
