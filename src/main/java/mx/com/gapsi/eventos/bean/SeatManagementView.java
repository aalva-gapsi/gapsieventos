/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.bean;

import static us.monoid.web.Resty.content;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.primefaces.context.RequestContext;

import mx.com.gapsi.eventos.facade.EventoFacade;
import mx.com.gapsi.eventos.model.Evento;
import mx.com.gapsi.eventos.model.EventoInvitado;
import mx.com.gapsi.eventos.model.Invitado;
import mx.com.gapsi.eventos.utils.HTTPUtil;
import mx.com.gapsi.eventos.utils.Page;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

@ManagedBean
@RequestScoped
public class SeatManagementView implements Serializable {
	private static final String SECRET_KEY = "secretKey";
	private static final String EVENT_KEY = "eventKey";
	private static final String STATUS_KEY = "status";
	private static final String OBJECTS = "objects";
	private static final String OBJECT_ID = "objectId";
	private static final String ORDER_ID = "orderId";
	private static final String EXTRA_DATA = "extraData";
	private static final String NAME = "name";

	private static final long serialVersionUID = -8198457936373933828L;

//	private static final String EVENT_KEY_VAL = "2a89c073-230c-4ba1-bbe0-ca2c24b96448";//DESA
//	private static final String EVENT_KEY_VAL = "9690fbee-9aeb-4152-b980-6526633dce9b"; //PROD
//	private static final String SECRET_KEY_VAL = "9d8b86e2-8679-4a6e-a2e9-8cb126a05816";

	private static final String seatIOChangeExtraData = "https://app.seats.io/api/changeExtraData";
	private static final String seatIOChangeStatus = "https://app.seats.io/api/changeStatus";
	private static final String seatIoRelease = "https://app.seats.io/api/release";
	private static final String seatIoBook = "https://app.seats.io/api/book";
	private static final String seatIoQueryByOrder = "https://app.seats.io/api/event/-eventKey-/orders/-orderId-/-secretKey-";

	private static final String SEATIO_STATUS_USED = "used";

	private static final String comma = ",";

	@ManagedProperty("#{sessionView}")
	private SessionView sessionView;
	
	private long idEventoInvitado;
	private EventoInvitado eventoInvitado;
	@ManagedProperty("#{eventoFacade}")
	private EventoFacade eventoFacade;

	private String seatsAllocated;

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(SeatManagementView.class.getName());
	@ManagedProperty("#{msg}")
	private transient ResourceBundle msg;

	private String seats;

	public SeatManagementView() {
	}

	@PostConstruct
	void init() {
		final FacesContext facesContext = FacesContext.getCurrentInstance();

		Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();

		String id = params.get("idEventoInvitado");

		String idEvento = params.get("idEvento");
		String idInvitado = params.get("idInvitado");

		String idPreviousPage = params.get("previousPage");

		if (idPreviousPage != null) {
			sessionView.setPreviousPage(Page.getPage(idPreviousPage));
		}
		
		if (id != null) {
			idEventoInvitado = Long.parseLong(id);
			logger.log(Level.INFO, "Usando el idEventoInvitado {0}", idEventoInvitado);

			eventoInvitado = eventoFacade.findEventoInvitado(idEventoInvitado);
			logger.log(Level.INFO, "Evento Invitado encontrado {0}", eventoInvitado);
			facesContext.getExternalContext().getSessionMap().put("idEventoInvitado", String.valueOf(idEventoInvitado));
			seatsAllocated = getSeatsAllocated();
		}if (idInvitado != null && idEvento != null ) {
			logger.log(Level.INFO, "Usando el idInvitado {0} y el idEvento {1}", new Object[]{idInvitado, idEvento});

			// Se asigna el eventoInvitado al atributo de instancia.
			//EventoInvitado eventoInvitado = eventoFacade.findEventoInvitado(1, Long.parseLong(idInvitad));
			eventoInvitado = eventoFacade.findEventoInvitado(Long.parseLong(idEvento), Long.parseLong(idInvitado));
			idEventoInvitado = eventoInvitado.getId();
			logger.log(Level.INFO, "Evento Invitado encontrado {0} con idInvitado y idEvento.", eventoInvitado);
			facesContext.getExternalContext().getSessionMap().put("idEventoInvitado", String.valueOf(idEventoInvitado));
			seatsAllocated = getSeatsAllocated();
		} else {
			
			//logger.log(Level.SEVERE, "Intentando obtener el idEventoInvitado de la session...");
			
			getIdEventoInvitadoFromSession();
		}
	}	
	
