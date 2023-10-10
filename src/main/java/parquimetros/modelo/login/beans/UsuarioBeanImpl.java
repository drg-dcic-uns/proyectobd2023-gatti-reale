package parquimetros.modelo.login.beans;

import java.util.Arrays;

//CLASE IMPLEMENTADA PROVISTA POR LA CATEDRA
// Representa un usuario del servidor de bases de datos (MySQL) a través del cual se realizaran las conexiones 
public class UsuarioBeanImpl implements UsuarioBean {
	
	private static final long serialVersionUID = 1L;	
	
	private String username;
	private String displayname;
	private String password;
	
	public UsuarioBeanImpl() {
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getDisplayname() {
		return this.displayname;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	
	/* Sacar? Creo que no se usa
	public boolean passwordCoincide(char[] clave) {
		
		boolean esCorrecto = false; 
		
		char[] passCorrecto = this.getPassword().toCharArray(); 

		if (clave.length == passCorrecto.length) {
			esCorrecto = Arrays.equals (clave, passCorrecto);
		}			

		Arrays.fill(passCorrecto,'0');
		
		return esCorrecto;
	}
	*/
}

