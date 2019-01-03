# Práctica 3

1. Creación de un SW CRUD (Create, Read, Update, Delete). El nombre del proyecto será `P3`. 

2. Implementación de la clase `Libro`:
```java
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
```

3. Creamos una clase _singleton_ de Java basada en enumeración que actuará como _proveedor de contenidos_ de nuestro SW:
```java
package modelo;

import java.util.HashMap;
import java.util.Map;

public enum LibroDao{
	INSTANCE;
	private Map<String, Libro> proveedorContenidos = new HashMap<String, Libro>();
	
	private LibroDao(){
		// Creamos 2 contenidos iniciales
		Libro libro = new Libro("1", "Diez negritos", "Agatha Christie");
		libro.setResumen("Diez personas reciben sentadas cartas firmadas por un desconocido Mr. Owen, "
				+ "que las invita a pasar unos días en la mansión que tiene en uno de los islotes de la "
				+ "costa de Devon. La primera noche, después de la cena, una voz los acusa, de ser culpables "
				+ "de un crimen. Lo que parece ser una broma macabra se convierte en una espantosas realidad cuando, "
				+ "uno por uno, los diez invitados son asesinados en un atmosfera de miedo y mutuas recriminaciones. "
				+ "La clave parece estar en una vieja canción infantil: 'Diez negritos se fueron a cenar, uno se ahogó "
				+ "y quedaron nueve. Nueve negritos trasnocharon mucho, uno no despertó, y quedaron ocho...'. ");
		proveedorContenidos.put("1", libro);
		
		libro = new Libro("2", "La voz del violín", "Andrea Camilleri");
		libro.setResumen("La aparente paz siciliana se ve truncada por el asesinato de una extraña. Una joven hermosa, "
				+ "mujer de un médico boloñés, aparece muerta en el chalet de ambos. Pocas pertenencias la acompañaban "
				+ "en la escena del crimen, aparte de un misterioso violín guardado en su estuche. Su bolsa de joyas se "
				+ "ha esfumado y todas las miradas se centran en un pariente desequilibrado que ha desaparecido la misma "
				+ "noche del crimen. Montalbano, con su parsimonia habitual, inicia la investigación. No cree a nadie, "
				+ "no se fía de nadie. Tras la muerte de un sospechoso, sus superiores dan por cerrado el caso, pero él,"
				+ " ni hablar. Transitando los límites de la legalidad, como es su costumbre, Montalbano ha de "
				+ "relacionarse y pactar con los elementos más indeseables y abyectos del hampa, iniciando un viaje a "
				+ "lo más oscuro del alma humana, en el fondo, su territorio predilecto.");
		proveedorContenidos.put("2", libro);
	}
	
	public Map<String, Libro> getModel(){
		return proveedorContenidos;
	}
}
```

Se utiliza el concepto de "objeto de acceso a datos" (DAO), que nos proporciona una interfaz abstracta para facilitar el acceso a los datos del contenedor desde una base de datos u otro mecanismo de persistencia.

4. Clase de Java para proporcionar los servicios REST necesarios para acceder y/o modificar nuestro proveedor de contenidos a las aplicaciones y otros SW. Para hacerlo vamos a utilizar un marco de trabajo _Jersey_ para servicios Web _RESTful_, que nos proporciona apoyo para trabajar con una interfaz de aplicaciones JAX y además es considerad actualmente como la implementación de referencia del estándar jsr 311.
	
	Programamos la clase `LibroRecurso` dentro del paquete `recursos`:
	
	- Método `GET` para acceder al recurso desde el navegador o desde una aplicación cliente.
	- Método `PUT` para incluir contenidos.
	- Método `DELETE` para suprimirlos.
