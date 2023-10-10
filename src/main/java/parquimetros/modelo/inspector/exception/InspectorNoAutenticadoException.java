package parquimetros.modelo.inspector.exception;

public class InspectorNoAutenticadoException extends Exception {

	private static final long serialVersionUID = 1L;

	public InspectorNoAutenticadoException(String mensaje) {
		super(mensaje);
	}	
}
