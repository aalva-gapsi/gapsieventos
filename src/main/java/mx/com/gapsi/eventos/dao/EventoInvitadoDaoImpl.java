/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.dao;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import mx.com.gapsi.eventos.model.EventoInvitado;
import mx.com.gapsi.eventos.utils.Encriptador;


@Component("eventoInvitadoDao")
public class EventoInvitadoDaoImpl  extends AbstractJpaDao<EventoInvitado> implements EventoInvitadoDao {

	/**
	 * Instantiates a new user dao impl.
	 */
	public EventoInvitadoDaoImpl() {
		setClazz(EventoInvitado.class);
	}

	@Override
	@Transactional
	public void create(EventoInvitado entity) {
		logger.log(Level.INFO, "EventoInvitadoDao-create[{0}]",entity);
		super.em.persist(entity);
		super.em.flush();
	
		// Se actualiza el campo id_md5
		Encriptador encriptador = new Encriptador();
		String idMD5 = encriptador.encriptar(String.valueOf(entity.getId()));
		entity.setIdMD5(idMD5);
		super.em.merge(entity);
	}

	@Override
	public EventoInvitado findOne(long idEvento, long idInvitado) {
		logger.log(Level.INFO, "EventoInvitadoDao-findOne("+idEvento+","+idInvitado+")");
		final TypedQuery<EventoInvitado> q = em.createQuery(
				"SELECT o  from EventoInvitado o WHERE  o.evento.id =:idEvento "
				+ "and o.invitado.id =:idInvitado",
				EventoInvitado.class);
		return q.setParameter("idEvento", idEvento).setParameter("idInvitado", idInvitado).getSingleResult();
	}
	
	@Override
	public List<EventoInvitado> findAllByIdInvitado(int idInvitado) {
		logger.log(Level.INFO, "EventoInvitadoDao-findAllByIdInvitado("+idInvitado+")");
		final TypedQuery<EventoInvitado> q = em.createQuery(
				"SELECT o  from EventoInvitado o WHERE o.invitado.id =:idInvitado",
				EventoInvitado.class);
		return q.setParameter("idInvitado", idInvitado).getResultList();
	}

	@Override
	public EventoInvitado findByIdMD5(String idMD5) {
		logger.log(Level.INFO, "EventoInvitadoDao-findByIdMD5("+idMD5+")");
		final TypedQuery<EventoInvitado> q = em.createQuery(
				"SELECT o  from EventoInvitado o WHERE o.idMD5 =:idMD5",
				EventoInvitado.class);
		EventoInvitado eventoInvitado = null;
		List<EventoInvitado> eventoInvitados = q.setParameter("idMD5", idMD5).getResultList();
		if (eventoInvitados != null && eventoInvitados.size() > 0) {
			eventoInvitado = eventoInvitados.get(0);
		}
		return eventoInvitado;
	}
	
	@Override
	@Transactional
	public EventoInvitado update(EventoInvitado entity){
		EventoInvitado outcome = null;
		logger.log(Level.INFO, "EventoInvitadoDao-create[{0}]",entity);
		outcome = super.em.merge(entity);
		super.em.flush();
		return outcome;
	}
}