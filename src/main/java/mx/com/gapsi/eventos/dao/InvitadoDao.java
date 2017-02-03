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

import mx.com.gapsi.eventos.model.Invitado;


public interface InvitadoDao {

	/**
	 * Find one.
	 *
	 * @param id the id
	 * @return the user
	 */
	Invitado findOne(long id);

	/**
	 * Find by email.
	 *
	 * @param email the email
	 * @return the list
	 */
	List<Invitado> findByEmail(String email);
	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	List<Invitado> findAll();

	/**
	 * Find all by idEvento.
	 * @param idEvento int
	 * @return the list
	 */
    List<Invitado> findAllByIdEvento(int idEvento);

	/**
	 * Find all by idEvento and status.
	 * @param idEvento int
	 * @param status String
	 * @return the list
	 */
    List<Invitado> findAllByIdEventoStatus(int idEvento, String status);
    
	/**
	 * Creates the.
	 *
	 * @param entity the entity
	 */
	void create(Invitado entity);

	/**
	 * Update.
	 *
	 * @param entity the entity
	 * @return the user
	 */
	Invitado update(Invitado entity);

	/**
	 * Delete.
	 *
	 * @param entity the entity
	 */
	void delete(Invitado entity);

	/**
	 * Delete by id.
	 *
	 * @param entityId the entity id
	 */
	void deleteById(long entityId);

	List<Invitado> find(String search);


}