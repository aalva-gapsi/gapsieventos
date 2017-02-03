package mx.com.gapsi.eventos.validator;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import mx.com.gapsi.eventos.facade.GuestFacade;
import mx.com.gapsi.eventos.model.Invitado;


@FacesValidator("emailValidator")
public class EmailValidator implements Validator{

	@ManagedProperty("#{guestFacade}")
	private GuestFacade guestFacade;
	
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    	Invitado invitado = guestFacade.findInvitadoByEmail(value.toString());
        if (invitado != null) {
        	//throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El email [" + value + "] ya est� registrado."));
        	
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El email [" + value + "] ya est� registrado."));
        
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El email [" + value + "] ya est� registrado."));
        	
        }
    }

	public GuestFacade getGuestFacade() {
		return guestFacade;
	}

	public void setGuestFacade(GuestFacade guestFacade) {
		this.guestFacade = guestFacade;
	}

}
