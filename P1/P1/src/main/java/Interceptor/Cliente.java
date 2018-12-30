package Interceptor;

import java.io.IOException;
import java.net.URISyntaxException;

//???? Habia algo mal o faltaba algo
public class Cliente{
	private GestorFiltros gestorFiltros;
	
	///////////AÑADIDO
	/*public Cliente(Interfaz objetivo){
		gestorFiltros = new GestorFiltros(objetivo);
	}*/
	///////////END_AÑADIDO
	
	public void setGestorFiltros(GestorFiltros gestorFiltros) {
		this.gestorFiltros = gestorFiltros;	
	}

	public void enviarPetición(double numVueltas) throws IOException, URISyntaxException {
		gestorFiltros.ejecutar(numVueltas);
	}
}

