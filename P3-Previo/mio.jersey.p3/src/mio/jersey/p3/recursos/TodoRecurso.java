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