```java
package recursos;

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

import modelo.Libro;
import modelo.LibroDao;

public class LibroRecurso {
	String id;
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	public LibroRecurso(UriInfo uriInfo, Request request, String id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	// Método GET para acceder al recurso desde el navegador
	@GET
	@Produces(MediaType.TEXT_XML)
	public Libro getBrowser(){
		Libro libro = LibroDao.INSTANCE.getModel().get(id);
		
		if (libro == null)
			throw new RuntimeException("Get: Libro con id " + id + " no encontrado");
		
		return libro;
	}
	
	// Método GET para acceder al recurso desde una aplicación
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Libro getApp(){
		Libro libro = LibroDao.INSTANCE.getModel().get(id);
		
		if (libro == null)
			throw new RuntimeException("GET: Libro con id " + id + " no encontrado");
		
		return libro;
	}
	
	// Método PUT para incluir contenido
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response putLibro(@PathParam("id") String id, Libro libro){
		Response res;
				
		if(LibroDao.INSTANCE.getModel().containsKey(libro.getId()))
			res = Response.noContent().build();
		else
			res = Response.created(uriInfo.getAbsolutePath()).build();
		
		LibroDao.INSTANCE.getModel().put(libro.getId(), libro);
		
		return res;
	}
	
	// Método DELETE para suprimirlo
	@DELETE
	public void deleteLibro(){
		Libro eliminado = LibroDao.INSTANCE.getModel().remove(id);
		
		if (eliminado == null)
			throw new RuntimeException("DELETE: Libro con id " + id + " no encontrado");
		
	}
	
}
```	

Y en la clase `LibrosRecursos`:

-
	- Método `POST` para el envio de datos y de formularios Web, para las aplicaciones cliente y navegadores.
	
```java
package recursos;

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

import modelo.*;

// Hara corresponder el recurso al URL libros
@Path("/libros")
public class LibrosRecurso{
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
	public List<Libro> getLibroBrowser(){
		List<Libro> libros = new ArrayList<Libro>();
		libros.addAll(LibroDao.INSTANCE.getModel().values());
		return libros;
	}
	
	// Devolverá la lista de todos los elementos contenidos en el 
	// proveedor a las aplicaciones cliente
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Libro> getLibroApp(){
		List<Libro> libros = new ArrayList<Libro>();
		libros.addAll(LibroDao.INSTANCE.getModel().values());
		return libros;
	}
	
	// Para obtener el número total de elementos en el proveedor
	// de contenidos
	@GET
	@Path("cont")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount(){
		int cont = LibroDao.INSTANCE.getModel().size();
		return String.valueOf(cont);
	}
	
	// Para enviar datos al servidor como un formulario Web
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newLibro(@FormParam("id") String id,
			@FormParam("titulo") String titulo,
			@FormParam("autor") String autor,
			@FormParam("resumen") String resumen,
			@Context HttpServletResponse servletResponse) 
					throws IOException{
		Libro libro = new Libro(id, titulo, autor);
		
		if(resumen != null){
			libro.setResumen(resumen);
		}
		
		LibroDao.INSTANCE.getModel().put(id, libro);
		servletResponse.sendRedirect("../crear_libro.html");
	}
	
	// Para poder pasarle argumentos a las operaciones en el servidor.
	// Permite por ejemplo escribir 
	//	http://localhost:8080/P3/rest/libros/1
	@Path("{libro}")
	public LibroRecurso getLibro(@PathParam("libro") String id){
		return new LibroRecurso(uriInfo, request, id);
	}
}
```
	
5. Completamos el archivo `web.xml` ubicado en la carpeta `WebContent/WEB-INF/web.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns="http://xmlns.jcp.org/xml/ns/javaee" 
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
	http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd" id="WebApp_ID" version="3.1">
	<display-name>P3</display-name>
	
	<servlet>
		<servlet-name>Servicio REST de Jersey</servlet-name>
		<servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>
		
		<!-- Registra recursos que estan ubicados dentro de recurso-->
		<init-param>
			<param-name>jersey.config.server.provider.packages</param-name>
			<param-value>recursos</param-value>
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
		<form action="../P3/rest/libros" method="POST">
		<label for="id">ID</label>
		<input name="id" />
		<br/>
		<label for="titulo">Titulo</label>
		<input name="titulo" />
		<br/>
		<label for="autor">Autor/a</label>
		<input name="autor" />
		<br/>
		Resumen:
		<TEXTAREA NAME="resumen" COLS=40 ROWS=6></TEXTAREA>
		<br/>
		<input type="submit" value="Aniadir" />
		</form>
	</body>
</html>
```

