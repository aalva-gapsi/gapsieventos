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

import mx.com.gapsi.eventos.model.EventoInvitado;


/**
 * The Interface EventoDao.
 */
public interface EventoInvitadoDao {

	/**
	 * Find one.
	 *
	 * @param id the id
	 * @return the user
	 */
	EventoInvitado findOne(long id);

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	List<EventoInvitado> findAll();

	/**
	 * Find all by id_invitado
	 * @param idInvitado int
	 * @return the list
	 */
	List<EventoInvitado> findAllByIdInvitado(int idInvitado);

	/**
	 * Find one by idMD5.
	 * @param idMD5 String
	 * @return EventoInvitado
	 */
	EventoInvitado findByIdMD5(String idMD5);
	
	/**
	 * Creates the.
	 *
	 * @param entity the entity
	 */
	void create(EventoInvitado entity);

	/**
	 * Update.
	 *
	 * @param entity the entity
	 * @return the user
	 */
	EventoInvitado update(EventoInvitado entity);

	/**
	 * Delete.
	 *
	 * @param entity the entity
	 */
	void delete(EventoInvitado entity);

	/**
	 * Delete by id.
	 *
	 * @param entityId the entity id
	 */
	void deleteById(long entityId);

	EventoInvitado findOne(long idEvento, long idInvitado);


}