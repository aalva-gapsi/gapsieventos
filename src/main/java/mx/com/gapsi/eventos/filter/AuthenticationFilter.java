/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.com.gapsi.eventos.bean.UserLoginView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Servlet Filter implementation class AuthenticationFilter.
 */
public class AuthenticationFilter implements Filter {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	/** The config. */
	private FilterConfig config;

	/**
	 * Default constructor.
	 */
	public AuthenticationFilter() {
	}

	/**
	 * Destroy.
	 *
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
		config = null;
	}

	/**
	 * Do filter.
	 *
	 * @param request the ServletRequest
	 * @param response the ServletResponse
	 * @param chain the FilterChain
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		HttpSession session = httpRequest.getSession(false);
		
		//chain.doFilter(request, response);
		
		if (session == null) {
			logger.warn("session is [{}]. Invalid user", session);
			goToLogin(httpRequest, response);
			//chain.doFilter(request, response);
		} else if(session.getAttribute(UserLoginView.USER_STORED_IN_SESSION) == null) {
			logger.warn("session is [{}] and is is [{}]. Invalid user", session, session.getId());
			goToLogin(httpRequest, response);
		} else {
			logger.info("Letting access as session is {} and id is {}", session, session.getId());
			chain.doFilter(request, response);
		}
	}

	/**
	 * Go to login.
	 *
	 * @param request the ServletRequest
	 * @param response the ServletResponse
	 */
	public void goToLogin(ServletRequest request, ServletResponse response) {
		logger.debug("Going to login");
		RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/login.xhtml");
		try {
			dispatcher.forward(request,response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inits the.
	 *
	 * @param fConfig the FilterConfig
	 * @throws ServletException the servlet exception
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		config = fConfig;
	}

}
