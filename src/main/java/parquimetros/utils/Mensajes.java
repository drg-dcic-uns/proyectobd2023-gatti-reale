package parquimetros.utils;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mensajes {
	
	private static Logger logger = LoggerFactory.getLogger(Mensajes.class);
	/*
	 * Clase utilitaria para gestionar los mensajes. 
	 * se conoce como internacionalización y localización, a menudo abreviado como i18n y l10n respectivamente.
	 *  
	 * La internacionalización se refiere a la práctica de diseñar y desarrollar software de tal manera que pueda 
	 * adaptarse fácilmente a diferentes idiomas y regiones sin necesidad de cambios significativos en el código fuente.
	 *  
	 * La localización, por otro lado, se refiere a la adaptación de una aplicación para que funcione en un idioma y 
	 * entorno cultural específico.
	 * 
	 * Crear archivos de recursos:
	 *      Crea archivos de propiedades para cada idioma que desees admitir. 
	 *      Por ejemplo, messages_en.properties para inglés 
	 *      y messages_es.properties para español.
	 *
	 * Coloca los archivos de recursos en el classpath: 
	 *      Asegúrate de que los archivos de recursos estén en el classpath de tu proyecto para que Java pueda encontrarlos.
	 * 
	 */
	
    private static ResourceBundle messages;

    static {
    	
    	logger.debug("Se intenta recuperar el locale de Config");
    	String idioma = Config.getProperty("locale");
    	logger.debug("El locale recuperado es {}", idioma);

    	Locale locale = new Locale(idioma); 
        messages = ResourceBundle.getBundle("messages", locale);
    }

    public static String getMessage(String key) {
        return messages.getString(key);
    }
}