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
