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
