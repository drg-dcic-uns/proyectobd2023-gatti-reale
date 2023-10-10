package parquimetros.modelo.parquimetro;

import java.util.ArrayList;

import parquimetros.modelo.Modelo;
import parquimetros.modelo.beans.ParquimetroBean;
import parquimetros.modelo.beans.TarjetaBean;
import parquimetros.modelo.beans.UbicacionBean;
import parquimetros.modelo.parquimetro.dto.EntradaEstacionamientoDTO;
import parquimetros.modelo.parquimetro.dto.EstacionamientoDTO;
import parquimetros.modelo.parquimetro.dto.SalidaEstacionamientoDTO;
import parquimetros.modelo.parquimetro.exception.ParquimetroNoExisteException;
import parquimetros.modelo.parquimetro.exception.SinSaldoSuficienteException;
import parquimetros.modelo.parquimetro.exception.TarjetaNoExisteException;

public interface ModeloParquimetro extends Modelo {

	/**
	 * Recupera una lista de tarjetas
	 * 
	 * @return ArrayList<TarjetaBean>
	 */
	public ArrayList<TarjetaBean> recuperarTarjetas() throws Exception;
	
	/**
	 * Recupera una lista de ubicaciones
	 * 
	 * @return ArrayList<UbicacionBean>
	 */
	public ArrayList<UbicacionBean> recuperarUbicaciones() throws Exception;

	/**
	 * Recupera una lista de parquimetros presentes en una ubicacion
	 * 
	 * @return ArrayList<ParquimetroBean>
	 */
	public ArrayList<ParquimetroBean> recuperarParquimetros(UbicacionBean ubicacion) throws Exception;

	/**
	 * Llama al SP que realiza el conectar de la tarjeta al parquimetro. 
	 * Si se produce alguno error se dispara una excepción.
	 * 
	 * retorna un objeto de tipo EntradaEstacionamientoDTO o SalidaEstacionamientoDTO según el resultado
	 * 
	 * @param parquimetro
	 * @param tarjeta
	 * @return
	 * @throws SinSaldoSuficienteException
	 * @throws ParquimetroNoExisteException
	 * @throws TarjetaNoExisteException
	 * @throws Exception
	 */
	public EstacionamientoDTO conectarParquimetro(ParquimetroBean parquimetro, TarjetaBean tarjeta) throws 	SinSaldoSuficienteException,
																										ParquimetroNoExisteException,
																										TarjetaNoExisteException,
																										Exception;
}
