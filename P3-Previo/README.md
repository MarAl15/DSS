# Configuración del marco de trabajo

## Jersey

1. [Descargar](http://repo1.maven.org/maven2/org/glassfish/jersey/bundles/jaxrs-ri/2.27/jaxrs-ri-2.27.zip)
<!-- 2. Extraer en `/usr/local`-->
2. [Descargar Jersey Client](https://jar-download.com/artifacts/com.sun.jersey/jersey-client/1.19.1/source-code)
3. [Descagar Jersey Media](http://www.java2s.com/Code/Jar/j/Downloadjerseymediamultipart21jar.htm)


# Ejemplo tutorial

1. Crearse un proyecto Web Dinámico (_Dynamic Web Project_) denominado `mio.jersey.primero`.

2. Usar Apache Tomcat v9.0 como _Target runtime_ y utilizar la configuración por defecto de este.

3. Asegurarnos que seleccionamos la opción de creación de un descriptor de despliegue (`web.xml`) al aceptar la vista Java del Proyecto.

Marcar _Generate web.xml deployment descriptor_ en el último paso de la creación del proyecto.

4. Copiar todos los `*.jar` de la distribucion de Jersey que nos hayamos instalado en el disco duro, contenidos en las carpetas `api`, `ext` y `lib`, en `WebContent/WEB-INF/lib` del proyeco en el IDE.

5. Añadirlos al _Build Path_
	- Botón derecho sobre el proyecto actual.
	- _Java Build Path_ > _Libraries_ > _Add Exterbak JARs..._
	- Acceder a la carpeta donde se encuentra y seleccionarlos.
	


6. Crear las siguientes clases:
-
	- `Todo:` representa el _dominio_ de los datos de la aplicación.
```java
package mio.jersey.primero.modelo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
// JAX apoya una correspondencia automática desde una clase JAXB 
// con anotaciones a XML y JSON
public class Todo {
	private String resumen;
	private String descripcion;
	
	public String gestResumen(){
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
}
```

-
	- `TodoRecurso:` devolverá una instancia de la clase que representa al dominio de datos `Todo` de nuestra aplicación.
```java
package mio.jersey.primero.recurso;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import mio.jersey.primero.modelo.Todo;

// Esta clase solo devuelve una instancia de la clase Todo
@Path("/todo")
public class TodoRecurso {
	// Este método se llamará si existe una petición XML desde el cliente
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Todo getXML(){
		Todo todo = new Todo();
		todo.setResumen("Este es mi primer todo");
		todo.setDescripcion("Este es mi primer todo");
		return todo;
	}
	
	// Lo que sigue se puede utilizar para comprobar la integración
	// con el navegador que utilicemos
	@GET
	@Produces({MediaType.TEXT_XML})
	public Todo getHTML(){
		Todo todo = new Todo();
		todo.setResumen("Este es mi primer todo");
		todo.setDescripcion("Este es mi primer todo");
		return todo;
	}
}
```	

7. Modificar el archivo de descripción de despliegue `web.xml` contenido en la carpeta `mio.jersey.primero/WebContent/WEB-INF`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xmlns="http://xmlns.jcp.org/xml/ns/javaee" 
xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd" id="WebApp_ID" version="3.1">
  <display-name>mio.jersey.primero</display-name>
  <servlet>
  	<servlet-name>Servicio REST de Jersey</servlet-name>
  	<servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>
  	<init-param>
  		<param-name>jersey.config.server.provider.packages</param-name>
  		<param-value>mio.jersey.primero.recurso</param-value>
  	</init-param>
  	<load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
  	<servlet-name>Servicio REST de Jersey</servlet-name>
  	<url-pattern>/rest/*</url-pattern>
  </servlet-mapping>
</web-app>
```

8. Ejecutar la aplicación:
```
Run as > Run on Server
```

9. Accedemos a la dirección http://localhost:8080/mio.jersey.primero/rest/todo

10. Crearse en el IDE otro proyecto (esta vez será un proyecto de Java _regular_) que llamamos `mio.jersey.primero.cliente` en donde también debemos incluir todos los `*.jar` de Jersey e incluirlos en el camino de construcción (_build path_) del nuevo proyecto.
	
11. Implementar la clase `Test`:
```java
package mio.jersey.primero.cliente;

import java.net.URI;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class Test {
	public static void main(String[] args){
		ClientConfig config = new DefaultClientConfig();
		Client cliente = Client.create(config);
		WebResource servicio = cliente.resource(getBaseURI());
		
		// Mostrar el contenido del recurso Todos como texto XML
		System.out.println("Mostrar contenido del recurso como Texto XML Plano");
		System.out.println(servicio.path("rest").
				path("todo").accept(MediaType.TEXT_XML).get(String.class));
		// Mostrar contenido para aplicaciones XML
		System.out.println("Mostrar contenido del recurso para aplicacion XML");
		System.out.println(servicio.path("rest").path("todo").
				accept(MediaType.APPLICATION_XML).get(String.class));/**/
		
	}

	private static URI getBaseURI(){
		return UriBuilder.fromUri("http://localhost:8080/mio.jersey.primero").build();
	}
}
```
	
12. Ejecutando el proyecto `mio.jersey.primero` en el servidor, lanzamos el test y obtenemos:
```
Mostrar contenido del recurso como Texto XML Plano
<?xml version="1.0" encoding="UTF-8" standalone="yes"?><todo><descripcion>Este es mi primer todo</descripcion></todo>
Mostrar contenido del recurso para aplicacion XML
<?xml version="1.0" encoding="UTF-8" standalone="yes"?><todo><descripcion>Este es mi primer todo</descripcion></todo>
```

## Otro ejemplo

1. Creación de un SW CRUD (Create, Read, Update, Delete). El nombre del proyecto será `mio.jersey.p3`. 

2. Implementación de la clase `Todo`:
```java
package mio.jersey.p3.modelo;

import javax.xml.bind.annotation.XmlRootElement;

// Definición del dominio de datos de la aplicación
@XmlRootElement
public class Todo {
	private String id;
	private String resumen;
	private String descripcion;
	
	public Todo(){}
	
	public Todo(String id, String resumen){
		this.id = id;
		this.resumen = resumen;
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
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
}
```

3. Creamos una clase _singleton_ de Java basada en enumeración que actuará como _proveedor de contenidos_ (reseñas bibliográficas, catalogos de objetos multimedia, cartera de clientes, etc) de nuestro SW:
```java
package mio.jersey.p3.modelo;

import java.util.HashMap;
import java.util.Map;

public enum TodoDao{
	INSTANCE;
	private Map<String, Todo> proveedorContenidos = new HashMap<String, Todo>();
	
	private TodoDao(){
		// Creamos 2 contenidos iniciales
		Todo todo = new Todo("1", "Aprender REST");
		todo.setDescripcion("Leer http://lsi.ugr.es/dsbcs/Documentos/Practica/practica3.html");
		proveedorContenidos.put("1", todo);
		todo = new Todo("2", "Apreder algo sobre DSBCS");
		todo.setDescripcion("Leer todo el material de http://lsi.ugr.es/dsbcs");
		proveedorContenidos.put("2", todo);
	}
	
	public Map<String, Todo> getModel(){
		return proveedorContenidos;
	}
}
```

Se utiliza el concepto de "objeto de acceso a datos" (DAO), que nos proporciona una interfaz abstracta para facilitar el acceso a los datos del contenedor desde una base de datos u otro mecanismo de persistencia.

4. Clase de Java para proporcionar los servicios REST necesarios para acceder y/o modificar nuestro proveedor de contenidos a las aplicaciones y otros SW. Para hacerlo vamos a utilizar un marco de trabajo _Jersey_ para servicios Web _RESTful_, que nos proporciona apoyo para trabajar con una interfaz de aplicaciones JAX y además es considerad actualmente como la implementación de referencia del estándar jsr 311.
	
	Programamos la clase `TodoRecurso` dentro del paquete `recursos`:
-
	- Método `GET` para acceder al recurso desde el navegador o desde una aplicación cliente.
	- Método `PUT` para incluir contenidos.
	- Método `DELETE` para suprimirlos.
```java
package mio.jersey.p3.recursos;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import mio.jersey.p3.modelo.Todo;
import mio.jersey.p3.modelo.TodoDao;

public class TodoRecurso {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String id;
	
	public TodoRecurso(UriInfo uriInfo, Request request, String id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	// Método GET para acceder al recurso desde el navegador
	@GET
	@Produces(MediaType.TEXT_XML)
	public Todo getBrowser(){
		Todo todo = TodoDao.INSTANCE.getModel().get(id);
		
		if (todo == null)
			throw new RuntimeException("Get: Todo con id " + id + " no encontrado");
		
		return todo;
	}
	
	// Método GET para acceder al recurso desde una aplicación
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Todo getApp(){
		Todo todo = TodoDao.INSTANCE.getModel().get(id);
		
		if (todo == null)
			throw new RuntimeException("GET: Todo con id " + id + " no encontrado");
		
		return todo;
	}
	
	// Método PUT para incluir contenido
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response putTodo(@PathParam("id") String id, Todo todo){
		Response res;
				
		if(TodoDao.INSTANCE.getModel().containsKey(todo.getId()))
			res = Response.noContent().build();
		else
			res = Response.created(uriInfo.getAbsolutePath()).build();
		
		TodoDao.INSTANCE.getModel().put(todo.getId(), todo);
		
		return res;
	}
	
	// Método DELETE para suprimirlo
	@DELETE
	public void deleteTodo(){
		Todo eliminado = TodoDao.INSTANCE.getModel().remove(id);
		
		if (eliminado == null)
			throw new RuntimeException("DELETE: Todo con id " + id + " no encontrado");
		
	}
		
}
```		
	Y en la clase `TodosRecursos`:
-	
	- Método `POST` para el envio de datos y de formularios Web, para las aplicaciones cliente y navegadores.
```java
package mio.jersey.p3.recursos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import mio.jersey.p3.modelo.*;

// Hara corresponder el recurso al URL todos
@Path("/todos")
public class TodosRecurso{
	// Permite insertar objetos contextuales en la clase,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	// Devolverá la lista de todos los elementos contenidos en el 
	// proveedor al navegador del usuario.
	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Todo> getTodoBrowser(){
		List<Todo> todos = new ArrayList<Todo>();
		todos.addAll(TodoDao.INSTANCE.getModel().values());
		return todos;
	}
	
	// Devolverá la lista de todos los elementos contenidos en el 
	// proveedor a las aplicaciones cliente
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Todo> getTodoApp(){
		List<Todo> todos = new ArrayList<Todo>();
		todos.addAll(TodoDao.INSTANCE.getModel().values());
		return todos;
	}
	
	// Para obtener el número total de elementos en el proveedor
	// de contenidos
	@GET
	@Path("cont")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount(){
		int cont = TodoDao.INSTANCE.getModel().size();
		return String.valueOf(cont);
	}
	
	// Para enviar datos al servidor como un formulario Web
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newTodo(@FormParam("id") String id,
			@FormParam("resumen") String resumen,
			@FormParam("descripcion") String descripcion,
			@Context HttpServletResponse servletResponse) 
					throws IOException{
		Todo todo = new Todo(id, resumen);
		
		if(descripcion != null){
			todo.setDescripcion(descripcion);
		}
		
		TodoDao.INSTANCE.getModel().put(id, todo);
		servletResponse.sendRedirect("../crear_todo.html");
	}
	
	// Para poder pasarle argumentos a las operaciones en el servidor.
	// Permite por ejemplo escribir 
	//	http://localhost:8080/mio.jersey.p3/rest/todos/1
	@Path("{todo}")
	public TodoRecurso getTodo(@PathParam("todo") String id){
		return new TodoRecurso(uriInfo, request, id);
	}
}
```
	
5. Completamos el archivo `web.xml` ubicado en la carpeta `WebContent/WEB-INF/web.xml`
```java
<?xml version="1.0" encoding="UTF-8"?>
	<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns="http://xmlns.jcp.org/xml/ns/javaee" 
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
	http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd" id="WebApp_ID" version="3.1">
	<display-name>mio.jersey.p3</display-name>
	
	<servlet>
		<servlet-name>Servicio REST de Jersey</servlet-name>
		<servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>
		
		<!-- Registra recursos que estan ubicados dentro de recurso-->
		<init-param>
			<param-name>jersey.config.server.provider.packages</param-name>
			<param-value>mio.jersey.p3.recursos</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
	
	<servlet-mapping>
		<servlet-name>Servicio REST de Jersey</servlet-name>
		<url-pattern>/rest/*</url-pattern>
	</servlet-mapping>
</web-app>
```

6. Creamos el archivo `crear_todo.html` dentro de `WebContent`:
```html
<!DOCTYPE html>
<html>
	<head>
		<title>Formulario para crear un nuevo recurso</title>
	</head>
	<body>
		<form action="../mio.jersey.p3/rest/todos" method="POST">
		<label for="id">ID</label>
		<input name="id" />
		<br/>
		<label for="resumen">Resumen</label>
		<input name="resumen" />
		<br/>
		Descripcion:
		<TEXTAREA NAME="descripcion" COLS=40 ROWS=6></TEXTAREA>
		<br/>
		<input type="submit" value="Aniadir" />
		</form>
	</body>
</html>
```

7. Ejecutamos la aplicación (Botón derecho sobre `mio.jersey.p3` > `Run As` > `Run on Server`) y accedemos a la dirección http://localhost:8080/mio.jersey.p3/rest/todos
```xml
<todoes>
	<todo>
		<descripcion>Leer http://lsi.ugr.es/dsbcs/Documentos/Practica/practica3.html</descripcion>
		<id>1</id>
		<resumen>Aprender REST</resumen>
	</todo>
	<todo>
		<descripcion>Leer todo el material de http://lsi.ugr.es/dsbcs</descripcion>
		<id>2</id>
		<resumen>Apreder algo sobre DSBCS</resumen>
	</todo>
</todoes>
```

8. Para mostrar de una manera sencilla toda la funcionalidad que nuestro servicio de provisión de contenidos puede ofrecer, vamos a programar varias versiones del cliente que irán mostrando progresivamente el resultado de las operaciones del servidor.

Para ello se crea una clase, en el fichero `Probador.java` dentro del paquete `mio.jersey.p3.cliente` (aunque también se puede crear otro proyecto Java como se realizó en el anterior ejemplo), que sirve para mostrar los contenidos del proveedor, después de introducir 2 elementos adicionales cn `PUT` y dentro de un formulario, según los formatos que hemos denominado _texto XML plano y formulario HTML_. Además se incluir el código correspondiente para comprobar que la eliminación se realiza correctamente.
```java
package mio.jersey.p3.cliente;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

import mio.jersey.p3.modelo.Todo;

public class Probador {
	public static void main(String[] args){
		ClientConfig config = new DefaultClientConfig();
		Client cliente = Client.create(config);
		WebResource servicio = cliente.resource(getBaseURI());
		
		// Crearse un tercer "objeto" todo, aparte de los 2 que ya
		// estan creados en TodoDao
		Todo todo = new Todo("3", "Este es el resumen del tercer registro");
		ClientResponse respuesta = servicio.path("rest").path("todos")
				.path(todo.getId()).accept(MediaType.APPLICATION_XML)
				.put(ClientResponse.class, todo);
		
		System.out.print("Codigo devuelto: ");
		// El código que se devuelve debería ser: 201 == created resource
		System.out.println(respuesta.getStatus());
		
		// Mostrar el contenido del recurso Todos como texto XML
		System.out.println("Mostrar como Texto XML Plano");
		System.out.println(servicio.path("rest").path("todos")
				.accept(MediaType.TEXT_XML).get(String.class));
		
		// Ahora nos creamos un cuarto recurso Todo utilizando
		// un formulario Web
		System.out.println("Creacion de un formulario");
		Form form = new Form();
		form.add("id", "4");
		form.add("resumen", "Demostración de la biblioteca-cliente para formularios");
		respuesta = servicio.path("rest").path("todos")
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.post(ClientResponse.class, form);
		System.out.println("Respuesta con el formulario"
				+ respuesta.getEntity(String.class));
		
		// Mostrar el contenido del recurso Todos, se ha debido
		// crear el elemento con id=4
		System.out.println("Contenidos del recurso despues de enviar el elemento id=4");
		System.out.println(servicio.path("rest").path("todos")
				.accept(MediaType.APPLICATION_XML).get(String.class));
	
		// Ahora vamos a eliminar el "objeto" con id=1 del recurso
		servicio.path("rest").path("todos/1").delete();
		
		// Mostramos el contenido del recurso Todo, el elemento
		// con id=1 debería haber sido eliminado
		System.out.println("El elemento con id=1 del recurso se ha eliminado");
		System.out.println(servicio.path("rest").path("todos")
				.accept(MediaType.APPLICATION_XML).get(String.class));
	}

	private static URI getBaseURI(){
		return UriBuilder.fromUri("http://localhost:8080/mio.jersey.p3").build();
	}
}
```

Obteniendo, al ejecutar el programa en el servidor y la clase anterior como aplicación Java (Botón derecho sobre el fichero `Probador.java` > `Run As` > `Java Application`), el resultado siguiente:
```java
Codigo devuelto: 201
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<todoes>
	<todo>
		<descripcion>Leer http://lsi.ugr.es/dsbcs/Documentos/Practica/practica3.html
		</descripcion>
		<id>1</id>
		<resumen>Aprender REST</resumen>
	</todo>
	<todo>
		<descripcion>Leer todo el material de http://lsi.ugr.es/dsbcs
		</descripcion>
		<id>2</id>
		<resumen>Aprender algo sobre DSBCS</resumen>
	</todo>
	<todo>
		<id>3</id>
		<resumen>Este es el resumen del tercer registro</resumen>
	</todo>
</todoes>

El elemento con id = 1 del recurso se ha eliminado
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<todoes>
	<todo>
		<descripcion>Leer todo el material de http://lsi.ugr.es/dsbcs
		</descripcion>
		<id>2</id>
		<resumen>Aprender algo sobre DSBCS</resumen>
	</todo>
	<todo>
		<id>3</id>
		<resumen>Este es el resumen del tercer registro</resumen>
	</todo>
</todoes>

Contenidos del recurso, despues de enviar el elemento id=4
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<todoes>
	<todo>
		<descripcion>Leer todo el material de http://lsi.ugr.es/dsbcs</descripcion>
		<id>2</id>
		<resumen>Aprender algo sobre DSBCS</resumen>
	</todo>
	<todo>
		<id>3</id>
		<resumen>Este es el resumen del tercer registro</resumen>
	</todo>
	<todo>
		<id>4</id>
		<resumen>Demostracion de la biblioteca-cliente para formularios</resumen>
	</todo>
</todoes>
```






	
