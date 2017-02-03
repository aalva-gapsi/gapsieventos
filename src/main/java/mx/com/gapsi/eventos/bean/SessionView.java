/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import mx.com.gapsi.eventos.utils.Page;

@ManagedBean(name="sessionView")
@SessionScoped
public class SessionView {

	private int idEvento;
	private String nombreEvento;

	private Page previousPage;
	
	/**
	 * Constructor.
	 */
	public SessionView() {
		super();
	}

	public int getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(int idEvento) {
		this.idEvento = idEvento;
	}

	public String getNombreEvento() {
		return nombreEvento;
	}

	public void setNombreEvento(String nombreEvento) {
		this.nombreEvento = nombreEvento;
	}

	public Page getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(Page previousPage) {
		this.previousPage = previousPage;
	}
	
}
