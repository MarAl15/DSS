# Configuración del marco de trabajo

## EclipseLink

1. [Descargar](https://www.eclipse.org/downloads/download.php?file=/rt/eclipselink/releases/2.7.3/eclipselink-2.7.3.v20180807-4be1041.zip)
2. Extraer en `/usr/local`

## Apache Derby

1. [Descargar](it.apache.contactlab.it//db/derby/db-derby-10.14.2.0/db-derby-10.14.2.0-bin.tar.gz)
2. Extraer en `/usr/local`
3. (Opcional) Cambiar el nombre de la carpeta

# Ejemplos

## Ejemplo simple 

1. Crear un nuevo proyecto java llamado `jpa.simple`
2. Crear el subdirectorio `lib` donde se ubicarán los siguientes JARs:
	- `eclipselink.jar` ubicado en la carpeta `/usr/local/eclipselink/jlib/`
	- `javax.persistence_2.2.1.v201807122140.jar` ubicados en `/usr/local/eclipselink/jlib/jpa/`
	- `derby.jar` ubicado en `/usr/local/db-derby/lib/`
3. Añadir los contenios del citado `lib` al `buildpath` de nuestro proyecto. [Pasos](http://www.vogella.com/tutorials/Eclipse/article.html#using-jars-libraries-in-eclipse)

4. Crear el paquete de Java `jpa.simple.modelo` donde se ubicará la clase `Completo.java`:
```java
package jpa.simple.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Completo{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	private String resumen;
	private String descripcion;
	
	public String getResumen(){
		return resumen;
	}
	
	public void setResumen(String resumen){
		this.resumen = resumen;
	}
	
	public String getDescripcion(){
		return descripcion;
	}
	
	public void setDescripcion(String descripcion){
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString(){
		return "Completo [resumen=" + resumen + ", descripcion=" + descripcion + "]";
	}
}
```

5. Nos creamos el subdirectorio `META-INF` en la carpeta `src` e incluimos el archivo `persistence.xml`:
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<persistence xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
  version="2.0" xmlns="http://java.sun.com/xml/ns/persistence">
  <persistence-unit name="completo" transaction-type="RESOURCE_LOCAL">
    <class>jpa.simple.modelo.Completo</class>
    <properties>
      <property name="javax.persistence.jdbc.driver" value="org.apache.derby.jdbc.EmbeddedDriver" />
      <property name="javax.persistence.jdbc.url" value="jdbc:derby:/home/mar/.baseDatos/completoBD;create=true" />
      <property name="javax.persistence.jdbc.user" value="prueba" />
      <property name="javax.persistence.jdbc.password" value="prueba" />

      <!-- EclipseLink debe crear este esquema de base de datos automaticamente -->
      <property name="eclipselink.ddl-generation" value="create-tables" />
      <property name="eclipselink.ddl-generation.output-mode" value="both" />
    </properties>
  </persistence-unit>
</persistence>
```

Es necesario cambiar el valor del parámetro eclipselink.ddl-generation.output-mode de `database` a `sql-script` o al valor `both`. Si lo hacemos de esta manera, podemos comprobar que se crearán los 2 archivos siguientes: `createDDL.jdbc` y `dropDDL.jdbc`.

Cabe notar que hay que eliminar la propiedad `eclipselink.ddl-generation` después de la primera ejecución de nuestro programa y para las ejecuciones posteriores.

6. Nos creamos la clase `Principal.java` que cada vez que se ejecute su método `main` se va a crear ua nueva entrada en a base de datos de soporte.
```java
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
```

## Ejemplo relaciones persistentes 

1. Crear un nuevo proyecto java llamado `jpa.eclipselink`
2. Crear el subdirectorio `lib` donde se ubicarán los siguientes JARs:
	- `eclipselink.jar` ubicado en la carpeta `/usr/local/eclipselink/jlib/`
	- `javax.persistence_2.2.1.v201807122140.jar` ubicados en `/usr/local/eclipselink/jlib/jpa/`
	- `derby.jar` ubicado en `/usr/local/db-derby/lib/`
3. Añadir los contenios del citado `lib` al `buildpath` de nuestro proyecto. [Pasos](http://www.vogella.com/tutorials/Eclipse/article.html#using-jars-libraries-in-eclipse)

4. Crear el paquete de Java que denominaremos`modelo` que ha de incluir las siguientes clases:
-
	- `Familia:` contiene métodos para obtener la identificación, descripción de una familia y un método que permita listar a todos los miembros de una familia (que son instancias de la clase "Persona"); además tendremos que establecer una relación uno-a-muchos con "Persona".
```java
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
```
-
	- `Persona:` ha de contener métodos para obtener el nombre y los apellidos de una persona, así como también otros para ID; una lista prvada de los empleos que ha tenido cada persona y las siguientes relacione: muchos-a-uno con `familia` a través del método `getFamilia()` y uno-a-muchos con `lista de empleos` a través del método `getListaEmpleos()`.
```java
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
```	
-
	- `Empleo:` ha de incluir como atributs privados: ID(del empleo), salario y descripción del empleo y los métodos `setter-getter` asociados.
```java
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
```

5. Nos creamos el subdirectorio `META-INF` en la carpeta `src` e incluimos el archivo `persistence.xml`:
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<persistence xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
  version="2.0" xmlns="http://java.sun.com/xml/ns/persistence">
  <persistence-unit name="relaciones_persistentes" transaction-type="RESOURCE_LOCAL">
    <class>jpa.simple.modelo.Persona</class>
    <class>jpa.simple.modelo.Familia</class>
    <class>jpa.simple.modelo.Empleo</class>
    <properties>
      <property name="javax.persistence.jdbc.driver" value="org.apache.derby.jdbc.EmbeddedDriver" />
      <property name="javax.persistence.jdbc.url" value="jdbc:derby:/home/mar/.baseDatos/relacionesBD;create=true" />
      <property name="javax.persistence.jdbc.user" value="prueba" />
      <property name="javax.persistence.jdbc.password" value="prueba" />

      <!-- EclipseLink debe crear este esquema de base de datos automaticamente -->
      <property name="eclipselink.ddl-generation" value="create-tables" />
      <property name="eclipselink.ddl-generation.output-mode" value="both" />
    </properties>
  </persistence-unit>
</persistence>
```

Es necesario cambiar el valor del parámetro eclipselink.ddl-generation.output-mode de `database` a `sql-script` o al valor `both`. Si lo hacemos de esta manera, podemos comprobar que se crearán los 2 archivos siguientes: `createDDL.jdbc` y `dropDDL.jdbc`.

Cabe notar que hay que eliminar la propiedad `eclipselink.ddl-generation` después de la primera ejecución de nuestro programa y para las ejecuciones posteriores.

6. Para probar la aplicación creamos una clase Test utilizando para ello el paquete de pruebas unitarias `JUnit` para Java.
-	
	- Importar `JUnit4`
```
Properties > Java Build Path > Libraries > Add Library > JUnit > Junit 4
``` 
-
	- Crear la clase `JpaTest`
```java
package jpa.eclipselink.principal;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpa.eclipselink.modelo.Familia;
import jpa.eclipselink.modelo.Persona;

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
		// Las personas no debe tener ninun asignado todavía
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
``` 



