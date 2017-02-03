package mx.com.gapsi.eventos.mail;

/**
 * Clase para generar el contenido HTML del cuerpo del correo.
 * 
 */
public final class MailMessageGenerator {

	private MailMessageGenerator() {
		super();
	}

	public static String getMessage(String urlConfirmacion) {
		String htmlMessage;
		try {
			htmlMessage = new String(
					/*  "&nbsp;&nbsp;&nbsp;&nbsp; Buenas Tardes: "
					+ "<br />"
					+ "<br />"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;Te invitamos a la presentación de Fashion Fest Kids de la Colección Primavera- Verano de Moda Infantil."
					+ "<br />"
					+ "<br />"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;Contaremos con la Presencia de las Top Model Internaciones <b>HANNAH DAVIS Y EMILY DIDONATO.</b>"
					+ "<br />"
					+ "<br />&nbsp;&nbsp;&nbsp;&nbsp;Te esperamos el jueves 3 de Marzo a las 16:00 hrs en el 5º Piso del estacionamiento de Liverpool Polanco. (Mariano Escobedo 425, Miguel Hidalgo, Chapultepec Morales, 11570, Ciudad de México."
					+ "<br />"
					+ "<br />"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;<b>Es indispensable presentar la invitación adjunta, ya que contiene un código personalizado con el que se dará acceso.</b>"
                    + "<br />"
                    + "<br />"
                    +*/ 
                    "<br /><br />"
                    + "<font size=\"6\">Si desea confirmar su asistencia de click <b><a href=\"" + urlConfirmacion + "\">aquí</a></font></b>."   
                    + "<br /><br />"
                    + "<img src=\"cid:image\">");
			return htmlMessage;
		} catch (Exception exc) {
			System.out.println("Error al generar el message.");
			exc.printStackTrace();
			throw exc;
		}
	}

	public static String getMessageImage64(String srcImage) {
		String htmlMessage;
		try {
			htmlMessage = 
					  "&nbsp;&nbsp;&nbsp;&nbsp; Buenas Tardes: "
					+ "<br />"
					+ "<br />"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;Te invitamos a la presentación de Fashion Fest Kids de la Colección Primavera- Verano de Moda Infantil."
					+ "<br />"
					+ "<br />"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;Contaremos con la Presencia de las Top Model Internaciones <b>HANNAH DAVIS Y EMILY DIDONATO.</b>"
					+ "<br />"
					+ "<br />&nbsp;&nbsp;&nbsp;&nbsp;Te esperamos el jueves 3 de Marzo a las 16:00 hrs en el 5º Piso del estacionamiento de Liverpool Polanco. (Mariano Escobedo 425, Miguel Hidalgo, Chapultepec Morales, 11570, Ciudad de México."
					+ "<br />"
					+ "<br />"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;<b>Es indispensable presentar la invitación adjunta, ya que contiene un código personalizado con el que se dará acceso.</b>"
                    + "<br />"
                    + "<br />"
                    + "<img src=\"" + srcImage + "\">";
			return htmlMessage;
		} catch (Exception exc) {
			System.out.println("Error al generar el message.");
			exc.printStackTrace();
			throw exc;
		}
	}
	
}
