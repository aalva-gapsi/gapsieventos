/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.dao;

import org.springframework.stereotype.Component;

import mx.com.gapsi.eventos.model.TipoInvitado;


@Component("tipoInvitadoDao")
public class TipoInvitadoDaoImpl  extends AbstractJpaDao<TipoInvitado> implements TipoInvitadoDao {

	public TipoInvitadoDaoImpl() {
		setClazz(TipoInvitado.class);
	}

}