# Práctica 2

1. Crear un nuevo proyecto Java.
2. En el `WebContet/WEB-INF/lib` se ubicarán los siguientes JARs:
	- `eclipselink.jar` ubicado en la carpeta `/usr/local/eclipselink/jlib/`
	- `javax.persistence_2.2.1.v201807122140.jar` ubicados en `/usr/local/eclipselink/jlib/jpa/`
	- `derby.jar` ubicado en `/usr/local/db-derby/lib/`
	- `servlet-api.jar` [descargado](http://www.java2s.com/Code/Jar/s/Downloadservletapijar.htm) 
3. Añadir los contenios del citado `lib` al `buildpath` de nuestro proyecto. [Pasos](http://www.vogella.com/tutorials/Eclipse/article.html#using-jars-libraries-in-eclipse)
4. Dentro del paquete `modelo` creamos las siguientes clases:
	- `Usuario`
```java
package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author Mª del Mar Alguacil Camarero
 * 
 */

@Entity
public class Usuario{
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;
	private String nombre;
	private String apellido;
	private String email;

	public Usuario() {
		nombre = "";
		apellido = "";
		email = "";
	}

	public Usuario(Usuario usuario){
		id = usuario.getId();
		nombre = usuario.getNombre();
		apellido = usuario.getApellido();
		email = usuario.getEmail();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	/* ELIMINADO
	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", apellido=" + apellido + ", email=" + email + "]";
	}
	*/
}
```
	- `BDUsuario`
```java
package modelo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * 
 * @author Mª del Mar Alguacil Camarero
 *
 */
public class BDUsuario{
	private static final String PERSISTENCE_UNIT_NAME = "administracion_usuarios";
	private static EntityManagerFactory factoria  = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);;

	/**
	 * 	INSERCIÓN DE USUARIO: Si el "usuario" no existe (suponiendo que el correo es único para cada persona), se 
	 * almacena en la base de datos de forma persistente.
	 * 		@param Usuario a insertar
	 */
	public static void insertar(Usuario usuario){
		// Creamos un gestor de entidades
		EntityManager em = factoria.createEntityManager();
		
		// Comprobamos si existe el usuario a partir de su correo electrónico
		if(!existeEmail(usuario.getEmail())){
			// Comenzamos una nueva transacción local de tal forma que 
			// pueda persistir como una nueva entidad.
			em.getTransaction().begin();
			
			em.persist(usuario);
			
			// Hacemos un "commit" de la transacción para salvar la entidad en la base de datos.
			em.getTransaction().commit();
			
			// Cerramos el EntityManager para no perder las entradas.
			em.close();	
		}
	}

	/**
	 * 	ACTUALIZACIÓN DATOS: Si el "usuario" existe, obtiene el usuario mediante una consulta, y modifica el nombre y los apellidos en la 
	 * base de datos.
	 * 		@param Usuario cuyos datos queremos actualizar
	 */
	public static void actualizar(Usuario usuario){
		// Creamos un gestor de entidades
		EntityManager em = factoria.createEntityManager();
		
		// Creamos la consulta necesaria para eliminar el usuario cuyos datos se indicarán después
		Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email");
		// Asignamos los parámetros
		q.setParameter("email", usuario.getEmail());
		
		// Comprobamos si existe algún usuario
		if(q.getResultList().size() != 0){
			// Obtenemos el usuario que nos interesa
			Usuario usuario_localizado = (Usuario) q.getSingleResult();
			
			// Comenzamos una nueva transacción local.
			em.getTransaction().begin();
						
			// Actualizamos los datos del usuario
			usuario_localizado.setNombre(usuario.getNombre());
			usuario_localizado.setApellido(usuario.getApellido());
			
			// Salvamos los cambios
			em.getTransaction().commit();
		}
		
		// Cerramos el gestor de entidades.
		em.close();
	}

	/**
	 * ELIMINACIÓN DE USUARIO: Si el "usuario" existe, se elimina de la base de datos de forma persistente.
	 * 		@param Usuario a eliminar
	 */
	public static void eliminar(Usuario usuario){
		// Creamos un gestor de entidades
		EntityManager em = factoria.createEntityManager();
		
		// Creamos la consulta necesaria para eliminar el usuario cuyos datos se indicarán después
		Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email");
		// Asignamos los parámetros
		q.setParameter("email", usuario.getEmail());
		
		// Comprobamos si existe algún usuario
		if(q.getResultList().size() != 0){
			// Obtenemos el usuario que nos interesa
			Usuario usuario_localizado = (Usuario) q.getSingleResult();
			
			// Comenzamos una nueva transacción local de tal manera que 
			// podamos hacer persistente una nueva entidad. 
			em.getTransaction().begin();
			
			// Eliminamos de la entidad
			em.remove(usuario_localizado);
			
			// Confirmamos la eliminación
			em.getTransaction().commit();
		}
		
		// Cerramos el gestor de entidades.
		em.close();
	}

	/**
	 * BÚSQUEDA DE CORREO ELECTRÓNICOS: 
	 * 		@param Correo electrónico
	 * 		@return Se devuelve "true" si existe el "correo". En caso contrario, "false".
	 */
	public static boolean existeEmail(String email){
		boolean existe = false;
		// Creamos un gestor de entidades
		EntityManager em = factoria.createEntityManager();
		
		// Crear la consulta para buscar el usuario con el correo que especificaremos a continuación
		Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email");
		// Asignamos los parámetros
		q.setParameter("email", email);
		
		if(q.getResultList().size() != 0)
			existe = true;
		
		// Cerramos el EntityManager
		em.close();	
					
		return existe;
	}
	
	/**
	 * SELECCIÓN DE USUARIO: 
	 * 		@param Correo electrónico asociado al usuario a seleccionar.
	 * 		@return Si existe el usuario, se devuelve este. En caso contrario, se devuelve "null".
	 */
	public static Usuario seleccionarUsuario(String email) {
		Usuario usuario = null;
		// Creamos un gestor de entidades
		EntityManager em = factoria.createEntityManager();
		
		// Crear la consulta para buscar el usuario con el correo que especificaremos a continuación
		Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email");
		// Asignamos los parámetros
		q.setParameter("email", email);
		
		if(q.getResultList().size() != 0){
			// Obtenemos el usuario
			usuario = (Usuario) q.getSingleResult();
		}
		
		// Cerramos el EntityManager
		em.close();	

		return usuario;
	}
	
	
	/**
	 * LISTA USUARIO
	 * 		@return Devuelve una lista de todos los usuarios almacenados en el sistema
	 */
	public static List<Usuario> listarUsuarios() {
		// Creamos un gestor de entidades
		EntityManager em = factoria.createEntityManager();
		
		// Crear la consulta para buscar el usuario con el correo que especificaremos a continuación
		Query q = em.createQuery("SELECT u FROM Usuario u");
		
		// Lista de usuarios
		List<Usuario> listaUsuarios = q.getResultList();
				
		// Cerramos el EntityManager
		em.close();	
		
		return listaUsuarios;
	}
}
```	
5. Modificamos `ListaCorreosServlet.java`
```java
package comunicacion;

import java.util.List;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.BDUsuario;
import modelo.Usuario;

@WebServlet("/ListaCorreosServlet")
public class ListaCorreosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String accion = request.getParameter("action");

		if (accion == null) {
			response.setContentType("text/html");
			PrintWriter writer = response.getWriter();
			writer.println("<h1>Practica 2 de DSS: Arquitecturas Software</h1>");
			
			//////MOD
			writer.println("<table>");
			writer.println("<thead>");
			writer.println("<tr>");
			writer.println("<th>Nombre</th>");
			writer.println("<th>Apellido</th>");
			writer.println("<th>Email</th>");
			writer.println("</tr>");
			writer.println("</thead>");
			writer.println("<tbody>");
			for (Usuario usuario: BDUsuario.listarUsuarios()) {
				writer.println("<tr>");
				writer.println("<td>" + usuario.getNombre() + "</td>");
				writer.println("<td>" + usuario.getApellido() + "</td>");
				writer.println("<td>" + usuario.getEmail() + "</td>");
				writer.println("</tr>");
			}
			writer.println("</tbody>");
			writer.println("</table>");
			/////END-MOD
		} else {
			//////MOD
			//PrintWriter writer2 = response.getWriter();
			
			String name = request.getParameter("nombre");
			String surname = request.getParameter("apellido");
			String email = request.getParameter("email");
			
			// Para informar del resultado
			ObjectOutputStream objeto = new ObjectOutputStream(response.getOutputStream());
			/////END-MOD
			
			switch (accion) {
			
			case "eliminarUsuario":
				if (BDUsuario.existeEmail(email)) {
					Usuario user = BDUsuario.seleccionarUsuario(email);
					BDUsuario.eliminar(user);
					
					//////MOD
					objeto.writeInt(0);
					objeto.writeObject("Usuario aliminado correctamente.");
					/////END-MOD
				} else {
					/////MOD
					//writer2.println("<span> Usuario no encontrado </span>");
					objeto.writeInt(1);
					objeto.writeObject("Usuario no encontrado.");
					/////END-MOD
				}
				break;
			case "aniadirUsuario":
				if (!BDUsuario.existeEmail(email)) {
					//////MOD
					//Usuario user = BDUsuario.seleccionarUsuario(email);
					Usuario user = new Usuario();
					user.setNombre(name);
					user.setApellido(surname);
					user.setEmail(email);
					/////END-MOD
					
					BDUsuario.insertar(user);
					
					//////MOD
					objeto.writeInt(0);
					objeto.writeObject("Usuario aniadido correctamente.");
					/////END-MOD
				} else {
					//////MOD
					//writer2.println("<span> El usuario ya existe </span>");
					
					objeto.writeInt(1);
					objeto.writeObject("El usuario ya existe.");
					/////END-MOD
				}
				break;
			case "actualizarUsuario":
				if (BDUsuario.existeEmail(email)) {
					Usuario user = BDUsuario.seleccionarUsuario(email);
					//////MOD
					user.setNombre(name);
					user.setApellido(surname);
					/////END-MOD
					
					BDUsuario.actualizar(user);
					
					//////MOD
					objeto.writeInt(0);
					objeto.writeObject("Usuario actualizado correctamente.");
					/////END-MOD
				} else {
					//////MOD
					//writer2.println("<span> El usuario no existe </span>");
					
					objeto.writeInt(1);
					objeto.writeObject("El usuario no existe.");
					/////END-MOD
				}
				break;
			//////MOD
			default:
				List<Usuario> listaUsuarios = BDUsuario.listarUsuarios();
				objeto.writeObject(listaUsuarios);
			/////END-MOD
			}
			
			//////MOD
			objeto.flush();
			objeto.close();
			/////END-MOD
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
```
6. Cambiar la siguiente línea en fichero `Cliente.java`:
```java
public static String urlString = "http://localhost:8080/P2/ListaCorreosServlet";
```
7. Creamos el fichero `persistence.xml` dentro de la carpeta `META-INF`:
```java
<?xml version="1.0" encoding="UTF-8" ?>
<persistence xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
  version="2.0" xmlns="http://java.sun.com/xml/ns/persistence">
  <persistence-unit name="administracion_usuarios" transaction-type="RESOURCE_LOCAL">
    <class>modelo.Usuario</class>
    <properties>
      <property name="javax.persistence.jdbc.driver" value="org.apache.derby.jdbc.EmbeddedDriver" />
      <property name="javax.persistence.jdbc.url" value="jdbc:derby:/home/mar/.baseDatos/BDUsuario;create=true" />
      <property name="javax.persistence.jdbc.user" value="prueba" />
      <property name="javax.persistence.jdbc.password" value="prueba" />
      
      <!-- Connection Pool - QUITADO -->
	  <property name="eclipselink.connection-pool.default.initial" value="1" />
	  <property name="eclipselink.connection-pool.node2.min" value="1"/>
	  <property name="eclipselink.connection-pool.node2.max" value="16"/>
	  <property name="eclipselink.connection-pool.node2.url" value="jdbc:derby:/home/mar/.baseDatos/BDUsuario;create=true"/>

      <!-- EclipseLink debe crear este esquema de base de datos automaticamente -->
      <property name="eclipselink.ddl-generation" value="create-tables" />
      <property name="eclipselink.ddl-generation.output-mode" value="both" />
    </properties>
  </persistence-unit>
```
