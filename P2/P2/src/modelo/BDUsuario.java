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
