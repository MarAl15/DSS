package Interceptor;

import java.io.IOException;
import java.net.URISyntaxException;

/*
 * GestorFiltros: crea la cadena de filtros y posee métodos para insertar los filtros en la
cadena y provocar que cada uno ejecute la petición del cliente y también el objetivo.
 */
public class GestorFiltros {
	private CadenaFiltros cadenaFiltros;
	
	public GestorFiltros(Interfaz objetivo) {
		cadenaFiltros = new CadenaFiltros();
		cadenaFiltros.setObjetivo(objetivo);
	}

	public void setFiltro(Filtro filtro) {
		cadenaFiltros.addFiltro(filtro);
	}
	
	public void ejecutar(double peticion) throws IOException, URISyntaxException{
		cadenaFiltros.ejecutar(peticion);
	}

}
