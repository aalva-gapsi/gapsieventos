/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.bean;

import java.io.InputStream;
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
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

import sun.misc.BASE64Encoder;
import mx.com.gapsi.eventos.facade.EventoFacade;
import mx.com.gapsi.eventos.model.Evento;

@ManagedBean
@RequestScoped
public class EventView implements Serializable {
	private static final long serialVersionUID = 2331711565115734881L;

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(GuestView.class.getName());

	@ManagedProperty("#{msg}")
	private transient ResourceBundle msg;

	@ManagedProperty("#{eventoFacade}")
	private EventoFacade eventoFacade;

	private Evento evento;

	// Atributos para gestionar la imagen.
	private UploadedFile fileImagen;
	private String srcImgInvitacion;

	private List<Evento> eventos;

	public EventView() {
	}
	
	@PostConstruct
	void init(){
		evento = new Evento();
		
		Map<String,String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	    String idEventoRequest =  requestParams.get("idEvento");
	    if (idEventoRequest != null && !idEventoRequest.isEmpty()) {
	    	evento = eventoFacade.findOneWithImagenInvitacion(Integer.parseInt(idEventoRequest));
	    	// Se convierte los bytes de la imagen de invitacion a Base64.
	    	if (evento.getImagenInvitacion() != null) {
	    		BASE64Encoder encoder = new BASE64Encoder();
	    		srcImgInvitacion = "data:image/png;base64," + encoder.encode(evento.getImagenInvitacion());
	    	} else {
	    		logger.log(Level.INFO, "No se obtuvo una imagen de la base de datos para el evento {0}.", idEventoRequest);
	    	}
	    }
	    
	}
	
