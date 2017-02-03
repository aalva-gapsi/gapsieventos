package mx.com.gapsi.eventos.mail;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import mx.com.gapsi.eventos.mail.activation.InputStreamDataSource;

public class MailSender {

	// final static private String EMAIL = "gapsiliverpool@gmail.com";
	// final static private String USER = "gapsiliverpool";
	// final static private String PASSWORD = "g4p5i_321";
	
	/***ATENCION:esta cuenta es de Isabel (usuario de la app), NO USAR PARA PRUEBAS, 
	los correos enviados se quedan en su bandeja de Enviados**/
	
	final static private String EMAIL = "ialcarazs@liverpool.com.mx";
	final static private String USER = "ialcarazs@liverpool.com.mx";
	final static private String PASSWORD = "akobukpfxazoccgt";

	final static private String HOST = "smtp.gmail.com";
	static private int PORT = 587;

	final static private String CONTENT_TYPE = "text/html; charset=utf-8";

	private MailSender() {
		super();
	}

	static public void sendMimeMultipartMail(String to, String subject, String message, InputStream imageInputStream) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.user", EMAIL);
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.auth", "true");

		if (PORT == 587) {
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.transport.protocol", "smtp");
		} else {
			props.put("mail.smtp.socketFactory.port", PORT);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			// props.put("mail.smtp.socketFactory.fallback", "false");
		}

		Authenticator auth = new MailSender.SMTPAuthenticator();
		Session session = Session.getInstance(props, auth);
		session.setDebug(true);

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(EMAIL));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		msg.setSubject(subject);

		// This mail has 2 part, the BODY and the embedded image
		MimeMultipart multipart = new MimeMultipart("related");

		// First part (the html)
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, CONTENT_TYPE);

		// Second part (the image)
		DataHandler dataHandler2 = new DataHandler(new InputStreamDataSource(imageInputStream, "InputStreamDataSource"));

		BodyPart messageBodyPart2 = new MimeBodyPart();
		messageBodyPart2.setDataHandler(dataHandler2);
		messageBodyPart2.addHeader("Content-ID", "<image>");

		// Add the two parts.
		multipart.addBodyPart(messageBodyPart);
		multipart.addBodyPart(messageBodyPart2);

		// Put everything together.
		msg.setContent(multipart);

		if (PORT == 587) {
			Thread t = Thread.currentThread();
			ClassLoader ccl = t.getContextClassLoader();
			t.setContextClassLoader(session.getClass().getClassLoader());
			try {
				Transport transport = session.getTransport("smtp");
				transport.connect(HOST, PORT, USER, PASSWORD);
				transport.sendMessage(msg, msg.getAllRecipients());
				transport.close();
			} finally {
				t.setContextClassLoader(ccl);
			}

		} else {
			Transport transport = session.getTransport("smtps");
			transport.connect(HOST, PORT, USER, PASSWORD);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		}
	}

	static public void sendSimpleMail(String to, String subject, String message) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.user", EMAIL);
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.auth", "true");

		if (PORT == 587) {
			props.put("mail.smtp.starttls.enable", "true");
		} else {
			props.put("mail.smtp.socketFactory.port", PORT);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			// props.put("mail.smtp.socketFactory.fallback", "false");
		}

		Authenticator auth = new MailSender.SMTPAuthenticator();
		Session session = Session.getInstance(props, auth);
		session.setDebug(true);

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(EMAIL));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		msg.setSubject(subject);

		msg.setContent(message, CONTENT_TYPE);

		if (PORT == 587) {
			Transport.send(msg);
		} else {
			Transport transport = session.getTransport("smtps");
			transport.connect(HOST, PORT, USER, PASSWORD);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		}
	}

	static class SMTPAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(EMAIL, PASSWORD);
		}
	}


	/**ATENCION: REVISAR USUARIO Y CONTRASEÑA. REVISAR QUE NO SEA LA CUENTA DE ISABEL (LA USUARIA) 
	 * DE LA APLICACION. Los correos enviados se quedan en la bandeja de enviados*/
	public static void main(String[] args) throws Exception {
		System.out.println("inciando prueba de correo...");
		FileInputStream fis = null;
		InputStream is = null;
		try {
			fis = new FileInputStream("C:\\Tmp\\liverpool\\fashionfest\\liver.jpg");
			is = new BufferedInputStream(fis);
			MailSender.sendMimeMultipartMail("aalva@grupoasesores.com.mx", "Prueba de correos", "Estamos realizando pruebas del correo", is);
			System.out.println("correo enviado");
		} finally {
			is.close();
			fis.close();
		}
		System.out.println("finaliza prueba");
	}

}
