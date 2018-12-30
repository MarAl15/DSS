package prueba;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "holaMundo", eager = true)
@RequestScoped

public class HolaMundo{
	@ManagedProperty(value="#{mensaje}")
	private Mensaje mensajeBean;
	private String mensaje = "Nada aun!";
	
	public HolaMundo(){
		System.out.println("Hola Mundo ha comenzado!");
		System.out.println(mensaje);
	}
	
	public String getMensaje(){		
		if(mensajeBean != null)
			mensaje = mensajeBean.getMensaje();

		return mensaje;
	}
	
	public void setMensajeBean(Mensaje m){
		this.mensajeBean = m;
	}
}
