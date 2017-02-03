/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.mail;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import mx.com.gapsi.eventos.bean.GuestView;
import mx.com.gapsi.eventos.model.Evento;
import mx.com.gapsi.eventos.model.EventoInvitado;
import mx.com.gapsi.eventos.model.Invitado;
import mx.com.gapsi.eventos.qrcode.QRCodeGenerator;
import mx.com.gapsi.eventos.utils.ImageUtil;
import mx.com.gapsi.eventos.utils.PDFReplacer;
import mx.com.gapsi.eventos.utils.QRGenerator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;
import java.util.logging.Logger;

import com.google.zxing.WriterException;
import com.lowagie.text.DocumentException;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGrid.Response;
import com.sendgrid.SendGridException;

@ManagedBean
@ApplicationScoped
public class ServicioCorreo {

	@ManagedProperty("#{msg}")
	private transient ResourceBundle msg;

	@ManagedProperty("#{qRGenerator}")
	private QRGenerator qrGenerator;

	@ManagedProperty("#{pDFReplacer}")
	private PDFReplacer pdfReplacer;

	private static final Logger logger = Logger.getLogger(ServicioCorreo.class.getName());

	private transient SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");

	private static final String KEY = "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";
	private static final String FROM_NAME = "Eventos Liverpool";
	private static final String FROM = "eventos@liverpool.com.mx";
	private static final String TITLE = "Fashion Fest Kids P-V 2016";

	private static final int IMAGE_CODE_QR_SIZE = 120;
	private static final String IMAGE_FORMAT = "png";
	
