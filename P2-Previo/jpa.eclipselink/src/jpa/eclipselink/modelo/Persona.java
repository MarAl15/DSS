package jpa.eclipselink.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Persona{
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	
	private String id;
	private String nombre;
	private String apellidos;
	private Familia familia;
	private List<Empleo> listaEmpleos = new ArrayList<Empleo>();
	
	public String getID(){
		return id;
	}
	
	public void setID(String id){
		this.id = id;
	}

	public String getNombre(){
		return nombre;
	}
	
	public void setNombre(String resumen){
		this.nombre = nombre;
	}
	
	public String getApellidos(){
		return apellidos;
	}
	
	public void setApellidos(String descripcion){
		this.apellidos = apellidos;
	}
	
	@ManyToOne
	public Familia getFamilia(){
		return familia;
	}
	
	public void setFamilia(Familia familia){
		this.familia = familia;
	}
	
	@OneToMany
	public List<Empleo> listaEmpleos(){
		return this.listaEmpleos;
	}
	
	public void setListaEmpleos(List<Empleo> listaEmpleos){
		this.listaEmpleos = listaEmpleos;
	}
	
	/*@Override
	public String toString(){
		return nombre+" "+apellidos;
	}*/
}


