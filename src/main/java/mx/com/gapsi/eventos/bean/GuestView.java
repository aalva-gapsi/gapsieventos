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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import us.monoid.json.JSONArray;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;
import mx.com.gapsi.eventos.facade.EventoFacade;
import mx.com.gapsi.eventos.facade.GuestFacade;
import mx.com.gapsi.eventos.mail.ServicioCorreo;
import mx.com.gapsi.eventos.model.Evento;
import mx.com.gapsi.eventos.model.EventoInvitado;
import mx.com.gapsi.eventos.model.Invitado;
import mx.com.gapsi.eventos.model.TipoInvitado;
import mx.com.gapsi.eventos.report.GuestReport;
import mx.com.gapsi.eventos.utils.HTTPUtil;
import mx.com.gapsi.eventos.utils.Page;

@ManagedBean
@ViewScoped
public class GuestView implements Serializable {

	private static final long serialVersionUID = 9017729950423994614L;
	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(GuestView.class.getName());

	private static final int ID_TIPO_PROVEEDOR = 4;

	private static final String urlSeatIoValidatePublicKey = "https://app.seats.io/api/event";
	
	@ManagedProperty("#{sessionView}")
	private SessionView sessionView;

	@ManagedProperty("#{guestFacade}")
	private GuestFacade guestFacade;

	@ManagedProperty("#{msg}")
	private transient ResourceBundle msg;

	@ManagedProperty("#{eventoFacade}")
	private EventoFacade eventoFacade;

	@ManagedProperty("#{servicioCorreo}")
	private ServicioCorreo servicioCorreo;

	private String search;

	private List<TipoInvitado> tipoInvitados;
	private List<Evento> eventos;
	
	private TipoInvitado tipoInvitado = new TipoInvitado();
	private int id;
	private int noBoletos = 1;
	private String nombre;
	private String apPaterno;
	private String apMaterno;
	private String email;
	private String invitador;
	private String empresa;
	private boolean enviarCorreo = true;
	private List<Invitado> invitados;
	private List<Invitado> filteredInvitados;

	private boolean emailInvalid = false;
	private int idEventoToFindInvitados = 0;

	public ResourceBundle getMsg() {
		return msg;
	}

	public void setMsg(ResourceBundle msg) {
		this.msg = msg;
	}

	public TipoInvitado getTipoInvitado() {
		return tipoInvitado;
	}

