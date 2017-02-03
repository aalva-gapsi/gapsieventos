/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.converter;

import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import mx.com.gapsi.eventos.dao.EventoDao;
import mx.com.gapsi.eventos.model.Evento;
import mx.com.gapsi.eventos.model.TipoInvitado;

@Named(value="eventoConverter")
@ApplicationScoped
public class EventoConverter implements Converter {
    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger("EventoConverter");
	
	//@Inject
    @Autowired
	private EventoDao eventoDao;
	
	public EventoConverter() {
		// TODO Auto-generated constructor stub
	}

	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		logger.entering("EventoConverter", "getAsObject",value);
		// TODO pasar a aop
		Evento evento = null;
		try {
			if (value != null && value.trim().length() > 0) {
				evento = eventoDao.findOne(Long.parseLong(value));
			}
		} catch (Exception e) {
			logger.severe(e.getMessage());
			throw new ConverterException(
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Evento."));
		}finally {
			logger.exiting("EventoConverter", "getAsObject",evento);
		}
		return evento;
	}
	 
	    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
	    	logger.entering("EventoConverter", "getAsString",object);
	    	 String valueOf = null;
	        if(object != null) {
	        	if(object instanceof Evento){
	        		valueOf = String.valueOf(((Evento) object).getId());
	        	}else{
	        		valueOf = object.toString();
	        	}
	        }
	        logger.exiting("EventoConverter", "getAsString",valueOf);
	        return valueOf;
	    }

		public EventoDao getEventoDao() {
			return eventoDao;
		}

		public void setEventoDao(EventoDao eventoDao) {
			this.eventoDao = eventoDao;
		}

}
