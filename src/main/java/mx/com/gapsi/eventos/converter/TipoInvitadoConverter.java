/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.converter;

import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import mx.com.gapsi.eventos.dao.TipoInvitadoDao;
import mx.com.gapsi.eventos.model.TipoInvitado;

//@FacesConverter("tipoInvitadoConverter")
//@ManagedBean(name = "tipoInvitadoConverterBean") 
@Named(value="tipoInvitadoConverter")
@ApplicationScoped
public class TipoInvitadoConverter implements Converter {
    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger("TipoInvitadoConverter");
	
//	@Inject
    @Autowired
	private TipoInvitadoDao tipoInvitadoDao;
	
	public TipoInvitadoConverter() {
		// TODO Auto-generated constructor stub
	}

	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		logger.entering("TipoInvitadoConverter", "getAsObject",value);
		// TODO pasar a aop
		TipoInvitado tipoInvitado = null;
		try {
			if (value != null && value.trim().length() > 0) {
				tipoInvitado = tipoInvitadoDao.findOne(Long.parseLong(value));
//				tipoInvitado = new TipoInvitado(1, "invitado");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			throw new ConverterException(
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid TipoInvitado."));
		}finally {
			logger.exiting("TipoInvitadoConverter", "getAsObject",tipoInvitado);
		}
		return tipoInvitado;
	}
	 
	    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
	    	logger.entering("TipoInvitadoConverter", "getAsString",object);
	    	 String valueOf = null;
	        if(object != null) {
	        	if(object instanceof TipoInvitado){
	        		valueOf = String.valueOf(((TipoInvitado) object).getId());
	        	}else{
	        		valueOf = object.toString();
	        	}
	        }
	        logger.exiting("TipoInvitadoConverter", "getAsString",valueOf);
	        return valueOf;
	    }

		/**
		 * @return the tipoInvitadoDao
		 */
		public TipoInvitadoDao getTipoInvitadoDao() {
			return tipoInvitadoDao;
		}

		/**
		 * @param tipoInvitadoDao the tipoInvitadoDao to set
		 */
		public void setTipoInvitadoDao(TipoInvitadoDao tipoInvitadoDao) {
			this.tipoInvitadoDao = tipoInvitadoDao;
		}   
}