	public void setTipoInvitado(TipoInvitado tipoInvitado) {
		this.tipoInvitado = tipoInvitado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApPaterno() {
		return apPaterno;
	}

	public void setApPaterno(String apPaterno) {
		this.apPaterno = apPaterno;
	}

	public String getApMaterno() {
		return apMaterno;
	}

	public void setApMaterno(String apMaterno) {
		this.apMaterno = apMaterno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInvitador() {
		return invitador;
	}

	public void setInvitador(String invitador) {
		this.invitador = invitador;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	
	public boolean isEnviarCorreo() {
		return enviarCorreo;
	}

	public void setEnviarCorreo(boolean enviarCorreo) {
		this.enviarCorreo = enviarCorreo;
	}

	@PostConstruct
	public void init() {

		if(tipoInvitados== null) tipoInvitados= guestFacade.findAllTipoInvitado(); 
		if(eventos == null) eventos = eventoFacade.findAllEventos();
	}

	public void selectEvento() {

		Map<String,String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	    String idEventoRequest =  requestParams.get("mainForm:evento_input");
	    int idEvento = Integer.parseInt(idEventoRequest);
	    
	    logger.log(Level.INFO, "selectEvento idEvento[{0}]", idEvento);
	    sessionView.setIdEvento(idEvento);
	    
	    // Se consulta el evento.
	    if (idEvento != 0) {
	    	Evento evento = eventoFacade.findOne(idEvento);
	    	sessionView.setNombreEvento(evento.getNombre());
	    }

	    // Se consultan los invitados con el nuevo idEvento.
	    this.getInvitados();
	    RequestContext.getCurrentInstance().update("mainForm");
	    
	    //this.idEventoToFindInvitados = idEvento;
	    
	}

	public void validateEmail(AjaxBehaviorEvent event) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();

		Map<String,String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	    String emailRequest=  requestParams.get("mainForm:email");

	    // Se obtiene el mail de la peticion ajax.
		/*UIInput input = (UIInput)event.getSource();
		String emailRequest = (String)input.getValue();
		logger.log(Level.INFO, "email getValue: " + input.getValue());*/

		// Se asigna el email de la peticion al atributo del ManagedBean.
		this.email = emailRequest;

		logger.log(Level.INFO, "Validando email: " + email);
		if (email != null && !email.isEmpty() && existeCorreo(email)) {
			this.emailInvalid = true;
			logger.log(Level.SEVERE, "Error el email [" + email + "] ya existe.");
			String message = MessageFormat.format(msg.getString("validation.email.exist"), email);
			facesContext.addMessage("emailValidationMessage", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("generic.error"), message));
			
			//facesContext.addMessage("mailMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("generic.error"),
			//		"El email [" + email + "] ya existe."));
			
		} else {
			this.emailInvalid = false;
			logger.log(Level.INFO, "Este email no existe.");
		}
	}

	/**
	 * Valida si existe el correo en la base de invitados, regresa true si existe
	 * , false en caso contrario.
	 * @param email String
	 * @return boolean
	 */
	private boolean existeCorreo(String email) {
		return guestFacade.findInvitadoByEmail(email) != null;
	}

	/**
	 * Validate user.
	 *
	 * @param event
	 *            the event
	 */
	public void createGuest(final ActionEvent event) {
		final RequestContext context = RequestContext.getCurrentInstance();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		//String urlToGo = "invitado_eventos.xhtml?faces-redirect=true";
		long idEventoInvitado = 0;

		Invitado invitado = new Invitado(tipoInvitado, nombre, apPaterno, apMaterno, email, invitador, empresa);
		// invitado.setId(id);
		logger.log(Level.INFO, "Creando Invitado {0}", invitado);
		boolean success = false;
		try {
			//invitado = guestFacade.createInvitado(invitado, noBoletos, idEvento);

			// Se accede al evento en sesion.
			Integer idEvento = sessionView.getIdEvento();
			
			// Se crea la relacion evento_invitado
			EventoInvitado eventoInvitado = guestFacade.createInvitado(invitado, noBoletos, idEvento);
			idEventoInvitado = eventoInvitado.getId();
			logger.log(Level.INFO, "Invitado creado exitosamente el {0} con {1}", new Object[] { invitado, idEventoInvitado });
			
			/*Codigo anterior
			invitado.setIdEventoInvitado(String.valueOf(eventoInvitado.hashCode()));
			sendMail(invitado);
			*/
			this.sendMailWithImage(eventoInvitado);

			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Error creating Invitado ", e);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg.getString("generic.error"),
					"Error al dar de alta al usuario"));
		}

		context.addCallbackParam("success", success);

