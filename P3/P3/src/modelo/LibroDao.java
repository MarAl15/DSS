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