	/**
	 * Env&iacute;a el correo de invitaci&oacute; con una imagen en html desde
	 * el servidor de correo gmail.
	 * @param eventoInvitado EventoInvitado.
	 */
	public void enviarCorreoWithImage(String URLConfirmacion, EventoInvitado eventoInvitado) throws Exception {
		logger.log(Level.INFO, "Inicia el envio de correo.");

		try {

			Evento evento = eventoInvitado.getEvento();
			Invitado invitado = eventoInvitado.getInvitado();

			String to = invitado.getEmail();
			String subject = evento.getSubjectEmail();

			int xImagenQR = evento.getImagenQRX();
			int yImagenQR = evento.getImagenQRY();

			// Se obtiene el mensaje del correo.
			logger.log(Level.INFO, "Obteniendo el cuerpo del correo...");
			String message = MailMessageGenerator.getMessage(URLConfirmacion);

			// Se obtiene la imagen1.
			logger.log(Level.INFO, "Obteniendo la imagen1...");
			InputStream inputStream = new ByteArrayInputStream(evento.getImagenInvitacion());
			BufferedImage image1 = ImageIO.read(inputStream);

			// Se obtiene la imagen2;
			logger.log(Level.INFO, "Obteniendo la imagen2...");
			BufferedImage image2 = QRCodeGenerator.generateQRCode(IMAGE_CODE_QR_SIZE, String.valueOf(eventoInvitado.hashCode()));
			
			// Se combinan las 2 imagenes en una sola.
			logger.log(Level.INFO, "Mezclando las 2 imagenes...");
			InputStream is = ImageUtil.getMixedImagesInputStream(IMAGE_FORMAT, image1, image2, xImagenQR, yImagenQR);

			//MailSender2.sendMailBodyPart(to, subject, message, is);
			logger.log(Level.INFO, "Enviando el correo con la imagen...");
			MailSender.sendMimeMultipartMail(to, subject, message, is);
			
			logger.log(Level.INFO, "Correo enviado a: " + to);
			
		} catch (Exception exc) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			exc.printStackTrace(pw);
			logger.log(Level.SEVERE, "Error al enviar el correo de invitacion. Error:", exc.getMessage());
			logger.log(Level.SEVERE, "StackTrace:", sw.toString());
			throw exc;
		}
	}
	
	
	
	
	
	
	
	// Solo es llamado desde el main.
	public void send(final Invitado invitado) throws SendGridException {
		final List<String> listaDistribucion = new ArrayList<>();

		String mensaje = "Favor de imprimir el codigo adjunto para facilitar su acceso.";
		final String header = "{  \"to\": [    \""
				+ invitado.getEmail()
				+ "\" ],  \"sub\": {    \"-nombreCliente-\": [ \""
				+ invitado.getNombre()
				+ "\"   ],    \"-fechaHora-\": [ \"el dia 23 de Junio de 2017 a las 5:00pm\"    ],    \"-fechaEnvio-\": [ \"25 de Junio 2015\"    ],    \"-direccionEvento-\": [      \"Alemania DF\"  ],    \"-nombreEvento-\": [      \"Fiesta 1\"   ]  },   \"filters\": {    \"templates\": {      \"settings\": {        \"enable\": 1,        \"template_id\": \"f56b01fc-7daa-42ec-8961-ac51dfaecd37\"      }    }  }}";
		Response response = null;

		final SendGrid sendgrid = new SendGrid(KEY);
		final SendGrid.Email email = new SendGrid.Email();
		for (final String para : listaDistribucion) {
			email.addTo(para);
		}
		email.setFromName(FROM_NAME);
		email.setFrom(FROM);
		email.setSubject(TITLE);
		email.setHtml(mensaje);
		email.setText(mensaje);
		email.addHeader("X-SMTPAPI", header);
		String stringDate = format.format(new Date());

		String absoluteDiskPath = "/home/ecapati/Development/git/fashionfestmexico/src/main/webapp/resources/invitations/kidsEvent.pdf";
		logger.log(Level.INFO, "Enviando archivo por: {}", absoluteDiskPath);

		File qrImageFile = new File("./tmp_" + stringDate + ".png");
		File pdfOriginalFile = new File(absoluteDiskPath);
		File pdfGeneratedFile = new File("./Invitation_Fashion_Fest_" + stringDate + ".pdf");
		File f = null;
		try {
			logger.log(Level.INFO, "Geenrando el qr para {0}", invitado.getIdEventoInvitado());
			QRGenerator qrGenerator = new QRGenerator();
			PDFReplacer pdfReplacer = new PDFReplacer();
			ByteArrayOutputStream stream = qrGenerator.generateByteArray(invitado.getIdEventoInvitado());
			FileOutputStream outputStream = new FileOutputStream(qrImageFile);
			stream.writeTo(outputStream);
			stream.close();
			pdfReplacer.generateAshwinFriends(pdfOriginalFile.getAbsolutePath(), pdfGeneratedFile.getAbsolutePath(),
					qrImageFile.getAbsolutePath());

			PDDocument document = PDDocument.loadNonSeq(pdfGeneratedFile, null);
			List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
			for (PDPage pdPage : pdPages) {
				BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
				String pngName = pdfGeneratedFile.getName().replaceAll(".pdf", ".png");
				ImageIOUtil.writeImage(bim,  pngName, 300);
			}
			document.close();

			String filePath = pdfGeneratedFile.getAbsolutePath().replaceAll(".pdf", ".png");
			f = new File(filePath);
			
			// email.addAttachment("file.png", qrImageFile);
			email.addAttachment(f.getName(), f);
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Error al generar el archivo", e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error al generar el archivo", e);
		} catch (DocumentException e) {
			logger.log(Level.SEVERE, "Error al generar el archivo", e);
		}

		logger.log(Level.FINE, "Borrando el archivo temporal {0} con resultado: {1}", new Object[] {qrImageFile.getAbsoluteFile(),
				qrImageFile.delete()});
		logger.log(Level.FINE, "Borrando el archivo temporal {0} con resultado: {1}", new Object[] {pdfGeneratedFile.getAbsoluteFile(),
				pdfGeneratedFile.delete()});
		logger.log(Level.FINE, "Borrando el archivo temporal {0} con resultado: {1}", new Object[] {f.getAbsoluteFile(),
				f.delete()});

		response = sendgrid.send(email);

		if (response.getCode() == 200) {
			System.out.println("Exito");
		} else {
			System.out.println("Fracaso");
		}

	}

	/**
	 * Envia un correo con SendGrid enviando la invitaci&oacute;n con una imagen adjunta.
	 * @param invitado Invitado
	 * @throws SendGridException
	 */
	public void enviarCorreo2(final Invitado invitado) throws SendGridException {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext extContext = context.getExternalContext();
		ServletContext servletContext = (ServletContext) extContext.getContext();

		final List<String> listaDistribucion = new ArrayList<>();

		String mensaje = "Favor de imprimir el codigo adjunto para facilitar su acceso.";
		final String header = "{  \"to\": [    \""
				+ invitado.getEmail()
				+ "\" ],  \"sub\": {    \"-nombreCliente-\": [ \""
				+ invitado.getNombre()
				+ "\"   ],    \"-fechaHora-\": [ \"el dia 23 de Junio de 2017 a las 5:00pm\"    ],    \"-fechaEnvio-\": [ \"25 de Junio 2015\"    ],    \"-direccionEvento-\": [      \"Alemania DF\"  ],    \"-nombreEvento-\": [      \"Fiesta 1\"   ]  },   \"filters\": {    \"templates\": {      \"settings\": {        \"enable\": 1,        \"template_id\": \"f56b01fc-7daa-42ec-8961-ac51dfaecd37\"      }    }  }}";
		Response response = null;

		final SendGrid sendgrid = new SendGrid(KEY);
		final SendGrid.Email email = new SendGrid.Email();
		
		for (final String para : listaDistribucion) {
			email.addTo(para);
		}
		email.setFromName(FROM_NAME);
		email.setFrom(FROM);
		email.setSubject(TITLE);
		email.setHtml(mensaje);
		email.setText(mensaje);
		email.addHeader("X-SMTPAPI", header);
		//String stringDate = format.format(new Date());

		//String absoluteDiskPath = "/home/ecapati/Development/git/fashionfestmexico/src/main/webapp/resources/invitations/FashionFest.png";
		String absoluteDiskPath = servletContext.getRealPath("resources/invitations/FashionFest.png");
		logger.log(Level.INFO, "Enviando archivo por: {}", absoluteDiskPath);

		//File qrImageFile = new File("./tmp_" + stringDate + ".png");
		//File pdfOriginalFile = new File(absoluteDiskPath);
		//File pdfGeneratedFile = new File("./Invitation_Fashion_Fest_" + stringDate + ".pdf");
		//File f = null;
		
		QRGenerator qrGenerator = new QRGenerator();
		try {
			logger.log(Level.INFO, "Se lee la imagen base de: {0}", absoluteDiskPath);
			InputStream inputStream = new BufferedInputStream(new FileInputStream(absoluteDiskPath));
			BufferedImage image1 = ImageIO.read(inputStream);

			logger.log(Level.INFO, "Geenrando el qr para {0}", invitado.getIdEventoInvitado());
			BufferedImage image2 = qrGenerator.generateQRCode(invitado.getIdEventoInvitado());
			
			logger.log(Level.INFO, "Combinando las imagenes para {0}...", invitado.getIdEventoInvitado());
			// create the new image, canvas size is the max. of both image sizes
			int w = Math.max(image1.getWidth(), image2.getWidth());
			int h = Math.max(image1.getHeight(), image2.getHeight());
			BufferedImage imageCombined = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);//BufferedImage.TYPE_INT_ARGB);

			// paint both images, preserving the alpha channels
			Graphics g = imageCombined.getGraphics();
			g.drawImage(image1, 0, 0, null);
			g.drawImage(image2, 922, 452, null);

			// Convert the image to inputstream.
			/*String filePath = "/Users/javier/Documents/ImagenCombinada.jpg";
			String fileType = "jpg";
			File myFile = new File(filePath);
			ImageIO.write(imageCombined, fileType, myFile);*/
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(imageCombined, "png", os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());

			// First, add an attachment
			email.addAttachment("image.png", is);
			// Map the name of the attachment to an ID
			email.addContentId("image.png", "ID_IN_HTML");
			// Map the ID in the HTML

			// email.addAttachment("file.png", qrImageFile);
			//email.addAttachment(f.getName(), f);
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Error al generar el archivo", e);
		} catch (IOException e) {
			logger.log(Level.SEVERE,"Error al generar el archivo", e);
		} catch (WriterException e) {
			logger.log(Level.SEVERE,"Error al generar el codigo qr", e);
		}

		/*logger.debug("Borrando el archivo temporal {} con resultado: {}", qrImageFile.getAbsoluteFile(),
				qrImageFile.delete());
		logger.debug("Borrando el archivo temporal {} con resultado: {}", pdfGeneratedFile.getAbsoluteFile(),
				pdfGeneratedFile.delete());
		logger.debug("Borrando el archivo temporal {} con resultado: {}", f.getAbsoluteFile(),
				f.delete());
		 */
		response = sendgrid.send(email);

		if (response.getCode() == 200) {
			System.out.println("Exito");
		} else {
			System.out.println("Fracaso");
		}

	}

	/**
	 * Env&iacute;a el correo con un archivo pdf con la invitaci&oacute;n.
	 * @param invitado Invitado
	 * @throws SendGridException
	 */
	public void enviarCorreo(final Invitado invitado) throws SendGridException {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		// logger.log(Level.INFO, "ServicioCorreo.enviarCorreo Invitado {0}",
		// invitado);
		final List<String> listaDistribucion = new ArrayList<>();

		String mensaje = " ";
		final String header = "{  \"to\": [    \""
				+ invitado.getEmail()
				+ "\" ],  \"sub\": {    \"-nombreCliente-\": [ \""
				+ invitado.getNombre()
				+ "\"   ],    \"-fechaHora-\": [ \"el dia 23 de Junio de 2017 a las 5:00pm\"    ],    \"-fechaEnvio-\": [ \""
				+ new Date()
				+ "\"    ],    \"-direccionEvento-\": [      \"Alemania DF\"  ],    \"-nombreEvento-\": [      \"Fiesta 1\"   ]  },   \"filters\": {    \"templates\": {      \"settings\": {        \"enable\": 1,        \"template_id\": \"f56b01fc-7daa-42ec-8961-ac51dfaecd37\"      }    }  }}";
		Response response = null;

		final SendGrid sendgrid = new SendGrid(KEY);
		final SendGrid.Email email = new SendGrid.Email();
		for (final String para : listaDistribucion) {
			email.addTo(para);
		}
		email.setFromName(FROM_NAME);
		email.setFrom(FROM);
		email.setSubject(TITLE);
		email.setHtml(mensaje);
		email.setText(mensaje);
		email.addHeader("X-SMTPAPI", header);
		String stringDate = format.format(new Date());

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext extContext = context.getExternalContext();
		ServletContext servletContext = (ServletContext) extContext.getContext();
		String absoluteDiskPath = servletContext.getRealPath("resources/invitations/kidsEvent.pdf");
		logger.log(Level.INFO, "Enviando archivo por: {}", absoluteDiskPath);

		File qrImageFile = new File("./tmp_" + stringDate + ".png");
		File pdfOriginalFile = new File(absoluteDiskPath);
		File pdfGeneratedFile = new File("./Invitation_Fashion_Fest_" + stringDate + ".pdf");
		File f = null;
		try {
			String idEventoInvitado = invitado.getIdEventoInvitado();
			logger.log(Level.INFO, "Geenrando el qr para {0}", idEventoInvitado);
			ByteArrayOutputStream stream = qrGenerator.generateByteArray(idEventoInvitado);
			FileOutputStream outputStream = new FileOutputStream(qrImageFile);
			stream.writeTo(outputStream);
			stream.close();
			pdfReplacer.generateAshwinFriends(pdfOriginalFile.getAbsolutePath(), pdfGeneratedFile.getAbsolutePath(),
					qrImageFile.getAbsolutePath());

			PDDocument document = PDDocument.loadNonSeq(pdfGeneratedFile, null);
			List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
			for (PDPage pdPage : pdPages) {
				BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
				String pngName = pdfGeneratedFile.getName().replaceAll(".pdf", ".png");
				ImageIOUtil.writeImage(bim,  pngName, 300);
			}
			document.close();

			String filePath = pdfGeneratedFile.getAbsolutePath().replaceAll(".pdf", ".png");
			f = new File(filePath);
			// email.addAttachment("file.png", qrImageFile);
			email.addAttachment(f.getName(), f);
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Error al generar el archivo", e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error al generar el archivo", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error al generar el archivo", e);
		}

		logger.log(Level.FINE, "Borrando el archivo temporal {0} con resultado: {1}", new Object[] {qrImageFile.getAbsoluteFile(),
				qrImageFile.delete()});
		logger.log(Level.FINE, "Borrando el archivo temporal {0} con resultado: {1}", new Object[] {pdfGeneratedFile.getAbsoluteFile(),
				pdfGeneratedFile.delete()});
		logger.log(Level.FINE, "Borrando el archivo temporal {0} con resultado: {1}", new Object[] {f.getAbsoluteFile(),
				f.delete()});

		response = sendgrid.send(email);

		if (response.getCode() == 200) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg.getString("generic.success"),
					"Envio de correo exitoso"));
		} else {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, msg.getString("envio.error.title"), msg
							.getString("envio.error.title")));
		}

	}

	public ResourceBundle getMsg() {
		return msg;
	}

	public void setMsg(final ResourceBundle msg) {
		this.msg = msg;
	}

	public QRGenerator getQrGenerator() {
		return qrGenerator;
	}

	public void setQrGenerator(QRGenerator qrGenerator) {
		this.qrGenerator = qrGenerator;
	}

	public PDFReplacer getPdfReplacer() {
		return pdfReplacer;
	}

	public void setPdfReplacer(PDFReplacer pdfReplacer) {
		this.pdfReplacer = pdfReplacer;
	}

	public static void main(String[] args) throws SendGridException {
		ServicioCorreo s = new ServicioCorreo();
		Invitado invitado = new Invitado();
		invitado.setIdEventoInvitado("1111");
		invitado.setEmail("capatino@gapsi.com.mx");
		s.send(invitado);
	}
}