		if (success) {
			/*try {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg.getString("generic.success"),
						"Invitado Creado."));
				
				urlToGo += "&idInvitado=" + invitado.getId();
				facesContext.getExternalContext().redirect(urlToGo);
			} catch (final IOException ex) {
				logger.log(Level.SEVERE, "Exception happend when redirecting logged used", ex);
			}*/
			
			
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg.getString("generic.success"),
					"Invitado Creado."));
			this.apMaterno="";
			this.apPaterno="";
			this.email="";
			this.enviarCorreo = true;
			this.invitador="";
			this.empresa = "";
			this.nombre ="";
			this.noBoletos=1;
			RequestContext.getCurrentInstance().reset("mainForm");
		}

	}
	public String resetGuest(){
		this.apMaterno="";
		this.apPaterno="";
		this.email="";
		this.enviarCorreo = true;
		this.invitador="";
		this.empresa = "";
		this.nombre ="";
		this.noBoletos=1;
		return null;
	}
	
	String urlToGos = "asignacion.xhtml?faces-redirect=true";

	public void seatManagement(long idEvento, long idInvitado) {
		logger.log(Level.INFO, "GuestView-seatManagement {0}", idInvitado);
		final RequestContext context = RequestContext.getCurrentInstance();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		
		long idEventoInvitado = 0;
		FacesMessage msg = new FacesMessage("Cambios realizados", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		boolean success = false;
		try {
			EventoInvitado eventoInvitado = eventoFacade.findEventoInvitado(idEvento, idInvitado);
			idEventoInvitado = eventoInvitado.getId();
			logger.log(Level.INFO, "eventoInvitado obtenido exitosamente {0}", eventoInvitado);

			success = true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error al consultar el Evento.", e);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error","Error al consultar el Evento"));
		}
		context.addCallbackParam("success", success);

		if (success) {
			try {
				urlToGos += "&idEventoInvitado=" + idEventoInvitado;
				facesContext.getExternalContext().redirect(urlToGos);
			} catch (final IOException ex) {
				logger.log(Level.SEVERE, "Exception happend when redirecting logged used", ex);
			}
		}
	}
	
	public void seatManagement(){
		seatManagement(1, id);
	}
	
	public String seatManagement(long id){
		seatManagement(1, id);
		logger.log(Level.INFO, "GuestView-seatManagement-id {0}", urlToGos);
		return  				urlToGos;
	}
	
	public String goSeatManagement(){
		urlToGos += "&idEventoInvitado=" + id;
		logger.log(Level.INFO, "GuestView-goSeatManagement {0}", urlToGos);
		return  				urlToGos;
	}
	
	public void seatManagementAction(final ActionEvent event){
		logger.log(Level.INFO, "GuestView-seatManagementAction {0}", id);
		seatManagement(1, id);
	}
	

	
	
	
	
	/*public void onSendEmail(final ActionEvent event) {
		logger.log(Level.INFO, "GuestView-onSendEmail {0}", id);
		final RequestContext context = RequestContext.getCurrentInstance();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		boolean success = false;
		String idInvitado = "0";
		try {
			if(id==0){
				idInvitado = (String)event.getComponent().getAttributes().get("idInvitado");
				logger.log(Level.INFO, "GuestView-onSendEmail-idInvitado {0}", idInvitado);
				try {
					id= Integer.valueOf(idInvitado);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
			
			EventoInvitado eventoInvitado = eventoFacade.findEventoInvitado(1, id);
			logger.log(Level.INFO, "eventoInvitado obtenido exitosamente {0}", eventoInvitado);
			enviarCorreo = true;
			sendMail(eventoInvitado.getInvitado());
			success = true;
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito",
					"Se envio la invitación."));
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Error sending email to  Invitado ", e);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
					"Error al enviar la invitación."));
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
					e.getMessage()));
		}

		context.addCallbackParam("success", success);
		if (success) {
		}

	}*/

	public String asignarLugares(int idInvitado, int idEvento, int previousPage) {
		logger.log(Level.INFO, "GuestView-asignarLugares idInvitado[{0}], idEvento[{1}]", new Object[] {id, idEvento});
		//final RequestContext context = RequestContext.getCurrentInstance();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final ExternalContext ctx = facesContext.getExternalContext();
		//boolean success = false;
		try {
			
			Evento evento = eventoFacade.findOne(idEvento);
			
			// Se valida si tiene configuradas las llaves SEATS.IO.
			if (!evento.isHasSeatsMaps()) {
				logger.log(Level.INFO, "El evento {0} no tiene llaves SEATS.IO.", evento.getId());
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg.getString("generic.error"),
						"El evento [" + evento.getNombre() + "] no tiene configuradas las llaves de SEATS.IO."));
			
			// Se valida que las llaves sean correctas.
			} else if (!validateSecretKey(evento) || !validatePublicKey(evento)) {
				logger.log(Level.INFO, "Las llaves del evento {0} no tiene configuradas las llaves correctamente.", evento.getId());
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg.getString("generic.error"),
						"Las llaves SEATS.IO del evento [" + evento.getNombre() + "] están mal configuradas."));
			} else {

				final HttpServletRequest origRequest = (HttpServletRequest) ctx.getRequest();
				final String webAppRoot = origRequest.getContextPath();
				final String asignacionUrl = webAppRoot + "/secure/asignacion.xhtml?idInvitado=" + idInvitado + "&idEvento=" + idEvento + "&previousPage=" + previousPage;
			
				ctx.redirect(asignacionUrl);
			}
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error asignar lugares ", exc);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
					"Error al asignar lugares."));
		}
		return null;
	}
	
	private static boolean validateSecretKey(Evento evento) {
		String eventKey;
		String publicKey;
		int codigoRespuesta;
		String urlTest = null;
		try {
			publicKey = evento.getLlavePublica();
			eventKey = evento.getLlaveEvento();
			urlTest = urlSeatIoValidatePublicKey + "/" + publicKey + "/" + eventKey;
			codigoRespuesta = HTTPUtil.sendGet(urlTest);
			return codigoRespuesta == 200;
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error al validar las llaves SEATS.IO al enviar la peticion GET a [{0}], error: [{1}]", new Object[] {urlTest, exc});
			return false;
		}
	}

	private static boolean validatePublicKey(Evento evento) {
		String eventKey;
		String secretKey;
		int codigoRespuesta;
		String urlTest = null;
		try {
			secretKey = evento.getLlaveSecreta();
			eventKey = evento.getLlaveEvento();
			urlTest = urlSeatIoValidatePublicKey + "/" + secretKey + "/" + eventKey + "/report/byStatus";
			codigoRespuesta = HTTPUtil.sendGet(urlTest);
			return codigoRespuesta == 200;
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error al validar las llaves SEATS.IO al enviar la peticion GET a [{0}], error: [{1}]", new Object[] {urlTest, exc});
			return false;
		}
	}
	
	public String onSendEmail5(int idInvitado, int idEvento) {
		id=idInvitado;
		logger.log(Level.INFO, "GuestView-onSendEmail5 idInvitado[{0}], idEvento[{1}]", new Object[] {id, idEvento});
		//final RequestContext context = RequestContext.getCurrentInstance();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		//boolean success = false;
		try {
			EventoInvitado eventoInvitado = eventoFacade.findEventoInvitado(idEvento, id);
			logger.log(Level.INFO, "eventoInvitado obtenido exitosamente {0}", eventoInvitado);
			enviarCorreo = true;
			
			/* Codigo anterior
			//eventoInvitado.getInvitado().setIdEventoInvitado(String.valueOf(eventoInvitado.getId()));
			eventoInvitado.getInvitado().setIdEventoInvitado(String.valueOf(eventoInvitado.hashCode()));
			sendMail(eventoInvitado.getInvitado());
			*/
			
			
			this.sendMailWithImage(eventoInvitado);
			
			
			

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error sending email to  Invitado ", e);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
					"Error al enviar la invitación."));
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
					e.getMessage()));
		}

		//context.addCallbackParam("success", success);
		//if (success) {
		//}
		return null;
	}
	/*public String onSendEmail6() {
		final RequestContext context = RequestContext.getCurrentInstance();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		  Map<String,String> params = 
				  facesContext.getExternalContext().getRequestParameterMap();
		  String idInvitado = params.get("idInvitado");
		
		logger.log(Level.INFO, "GuestView-onSendEmail6 {0}", idInvitado);
		boolean success = false;
		try {
			id=Integer.valueOf(idInvitado);
			EventoInvitado eventoInvitado = eventoFacade.findEventoInvitado(1, id);
			logger.log(Level.INFO, "eventoInvitado obtenido exitosamente {0}", eventoInvitado);
			enviarCorreo = true;
			sendMail(eventoInvitado.getInvitado());
			success = true;
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito",
					"Se envio la invitación."));
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Error sending email to  Invitado ", e);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
					"Error al enviar la invitación."));
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
					e.getMessage()));
//			context.addCallbackParam("error", e.getMessage());
		}

		context.addCallbackParam("success", success);
		if (success) {
		}
		return null;
	}*/
	
	
	
	
	
	
	
	/**
	 * Method used when a row is updated.
	 *
	 * @param event
	 *            the event
	 */
	public void onRowEdit(RowEditEvent event) {
		Invitado modifiedValue = (Invitado) event.getObject();
		logger.log(Level.INFO, "Invitado Value updated [{0}] ", modifiedValue);
		logger.log(Level.INFO, "Invitado Tipo Invitado [{0}] ", modifiedValue.getTipoInvitado().getId().intValue());
		logger.log(Level.INFO, "Invitado Empresa [{0}] ", modifiedValue.getEmpresa());
		
		
		// Se valida el campo empresa como obligatorio si el tipoinvitado es proveedor.
		if (modifiedValue != null && 
			modifiedValue.getTipoInvitado() != null &&
			modifiedValue.getTipoInvitado().getId() != null && 
			modifiedValue.getTipoInvitado().getId().intValue() == ID_TIPO_PROVEEDOR && modifiedValue.getEmpresa().equals("")) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "El campo Empresa es requerido.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
		
		// Se valida el correo del invitado no lo tenga otro invitado.
		if (modifiedValue != null && modifiedValue.getEmail() != null) {
			List<Invitado> invitados = guestFacade.findInvitadosByEmail(modifiedValue.getEmail());
			for (Invitado invitado : invitados) {
				if (invitado.getEmail().equals(modifiedValue.getEmail()) && 
					invitado.getId().intValue() != modifiedValue.getId().intValue()) {
					String message = "El email [" + modifiedValue.getEmail() + "] ya está registrado.";
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", message);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					FacesContext.getCurrentInstance().validationFailed();
					return;
				}
			}
		}
		
		
		if (modifiedValue != null && 
			modifiedValue.getTipoInvitado() != null &&
			modifiedValue.getTipoInvitado().getId() != null && 
			modifiedValue.getTipoInvitado().getId().intValue() != ID_TIPO_PROVEEDOR) {
			modifiedValue.setEmpresa("");
		}

		if (modifiedValue != null && modifiedValue.getId() != null) {
			guestFacade.update(modifiedValue);
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

	/**
	 * @return the guestFacade
	 */
	public GuestFacade getGuestFacade() {
		return guestFacade;
	}

	/**
	 * @param guestFacade
	 *            the guestFacade to set
	 */
	public void setGuestFacade(GuestFacade guestFacade) {
		this.guestFacade = guestFacade;
	}

	/**
	 * @return the tipoInvitados
	 */
	public List<TipoInvitado> getTipoInvitados() {
		return this.tipoInvitados;
	}

	/**
	 * @param tipoInvitados
	 *            the tipoInvitados to set
	 */
	public void setTipoInvitados(List<TipoInvitado> tipoInvitados) {
		this.tipoInvitados = tipoInvitados;
	}

	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	/**
	 * @return the invitados
	 */
	public List<Invitado> getInvitados() {

		// Si NO existe una consulta previa de invitados se realiza la consulta.
		// O si la lista previa se realizo para un evento difernte al seleccionado se realiza la consulta.
		if(invitados == null || idEventoToFindInvitados != sessionView.getIdEvento()) {
			idEventoToFindInvitados = sessionView.getIdEvento();

			logger.log(Level.INFO, "GuestView.getInvitados() idEvento[{0}]", idEventoToFindInvitados);
			
			if (idEventoToFindInvitados == 0) {
				invitados = guestFacade.findAllInvitados();
			} else {
				invitados = guestFacade.findAllInvitadosByIdEvento(idEventoToFindInvitados);
			}

		}

		return invitados;
	}

	/**
	 * @param invitados
	 *            the invitados to set
	 */
	public void setInvitados(List<Invitado> invitados) {
		this.invitados = invitados;
	}

	/**
	 * Envia el correo con la invitacion como pdf adjunto.
	 * @param invitado Invitado
	 */
	/*private void sendMailAnterior(Invitado invitado) {
		logger.log(Level.INFO, "GuestView.sendMail Invitado {0}", invitado);
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		logger.log(Level.INFO, "GuestView.sendMail enviarCorreo? {0}", enviarCorreo);
		if (enviarCorreo) {
			try {
				servicioCorreo.enviarCorreo(invitado);
				logger.log(Level.INFO, "Enviar Correo con Invitacion {0}", invitado.getEmail());
				
			} catch (SendGridException e) {
				logger.log(Level.SEVERE, "Error al enviar mail a {}", invitado);
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("generic.error"),
						"No se pudo enviar el correo. " + e.getMessage()));
			}
		}
	}*/

	private void sendMailWithImage(EventoInvitado eventoInvitado) {
		logger.log(Level.INFO, "GuestView.sendMailWithImage Invitado {0}", eventoInvitado.getInvitado());
		logger.log(Level.INFO, "GuestView.sendMailWithImage Evento {0}", eventoInvitado.getEvento());
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final RequestContext context = RequestContext.getCurrentInstance();
		final ExternalContext ctx = facesContext.getExternalContext();
		final HttpServletRequest origRequest = (HttpServletRequest) ctx.getRequest();
		logger.log(Level.INFO, "GuestView.sendMailWithImage enviarCorreo? {0}", enviarCorreo);
		boolean success;
		if (enviarCorreo) {
			try {

				final String webApp = origRequest.getScheme() + ":" + "//" + origRequest.getServerName() + ":" + origRequest.getServerPort() + origRequest.getContextPath();
				String urlConfirmacion = webApp  + "/invitation/confirmacionEvento.xhtml?id=" + eventoInvitado.getIdMD5();

				// Se consulta el evento con los bytes de la imagen de la invitacion.
				Evento eventoConImagenInvitacion = eventoFacade.findOneWithImagenInvitacion(eventoInvitado.getEvento().getId());
				eventoInvitado.setEvento(eventoConImagenInvitacion);

				// Se valida si el evento tiene imagen.
				if (eventoConImagenInvitacion.getImagenInvitacion() != null) {
					servicioCorreo.enviarCorreoWithImage(urlConfirmacion, eventoInvitado);
					logger.log(Level.INFO, "Se envio Correo con Invitacion {0}, webAppRoot [{1}]", new Object[]{eventoInvitado.getInvitado().getEmail(), webApp});
					
					success = true;
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Se envió la invitación correctamente."));
					context.addCallbackParam("success", success);
				} else {
					logger.log(Level.INFO, "El evento {0} no tiene una imagen para la invitacion.", eventoConImagenInvitacion.getId());
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg.getString("generic.error"),
							"El evento [" + eventoConImagenInvitacion.getNombre() + "] no tiene configurada una imagen para enviar la invitación."));
				}

			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error al enviar mail a {0}", eventoInvitado.getInvitado());
				logger.log(Level.SEVERE, "Exception.message: {0}", e.getMessage());
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("generic.error"),
						"No se pudo enviar el correo."));
			}
		}
	}
	
	public ServicioCorreo getServicioCorreo() {
		return servicioCorreo;
	}

	public void setServicioCorreo(ServicioCorreo servicioCorreo) {
		this.servicioCorreo = servicioCorreo;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		logger.log(Level.INFO, "GuestView-setId {0}", id);
		this.id = id;
	}

	//	public void setId(String id) {
