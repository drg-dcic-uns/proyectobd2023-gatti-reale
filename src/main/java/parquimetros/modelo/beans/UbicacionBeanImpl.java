package parquimetros.modelo.beans;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//CLASE IMPLEMENTADA PROVISTA POR LA CATEDRA
//Representa como un objeto una tupla de la tabla "ubicaciones"
public class UbicacionBeanImpl implements Serializable, UbicacionBean {
	
	private static Logger logger = LoggerFactory.getLogger(UbicacionBeanImpl.class);
	
	private static final long serialVersionUID = 1L;

	private String calle;
	private int altura;
	private double tarifa;

	@Override
	public String getCalle() {
		return calle;
	}

	@Override
	public void setCalle(String calle) {
		this.calle = calle;		
	}

	@Override
	public int getAltura() {
		return altura;
	}

	@Override
	public void setAltura(int altura) {
		this.altura = altura;		
	}

	@Override
	public double getTarifa() {
		return tarifa;
	}

	@Override
	public void setTarifa(double tarifa) {
		this.tarifa = tarifa;		
	}
	

	@Override
	public String toString() {
		return this.calle + " Nº " +  String.valueOf(this.altura);		
	}
	
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UbicacionBeanImpl) {
        	UbicacionBeanImpl ub = (UbicacionBeanImpl) obj;
            if (ub.getCalle().equals(this.getCalle()) && 
            	(ub.getAltura() == this.getAltura()) &&
            	(ub.getTarifa() == this.getTarifa()) 
            ){
                return true;
            }
        }
        return false;       
    }	
}
