/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.dao;

import java.io.Serializable;
import java.util.List;


/**
 * The Interface JpaDao.
 *
 * @param <T> the generic type
 */
public interface JpaDao<T extends Serializable> {

	/**
	 * Sets the clazz.
	 *
	 * @param clazzToSet the new clazz
	 */
	void setClazz(Class<T> clazzToSet);

	/**
	 * Find one.
	 *
	 * @param id the id
	 * @return the t
	 */
	T findOne(long id);

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	List<T> findAll();

	/**
	 * Creates the.
	 *
	 * @param entity the entity
	 */
	void create(T entity);

	/**
	 * Update.
	 *
	 * @param entity the entity
	 * @return the t
	 */
	T update(T entity);

	/**
	 * Delete.
	 *
	 * @param entity the entity
	 */
	void delete(T entity);

	/**
	 * Delete by id.
	 *
	 * @param entityId the entity id
	 */
	void deleteById(long entityId);

}