/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.bean;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;

import mx.com.gapsi.eventos.facade.GuestFacade;
import mx.com.gapsi.eventos.model.Invitado;


@ManagedBean(name="filterGuestView")
@ViewScoped
public class FilterGuestView implements Serializable {

	private List<Invitado> invitados;
	private List<Invitado> filteredInvitados; 
	@ManagedProperty("#{msg}")
	private transient ResourceBundle msg;
	@ManagedProperty("#{guestFacade}")
	private GuestFacade guestFacade;
	/**
	 * 
	 */
	private static final long serialVersionUID = 750982585348237004L;

	public FilterGuestView() {
		// TODO Auto-generated constructor stub
	}
	@PostConstruct
    public void init() {
		invitados = guestFacade.findAllInvitados();
    }

}
