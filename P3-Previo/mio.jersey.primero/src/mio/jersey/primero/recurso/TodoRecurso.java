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
