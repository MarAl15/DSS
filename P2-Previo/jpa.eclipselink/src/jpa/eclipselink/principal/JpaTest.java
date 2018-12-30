package jpa.eclipselink.principal;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpa.eclipselink.modelo.Familia;
import jpa.eclipselink.modelo.Persona;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaTest{
	private static final String PERSISTENCE_UNIT_NAME = "relaciones_persistentes";
	private EntityManagerFactory factoria;
	
	@Before
	public void setUp() throws Exception{
		factoria = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factoria.createEntityManager();
		
		// Comenzar una nueva transacción local de tal forma que 
		// pueda persistir como una nueva entidad.
		em.getTransaction().begin();
		
		// Leer las entradas que hay en la base de datos.
		// Las personas no debe tener ninguno asignado todavía
		Query q = em.createQuery("Select m from Persona m");
		
		// Comprobar si necesitamos crear entradas en la base
		boolean crearseNuevasEntradas = (q.getResultList().size() == 0);
		if(crearseNuevasEntradas){
			// Vamos a ello...
			assertTrue(q.getResultList().size()==0);
			Familia familia = new Familia();
			familia.setDescripcion("Familia Martinez");
			em.persist(familia);
			
			for(int i=0; i<20; i++){
				// Crearse una "persona" y asignar sus atributos con
				// setNombre(...) y setApellidos(...)
				Persona persona = new Persona();
				persona.setNombre("Damian "+i);
				persona.setApellidos("Martinez");
				
				em.persist(persona);
				
				// Añadir "persona" a ListArray<Persona> que represeta
				// a "familia"
				familia.setMiembros(persona);
				
				// Ahora hacemos que persista la relacion familia-persona
				em.persist(persona);
				em.persist(familia);
			}
		}
		
		// Ahora hay que hacer "commit" de la transacción, lo que
		// causa que la entidad se salve en la base de datos.
		em.getTransaction().commit();
		
		// Ahora hay que cerrar el EntityManager o perderemos
		// nuestra entradas.
		em.close();		
	} //setUp()
	
	@Test
	public void comprobaPersonas(){
		// Ahora vamos a comprobar la base de datos para ver si las 
		// entradas que hemos cread están allí. Para eso, nos 
		// creamos un gestor de entidades "fresco".
		EntityManager em = factoria.createEntityManager();
		
		// Realizaremos una consulta simple que consistirá en 
		// seleccionar a todas las personas.
		Query q = em.createQuery("Select m from Persona m");
		
		// Si todo ha ido bien, en la lista de personas hemos de 
		// tener a 20 miembros: utilizar "assertTrue()", 
		// "getResultList()" y "size()" para comprobarlo.
		assertTrue(q.getResultList().size() == 20);
		
		// Acordarse de cerrar el gestor de entidades.
		em.close();
	}
	
	@Test
	public void comprobarFamilia(){
		// Para esto, nos creamos un gestor de entidades "fresco"
		EntityManager em = factoria.createEntityManager();
		
		// Recorre cada una de las entidades y mostrar cada uno
		// de sus campos asi como la fecha de creación.
		Query q = em.createQuery("Select f from Familia f");
		
		// Deberíamos tener una familia on 20 personas.
		// Utilizar "assertTrue()", "getResultList()", "size()" y 
		// "getSingleResult()" para determinarlo.
		assertTrue(q.getResultList().size() == 1);
		assertTrue(((Familia) q.getSingleResult()).getMiembros().size() == 20);
		
		// Acordarse de cerrar el gestor de entidades.
		em.close();
	}
	
	@Test(expected = javax.persistence.NoResultException.class)
	public void eliminarPersona(){
		// Para esto, nos creamos un gestor de entidades "fresco"
		EntityManager em = factoria.createEntityManager();
		
		// Comenzar una nueva transacción local de tal manera que 
		// podamos hacer persistente una nueva entidad. 
		em.getTransaction().begin();
		
		// Crear la consulta necesaria para eliminar la persona 
		// de nombre y apellidos que indicaré después
		Query q = em.createQuery("SELECT p FROM Persona p WHERE p.nombre = :nombre AND p.apellidos = :apellidos");
		
		// Asignamos los parámetros
		q.setParameter("nombre", "Damian_1");
		q.setParameter("apellidos", "Martinez");
		
		// Utilizar el método: "getSingleResult()" para obtener
		// a la persona que me interesa y los métodos: "remove(persona)"
		// y "commit" para eliminarla de la entidad y confirmar 
		// la eliminación, respectivamente.
		Persona persona = (Persona) q.getSingleResult();
		em.remove(persona);
		em.getTransaction().commit();
		
		// Acordarse de cerrar el gestor de entidades.
		em.close();
	}
	
}
