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

import mx.com.gapsi.eventos.model.Evento;


/**
 * The Interface EventoDao.
 */
public interface EventoDao {

	/**
	 * Find one.
	 *
	 * @param id the id
	 * @return the user
	 */
	Evento findOne(long id);

	/**
	 * Find one.
	 *
	 * @param id the id
	 * @return the user
	 */
	Evento findOneWithImagenInvitacion(long id);
	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	List<Evento> findAll();

	/**
	 * Find all available.
	 * @param idInvitado
	 * @return the list
	 */
	List<Evento> findAllAvailableByIdInvitado(int idInvitado);

	/**
	 * Creates the.
	 *
	 * @param entity the entity
	 */
	void create(Evento entity);

	/**
	 * Update.
	 *
	 * @param entity the entity
	 * @return the user
	 */
	Evento update(Evento entity);

	/**
	 * Delete.
	 *
	 * @param entity the entity
	 */
	void delete(Evento entity);

	/**
	 * Delete by id.
	 *
	 * @param entityId the entity id
	 */
	void deleteById(long entityId);


}