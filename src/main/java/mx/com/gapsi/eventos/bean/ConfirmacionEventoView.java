/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import mx.com.gapsi.eventos.facade.EventoInvitadoFacade;
import mx.com.gapsi.eventos.facade.GuestFacade;
import mx.com.gapsi.eventos.model.EventoInvitado;

@ManagedBean
@RequestScoped
public class ConfirmacionEventoView implements Serializable {
	
	private static final long serialVersionUID = 3905371885213086549L;
	
	private final Logger logger = Logger.getLogger(ConfirmacionEventoView.class.getName());
	
	private String id;
	
	@ManagedProperty("#{msg}")
	private transient ResourceBundle msg;
	
	@ManagedProperty("#{eventoInvitadoFacade}")
	private EventoInvitadoFacade eventoInvitadoFacade;

	@ManagedProperty("#{guestFacade}")
	private GuestFacade guestFacade;

	private EventoInvitado eventoInvitado;

	@PostConstruct
	public void init() {
		final ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		try {
			final HttpServletRequest origRequest = (HttpServletRequest) ctx.getRequest();
			final String webAppRoot = origRequest.getContextPath();
			final String errorUrl = webAppRoot + "/applicationError.xhtml";
			
			
			Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
			String idEventoInvitadoMD5 = params.get("id");

			// SI el id no viene en la URL se obtiene del formulario.
			if (idEventoInvitadoMD5 == null) {
				idEventoInvitadoMD5 = params.get("mainForm:id");
			}

			logger.log(Level.INFO, "ConfirmacionEventoView con idEventoInvitadoMD5: {0}.", idEventoInvitadoMD5);
			
			// Se valida si llega el idMD5 del usuario.
			if (idEventoInvitadoMD5 == null) {
				logger.log(Level.SEVERE, "No llego el parametro id.");
				logger.info("Retrieving data for: " + errorUrl);
				ctx.redirect(errorUrl);
			} else {
				// Se valida si existe ese idMD5 en base de datos.
				eventoInvitado = eventoInvitadoFacade.findByIdMD5(idEventoInvitadoMD5);
				if (eventoInvitado == null) {
					logger.log(Level.SEVERE, "No existe un evento_invitado con el MD5 {0}.", idEventoInvitadoMD5);
					logger.info("Retrieving data for: " + errorUrl);
					ctx.redirect(errorUrl);
				}

				logger.log(Level.INFO, "El idEventoInvitadoMD5 {0} es correcto.", idEventoInvitadoMD5);
			}
			
			id = idEventoInvitadoMD5;
		} catch (Exception exc) {
			logger.severe("Exception happend when redirecting logged used" + exc);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("generic.error"),
					"Error en la aplicación." + exc.getMessage()));
		}
	}
	
	public void confirmOk(final ActionEvent event) {
		confirmEventoInvitado(true);
	}
	
	public void confirmNoOk(final ActionEvent event) {
		confirmEventoInvitado(false);
	}

	private void confirmEventoInvitado(boolean asistira) {
		final ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		String errorUrl = null;
		try {
			final HttpServletRequest origRequest = (HttpServletRequest) ctx.getRequest();
			final String webAppRoot = origRequest.getContextPath();
			final String saludoUrl = webAppRoot + "/invitation/confirmacionSaludos.xhtml";
			errorUrl = webAppRoot + "/applicationError.xhtml";

			logger.log(Level.INFO, "confirmEventoInvitado asistira: [{0}], idEventoInvitadoMD5 [{1}].", new Object[]{asistira, id});

			eventoInvitado.setAsistira(asistira);
			EventoInvitado eventoInvitadoModified = eventoInvitadoFacade.update(eventoInvitado);
			logger.log(Level.INFO, "EventoInvitado actualizado exitosamente con {0} para confirmacion {1}",
						new Object[] { eventoInvitadoModified.getId(), asistira });
				
			logger.log(Level.INFO, "Confirmacion exitosa.");
			logger.info("Retrieving data for: " + saludoUrl);
			ctx.redirect(saludoUrl);

		} catch (Exception exc) {
			logger.severe("Exception happend when updating evento_invitado" + exc);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("generic.error"),
					"Error en la aplicación." + exc.getMessage()));
			try {
				ctx.redirect(errorUrl);
			} catch (IOException e) {
				logger.severe("Exception happend when trying redirect" + exc);
			}
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ResourceBundle getMsg() {
		return msg;
	}

	public void setMsg(ResourceBundle msg) {
		this.msg = msg;
	}

	public EventoInvitadoFacade getEventoInvitadoFacade() {
		return eventoInvitadoFacade;
	}

	public void setEventoInvitadoFacade(EventoInvitadoFacade eventoInvitadoFacade) {
		this.eventoInvitadoFacade = eventoInvitadoFacade;
	}

	public GuestFacade getGuestFacade() {
		return guestFacade;
	}

	public void setGuestFacade(GuestFacade guestFacade) {
		this.guestFacade = guestFacade;
	}

	public EventoInvitado getEventoInvitado() {
		return eventoInvitado;
	}

	public void setEventoInvitado(EventoInvitado eventoInvitado) {
		this.eventoInvitado = eventoInvitado;
	}

}
