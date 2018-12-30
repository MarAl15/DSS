package Interceptor;

import java.io.IOException;
import java.net.URISyntaxException;

public class Calcular implements Filtro{
	private double INTERVALO;
	
	public Calcular(){
		INTERVALO = 7200; // m/h en un intervalo de 2 horas
	}
	
	public double ejecutar(Object o)  throws IOException, URISyntaxException{
		double distancia = (Double) o;
		double velocidad = distancia*3600/INTERVALO;
		
		return velocidad;
	}

}
