package Interceptor;

import java.io.IOException;
import java.net.URISyntaxException;

// Interfaz Filtro que ha de implementar la clase filtro especı́fico, en nuestro caso esta clase 
// será la clase Calcular (velocidad).
public interface Filtro{	
	public double ejecutar(Object o) throws IOException, URISyntaxException;
}