	private void getIdEventoInvitadoFromSession() {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		logger.log(Level.INFO, "El id es nulo  usando evento por default");
		Object obj = facesContext.getExternalContext().getSessionMap().get("idEventoInvitado");
		if (obj != null) {
			idEventoInvitado = Long.parseLong((String) obj);
			eventoInvitado = eventoFacade.findEventoInvitado(idEventoInvitado); //
			seatsAllocated = getSeatsAllocated();
		} else {
			eventoInvitado = new EventoInvitado(new Evento(), new Invitado());
		}
	}

	/**
	 * Convierte una lista de seats en cadena de caracteres a
	 * una lista de String's.
	 * @param seats String
	 * @return List<String>
	 */
	private static List<String> seatsToList(String seats) {
		logger.info("Seats to execute action on: " + seats);
		// Remover [], quitar espacios
		String results = seats.replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ", "");
		List<String> listOfSeats = Arrays.asList(results.split(comma));
		return listOfSeats;
	}
	
	private List<String> getSeatsAsLeast() {
		logger.info("Seats to execute action on: " + seats);
		// Remover [], quitar espacios
		//String results = seats.replaceAll("\\[", "");
		//results = results.replaceAll("\\]", "");
		String results = seats.replaceAll(" ", "");
		List<String> listOfSeats = Arrays.asList(results.split(comma));
		return listOfSeats;
	}
	
