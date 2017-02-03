/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.utils;

/**
 * Enumeraci&oacute;n con las p&aacute;ginas posibles
 * previas a la pantalla de asignacion.xhtml.
 */
public enum Page {

	CONFIRMACION("1", "confirmacion.xhtml"),
	INVITADOS("2", "invitados.xhtml"),
	INVITADO_EVENTOS("3", "invitado_eventos.xhtml");

	private String id;
	private String url;

	private Page(String id, String url) {
		this.id = id;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	/**
	 * Regresa la enumeraci&oacute;n en base al id.
	 * @param id String
	 * @return PreviewPage
	 */
	public static Page getPage(String id) {
		if (id != null) {
			for (Page pp : Page.values()) {
				if (id.equalsIgnoreCase(pp.id)) {
					return pp;
				}
			}
		}
		return null;
	}

}
