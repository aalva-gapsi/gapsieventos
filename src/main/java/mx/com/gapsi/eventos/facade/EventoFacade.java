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

import mx.com.gapsi.eventos.model.Evento;
import mx.com.gapsi.eventos.model.EventoInvitado;

public interface EventoFacade {

	Evento findOne(long idFashionFest);

	Evento findOneWithImagenInvitacion(long idFashionFest);

	EventoInvitado findEventoInvitado(long idEventoInvitado);

	EventoInvitado findEventoInvitado(long idEvento, long idInvitado);
	
	List<Evento> findAllEventos();
	
	List<Evento> findAllAvailableByIdInvitado(int idInvitado);
	
	EventoInvitado updateValue(EventoInvitado eventoInvitado);

	Evento createEvento(Evento evento);
	
	Evento update(Evento modifiedValue);

}
