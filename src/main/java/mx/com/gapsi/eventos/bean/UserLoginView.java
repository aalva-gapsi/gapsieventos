/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */

package mx.com.gapsi.eventos.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import mx.com.gapsi.eventos.exception.LoginException;
import mx.com.gapsi.eventos.facade.Login;
import mx.com.gapsi.eventos.model.Usuario;

/**
 * The Class UserLoginView.
 */
@ManagedBean
@SessionScoped
/**
 * Bean used to handle the login process.
 *
 * @author Carlos Patino
 */
public class UserLoginView implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6827111118376791950L;

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger("UserLoginView");

    /** The user persistent service. */
    // @ManagedProperty("#{userDao}")

    /** The msg. */
    @ManagedProperty("#{msg}")
    private transient ResourceBundle msg;

    @ManagedProperty("#{login}")
    private transient Login loginImpl;

    /** The Constant USER_STORED_IN_SESSION. */
    public static final String USER_STORED_IN_SESSION = "user";

    /** The username. */
    private String username;

    /** The password. */
    private String password;

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username
     *            the new username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password
     *            the new password
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Logout.
     */
    public void logout() {
        final ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        try {
            final HttpServletRequest origRequest = (HttpServletRequest) ctx.getRequest();
            final String webAppRoot = origRequest.getContextPath();
            final String login = webAppRoot + "/login.xhtml";
            //logger.info("Logout to  {}", login);
            removeDataInSession(FacesContext.getCurrentInstance(), USER_STORED_IN_SESSION);
            ctx.redirect(login);
        } catch (final IOException ex) {
            //logger.error("Exception happend when redirecting logged used", ex);
        }
    }

    /**
     * Validate user.
     *
     * @param event
     *            the event
     */
    public void validateUser(final ActionEvent event) {
        String urlToGo = "";
        final RequestContext context = RequestContext.getCurrentInstance();
        boolean loggedIn = false;

        final FacesContext facesContext = FacesContext.getCurrentInstance();
        logger.log(Level.INFO, "Se valida " + username + " , "  );
        Usuario usuerFromDB = null;
		try {
			usuerFromDB = loginImpl.validarLogin(username,password);
            logger.log(Level.INFO, "Se valido login" + usuerFromDB);	
		} catch (LoginException e){
            logger.log(Level.WARNING, "Failed login attempt " + e.getMessage());
		}
		catch (Exception e) {
            logger.log(Level.WARNING, "Error login attempt " + e.getMessage());		
		}
		
        if (usuerFromDB == null) {
        	logger.log(Level.WARNING, "Failed login attempt for user [{}]", username);	
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg.getString("login.error"),
                    msg.getString("login.error.msg")));
        } else {
            loggedIn = true;
            logger.log(Level.INFO, "Pasamos [{}]", username);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido", username));
            urlToGo = facesContext.getExternalContext().getRequestContextPath() +"/secure/invitado.xhtml?faces-redirect=true";
            loggedIn = true;
            saveDataInSession(facesContext, usuerFromDB);
        }

        context.addCallbackParam("loggedIn", loggedIn);

        if (loggedIn) {
            try {
                facesContext.getExternalContext().redirect(urlToGo);
            } catch (final IOException ex) {
            	logger.log(Level.SEVERE,"Exception happend when redirecting logged used", ex);	
            }
        }
    }

    /**
     * Save data in session.
     *
     * @param facesContext
     *            the faces context
     * @param userStoredInSession
     *            the user stored in session
     * @param usuerFromDB
     *            the usuer from db
     */
    private void saveDataInSession(final FacesContext facesContext, final Usuario user) {
        final HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        //logger.debug("Adding value to session {} with session id [{}] to [{}]", session, session.getId(),USER_STORED_IN_SESSION);
        session.setAttribute(USER_STORED_IN_SESSION, user);
    }

    /**
     * Removes the data in session.
     *
     * @param ctx
     *            the ctx
     * @param userStoredInSession
     *            the user stored in session
     */
    private void removeDataInSession(final FacesContext facesContext, final String userStoredInSession) {
        final HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        //logger.debug("Removing value from session {} with session id [{}] with [{}]", session, session.getId(), userStoredInSession);
        session.removeAttribute(userStoredInSession);
        session.invalidate();
    }

    /**
     * Gets the msg.
     *
     * @return the msg
     */
    public ResourceBundle getMsg() {
        return msg;
    }

    /**
     * Sets the msg.
     *
     * @param msg
     *            the new msg
     */
    public void setMsg(final ResourceBundle msg) {
        this.msg = msg;
    }

  
	/**
	 * @return the login
	 */
	public Login getLoginImpl() {
		return loginImpl;
	}

	/**
	 * @param login the login to set
	 */
	public void setLoginImpl(Login loginImpl) {
		this.loginImpl = loginImpl;
	}

}
