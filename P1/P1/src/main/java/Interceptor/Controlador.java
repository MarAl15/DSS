package Interceptor;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="controlador", eager=true)
@SessionScoped
public class Controlador implements Serializable{
	private boolean encendido = false;
	
	@ManagedProperty(value = "#{texto}")
	private String texto;
	
	@ManagedProperty(value = "#{txtBoton}")
	private String txtBoton;
	
	@ManagedProperty(value = "#{colorBoton}")
	private String colorBoton;
	
	private void encender(){
		if(!encendido){
			encendido = true;
			texto = "ENCENDIDO";
			txtBoton = "Apagar";
			colorBoton = "blue";
		}
	}
	 
	private void apagar(){
		if(encendido) {
			encendido = false;
			texto = "APAGADO";
			txtBoton = "Encender";
			colorBoton = "red";
		}
	}
	
	public void encender_apagar(){
		if(!encendido)
			encender();
		else
			apagar();
	}
	
	public void acelerar(){
		if(encendido) {
			texto = "ACELERANDO";
		}
	}
	
	// TEXTO
	public String getTexto(){
		if(texto != null)
			return texto;
		else
			return "APAGADO";
	}
	
	public void setTexto(String texto){
		this.texto = texto;
	}
	
	// TEXTO BOTÓN
	public String getTxtBoton(){
		if(txtBoton != null)
			return txtBoton;
		else
			return "Encender";
	}
	
	public void setTxtBoton(String txtBoton){
		this.txtBoton = txtBoton;
	}
	
	
	// COLOR BOTÓN
	public String getColorBoton(){
		if(colorBoton != null)
			return colorBoton;
		else
			return "red";
	}
	
	public void setColorBoton(String colorBoton){
		this.colorBoton = colorBoton;
	}
}
