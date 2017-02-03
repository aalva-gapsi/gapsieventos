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
import mx.com.gapsi.eventos.dao.InvitadoDao;
import mx.com.gapsi.eventos.dao.TipoInvitadoDao;
import mx.com.gapsi.eventos.model.Evento;
import mx.com.gapsi.eventos.model.EventoInvitado;
import mx.com.gapsi.eventos.model.Invitado;
import mx.com.gapsi.eventos.model.TipoInvitado;

/**
 * @author Don
 *
 */
@Component("guestFacade")
public class GuestFacadeImpl implements GuestFacade {

	private static final int MIN_LENGTH = 3;
	//private static final long ID_FASHION_FEST = 1;
	Logger logger = Logger.getLogger(GuestFacadeImpl.class.getName());
	// @Inject
	@Autowired
	private InvitadoDao invitadoDao;
	// @Inject
	@Autowired
	private TipoInvitadoDao tipoInvitadoDao;
	// @Inject
	@Autowired
	private EventoInvitadoDao eventoInvitadoDao;
	@Autowired
	private EventoDao eventoDao;
	
	/**
	 * 
	 */
	public GuestFacadeImpl() {
		super();
		logger.log(Level.FINE, "Creating class" + GuestFacadeImpl.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.liverpool.com.fest.facade.GuestFacade#findInvitado()
	 */
	@Override
	public Invitado findInvitadoById(long id) {
		logger.log(Level.INFO, "findOne[{0}]", id);
		return invitadoDao.findOne(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.liverpool.com.fest.facade.GuestFacade#findInvitadoByEmail()
	 */
	@Override
	public Invitado findInvitadoByEmail(String email) {
		logger.log(Level.INFO, "findInvitadoByEmail[{0}]", email);
		Invitado invitado = null;
		List<Invitado> invitados = invitadoDao.findByEmail(email);
		if (invitados != null && invitados.size() > 0) {
			invitado = invitados.get(0);
		}
		return invitado;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.liverpool.com.fest.facade.GuestFacade#findInvitadosByEmail()
	 */
	@Override
	public List<Invitado> findInvitadosByEmail(String email) {
		logger.log(Level.INFO, "findInvitadosByEmail[{0}]", email);
		return invitadoDao.findByEmail(email);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.liverpool.com.fest.facade.GuestFacade#findAllTipoInvitado()
	 */
	@Override
	public List<TipoInvitado> findAllTipoInvitado() {
		logger.log(Level.INFO, "findAllTipoInvitado");
		return tipoInvitadoDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.liverpool.com.fest.facade.GuestFacade#createInvitado(mx.liverpool.com.
	 * fest.model.Invitado)
	 */
	@Override
	public EventoInvitado createInvitado(Invitado invitado, Integer noBoletos, Integer idEvento) {
		logger.log(Level.INFO, "GuestFacade.createInvitado[{0}]", invitado.toString());
		TipoInvitado tipoInvitado = tipoInvitadoDao.findOne(invitado.getTipoInvitado().getId());
		invitado.setTipoInvitado(tipoInvitado);

		logger.log(Level.INFO, "GuestFacade.tipoInvitadoDao.findOne[{0}]", invitado.getTipoInvitado().getId());
		invitadoDao.create(invitado);
		logger.log(Level.INFO, "GuestFacade.invitadoDao.create[{0}]", invitado.toString());
		//return invitado;

		// Se crea el eventoinvitado.
		Evento evento = eventoDao.findOne(idEvento);
		EventoInvitado eventoInvitado = new EventoInvitado(evento,invitado);
		eventoInvitado.setNoBoletos(noBoletos);
		eventoInvitado.setEstatus("LIBRE");
		this.assignTickets(eventoInvitado);
		logger.log(Level.INFO, "GuestFacade.assignTickets{0}]", eventoInvitado.toString());
		return eventoInvitado;
	}

	/**
	 * @return the invitadoDao
	 */
	public InvitadoDao getInvitadoDao() {
		return invitadoDao;
	}

	/**
	 * @param invitadoDao
	 *            the invitadoDao to set
	 */
	public void setInvitadoDao(InvitadoDao invitadoDao) {
		this.invitadoDao = invitadoDao;
	}

	/**
	 * @return the tipoInvitadoDao
	 */
	public TipoInvitadoDao getTipoInvitadoDao() {
		return tipoInvitadoDao;
	}

	/**
	 * @param tipoInvitadoDao
	 *            the tipoInvitadoDao to set
	 */
	public void setTipoInvitadoDao(TipoInvitadoDao tipoInvitadoDao) {
		this.tipoInvitadoDao = tipoInvitadoDao;
	}

	@Override
	public List<Invitado> findInvitado(String search) {
		logger.log(Level.INFO, "findAllTipoInvitado");
		List<Invitado> invitados = null;
		if (search != null && search.length() > MIN_LENGTH) {
			invitados = invitadoDao.find(search);
		} else {

		}
		return invitados;
	}

	@Override
	public void assignTickets(EventoInvitado eventoInvitado) {
		eventoInvitadoDao.create(eventoInvitado);

	}

	/**
	 * @return the eventoInvitado
	 */
	public EventoInvitadoDao getEventoInvitadoDao() {
		return eventoInvitadoDao;
	}

	/**
	 * @param eventoInvitado
	 *            the eventoInvitado to set
	 */
	public void setEventoInvitadoDao(EventoInvitadoDao eventoInvitadoDao) {
		this.eventoInvitadoDao = eventoInvitadoDao;
	}

	@Override
	public Invitado update(Invitado invitado) {
		logger.log(Level.INFO, "GuestFacade-update 1[{0}]", invitado);
		TipoInvitado tipoInvitado = tipoInvitadoDao.findOne(invitado.getTipoInvitado().getId());
		invitado.setTipoInvitado(tipoInvitado);
        Invitado newInvitado = invitadoDao.update(invitado);
        logger.log(Level.INFO, "GuestFacade-update 2a[{0}]", invitado);
        logger.log(Level.INFO, "GuestFacade-update 2b[{0}]", newInvitado);
		return newInvitado;
	}

	@Override
	public List<Invitado> findAllInvitados() {
		return invitadoDao.findAll();

	}

	@Override
	public List<Invitado> findAllInvitadosByIdEvento(int idEvento) {
		return invitadoDao.findAllByIdEvento(idEvento);
	}

	@Override
	public List<Invitado> findAllInvitadosByIdEventoStatus(int idEvento, String status) {
		return invitadoDao.findAllByIdEventoStatus(idEvento, status);
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

	

}
