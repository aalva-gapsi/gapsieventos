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

import mx.com.gapsi.eventos.model.TipoInvitado;


/**
 * The Interface TipoInvitadoDao.
 */
public interface TipoInvitadoDao {

	/**
	 * Find one.
	 *
	 * @param id the id
	 * @return the user
	 */
	TipoInvitado findOne(long id);

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	List<TipoInvitado> findAll();

	/**
	 * Creates the.
	 *
	 * @param entity the entity
	 */
	void create(TipoInvitado entity);

	/**
	 * Update.
	 *
	 * @param entity the entity
	 * @return the user
	 */
	TipoInvitado update(TipoInvitado entity);

	/**
	 * Delete.
	 *
	 * @param entity the entity
	 */
	void delete(TipoInvitado entity);

	/**
	 * Delete by id.
	 *
	 * @param entityId the entity id
	 */
	void deleteById(long entityId);


}