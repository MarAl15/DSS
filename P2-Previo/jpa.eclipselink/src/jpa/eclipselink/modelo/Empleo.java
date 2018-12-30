package jpa.eclipselink.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Empleo{
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	
	private int id;
	private double salario;
	private String descripcionTrabajo;
	
	public int getID(){
		return id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public double getSalario(){
		return salario;
	}
	
	public void setSalario(double salario){
		this.salario = salario;
	}
	
	public String getDescripcion(){
		return descripcionTrabajo;
	}
	
	public void setDescripcion(String descripcionTrabajo){
		this.descripcionTrabajo = descripcionTrabajo;
	}
}
