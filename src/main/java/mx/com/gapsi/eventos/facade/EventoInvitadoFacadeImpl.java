package mx.com.gapsi.eventos.facade;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.com.gapsi.eventos.dao.EventoInvitadoDao;
import mx.com.gapsi.eventos.model.EventoInvitado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("eventoInvitadoFacade")
public class EventoInvitadoFacadeImpl implements EventoInvitadoFacade {
	Logger logger = Logger.getLogger(EventoInvitadoFacadeImpl.class.getName());

	@Autowired
	private EventoInvitadoDao eventoInvitadoDao;

	public EventoInvitadoFacadeImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public EventoInvitado findOne(long idEventoInvitado) {
		return eventoInvitadoDao.findOne(idEventoInvitado);
	}

	public EventoInvitado findByIdMD5(String idMD5) {
		return eventoInvitadoDao.findByIdMD5(idMD5);
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
	public List<EventoInvitado> findAllEventoInvitados() {
		logger.log(Level.INFO, "EventoInvitadoFacade-findAllEventoInvitados", "");
		return eventoInvitadoDao.findAll();
	}

	public List<EventoInvitado> findAllEventoInvitadosByIdInvitado(int idInvitado) {
		logger.log(Level.INFO, "EventoInvitadoFacade-findAllEventoInvitadosByIdInvitado[{0}]", idInvitado);
		return eventoInvitadoDao.findAllByIdInvitado(idInvitado);
	}
	
	@Override
	public EventoInvitado update(EventoInvitado eventoInvitado) {
		logger.log(Level.INFO, "EventoInvitadoFacade-update 1[{0}]", eventoInvitado);
        EventoInvitado newEventoInvitado = eventoInvitadoDao.update(eventoInvitado);
        logger.log(Level.INFO, "EventoInvitadoFacade-update 2a[{0}]", newEventoInvitado);
		return newEventoInvitado;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.liverpool.com.fest.facade.EventoInvitadoFacade#createEventoInvitado(mx.liverpool.com.
	 * fest.model.EventoInvitado)
	 */
	@Override
	public EventoInvitado createEventoInvitado(EventoInvitado eventoInvitado) {
		logger.log(Level.INFO, "EventoInvitadoFacade.createEventoInvitado[{0}]", eventoInvitado.toString());
		eventoInvitadoDao.create(eventoInvitado);
		return eventoInvitado;
	}

}