7. Ejecutamos la aplicación (Botón derecho sobre `P3` > `Run As` > `Run on Server`) y accedemos a la dirección http://localhost:8080/P3/rest/libros
```xml
<libroes>
	<libro>
		<autor>Agatha Christie</autor>
		<id>1</id>
		<resumen>
			Diez personas reciben sentadas cartas firmadas por un desconocido Mr. Owen, que las invita a pasar unos días en la mansión que tiene en uno de los islotes de la costa de Devon. La primera noche, después de la cena, una voz los acusa, de ser culpables de un crimen. Lo que parece ser una broma macabra se convierte en una espantosas realidad cuando, uno por uno, los diez invitados son asesinados en un atmosfera de miedo y mutuas recriminaciones. La clave parece estar en una vieja canción infantil: 'Diez negritos se fueron a cenar, uno se ahogó y quedaron nueve. Nueve negritos trasnocharon mucho, uno no despertó, y quedaron ocho...'. 
		</resumen>
		<titulo>Diez negritos</titulo>
	</libro>
	<libro>
		<autor>Andrea Camilleri</autor>
		<id>2</id>
		<resumen>
			La aparente paz siciliana se ve truncada por el asesinato de una extraña. Una joven hermosa, mujer de un médico boloñés, aparece muerta en el chalet de ambos. Pocas pertenencias la acompañaban en la escena del crimen, aparte de un misterioso violín guardado en su estuche. Su bolsa de joyas se ha esfumado y todas las miradas se centran en un pariente desequilibrado que ha desaparecido la misma noche del crimen. Montalbano, con su parsimonia habitual, inicia la investigación. No cree a nadie, no se fía de nadie. Tras la muerte de un sospechoso, sus superiores dan por cerrado el caso, pero él, ni hablar. Transitando los límites de la legalidad, como es su costumbre, Montalbano ha de relacionarse y pactar con los elementos más indeseables y abyectos del hampa, iniciando un viaje a lo más oscuro del alma humana, en el fondo, su territorio predilecto.
		</resumen>
		<titulo>La voz del violín</titulo>
	</libro>
</libroes>
```

Accediendo a http://localhost:8080/P3/crear_libro.html podemos crear o modificar un elemento.

8. Para mostrar de una manera sencilla toda la funcionalidad que nuestro servicio de provisión de contenidos puede ofrecer, vamos a programar varias versiones del cliente que irán mostrando progresivamente el resultado de las operaciones del servidor.

Para ello se crea una clase, en el fichero `Probador.java` dentro del paquete `cliente` (aunque también se puede crear otro proyecto Java como se realizó en el anterior ejemplo), que sirve para mostrar los contenidos del proveedor, después de introducir 2 elementos adicionales cn `PUT` y dentro de un formulario, según los formatos que hemos denominado _texto XML plano y formulario HTML_. Además se incluir el código correspondiente para comprobar que la eliminación se realiza correctamente.
```java
package cliente;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

import modelo.Libro;

public class Probador {
	public static void main(String[] args){
		ClientConfig config = new DefaultClientConfig();
		Client cliente = Client.create(config);
		WebResource servicio = cliente.resource(getBaseURI());
		
		// Crearse un tercer "objeto" libro, aparte de los 2 que ya
		// estan creados en LibroDao
		Libro libro = new Libro("3", "La vuelta al mundo en 80 dias", "Julio Verne");
		ClientResponse respuesta = servicio.path("rest").path("libros")
				.path(libro.getId()).accept(MediaType.APPLICATION_XML)
				.put(ClientResponse.class, libro);
		
		System.out.print("Codigo devuelto: ");
		// El código que se devuelve debería ser: 201 == created resource
		System.out.println(respuesta.getStatus());
		
		// Mostrar el contenido del recurso Libros como texto XML
		System.out.println("Mostrar como Texto XML Plano");
		System.out.println(servicio.path("rest").path("libros")
				.accept(MediaType.TEXT_XML).get(String.class));
		
		// Ahora nos creamos un cuarto recurso Libro utilizando
		// un formulario Web
		System.out.println("Creacion de un formulario");
		Form form = new Form();
		form.add("id", "4");
		form.add("resumen", "Demostración de la biblioteca-cliente para formularios");
		respuesta = servicio.path("rest").path("libros")
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.post(ClientResponse.class, form);
		System.out.println("Respuesta con el formulario"
				+ respuesta.getEntity(String.class));
		
		// Mostrar el contenido del recurso Libros, se ha debido
		// crear el elemento con id=4
		System.out.println("Contenidos del recurso despues de enviar el elemento id=4");
		System.out.println(servicio.path("rest").path("libros")
				.accept(MediaType.APPLICATION_XML).get(String.class));
	
		// Ahora vamos a eliminar el "objeto" con id=1 del recurso
		servicio.path("rest").path("libros/1").delete();
		
		// Mostramos el contenido del recurso Libro, el elemento
		// con id=1 debería haber sido eliminado
		System.out.println("El elemento con id=1 del recurso se ha eliminado");
		System.out.println(servicio.path("rest").path("libros")
				.accept(MediaType.APPLICATION_XML).get(String.class));
	}

	private static URI getBaseURI(){
		return UriBuilder.fromUri("http://localhost:8080/P3").build();
	}
}
```

