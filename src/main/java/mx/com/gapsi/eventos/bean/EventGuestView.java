package mx.com.gapsi.eventos.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import mx.com.gapsi.eventos.facade.EventoFacade;
import mx.com.gapsi.eventos.facade.EventoInvitadoFacade;
import mx.com.gapsi.eventos.facade.GuestFacade;
import mx.com.gapsi.eventos.model.Evento;
import mx.com.gapsi.eventos.model.EventoInvitado;
import mx.com.gapsi.eventos.model.Invitado;
import mx.com.gapsi.eventos.utils.Page;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

@ManagedBean
@ViewScoped
public class EventGuestView implements Serializable {
	private static final long serialVersionUID = 2331711565115734881L;
	//private static final long ID_FASHION_FEST = 1;

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(GuestView.class.getName());
	
	private Invitado invitado;
	//private Evento evento;
	
	private EventoInvitado eventoInvitado;

	@ManagedProperty("#{msg}")
	private transient ResourceBundle msg;

	@ManagedProperty("#{eventoInvitadoFacade}")
	private EventoInvitadoFacade eventoInvitadoFacade;

	@ManagedProperty("#{guestFacade}")
	private GuestFacade guestFacade;

	@ManagedProperty("#{eventoFacade}")
	private EventoFacade eventoFacade;

	// Eventos que el invitado no tiene asigandos.
	private List<Evento> eventosAvailable;

	private List<EventoInvitado> eventoInvitados;

	public EventGuestView() {
	}

	@PostConstruct
	void init(){
		final FacesContext facesContext = FacesContext.getCurrentInstance();

		Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
		String idInvitado = params.get("idInvitado");

		if (idInvitado != null) {
			logger.log(Level.INFO, "Usando el idInvitado {0}", idInvitado);

			invitado = guestFacade.findInvitadoById(Integer.parseInt(idInvitado));
			logger.log(Level.INFO, "Invitado encontrado {0}", invitado.getId());

			eventoInvitados = eventoInvitadoFacade.findAllEventoInvitadosByIdInvitado(Integer.parseInt(idInvitado));
			logger.log(Level.INFO, "Evento Invitados encontrados: {0}", eventoInvitados.size());

			eventosAvailable = eventoFacade.findAllAvailableByIdInvitado(Integer.parseInt(idInvitado));
			logger.log(Level.INFO, "Evento Disponibles encontrados: {0}", eventosAvailable.size());
		}
		
		eventoInvitado = new EventoInvitado();
		eventoInvitado.setEvento(new Evento());
	}

	/**
	 * @return the eventoInvitadoFacade
	 */
	public EventoInvitadoFacade getEventoInvitadoFacade() {
		return eventoInvitadoFacade;
	}

	/**
	 * @param eventoInvitadoFacade the eventoInvitadoFacade to set
	 */
	public void setEventoInvitadoFacade(EventoInvitadoFacade eventoInvitadoFacade) {
		this.eventoInvitadoFacade = eventoInvitadoFacade;
	}

	public GuestFacade getGuestFacade() {
		return guestFacade;
	}

	public void setGuestFacade(GuestFacade guestFacade) {
		this.guestFacade = guestFacade;
	}

	public EventoFacade getEventoFacade() {
		return eventoFacade;
	}

	public void setEventoFacade(EventoFacade eventoFacade) {
		this.eventoFacade = eventoFacade;
	}

	public ResourceBundle getMsg() {
		return msg;
	}

	public void setMsg(ResourceBundle msg) {
		this.msg = msg;
	}

