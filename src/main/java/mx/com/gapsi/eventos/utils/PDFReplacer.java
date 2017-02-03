/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PushbuttonField;
import com.lowagie.text.pdf.XfaForm;

@ManagedBean
@ApplicationScoped
public class PDFReplacer {
	Logger logger = Logger.getLogger(PDFReplacer.class.getName());

	/**
	 * @param args
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void main(String[] args) throws IOException, DocumentException {
		// createSamplePDF();
		PDFReplacer replacer = new PDFReplacer();
		replacer.generateAshwinFriends(
				"/home/ecapati/Development/git/fashionfestmexico/src/main/webapp/resources/invitations/kidsEvent.pdf",
				"/home/ecapati/Development/git/fashionfestmexico/src/main/webapp/resources/invitations/test.pdf", "/home/ecapati/Development/git/fashionfestmexico/tmp_1.png");
	}

	public void generateAshwinFriends(String originalPdf, String generatedPdf, String qrLocation) throws IOException,
			FileNotFoundException, DocumentException {
		PdfReader pdfTemplate = new PdfReader(originalPdf);
		FileOutputStream fileOutputStream = new FileOutputStream(generatedPdf);
		PdfStamper stamper = new PdfStamper(pdfTemplate, fileOutputStream);

		stamper.setFormFlattening(true);

		AcroFields form = stamper.getAcroFields();

		XfaForm xfa = form.getXfa();
		logger.log(Level.INFO, "FamilyTreeStorage Form Type" + ((xfa.isXfaPresent()) ? "XFA Form" : "AcroForm"));

		Set<String> fields = form.getFields().keySet();
		if (!fields.isEmpty()) {
			for (String key : fields) {
				logger.log(Level.INFO,
						"FamilyTreeStorage" + String.format("key= %s; type= %d;", key, form.getFieldType(key)));
			}
		} else {
			logger.log(Level.INFO, "FamilyTreeStorage" + "AcroFields returned empty set.");
		}

		form.setField("fecha", "16 de Septiembre del 2015");
		form.setField("hora", "7:30 pm");
		form.setField("direccion", "Liverpool Polanco, 5to piso del estacionamiendo (Mariano Escobedo 425 Polanco)");

		PdfContentByte content = stamper.getOverContent(pdfTemplate.getNumberOfPages());
		Image image = Image.getInstance(qrLocation); // Image.getInstance(new URL("Absolute URL to the image to be embedded"));

		PushbuttonField ad = form.getNewPushbuttonFromField("codigoQR");
		ad.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
		ad.setProportionalIcon(true);
		ad.setImage(image);
		form.replacePushbuttonField("codigoQR", ad.getField());

		image.setAbsolutePosition(450, 650);
		image.scaleAbsolute(200, 200);
		content.addImage(image);

		stamper.close();
		pdfTemplate.close();
	}
}