	/**
	 * Validate user.
	 *
	 * @param event
	 *            the event
	 */
	public void createEvent(final ActionEvent event) {
		final RequestContext context = RequestContext.getCurrentInstance();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		//String urlToGo = "event.xhtml?faces-redirect=true";
		long idEvento = 0;

		logger.log(Level.INFO, "Creando Evento {0}", evento);
		boolean success = false;
		try {
			
			// SI se selecciono una imagen en el formulario, se actualiza la imagen en base de datos.
			if (fileImageSelected()) {
				logger.log(Level.INFO, "La imagen de invitacion para el evento {0} es {1}.", new Object[] { idEvento, fileImagen.getFileName() });
				InputStream inputStream = fileImagen.getInputstream();
				byte[] bytesImagenInvitacion = IOUtils.toByteArray(inputStream);
				evento.setImagenInvitacion(bytesImagenInvitacion);
			} else {
				logger.log(Level.INFO, "No llego la imagen al crear el evento {0}", idEvento);
			}
			
			Evento eventoCreated = eventoFacade.createEvento(evento);
			idEvento = eventoCreated.getId();
			logger.log(Level.INFO, "Evento creado exitosamente con {0}", new Object[] { idEvento });
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Error creating Evento ", e);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg.getString("generic.error"),
					"Error al dar de alta el evento"));
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
					"Evento Creado."));
			/*this.nombre ="";
			this.llaveEvento = "";
			this.llaveSecreta = "";
			this.descripcion = "";
			this.totalAsientos = 0;*/

			RequestContext.getCurrentInstance().reset("mainForm");
		}

	}

	/**
	 * Actualiza un evento.
	 * @param event ActionEvent
	 */
	public void updateEvent(final ActionEvent event) {
		final RequestContext context = RequestContext.getCurrentInstance();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		long idEvento = 0;
		
		logger.log(Level.INFO, "Actualizando Evento {0}", evento);
		logger.log(Level.INFO, "Actualizando Evento con imagen [{0}]", fileImagen.getFileName());
		boolean success = false;
		try {

			// SI se selecciono una imagen en el formulario, se actualiza la imagen en base de datos.
			if (fileImageSelected()) {
				logger.log(Level.INFO, "La imagen de invitacion en la actualizacion para el evento {0} es {1}.", new Object[] { idEvento, fileImagen.getFileName() });
				InputStream inputStream = fileImagen.getInputstream();
				byte[] bytesImagenInvitacion = IOUtils.toByteArray(inputStream);
				BASE64Encoder encoder = new BASE64Encoder();
	    		srcImgInvitacion = "data:image/png;base64," + encoder.encode(bytesImagenInvitacion);
				evento.setImagenInvitacion(bytesImagenInvitacion);
			} else {
				evento.setImagenInvitacion(null);
				logger.log(Level.INFO, "No llego la imagen al actualizar el evento {0}", idEvento);
			}

			Evento eventoCreated = eventoFacade.update(evento);
			idEvento = eventoCreated.getId();
			logger.log(Level.INFO, "Evento actualizado exitosamente con id {0}", new Object[] { idEvento });

			// Si NO se selecciono una imagen en el formulario, se consulta la imagen en base de datos para mostrarla.
			if (!fileImageSelected()) {
				Evento eventoUpdated = eventoFacade.findOneWithImagenInvitacion(idEvento);
				if (eventoUpdated.getImagenInvitacion() != null) {
		    		BASE64Encoder encoder = new BASE64Encoder();
		    		srcImgInvitacion = "data:image/png;base64," + encoder.encode(eventoUpdated.getImagenInvitacion());
		    	}
			}

			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Error updating Evento ", e);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg.getString("generic.error"),
					"Error al actualizar el evento"));
		}
		
		context.addCallbackParam("success", success);
		
		if (success) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg.getString("generic.success"),
					"Evento Actualizado."));
		}
	}

	/**
	 * Method used when a row is updated.
	 *
	 * @param event
	 *            the event
	 */
	public void onRowEdit(RowEditEvent event) {
		Evento modifiedValue = (Evento) event.getObject();
		logger.log(Level.INFO, "Evento Value updated [{0}] ", modifiedValue);
		
		/*if (modifiedValue != null && 
			modifiedValue.getTipoInvitado() != null &&
			modifiedValue.getTipoInvitado().getId() != null && 
			modifiedValue.getTipoInvitado().getId().intValue() == ID_TIPO_PROVEEDOR && modifiedValue.getEmpresa().equals("")) {
			FacesMessage msg = new FacesMessage("El campo Empresa es requerido.", "");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}*/

		if (modifiedValue != null && modifiedValue.getId() != null) {
			eventoFacade.update(modifiedValue);
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

	public String resetEvent(){
		this.evento = null;
		return null;
	}

	/**
	 * Regresa verdadero si se seleccion&oacute; una imagen desde el formulario,
	 * false en caso contrario.
	 * @return boolean
	 */
	private boolean fileImageSelected() {
		return fileImagen != null &&
			   fileImagen.getFileName() != null &&
			   !fileImagen.getFileName().isEmpty();
	}

	/**
	 * @return the evento
	 */
	public Evento getEvento() {
		return evento;
	}

	/**
	 * @param evento the evento to set
	 */
	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	/**
	 * @return the eventoFacade
	 */
	public EventoFacade getEventoFacade() {
		return eventoFacade;
	}

	/**
	 * @param eventoFacade the eventoFacade to set
	 */
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
	 * @return the eventos
	 */
	public List<Evento> getEventos() {
		if(this.eventos == null) eventos = eventoFacade.findAllEventos();
		return eventos;
	}

	/**
	 * @param eventos
	 *            the eventos to set
	 */
	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	public UploadedFile getFileImagen() {
		return fileImagen;
	}

	public void setFileImagen(UploadedFile fileImagen) {
		this.fileImagen = fileImagen;
	}

	public String getSrcImgInvitacion() {
		return srcImgInvitacion;
	}

	public void setSrcImgInvitacion(String srcImgInvitacion) {
		this.srcImgInvitacion = srcImgInvitacion;
	}

}
