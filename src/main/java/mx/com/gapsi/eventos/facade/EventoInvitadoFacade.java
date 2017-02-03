package mx.com.gapsi.eventos.facade;

import java.util.List;

import mx.com.gapsi.eventos.model.EventoInvitado;

public interface EventoInvitadoFacade {

	EventoInvitado findOne(long idEventoInvitado);
	
	EventoInvitado findByIdMD5(String idMD5);
	
	List<EventoInvitado> findAllEventoInvitados();

	List<EventoInvitado> findAllEventoInvitadosByIdInvitado(int idInvitado);
	
	EventoInvitado createEventoInvitado(EventoInvitado eventoInvitado);
	
	EventoInvitado update(EventoInvitado modifiedValue);

}
