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

import mx.com.gapsi.eventos.model.Invitado;

@Component("invitadoDao")
public class InvitadoDaoImpl extends AbstractJpaDao<Invitado> implements InvitadoDao {

	public InvitadoDaoImpl() {
		setClazz(Invitado.class);
	}

	@Override
	public List<Invitado> find(String search) {
		logger.log(Level.INFO, "Running find");
		final TypedQuery<Invitado> q = em.createQuery(
				"SELECT o  from Invitado o WHERE  o.nombre like %:search% "
				+ "or o.apPaterno like %:search% "
				+ "or o.apMaterno like %:search% "
				+ "or o.email like %:search% ",
				Invitado.class);
		return q.setParameter("search", search).getResultList();
	}
	
	@Override
	@Transactional
	public void create(Invitado entity) {
		super.em.persist(entity);
//		super.em.refresh(entity); //Get the ID 
		super.em.flush();	//Get the ID 
	}

    /*
     * (non-Javadoc)
     * @see com.gapsi.fatca.dao.JpaDao#findAll()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Invitado> findAll() {
        logger.log(Level.INFO, "Running find all");
        return em.createQuery("SELECT t  from  Invitado t order by t.nombre").getResultList();
    }

    /*
     * (non-Javadoc)
     * @see com.gapsi.fatca.dao.JpaDao#findAllByIdEvento()
     */
    @Override
    public List<Invitado> findAllByIdEvento(int idEvento) {
        logger.log(Level.INFO, "Running find all by idEvento [{0}]", idEvento);
        //final TypedQuery<Invitado> q = em.createQuery("SELECT t  from  Invitado t where t.eventos.id = :idEvento order by t.nombre", Invitado.class);

        final TypedQuery<Invitado> q = em.createQuery("SELECT i "
        		                                    + "FROM Invitado i "
        		                                    + "JOIN FETCH i.eventoInvitados "
        		                                    + "WHERE i.eventos.id = :idEvento "
													+ "ORDER BY i.nombre", Invitado.class);
        return q.setParameter("idEvento", idEvento).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see com.gapsi.fatca.dao.JpaDao#findAllByIdEvento()
     */
    @Override
    public List<Invitado> findAllByIdEventoStatus(int idEvento, String status) {
        logger.log(Level.INFO, "Running find all by idEvento [{0}]", idEvento);
        /*final TypedQuery<Invitado> q = em.createQuery("SELECT i FROM Invitado i INNER JOIN i.eventoInvitados ei "
        											+ "WHERE i.eventos.id = :idEvento "
        											+ "AND ei.estatus = :status "
        											+ "ORDER BY i.nombre", Invitado.class);*/
        /*final TypedQuery<Invitado> q = em.createQuery("SELECT i FROM Invitado i INNER JOIN i.eventos e INNER JOIN i.eventoInvitados ei "
        											+ "WHERE e.id = :idEvento "
        											+ "AND ei.estatus = :status "
        											+ "ORDER BY i.nombre", Invitado.class);*/
        
        final TypedQuery<Invitado> q = em.createQuery("SELECT i FROM Invitado i INNER JOIN i.eventoInvitados ei INNER JOIN ei.evento e "
													+ "WHERE e.id = :idEvento "
													+ "AND ei.estatus = :status "
													+ "ORDER BY i.nombre", Invitado.class);
        
        return q.setParameter("idEvento", idEvento).setParameter("status", status).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see com.gapsi.fatca.dao.JpaDao#findAll()
     */
	@Override
	public List<Invitado> findByEmail(String email) {
        logger.log(Level.INFO, "Running findByEmail");

        //Invitado invitado = null;
        final TypedQuery<Invitado> q = em.createQuery("SELECT t  from  Invitado t WHERE t.email = :search ", Invitado.class);
        //List<Invitado> invitados = q.setParameter("search", "%" + email + "%").getResultList();
        return q.setParameter("search", email).getResultList();
   	}
    
}