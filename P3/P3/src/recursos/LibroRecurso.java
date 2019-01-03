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
