/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@ManagedBean
@ApplicationScoped
public class QRGenerator {
	
	public static void main(String[] args) throws IOException {
		QRGenerator q = new QRGenerator();
		ByteArrayOutputStream stream = q.generateByteArray("Este es mi contenido");
		File file = new File("./tmp_1.png");
		FileOutputStream outputStream = new FileOutputStream (file); 
		stream.writeTo(outputStream);
		stream.close();	
	}

	private ByteArrayOutputStream generateQR(final String content, final String type) {
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		final int size = 200;
		try {
			final Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			final QRCodeWriter qrCodeWriter = new QRCodeWriter();
			final BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hintMap);
			final int CrunchifyWidth = byteMatrix.getWidth();
			final BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
			image.createGraphics();

			final Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
			graphics.setColor(Color.BLACK);

			for (int i = 0; i < CrunchifyWidth; i++) {
				for (int j = 0; j < CrunchifyWidth; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}
			ImageIO.write(image, type, stream);
			
		} catch (final WriterException e) {
		} catch (final IOException e) {
		}
		return stream;
	}

	public BufferedImage generateQRCode(String text) throws WriterException {
		int size = 122;
		//String fileType = "png";
		try {
			
			Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			
			// Now with zxing version 3.2.1 you could change border size (white border size to just 1)
			hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
 
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size, hintMap);
			int CrunchifyWidth = byteMatrix.getWidth();
			BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
			image.createGraphics();
 
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
			graphics.setColor(Color.BLACK);
 
			for (int i = 0; i < CrunchifyWidth; i++) {
				for (int j = 0; j < CrunchifyWidth; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}
			//ImageIO.write(image, fileType, myFile);
			System.out.println("\n\nYou have successfully created QR Code.");
			
			return image;
		} catch (WriterException e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	public ByteArrayOutputStream generateByteArray(final String content) {
		return generateQR(content, "png");
	}
	
	public String generateBase64(final String content) {
		ByteArrayOutputStream stream = generateQR(content, "png");
		final StringBuilder sb = new StringBuilder();
		sb.append("data:image/png;base64,");
		// sb.append(Base64.encodeBase64URLSafeString(stream.toByteArray()));
		sb.append(Base64.encodeBase64String(stream.toByteArray()));
		// sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(stream.toByteArray(),
		// false)));
		return sb.toString();
	}

}
