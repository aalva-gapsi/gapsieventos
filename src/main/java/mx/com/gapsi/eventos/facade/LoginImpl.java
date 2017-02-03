/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.facade;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.com.gapsi.eventos.dao.UsuarioDao;
import mx.com.gapsi.eventos.exception.LoginException;
import mx.com.gapsi.eventos.model.Usuario;


@Component("login")
public class LoginImpl implements Login {
	Logger logger = Logger.getLogger("LoginImpl");
	@Inject
	private UsuarioDao usuarioDao;
	public LoginImpl() {
		super();
		logger.log(Level.FINE, "Creating class" +LoginImpl.class );
	}

	public Usuario validarLogin(Usuario usuario) throws LoginException{		
		logger.log(Level.INFO, "validarLogin -Using usuarioDao" +usuarioDao );
		Usuario usuari = usuarioDao.findOne(usuario.getUsuario(), usuario.getContrasenia());
		if (usuari == null){
			throw new LoginException("Usuario o password erroneo.");
		}
		return usuari;
	 }

	/**
	 * @return the usuarioDao
	 */
	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	/**
	 * @param usuarioDao the usuarioDao to set
	 */
	public void setUsuarioDao(UsuarioDao usuarioDao) {
		logger.log(Level.INFO, "setUsuarioDao" +usuarioDao );
		this.usuarioDao = usuarioDao;
	}

	/* (non-Javadoc)
	 * @see mx.liverpool.com.fest.facade.Login#validarLogin(java.lang.String, java.lang.String)
	 */
	@Override
	public Usuario validarLogin(String username, String password) throws LoginException {
		Usuario usuario = new Usuario();
		usuario.setUsuario(username);
		usuario.setContrasenia(password);
		return validarLogin(usuario);
	}
	
	
	}
