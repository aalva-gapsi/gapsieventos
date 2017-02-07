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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// import org.springframework.transaction.annotation.Transactional;

/**
 * The Class AbstractJpaDao.
 *
 * @param <T>
 *            the generic type
 */
@Component
public abstract class AbstractJpaDao<T extends Serializable> implements JpaDao<T> {
    private static final String UNIT_NAME = "openjpa-todo";
	Logger logger = Logger.getLogger("AbstractJpaDao");
    /** The clazz. */
    private Class<T> clazz;

	
    
    @PersistenceUnit(unitName = UNIT_NAME)
//    @PersistenceUnit
    protected EntityManagerFactory emf;   
    
    @PersistenceContext(unitName=UNIT_NAME)
//    @PersistenceContext
	protected EntityManager em;

    public AbstractJpaDao() {
        super();
        logger.log(Level.INFO, "Building EMF");
		em = getEm();
    }
	@PostConstruct
	public void init(){
			logger.log(Level.INFO, "Entity Manager " + em);
			logger.log(Level.INFO, "Entity Manager F " + emf);
	}
    /*
     * (non-Javadoc)
     * @see com.gapsi.fatca.dao.JpaDao#setClazz(java.lang.Class)
     */
    @Override
    public final void setClazz(final Class<T> clazzToSet) {
        this.clazz = clazzToSet;
        logger.log(Level.INFO, "setClazz"+clazzToSet);
    }

    /*
     * (non-Javadoc)
     * @see com.gapsi.fatca.dao.JpaDao#findOne(long)
     */
    @Override
    public T findOne(final long id) {
        return em.find(clazz, id);
    }

    /*
     * (non-Javadoc)
     * @see com.gapsi.fatca.dao.JpaDao#findAll()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        logger.log(Level.INFO, "Running find all");
        return em.createQuery("SELECT t  from " + clazz.getName() + " t").getResultList();
    }

    /*
     * (non-Javadoc)
     * @see com.gapsi.fatca.dao.JpaDao#create(T)
     */
    @Override
     @Transactional
    public void create(final T entity) {
        em.persist(entity);
    }

    /*
     * (non-Javadoc)
     * @see com.gapsi.fatca.dao.JpaDao#update(T)
     */
    @Override
     @Transactional
    public T update(final T entity) {
        return em.merge(entity);
    }

    /*
     * (non-Javadoc)
     * @see com.gapsi.fatca.dao.JpaDao#delete(T)
     */
    @Override
     @Transactional
    public void delete(final T entity) {
        em.remove(entity);
    }

    /*
     * (non-Javadoc)
     * @see com.gapsi.fatca.dao.JpaDao#deleteById(long)
     */
    @Override
     @Transactional
    public void deleteById(final long entityId) {
        final T entity = findOne(entityId);
        delete(entity);
    }

	
	private EntityManager getEm() {
		
		return em;
	}

	/**
	 * @param em the em to set
	 */
	public void setEm(EntityManager em) {
		this.em = em;
	}

	/**
	 * @return the emf
	 */
	public EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * @param emf the emf to set
	 */
	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}

	// Method 2: Parsing VCAP_SERVICES environment variable
    // The VCAP_SERVICES environment variable contains all the credentials of 
	// services bound to this application. You can parse it to obtain the information 
	// needed to connect to the SQL Database service. SQL Database is a service
	// that the Liberty buildpack auto-configures as described above, so parsing
	// VCAP_SERVICES is not a best practice.

	// see HelloResource.getInformation() for an example

}