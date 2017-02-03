package mx.com.gapsi.eventos.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * Clase de utiler&iacute;a para mezclar dos imagenes en una sola.
 *
 */
public final class ImageUtil {

	private ImageUtil() {
		super();
	}
	                                                                         //922, 452
	/**
	 * Mezcla dos imagenes en una sola, sobreponiendo la segunda en la primera.
	 * @param imageFormat String.- png, jpg.
	 * @param image1 BufferedImage
	 * @param image2 BufferedImage
	 * @param xImage2 int
	 * @param yImage2 int
	 * @return BufferedImage
	 * @throws IOException
	 */
	public static InputStream getMixedImagesInputStream(String imageFormat, // png 
			BufferedImage image1, BufferedImage image2, int xImage2, int yImage2) throws IOException {

		BufferedImage mixedImage = getBufferedImageMixedImages(image1, image2, xImage2, yImage2);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(mixedImage, imageFormat, os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());

		return is;
	}

	/**
	 * Mezcla dos imagenes en una sola, sobreponiendo la segunda en la primera.
	 * @param imageFormat String.- png, jpg.
	 * @param image1 BufferedImage
	 * @param image2 BufferedImage
	 * @param xImage2 int
	 * @param yImage2 int
	 * @return BufferedImage
	 * @throws IOException
	 */
	public static BufferedImage getBufferedImageMixedImages(BufferedImage image1, BufferedImage image2, int xImage2, int yImage2) {
		// create the new image, canvas size is the max. of both image sizes
		int w = Math.max(image1.getWidth(), image2.getWidth());
		int h = Math.max(image1.getHeight(), image2.getHeight());
		BufferedImage mixedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);//BufferedImage.TYPE_INT_ARGB);

		// paint both images, preserving the alpha channels
		Graphics g = mixedImage.getGraphics();
		g.drawImage(image1, 0, 0, null);
		g.drawImage(image2, xImage2, yImage2, null);
		
		return mixedImage;
	}
	
}
