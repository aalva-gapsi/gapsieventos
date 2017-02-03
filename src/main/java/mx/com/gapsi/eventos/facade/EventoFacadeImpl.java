/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.facade;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.com.gapsi.eventos.dao.EventoDao;
import mx.com.gapsi.eventos.dao.EventoInvitadoDao;
import mx.com.gapsi.eventos.model.Evento;
import mx.com.gapsi.eventos.model.EventoInvitado;

@Component("eventoFacade")
public class EventoFacadeImpl implements EventoFacade {
	Logger logger = Logger.getLogger(EventoFacadeImpl.class.getName());
	@Autowired	
	private EventoDao eventoDao;
	@Autowired
	private EventoInvitadoDao eventoInvitadoDao;

	public EventoFacadeImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Evento findOne(long idFashionFest) {
		return eventoDao.findOne(idFashionFest);
	}

	@Override
	public Evento findOneWithImagenInvitacion(long idFashionFest) {
		return eventoDao.findOneWithImagenInvitacion(idFashionFest);
	}
	
	/**
	 * @return the eventoDao
	 */
	public EventoDao getEventoDao() {
		return eventoDao;
	}

	/**
	 * @param eventoDao the eventoDao to set
	 */
	public void setEventoDao(EventoDao eventoDao) {
		this.eventoDao = eventoDao;
	}

	/**
	 * @return the eventoInvitadoDao
	 */
	public EventoInvitadoDao getEventoInvitadoDao() {
		return eventoInvitadoDao;
	}

	/**
	 * @param eventoInvitadoDao the eventoInvitadoDao to set
	 */
	public void setEventoInvitadoDao(EventoInvitadoDao eventoInvitadoDao) {
		this.eventoInvitadoDao = eventoInvitadoDao;
	}

	@Override
	public EventoInvitado findEventoInvitado(long idEventoInvitado) {
		return eventoInvitadoDao.findOne(idEventoInvitado);
	}
	
	@Override
	public EventoInvitado findEventoInvitado(long idEvento, long idInvitado) {
		return eventoInvitadoDao.findOne(idEvento,idInvitado);
	}

	@Override
	public EventoInvitado updateValue(EventoInvitado eventoInvitado) {
		return eventoInvitadoDao.update(eventoInvitado);
	}

	@Override
	public List<Evento> findAllEventos() {
		logger.log(Level.INFO, "EventoFacade-findAllEventos", "");
		return eventoDao.findAll();
	}

	@Override
	public List<Evento> findAllAvailableByIdInvitado(int idInvitado) {
		logger.log(Level.INFO, "EventoFacade-findAllAvailableByIdInvitado [{0}]", idInvitado);
		return eventoDao.findAllAvailableByIdInvitado(idInvitado);
	}

	@Override
	public Evento update(Evento evento) {
		logger.log(Level.INFO, "EventoFacade-update [{0}]", evento);
		//logger.log(Level.INFO, "EventoFacade-update imagenInvitacion: [{0}]", evento.getImagenInvitacion());

		if (evento.getImagenInvitacion() == null) {
			Evento oldEvento = eventoDao.findOneWithImagenInvitacion(evento.getId());
			evento.setImagenInvitacion(oldEvento.getImagenInvitacion());
		}
		
		Evento newEvento = eventoDao.update(evento);
		
		
		/*if (evento.getImagenInvitacion() != null) {
			newEvento = eventoDao.update(evento);
		} else {
			// Se consulta la entidad para no actualizar la imagen cuando es null.
			Evento oldEvento = eventoDao.findOneWithImagenInvitacion(evento.getId());
			evento.setImagenInvitacion(oldEvento.getImagenInvitacion());
			
			//oldEvento.setNombre(evento.getNombre());
			//oldEvento.setDescripcion(evento.getDescripcion());
			//oldEvento.setFecha(evento.getFecha());
			//oldEvento.setLugar(evento.getLugar());
			//oldEvento.setSubjectEmail(evento.getSubjectEmail());
			//oldEvento.setTotalAsientos(evento.getTotalAsientos());
			//oldEvento.setLlaveEvento(evento.getLlaveEvento());
			//oldEvento.setLlaveSecreta(evento.getLlaveSecreta());
			//oldEvento.setLlavePublica(evento.getLlavePublica());
			//oldEvento.setImagenQRX(evento.getImagenQRX());
			//oldEvento.setImagenQRY(evento.getImagenQRY());

			//logger.log(Level.INFO, "EventoFacade-update imagenInvitacion de base de datos: [{0}]", oldEvento.getImagenInvitacion());

			//newEvento = eventoDao.update(oldEvento);
			
			new Evento = eventoDao.update(evento);
		}*/
		
        logger.log(Level.INFO, "EventoFacade-update 2a[{0}]", newEvento);
		return newEvento;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.liverpool.com.fest.facade.EventoFacade#createEvento(mx.liverpool.com.
	 * fest.model.Evento)
	 */
	@Override
	public Evento createEvento(Evento evento) {
		logger.log(Level.INFO, "EventoFacade.createEvento[{0}]", evento.toString());
		eventoDao.create(evento);
		return evento;
	}

}
