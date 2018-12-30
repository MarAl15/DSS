package jpa.eclipselink.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Familia {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	
	private int id;
	private String descripcion;
	
	@OneToMany(mappedBy = "familia")
	private final List<Persona> miembros = new ArrayList<Persona>();
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getDescripcion(){
		return descripcion;
	}
	
	public void setDescripcion(String descripcion){
		this.descripcion = descripcion;
	}
	
	public List<Persona> getMiembros(){
		return miembros;
	}
	
	public void setMiembros(Persona persona){
		miembros.add(persona);
	}
	
	/*@Override
	public String toString(){
		String listaFamilia= "";
		
		for(Persona miembro: miembros)
			listaFamilia += miembro.toString()+"\n";
		
		return listaFamilia;
	}*/
}
