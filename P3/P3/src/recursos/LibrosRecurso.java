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