	public void releaseSeats(ActionEvent actionEvent) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		try {
			List<String> listOfSeats = getSeatsAsLeast();
			logger.log(Level.INFO, "Asientos a liberar: "+listOfSeats+ " asientos actuales "+ seatsAllocated );
//			Se valida que el asiento este asociado al invitado. 
			for (String seatToRelease : listOfSeats) {
			    if(seatsAllocated!= null && !seatsAllocated.isEmpty() && !this.seatsAllocated.contains(seatToRelease)){
			    	throw new Exception("El asiento "+seatToRelease+ " no pertenece al invitado.");
			    }
			}
			
			
			callSeatIo(seatIoRelease);

			// Si despues de liberar asientos, la lista de asientos asignados esta vacia se cambia el estatus a LIBRE.
			List<String> listOfSeatsAllocated = seatsToList(seatsAllocated);
			@SuppressWarnings("unchecked")
			List<String> seatsAllocatedNow = (List<String>)CollectionUtils.subtract(listOfSeatsAllocated, listOfSeats);
			logger.log(Level.INFO, "Asientos aun asignados: "+seatsAllocatedNow.size());
			if (seatsAllocatedNow.size() == 0) {
				if (idEventoInvitado == 0) {
					getIdEventoInvitadoFromSession(); 
				}
				eventoInvitado.setEstatus(EventoInvitado.ESTATUS_LIBRE);
				eventoFacade.updateValue(eventoInvitado);
			}
			
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					msg.getString("generic.success"), "Se han liberado los asientos exitosamente"));
		} catch (IllegalStateException e) {
			logger.log(Level.SEVERE, "Error: ", e);
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("generic.error"),
							"Problema al liberar los asientos."));
		}catch (Exception e) {
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("generic.error"),
							e.getMessage()));
		}
	}

	public void bookSeats(ActionEvent actionEvent) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		try {
			String name = this.eventoInvitado.getInvitado().getNombreFull();
			List<String> listOfSeats = getSeatsAsLeast();
			bookSeatIO(listOfSeats, name);
			//callSeatIo(seatIoBook);
			
			// Se asignan el nombre completo como extraData a cada objeto.
			/*String name = this.eventoInvitado.getInvitado().getNombreFull();
			List<String> listOfSeats = getSeatsAsLeast();
			for (String seat: listOfSeats) {
				this.changeExtraDataSeatIO(seat, name);
			}*/
			
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					msg.getString("generic.success"), "Se han reservado los asientos existosamente"));
			if (idEventoInvitado == 0) {
				getIdEventoInvitadoFromSession(); 
			}
			eventoInvitado.setEstatus("ASIGNADO");
			eventoFacade.updateValue(eventoInvitado);
		} catch (IllegalStateException e) {
			logger.log(Level.SEVERE, "Error: ", e);
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("generic.success"),
							"Problema al reservar los asientos."));
		}catch (Exception e) {
			logger.log(Level.SEVERE, "Error: ", e);
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("generic.success"),
							"Problema al actualizar el estatus del invitado."));			
		}
	}

	private void callSeatIo(final String service) throws IllegalStateException {
		try {
			Resty resty = new Resty();
			JSONObject jsonObj = new JSONObject();
			List<String> listOfSeats = getSeatsAsLeast();
			jsonObj = jsonObj.accumulate(OBJECTS, listOfSeats);
			jsonObj = jsonObj.accumulate(EVENT_KEY, eventoInvitado.getEvento().getLlaveEvento());//jsonObj = jsonObj.accumulate(EVENT_KEY, EVENT_KEY_VAL);
			jsonObj = jsonObj.accumulate(SECRET_KEY, eventoInvitado.getEvento().getLlaveSecreta());//jsonObj = jsonObj.accumulate(SECRET_KEY, SECRET_KEY_VAL);
			
			if (idEventoInvitado == 0) {
				getIdEventoInvitadoFromSession(); 
			}
			
			jsonObj = jsonObj.accumulate(ORDER_ID, idEventoInvitado);
			logger.info(jsonObj.toString());
			JSONResource r = resty.json(service, content(jsonObj));
			seats = "";
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error: ", e);
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Reserva asientos en SeatsIO asignando el nombre del invitado como extraData. 
	 * @param listOfSeats List<String>
	 * @param name String
	 * @throws IllegalStateException
	 */
	private void bookSeatIO(List<String> listOfSeats, String name) throws IllegalStateException {
		try {
			JSONObject jsonExtraData = new JSONObject();
			jsonExtraData = jsonExtraData.accumulate(NAME, StringEscapeUtils.escapeHtml(name));

			List<JSONObject> listOfObjects = new ArrayList<JSONObject>();
			for (String seat : listOfSeats) {
				JSONObject jsonObject = new JSONObject();
				jsonObject = jsonObject.accumulate("objectId", seat);
				jsonObject = jsonObject.accumulate(EXTRA_DATA, jsonExtraData);

				listOfObjects.add(jsonObject);
			}
			
			Resty resty = new Resty();
			JSONObject jsonObj = new JSONObject();
			jsonObj = jsonObj.accumulate(OBJECTS, listOfObjects);
			jsonObj = jsonObj.accumulate(EVENT_KEY, eventoInvitado.getEvento().getLlaveEvento());//jsonObj = jsonObj.accumulate(EVENT_KEY, EVENT_KEY_VAL);
			jsonObj = jsonObj.accumulate(SECRET_KEY, eventoInvitado.getEvento().getLlaveSecreta());//jsonObj = jsonObj.accumulate(SECRET_KEY, SECRET_KEY_VAL);
			
			if (idEventoInvitado == 0) {
				getIdEventoInvitadoFromSession(); 
			}
			
			jsonObj = jsonObj.accumulate(ORDER_ID, idEventoInvitado);
			logger.info(jsonObj.toString());
			JSONResource r = resty.json(seatIoBook, content(jsonObj));
			seats = "";
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error: ", e);
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Cambia el etatus de una lista de objetos en SeatsIO.
	 * @param status String
	 * @param seats List<String>
	 * @throws IllegalStateException
	 */
	private void changeStatusSeatIO(String status, List<String> listOfSeats, String name) throws IllegalStateException {
		try {
			JSONObject jsonExtraData = new JSONObject();
			jsonExtraData = jsonExtraData.accumulate(NAME, StringEscapeUtils.escapeHtml(name));

			List<JSONObject> listOfObjects = new ArrayList<JSONObject>();
			for (String seat : listOfSeats) {
				JSONObject jsonObject = new JSONObject();
				jsonObject = jsonObject.accumulate("objectId", seat);
				jsonObject = jsonObject.accumulate(EXTRA_DATA, jsonExtraData);

				listOfObjects.add(jsonObject);
			}
			
			JSONObject jsonObj = new JSONObject();
			jsonObj = jsonObj.accumulate(OBJECTS, listOfObjects);
			jsonObj = jsonObj.accumulate(STATUS_KEY, status);
			jsonObj = jsonObj.accumulate(EVENT_KEY, eventoInvitado.getEvento().getLlaveEvento());//jsonObj = jsonObj.accumulate(EVENT_KEY, EVENT_KEY_VAL);
			jsonObj = jsonObj.accumulate(SECRET_KEY, eventoInvitado.getEvento().getLlaveSecreta());//jsonObj = jsonObj.accumulate(SECRET_KEY, SECRET_KEY_VAL);
			
			
			String JSONBody = jsonObj.toString();
			logger.info(jsonObj.toString());
			HTTPUtil.sendPost(seatIOChangeStatus, JSONBody);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error: ", e);
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Cambia el extraData de un objeto en SeatsIO.
	 * @param name String
	 * @param seat String
	 * @throws IllegalStateException
	 */
	private void changeExtraDataSeatIO(String seat, String name) throws IllegalStateException {
		try {
			JSONObject jsonExtraData = new JSONObject();
			jsonExtraData = jsonExtraData.accumulate(NAME, name);

			JSONObject jsonObj = new JSONObject();
			jsonObj = jsonObj.accumulate(OBJECT_ID, seat);
			jsonObj = jsonObj.accumulate(EXTRA_DATA, jsonExtraData);
			jsonObj = jsonObj.accumulate(EVENT_KEY, eventoInvitado.getEvento().getLlaveEvento());//jsonObj = jsonObj.accumulate(EVENT_KEY, EVENT_KEY_VAL);
			jsonObj = jsonObj.accumulate(SECRET_KEY, eventoInvitado.getEvento().getLlaveSecreta());//jsonObj = jsonObj.accumulate(SECRET_KEY, SECRET_KEY_VAL);
			
			
			String JSONBody = jsonObj.toString();
			logger.info(jsonObj.toString());
			HTTPUtil.sendPost(seatIOChangeExtraData, JSONBody);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error: ", e);
			throw new IllegalStateException(e);
		}
	}
	
	public static void main(String[] args) {
		try {
			
			final String EVENT_KEY_VAL = "2a89c073-230c-4ba1-bbe0-ca2c24b96448";//DESA
			final String SECRET_KEY_VAL = "9d8b86e2-8679-4a6e-a2e9-8cb126a05816";
			
			JSONObject jsonExtraData = new JSONObject();
			jsonExtraData = jsonExtraData.accumulate(NAME, "Javier Islas Garcia");

			List<String> listOfSeats = new ArrayList<String>();
			listOfSeats.add("A-15");
			listOfSeats.add("A-16");
			
			List<JSONObject> array = new ArrayList<JSONObject>();
			for (String seat : listOfSeats) {
				JSONObject jsonObject = new JSONObject();
				jsonObject = jsonObject.accumulate("objectId", seat);
				jsonObject = jsonObject.accumulate(EXTRA_DATA, jsonExtraData);

				array.add(jsonObject);
			}

			JSONObject jsonObj = new JSONObject();
			jsonObj = jsonObj.accumulate(OBJECTS, array);
			jsonObj = jsonObj.accumulate(EVENT_KEY, EVENT_KEY_VAL);
			jsonObj = jsonObj.accumulate(SECRET_KEY, SECRET_KEY_VAL);
			
			jsonObj = jsonObj.accumulate(ORDER_ID, 1001);
			System.out.println("jsonObj:" + jsonObj.toString());
			
			/*Resty resty = new Resty();

			JSONObject jsonObj = new JSONObject();
			List<String> listOfSeats = new ArrayList<String>();
			listOfSeats.add("B-5");
			listOfSeats.add("B-4");
			listOfSeats.add("B-3");
			listOfSeats.add("B-2");
			listOfSeats.add("B-1");
			jsonObj = jsonObj.accumulate(OBJECTS, listOfSeats);
			jsonObj = jsonObj.accumulate(EVENT_KEY, EVENT_KEY_VAL);
			jsonObj = jsonObj.accumulate(SECRET_KEY, SECRET_KEY_VAL);
			System.out.println(jsonObj);
			JSONResource r = resty.json(seatIoRelease, content(jsonObj));
			System.out.println(r);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResourceBundle getMsg() {
		return msg;
	}

	public void setMsg(ResourceBundle msg) {
		this.msg = msg;
	}

	public String getSeats() {
		return seats;
	}

	public void setSeats(String seats) {
		this.seats = seats;
	}

	/**
	 * @return the idEventoInvitado
	 */
	public long getIdEventoInvitado() {
		return idEventoInvitado;
	}

	/**
	 * @param idEventoInvitado
	 *            the idEventoInvitado to set
	 */
	public void setIdEventoInvitado(long idEventoInvitado) {
		this.idEventoInvitado = idEventoInvitado;
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
	 * @return the eventoInvitado
	 */
	public EventoInvitado getEventoInvitado() {
		return eventoInvitado;
	}

	/**
	 * @param eventoInvitado
	 *            the eventoInvitado to set
	 */
	public void setEventoInvitado(EventoInvitado eventoInvitado) {
		this.eventoInvitado = eventoInvitado;
	}

	public String getSeatsAllocated() {
		logger.log(Level.INFO, "getSeatsAllocated EventoInvitado: {0}", eventoInvitado);
		logger.log(Level.INFO, "getSeatsAllocated idEventoInvitado: {0}", idEventoInvitado);
		String EVENT_KEY_VAL = eventoInvitado.getEvento().getLlaveEvento();
		String SECRET_KEY_VAL = eventoInvitado.getEvento().getLlaveSecreta();

		if (EVENT_KEY_VAL != null && SECRET_KEY_VAL != null) {
			String urlToCall = seatIoQueryByOrder.replaceAll("-eventKey-", EVENT_KEY_VAL)
				.replaceAll("-orderId-", String.valueOf(idEventoInvitado)).replaceAll("-secretKey-", SECRET_KEY_VAL);
			logger.log(Level.INFO, "getSeatsAllocated Url to invoke: {0}", urlToCall);
			seatsAllocated = getHTML(urlToCall);
			logger.log(Level.INFO, "seatsAllocated: {0}", seatsAllocated);
		} else {
			logger.log(Level.WARNING, "getSeatsAllocated el evento: {0} no tiene llaves SEATS.IO." , eventoInvitado.getEvento());
		}

		return seatsAllocated;
	}

	public void setSeatsAllocated(String seatsAllocated) {
		this.seatsAllocated = seatsAllocated;
	}

	public static String getHTML(String urlToRead) {
		List<String> result = new ArrayList<>();
		try {
			Resty r = new Resty();
			JSONResource hello = r.json(urlToRead);
			JSONArray array = hello.array();
			for(int i = 0; i< array.length(); i++){
				result.add(array.getString(i));
			}
		} catch (Exception e) {
			logger.severe("Error " + e.getMessage());
		}
		
		return result.toString();
	}
	
	public void confirmSeats() {
		logger.info("Confirmando asientos para idEventoInvitado: " + idEventoInvitado);
		eventoInvitado = eventoFacade.findEventoInvitado(idEventoInvitado);
		final RequestContext context = RequestContext.getCurrentInstance();
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		String urlToGo = "confirmacion.xhtml?faces-redirect=true";
		boolean success = false;
		if (eventoInvitado != null) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					msg.getString("generic.success"), "Actualizacion exitosa"));
			eventoInvitado.setEstatus("OCUPADO");
			eventoFacade.updateValue(eventoInvitado);

			// Si el evento tiene mapas Seats.io.
			if (this.isHasSeatsMaps()) {
				// Se cambia el estatus de los objetos en seatsio a 'used'.
				List<String> listOfSeatsAllocated = seatsToList(seatsAllocated);
				String name = this.eventoInvitado.getInvitado().getNombreFull();
				this.changeStatusSeatIO(SEATIO_STATUS_USED, listOfSeatsAllocated, name);
			}
			
			success = true;
		} else {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					msg.getString("generic.success"), "Error al actualizar el campo"));
		}
		
		
		context.addCallbackParam("success", success);

		if (success) {
			try {
				facesContext.getExternalContext().redirect(urlToGo);
			} catch (final IOException ex) {
				logger.log(Level.SEVERE, "Exception happend when redirecting logged used", ex);
			}
		}
	}
	
	public boolean isSeatAvailable() {
		logger.info("Verificando confirmacion de asientos para idEventoInvitado: " + idEventoInvitado);
		eventoInvitado = eventoFacade.findEventoInvitado(idEventoInvitado);
		if (eventoInvitado != null && eventoInvitado.getEstatus() != null && eventoInvitado.getEstatus().equals("OCUPADO")) {
			logger.info("asiento ya confirmado " + idEventoInvitado);
			return false;
		} 
		logger.info("asiento pendiente de confirmar " + idEventoInvitado);
		return true;
	}
	
	public boolean isHasSeatsMaps() {
		if (eventoInvitado == null) {
			return false;
		}
		return  eventoInvitado.getEvento().isHasSeatsMaps();
			
	}

	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}
}