	/**
	 * Validate user.
	 *
	 * @param event
	 *            the event
	 */
	public void assignEvent(final ActionEvent event) {
		final RequestContext context = RequestContext.getCurrentInstance();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		//String urlToGo = "event.xhtml?faces-redirect=true";
		long idEventoInvitado = 0;

		logger.log(Level.INFO, "Asignado el Evento {0} al invitado {1}", new Object[] {eventoInvitado.getEvento().getId() , invitado.getId()});
		boolean success = false;
		try {
			eventoInvitado.setInvitado(invitado);
			eventoInvitado.setNoBoletos(0);
			
			EventoInvitado eventoInvitadoCreated = eventoInvitadoFacade.createEventoInvitado(eventoInvitado);
			idEventoInvitado = eventoInvitadoCreated.getId();
			logger.log(Level.INFO, "EventoInvitado creado exitosamente con {1}", new Object[] { idEventoInvitado });
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Error creating EventoInvitado ", e);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg.getString("generic.error"),
					"Error al asignar el evento al invitado"));
		}

		context.addCallbackParam("success", success);

		if (success) {
//			try {
//				urlToGo += "&idEventoInvitado=" + idEventoInvitado;
//				facesContext.getExternalContext().redirect(urlToGo);
//			} catch (final IOException ex) {
//				logger.log(Level.SEVERE, "Exception happend when redirecting logged used", ex);
//			}
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg.getString("generic.success"),
					"Evento Asignado."));
			/*this.nombre ="";
			this.llaveEvento = "";
			this.llaveSecreta = "";
			this.descripcion = "";
			this.totalAsientos = 0;*/

			RequestContext.getCurrentInstance().reset("mainForm");
		}

	}
	
	public void findAllEventosByInvitado(final int idInvitado) {
		//final RequestContext context = RequestContext.getCurrentInstance();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		String urlToGo = "invitado_eventos.xhtml";//?faces-redirect=true";
		eventoInvitados = eventoInvitadoFacade.findAllEventoInvitadosByIdInvitado(idInvitado);
		try {
			urlToGo += "&idInvitado=" + idInvitado;
			facesContext.getExternalContext().redirect(urlToGo);
		} catch (final IOException ex) {
			logger.log(Level.SEVERE, "Exception happend when redirecting logged used", ex);
		}
	}
	
	public void createEventGuest(final ActionEvent event) {
		final RequestContext context = RequestContext.getCurrentInstance();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		//String urlToGo = "invitado_evento.xhtml?faces-redirect=true";
		long idEventoInvitado = 0;

		logger.log(Level.INFO, "Creando InvitadoEvento {0}", eventoInvitado);
		boolean success = false;
		try {
			EventoInvitado eventoInvitadoCreated = eventoInvitadoFacade.createEventoInvitado(eventoInvitado);
			idEventoInvitado = eventoInvitadoCreated.getId();
			logger.log(Level.INFO, "EventoInvitado creado exitosamente con {1}", new Object[] { idEventoInvitado });
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Error creating Evento ", e);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg.getString("generic.error"),
					"Error al asignar el evento al invitado."));
		}

		context.addCallbackParam("success", success);

		if (success) {
//			try {
//				urlToGo += "&idEventoInvitado=" + idEventoInvitado;
//				facesContext.getExternalContext().redirect(urlToGo);
//			} catch (final IOException ex) {
//				logger.log(Level.SEVERE, "Exception happend when redirecting logged used", ex);
//			}
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg.getString("generic.success"),
					"Asignación del evento creada."));
			/*this.nombre ="";
			this.llaveEvento = "";
			this.llaveSecreta = "";
			this.descripcion = "";
			this.totalAsientos = 0;*/

			RequestContext.getCurrentInstance().reset("mainForm");
		}

	}

	public String resetEventGuest(){
		this.eventoInvitado = new EventoInvitado();
		this.eventoInvitado.setEvento(new Evento());

		this.eventoInvitados = eventoInvitadoFacade.findAllEventoInvitadosByIdInvitado(invitado.getId());
		logger.log(Level.INFO, "Evento Invitados encontrados: {0}", eventoInvitados.size());

		this.eventosAvailable = eventoFacade.findAllAvailableByIdInvitado(invitado.getId());
		logger.log(Level.INFO, "Evento Disponibles encontrados: {0}", eventosAvailable.size());

		return null;
	}

	/**
	 * @return the eventoInvitados
	 */
	public List<EventoInvitado> getEventoInvitados() {
		if(this.eventoInvitados == null) eventoInvitados = eventoInvitadoFacade.findAllEventoInvitados();
		return eventoInvitados;
	}

	/**
	 * @param eventoInvitados
	 *            the eventoInvitados to set
	 */
	public void setEventoInvitados(List<EventoInvitado> eventoInvitados) {
		this.eventoInvitados = eventoInvitados;
	}
	
	public List<Evento> getEventosAvailable() {
		return eventosAvailable;
	}

	public void setEventosAvailable(List<Evento> eventosAvailable) {
		this.eventosAvailable = eventosAvailable;
	}

	/**
	 * Method used when a row is updated.
	 *
	 * @param event
	 *            the event
	 */
	public void onRowEdit(RowEditEvent event) {
		EventoInvitado modifiedValue = (EventoInvitado) event.getObject();
		logger.log(Level.INFO, "EventoInvitado Value updated [{0}] ", modifiedValue);

		if (modifiedValue != null && modifiedValue.getId() != null) {
			eventoInvitadoFacade.update(modifiedValue);
		}
		FacesMessage msg = new FacesMessage("Cambios realizados", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * On row cancel.
	 *
	 * @param event
	 *            the event
	 */
	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("La modificacion fue cancelada", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public Invitado getInvitado() {
		return invitado;
	}

	public void setInvitado(Invitado invitado) {
		this.invitado = invitado;
	}

	public EventoInvitado getEventoInvitado() {
		return eventoInvitado;
	}

	public void setEventoInvitado(EventoInvitado eventoInvitado) {
		this.eventoInvitado = eventoInvitado;
	}

	public Page getPage() {
		return Page.INVITADO_EVENTOS;
	}

}