Obteniendo, al ejecutar el programa en el servidor y la clase anterior como aplicación Java (Botón derecho sobre el fichero `Probador.java` > `Run As` > `Java Application`), el resultado siguiente:
```xml
Codigo devuelto: 201

Mostrar como Texto XML Plano
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<libroes>
	<libro>
		<autor>Agatha Christie</autor>
		<id>1</id>
		<resumen>
			Diez personas reciben sentadas cartas firmadas por un desconocido Mr. Owen, que las invita a pasar unos días en la mansión que tiene en uno de los islotes de la costa de Devon. La primera noche, después de la cena, una voz los acusa, de ser culpables de un crimen. Lo que parece ser una broma macabra se convierte en una espantosas realidad cuando, uno por uno, los diez invitados son asesinados en un atmosfera de miedo y mutuas recriminaciones. La clave parece estar en una vieja canción infantil: 'Diez negritos se fueron a cenar, uno se ahogó y quedaron nueve. Nueve negritos trasnocharon mucho, uno no despertó, y quedaron ocho...'. 
		</resumen>
		<titulo>Diez negritos</titulo>
	</libro>
	<libro>
		<autor>Andrea Camilleri</autor>
		<id>2</id>
		<resumen>
			La aparente paz siciliana se ve truncada por el asesinato de una extraña. Una joven hermosa, mujer de un médico boloñés, aparece muerta en el chalet de ambos. Pocas pertenencias la acompañaban en la escena del crimen, aparte de un misterioso violín guardado en su estuche. Su bolsa de joyas se ha esfumado y todas las miradas se centran en un pariente desequilibrado que ha desaparecido la misma noche del crimen. Montalbano, con su parsimonia habitual, inicia la investigación. No cree a nadie, no se fía de nadie. Tras la muerte de un sospechoso, sus superiores dan por cerrado el caso, pero él, ni hablar. Transitando los límites de la legalidad, como es su costumbre, Montalbano ha de relacionarse y pactar con los elementos más indeseables y abyectos del hampa, iniciando un viaje a lo más oscuro del alma humana, en el fondo, su territorio predilecto.
		</resumen>
		<titulo>La voz del violín</titulo>
	</libro>
	<libro>
		<autor>Julio Verne</autor>
		<id>3</id>
		<titulo>La vuelta al mundo en 80 dias</titulo>
	</libro>
</libroes>

Creacion de un formulario
Respuesta con el formulario<!DOCTYPE html>
<html>
	<head>
		<title>Formulario para crear un nuevo recurso</title>
	</head>
	<body>
		<form action="../P3/rest/libros" method="POST">
		<label for="id">ID</label>
		<input name="id" />
		<br/>
		<label for="titulo">Titulo</label>
		<input name="titulo" />
		<br/>
		<label for="autor">Autor/a</label>
		<input name="autor" />
		<br/>
		Resumen:
		<TEXTAREA NAME="resumen" COLS=40 ROWS=6></TEXTAREA>
		<br/>
		<input type="submit" value="Aniadir" />
		</form>
	</body>
</html>

Contenidos del recurso despues de enviar el elemento id=4
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<libroes>
	<libro>
		<autor>Agatha Christie</autor>
		<id>1</id>
		<resumen>
			Diez personas reciben sentadas cartas firmadas por un desconocido Mr. Owen, que las invita a pasar unos días en la mansión que tiene en uno de los islotes de la costa de Devon. La primera noche, después de la cena, una voz los acusa, de ser culpables de un crimen. Lo que parece ser una broma macabra se convierte en una espantosas realidad cuando, uno por uno, los diez invitados son asesinados en un atmosfera de miedo y mutuas recriminaciones. La clave parece estar en una vieja canción infantil: 'Diez negritos se fueron a cenar, uno se ahogó y quedaron nueve. Nueve negritos trasnocharon mucho, uno no despertó, y quedaron ocho...'. 
		</resumen>
		<titulo>Diez negritos</titulo>
	</libro>
	<libro>
		<autor>Andrea Camilleri</autor>
		<id>2</id>
		<resumen>
			La aparente paz siciliana se ve truncada por el asesinato de una extraña. Una joven hermosa, mujer de un médico boloñés, aparece muerta en el chalet de ambos. Pocas pertenencias la acompañaban en la escena del crimen, aparte de un misterioso violín guardado en su estuche. Su bolsa de joyas se ha esfumado y todas las miradas se centran en un pariente desequilibrado que ha desaparecido la misma noche del crimen. Montalbano, con su parsimonia habitual, inicia la investigación. No cree a nadie, no se fía de nadie. Tras la muerte de un sospechoso, sus superiores dan por cerrado el caso, pero él, ni hablar. Transitando los límites de la legalidad, como es su costumbre, Montalbano ha de relacionarse y pactar con los elementos más indeseables y abyectos del hampa, iniciando un viaje a lo más oscuro del alma humana, en el fondo, su territorio predilecto.
		</resumen>
		<titulo>La voz del violín</titulo>
	</libro>
	<libro>
		<autor>Julio Verne</autor>
		<id>3</id>
		<titulo>La vuelta al mundo en 80 dias</titulo>
	</libro>
	<libro>
		<id>4</id>
		<resumen>
			Demostración de la biblioteca-cliente para formularios
		</resumen>
	</libro>
</libroes>

El elemento con id=1 del recurso se ha eliminado
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<libroes>
	<libro>
		<autor>Andrea Camilleri</autor>
		<id>2</id>
		<resumen>
			La aparente paz siciliana se ve truncada por el asesinato de una extraña. Una joven hermosa, mujer de un médico boloñés, aparece muerta en el chalet de ambos. Pocas pertenencias la acompañaban en la escena del crimen, aparte de un misterioso violín guardado en su estuche. Su bolsa de joyas se ha esfumado y todas las miradas se centran en un pariente desequilibrado que ha desaparecido la misma noche del crimen. Montalbano, con su parsimonia habitual, inicia la investigación. No cree a nadie, no se fía de nadie. Tras la muerte de un sospechoso, sus superiores dan por cerrado el caso, pero él, ni hablar. Transitando los límites de la legalidad, como es su costumbre, Montalbano ha de relacionarse y pactar con los elementos más indeseables y abyectos del hampa, iniciando un viaje a lo más oscuro del alma humana, en el fondo, su territorio predilecto.
		</resumen>
		<titulo>La voz del violín</titulo>
	</libro>
	<libro>
		<autor>Julio Verne</autor>
		<id>3</id>
		<titulo>La vuelta al mundo en 80 dias</titulo>
	</libro>
	<libro>
		<id>4</id>
		<resumen>
			Demostración de la biblioteca-cliente para formularios
		</resumen>
	</libro>
</libroes>
```	
