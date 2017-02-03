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

import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import mx.com.gapsi.eventos.model.Evento;

@Component("eventoDao")
public class EventoDaoImpl  extends AbstractJpaDao<Evento> implements EventoDao {

	/**
	 * Instantiates a new user dao impl.
	 */
	public EventoDaoImpl() {
		setClazz(Evento.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Evento> findAllAvailableByIdInvitado(int idInvitado) {
		logger.log(Level.INFO, "EventoDaoImpl-findAllAvailableByIdInvitado[{0}]", idInvitado);

		Query q = em.createNativeQuery("SELECT e.ID_EVENTO, e.DESCRIPCION, e.LLAVE_EVENTO, e.LLAVE_SECRETA, e.NOMBRE, e.TOTAL_ASIENTOS " + 
				 					   "FROM EVENTO e " + 
				 					   "WHERE NOT EXISTS (SELECT x.id_evento FROM EVENTO_INVITADO x WHERE x.id_evento = e.id_evento AND x.id_invitado = ?1)", Evento.class);
		q.setParameter(1, idInvitado);
		return q.getResultList();
	}

    /*
     * (non-Javadoc)
     * @see com.gapsi.fatca.dao.JpaDao#findAll()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Evento> findAll() {
        logger.log(Level.INFO, "Running find all eventos");
        return em.createQuery("SELECT t  FROM Evento t ORDER BY t.fecha DESC ").getResultList();
    }
	
    /*
     * (non-Javadoc)
     * @see com.gapsi.fatca.dao.JpaDao#findOne(long)
     */
    @Override
    public Evento findOne(final long id) {
        return em.find(Evento.class, id);
    }

	/**
	 * Find one.
	 *
	 * @param id the id
	 * @return the user
	 */
	@Override
	@Transactional
	public Evento findOneWithImagenInvitacion(long id) {
		Evento evento = em.find(Evento.class, id);
		evento.getImagenInvitacion();
		return evento;
	}
    
}