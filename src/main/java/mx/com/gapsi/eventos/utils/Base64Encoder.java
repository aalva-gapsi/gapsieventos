package mx.com.gapsi.eventos.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;

public class Base64Encoder {

	public Base64Encoder() {
		// TODO Auto-generated constructor stub
	}

/**
 * The Class CodecBase64Util.
 *  using  org.apache.commons.codec.binary.Base64;
           org.apache.commons.codec.binary.BaseNCodec;

 */

	/** The b64. */
	// null lineSeparator same as saying no-chunking
	private Base64 b64 = new Base64(BaseNCodec.MIME_CHUNK_SIZE, null);

	public byte[] decode(byte[] b) throws Exception {
		return b64.decode(b);
	}
	public byte[] encode(byte[] b) throws Exception {
		return b64.encode(b);
	}

	public byte[] decode(String string) {
		return b64.decode(string);
	}

	public ByteArrayOutputStream encode(String filePath) throws Exception {
		FileOutputStream fos = null;
		fos=new FileOutputStream(filePath + "_encoded");

		byte fileContent[] = new byte[3000];
		try (FileInputStream fin = new FileInputStream(filePath)) {
			while (fin.read(fileContent) >= 0) {
				byte[] encoded = Base64.encodeBase64(fileContent);
				fos.write(encoded);
				fos.flush();
				fos.close();
			}
		}
		return null;
	}

	/**
	 * Possible OutOfMemory if file is too big.
	 *
	 * @param file the file
	 * @return the byte[]
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] encode(File file) throws FileNotFoundException, IOException {
		byte[] encoded;
		try (FileInputStream fin = new FileInputStream(file)) {
			byte fileContent[] = new byte[(int) file.length()];
			fin.read(fileContent);
			encoded = Base64.encodeBase64(fileContent);
		}
		return encoded;   
	}
	
	public String encodeToString(BufferedImage image, String type) {  
        String imageString = null;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
  
        try {  
            ImageIO.write(image, type, bos);  
            byte[] imageBytes = bos.toByteArray();  
  
            imageString = new String(encode(imageBytes), StandardCharsets.UTF_8);  
  
            bos.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return imageString;  
    }  
}
