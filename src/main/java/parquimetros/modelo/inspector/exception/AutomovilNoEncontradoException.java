package parquimetros.modelo.inspector.exception;

public class AutomovilNoEncontradoException extends Exception {

	private static final long serialVersionUID = 1L;

	public AutomovilNoEncontradoException() {
		super();
	}
	
	public AutomovilNoEncontradoException(String mensaje) {
		super(mensaje);
	}
}
