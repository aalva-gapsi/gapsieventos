package mx.com.gapsi.eventos.utils;

import java.security.MessageDigest;

import org.apache.log4j.Logger;

/**
 * @Autor: Genaro Bermúdez [29/02/2016]
 * @Modificado: Genaro Bermúdez [29/02/2016]
 * @Descripción: Clase utilitaria que se encarga de encriptar una cadena
 * @See: <a href = "https://es.wikipedia.org/wiki/MD5" /> MD5 </a>
 */
public class Encriptador {

	/** Preparación de las variables locales de a clase */
	private static final Logger logger = Logger.getLogger(Encriptador.class);
	private String algoritmoCifrado;
	private String salCifrado;
	
	public static void main(String[] args) {
		Encriptador encriptador = new Encriptador();
		String idMD5 = encriptador.encriptar(args[0]);
		System.out.println(idMD5);
	}

	public Encriptador() {
		algoritmoCifrado = "MD5";
		salCifrado = "SD8DJ34HE9DLSJ";
	}

	public String encriptar(String entrada) {
		logger.info("Ha ingresado al método encriptar entrada: " + entrada);
		String valorHash = (new StringBuffer(1024)).append(entrada).append(salCifrado).toString();
		String resultado = "";
		byte[] hashClave = null;
		try {
			MessageDigest algoritmo = MessageDigest.getInstance(algoritmoCifrado);
			algoritmo.reset();
			algoritmo.update(valorHash.getBytes());
			hashClave = algoritmo.digest();
		} catch (Exception e) {
			logger.info("Ha ocurrido un error al tratar de encriptar la cadena. Causa: " + e.getCause());
		}
		resultado = getHexDigitString(hashClave);
		logger.info("Ha finalizado al método encriptar con el siguiente resultado: " + resultado);
		return resultado;
	}

	private String getHexDigitString(byte[] b) {
		int n = b.length;
		if (n == 0) {
			return "";
		}
		StringBuffer buf = new StringBuffer(b.length + 1);
		for (int i = 0; i < n; i++) {
			String c = Integer.toHexString(b[i] & 0xFF);
			if (c.length() == 1) {
				buf.append("0");
			}
			buf.append(c);
		}
		return buf.toString();
	}
	
}