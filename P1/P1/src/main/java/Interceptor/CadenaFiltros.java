package Interceptor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class CadenaFiltros{
	// private // declarar: filtros es un ArrayLst
	// generico de elementos Filtro
	private ArrayList<Filtro> filtros;	
	private Interfaz objetivo;
	
	public void addFiltro(Filtro filtro){
		filtros.add(filtro);
	}
	
	public void ejecutar(double peticion) throws IOException, URISyntaxException{
		for(Filtro filtro: filtros){
			System.out.println("Nueva velocidad (m/s)"+filtro.ejecutar(peticion));
		}
		objetivo.ejecutar(peticion);
	}
	
	public void setObjetivo(Interfaz objetivo){
		this.objetivo = objetivo;
	}
}
