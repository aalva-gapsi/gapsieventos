/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */

package mx.com.gapsi.eventos.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import mx.com.gapsi.eventos.model.Usuario;

/**
 * The Class UsuarioDaoImpl.
 */
@Component("usuarioDao")
public class UsuarioDaoImpl extends AbstractJpaDao<Usuario> implements UsuarioDao {
    Logger logger = Logger.getLogger("UsuarioDaoImpl");
    
    /**
     * Instantiates a new user dao impl.
     */
    public UsuarioDaoImpl() {
    	super();
        logger.log(Level.FINE, "Creating class" +UsuarioDaoImpl.class );
        setClazz(Usuario.class);
    }
    
	@PostConstruct
	public void init(){
			logger.log(Level.INFO, "Usuario Dao - Entity Manager " + em);
			logger.log(Level.INFO, "Usuario Dao - Entity Manager F " + emf);
	}
	@Override
	public Usuario findOne(String usuario, String contrasenia) {
        logger.log(Level.FINE, "Running find one");
        final Query createQuery = em.createQuery("SELECT t  from Usuario t WHERE  t.usuario = :usuario and t.contrasenia = :contrasenia", Usuario.class);
		return (Usuario) createQuery.setParameter("usuario", usuario).setParameter("contrasenia", contrasenia).getSingleResult();
	}

}
