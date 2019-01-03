package modelo;

import javax.xml.bind.annotation.XmlRootElement;

// Definición del dominio de datos de la aplicación
@XmlRootElement
public class Libro{
	private String id;
	private String titulo;
	private String autor;
	private String resumen;
	
	public Libro(){}
	
	public Libro(String id, String titulo, String autor){
		this.id = id;
		this.titulo = titulo;
		this.autor = autor;
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getTitulo(){
		return titulo;
	}
	
	public void setTitulo(String titulo){
		this.titulo = titulo;
	}
	
	public String getAutor(){
		return autor;
	}
	
	public void setAutor(String autor){
		this.autor = autor;
	}
	
	public String getResumen(){
		return resumen;
	}
	
	public void setResumen(String resumen){
		this.resumen = resumen;
	}
}
