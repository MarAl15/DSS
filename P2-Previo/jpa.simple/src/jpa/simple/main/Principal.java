package jpa.simple.main;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import jpa.simple.modelo.Completo;

public class Principal {
	private static final String PERSISTENCE_UNIT_NAME="completo";
	private static EntityManagerFactory factoria;
	
	public static void main(String[] args){
		factoria = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factoria.createEntityManager();	
		// Leer las entradas existentes y escribir en la consola
		Query q = em.createQuery("select t from Completo t");
		
		// Crearse una lista con template: "Completo" a la que 
		// asignaremos el resultado de la consulta e a base de
		// datos "q.getResultList()"
		List<Completo> listaCompleto = q.getResultList();
		
		// Iterar en la lista e imprimir las instancias "completo"
		for(Completo c: listaCompleto)
			System.out.println(c);
		
		// Ahora imprimimos el número de registros que tiene ya la 
		// base de datos
		System.out.println("Tamaño: "+listaCompleto.size());
		
		// Ahora vamos a trabajar con una transacción en la base de
		// datos
		em.getTransaction().begin();
		
		// Crearse una instancia de completo y utilizar los métodos
		// "setResumen()" y "setDescripcon()"
		Completo completo = new Completo();
		completo.setResumen("Prueba");
		completo.setDescripcion("Para probar");
		
		// Posteriormente hay que decir al gestor de entidad (em)
		// que la instancia va a ser persistente; conseguir la 
		// transacción ("em.getTransaction()") y hacera definitiva
		// ("commit()")
		em.persist(completo);
		em.getTransaction().commit();
		
		
		// Por último, hay que cerrar al gestor de entidad
		em.close();
	}
}
