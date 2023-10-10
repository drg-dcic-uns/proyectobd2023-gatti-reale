package parquimetros.vista.login;

import java.util.List;

import parquimetros.vista.Ventana;

public interface VentanaLogin extends Ventana {

	void poblarComboTipoUsuario(List<String> nombresUsuarios);
	
}
