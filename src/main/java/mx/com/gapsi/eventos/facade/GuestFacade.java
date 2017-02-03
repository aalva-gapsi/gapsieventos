/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.facade;

import java.util.List;

import mx.com.gapsi.eventos.model.EventoInvitado;
import mx.com.gapsi.eventos.model.Invitado;
import mx.com.gapsi.eventos.model.TipoInvitado;

/**
 * @author Don
 *
 */
public interface GuestFacade {

	Invitado findInvitadoById(long id);
	
	Invitado findInvitadoByEmail(String email);
	
	List<Invitado> findInvitadosByEmail(String email);
	
	List<TipoInvitado> findAllTipoInvitado();

	List<Invitado> findInvitado(String search);

	void assignTickets(EventoInvitado eventoInvitado);

	Invitado update(Invitado modifiedValue);

	List<Invitado> findAllInvitados();

	List<Invitado> findAllInvitadosByIdEvento(int idEvento);

	List<Invitado> findAllInvitadosByIdEventoStatus(int idEvento, String status);

	EventoInvitado createInvitado(Invitado invitado, Integer noBoletos, Integer idEvento);
	}

