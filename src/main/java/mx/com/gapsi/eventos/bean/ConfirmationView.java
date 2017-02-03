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
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import mx.com.gapsi.eventos.facade.EventoFacade;
import mx.com.gapsi.eventos.model.EventoInvitado;
import mx.com.gapsi.eventos.utils.Page;

@ManagedBean
@RequestScoped
public class ConfirmationView implements Serializable {
	private static final long serialVersionUID = 632469844867213247L;
	private final Logger logger = Logger.getLogger(ConfirmationView.class.getName());
	@ManagedProperty("#{msg}")
	private transient ResourceBundle msg;
	@ManagedProperty("#{eventoFacade}")
	private EventoFacade eventoFacade;

	private long idEventoInvitado;
	private String qrCode = "";

	public void validateInvitation() {
		final ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		try {
			final HttpServletRequest origRequest = (HttpServletRequest) ctx.getRequest();
			final String webAppRoot = origRequest.getContextPath();
//			idEventoInvitado - is hash value, so it has to be revert
			idEventoInvitado = Long.parseLong(qrCode);
			EventoInvitado temp = new EventoInvitado(new Long(idEventoInvitado).intValue());
			EventoInvitado eventoInvitado = eventoFacade.findEventoInvitado(temp.revertHashCode());

			if (eventoInvitado != null) {
				idEventoInvitado = eventoInvitado.getId();

				final String assignUrl = webAppRoot + "/secure/asignacion.xhtml?idEventoInvitado=" + idEventoInvitado + "&previousPage=" + getPage();
				logger.info("Retrieving data for: " + assignUrl);

				ctx.redirect(assignUrl);

			} else {

				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("generic.error"),
								"El código QR no es válido."));
			}

		} catch (final Exception ex) {
			logger.severe("Exception happend when redirecting logged used" + ex);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("generic.error"),
					"Error en la aplicación." + ex.getMessage()));
		}
	}

	public ResourceBundle getMsg() {
		return msg;
	}

	public void setMsg(ResourceBundle msg) {
		this.msg = msg;
	}

	public long getIdEventoInvitado() {
		return idEventoInvitado;
	}

	public void setIdEventoInvitado(long idEventoInvitado) {
		this.idEventoInvitado = idEventoInvitado;
	}

	public EventoFacade getEventoFacade() {
		return eventoFacade;
	}

	public void setEventoFacade(EventoFacade eventoFacade) {
		this.eventoFacade = eventoFacade;
	}

	/**
	 * @return the qrCode
	 */
	public String getQrCode() {
		return qrCode;
	}

	/**
	 * @param qrCode the qrCode to set
	 */
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	
	public Page getPage() {
		return Page.CONFIRMACION;
	}

}