//		logger.log(Level.INFO, "GuestView-setIdString {0}", id);
//		try {
//			this.id = Integer.valueOf(id);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	/**
	 * @return the search
	 */
	public String getSearch() {
		return search;
	}

	/**
	 * @param search
	 *            the search to set
	 */
	public void setSearch(String search) {
		this.search = search;
	}

	/**
	 * @return the filteredInvitados
	 */
	public List<Invitado> getFilteredInvitados() {
		return filteredInvitados;
	}

	/**
	 * @param filteredInvitados
	 *            the filteredInvitados to set
	 */
	public void setFilteredInvitados(List<Invitado> filteredInvitados) {
		this.filteredInvitados = filteredInvitados;
	}

	/**
	 * @return the eventoFacade
	 */
	public EventoFacade getEventoFacade() {
		return eventoFacade;
	}

	/**
	 * @param eventoFacade
	 *            the eventoFacade to set
	 */
	public void setEventoFacade(EventoFacade eventoFacade) {
		this.eventoFacade = eventoFacade;
	}

	/**
	 * @return the noBoletos
	 */
	public int getNoBoletos() {
		return noBoletos;
	}

	/**
	 * @param noBoletos the noBoletos to set
	 */
	public void setNoBoletos(int noBoletos) {
		this.noBoletos = noBoletos;
	}

	/**
	 * Valida si el tipo de invitado es proveedor, regresa verdadero si es
	 * proveedor, falso en caso contrario.
	 * @return boolean
	 */
	public boolean isCheckProveedor() {
		if (this.tipoInvitado == null || this.tipoInvitado.getId() == null) {
			return false;
		}
        return this.tipoInvitado.getId().intValue() == ID_TIPO_PROVEEDOR ? true : false;
    }
	
	/**
	 * Valida si el tipo de invitado es proveedor, regresa verdadero si es
	 * proveedor, falso en caso contrario.
	 * @return boolean
	 */
	public boolean isCheckProveedor(Invitado invitado) {
		if (invitado.getTipoInvitado() == null || invitado.getTipoInvitado().getId() == null) {
			return false;
		}
        return invitado.getTipoInvitado().getId().intValue() == ID_TIPO_PROVEEDOR ? true : false;
    }

	public void getReportData() throws IOException{
		logger.log(Level.INFO, "GuestView-getReportData");
		
		OutputStream out = null;
		FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext ectx = ctx.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
        try {

        	// Se obtiene el id del evento en session.
        	int idEvento = sessionView.getIdEvento();
        	String status = "OCUPADO";

        	logger.log(Level.INFO, "GuestView-getReportData Consultando el evento {0}", idEvento);
        	Evento evento = eventoFacade.findOne(idEvento);

        	logger.log(Level.INFO, "GuestView-getReportData Consultando invitados por el evento {0}", idEvento);
        	List<Invitado> invitados = guestFacade.findAllInvitadosByIdEvento(idEvento);
        	logger.log(Level.INFO, "GuestView-getReportData Invitados: {0}", invitados.size());

        	logger.log(Level.INFO, "GuestView-getReportData Consultando invitados por el evento y estatus {0}, {1}", new Object[] {idEvento, status});
        	List<Invitado> invitadosStatusOcupado = guestFacade.findAllInvitadosByIdEventoStatus(idEvento, status);
        	logger.log(Level.INFO, "GuestView-getReportData Invitados: {0}", invitadosStatusOcupado.size());

        	GuestReport report = new GuestReport(invitados, invitadosStatusOcupado);

        	Date date = new Date();
        	DateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
        	String fileName = "\"invitados_" + evento.getNombre().toLowerCase().replaceAll(" ", "_") + "_" + formatter.format(date) + ".xls\"";
        	
	        response.setContentType("application/vnd.ms-excel");
	        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
	        out = response.getOutputStream();
	        
	        report.write(out);
	        //workbook.write(out);

	    } catch (Exception e) {
	    	logger.log(Level.SEVERE, "GuestView-getReportData Error al generar el reporte: {0}" + e.getMessage());

	    	StringWriter sw = new StringWriter();
	    	PrintWriter pw = new PrintWriter(sw);
	    	e.printStackTrace(pw);
	    	logger.log(Level.SEVERE, "GuestView-getReportData StackTrace: ", sw.toString());
	    	
	    } finally {
	    	if (out != null) {
	    		out.close();
	    	}
	    	//ctx.responseComplete();
	    }
	}

	
	/*public void postProcessXLS(Object document) {
		final int CHARACTER_SIZE = 256;

        HSSFWorkbook wb = (HSSFWorkbook) document;
        // Se asigna el nombre de la primera hoja.
        wb.setSheetName(0, "Invitados");

        // Se agrega una hoja con el resumen del evento.
        String safeName = WorkbookUtil.createSafeSheetName("Totales"); // returns " O'Brien's sales   "
        HSSFSheet sheet1 = wb.createSheet(safeName);
        sheet1.setColumnWidth(0, 28 * CHARACTER_SIZE);
        
        Row row = sheet1.createRow((short)0);
        row.createCell(0).setCellValue("Total de invitados");
        row.createCell(1).setCellValue(356);

        Row row1 = sheet1.createRow((short)1);
        row1.createCell(0).setCellValue("Total de invitados confirmados");
        row1.createCell(1).setCellValue(300);

        // Se agrega el estilo a los t�tulos de las hojas.
        HSSFCellStyle titleStyle = getTitleStyle(wb);

        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
        for(int jdx = 0; jdx < header.getPhysicalNumberOfCells(); jdx++) {
        	HSSFCell cell = header.getCell(jdx);
        	cell.setCellStyle(titleStyle);
        	
        	sheet.setColumnWidth(jdx, 12 * CHARACTER_SIZE);
        }
    }

	private Font getTitleFont(HSSFWorkbook wb) {
	    Font font = wb.createFont();
	    font.setBold(true);
	    return font;
	}

	private HSSFCellStyle getTitleStyle(HSSFWorkbook wb) {
		HSSFCellStyle styleTitle = wb.createCellStyle();
	    styleTitle.setFont(getTitleFont(wb));
	    styleTitle.setAlignment(CellStyle.ALIGN_CENTER);
	    return styleTitle;
	}*/
	
	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public boolean getEmailInvalid() {
		return emailInvalid;
	}

	public void setEmailInvalid(boolean emailInvalid) {
		this.emailInvalid = emailInvalid;
	}

	public Page getPage() {
		return Page.INVITADOS;
	}

}
