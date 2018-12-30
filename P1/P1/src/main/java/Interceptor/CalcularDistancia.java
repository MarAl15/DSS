package Interceptor;

import java.io.IOException;
import java.net.URISyntaxException;

public class CalcularDistancia implements Filtro{
	private double RADIO;
	private double revolAnt;
	
	public CalcularDistancia(){
		RADIO = 1; //????
		revolAnt = 0;
	}
	
	public double ejecutar(Object o) throws IOException, URISyntaxException{
		double revoluciones = (Double) o;
		double distancia = (revoluciones-revolAnt)*2*RADIO*3.1416;
		
		revolAnt = revoluciones;
		return distancia;
	}
}
