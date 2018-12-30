package Interceptor;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Interfaz {
	public double ejecutar(Object o) throws IOException, URISyntaxException{
		//String url = "http://localhost:8080/P1/home.jsf";
		String url = "http://localhost:8080/P1/home.xhtml";
		
		if(Desktop.isDesktopSupported()){
			// Windows
			Desktop.getDesktop().browse(new URI(url));
		}
		else{
			// Ubuntu
			Runtime runtime = Runtime.getRuntime();
			runtime.exec("/usr/bin/firefox -new-window"+url);
		}
		
		return 0.0d;
	}
}
