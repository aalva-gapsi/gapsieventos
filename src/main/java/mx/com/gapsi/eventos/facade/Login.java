/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.facade;

import mx.com.gapsi.eventos.exception.LoginException;
import mx.com.gapsi.eventos.model.Usuario;

public interface Login {

	Usuario validarLogin(String username, String password) throws LoginException;

}