package it.airdesk.airdesk_app.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.airdesk.airdesk_app.model.auth.Host;
import it.airdesk.airdesk_app.service.auth.HostService;


/**
 * Classe Validator per la validazione dei dati inseriti nella registrazione di un nuovo Host
 * che implementa metodi di controllo dei campi di inserimento e dei vari vincoli di unicita'
 */
@Component
public class HostValidator implements Validator{
	
	final Integer MAX_NAME_LENGTH = 20;
    final Integer MIN_NAME_LENGTH = 2;
	
	@Autowired
	private HostService hostService;
	
    private static final Logger logger = LoggerFactory.getLogger(HostValidator.class);
	
    /*
     * Metodo per il controllo di riempimento dei campi per cui e' richiesto e di controllo di
     * non esistenza dell'host da inserire
     */
	@Override
	public void validate(Object o, Errors errors) {
		
		Host host = (Host) o;
		String name = host.getName().trim();
		String surname = host.getSurname().trim();

		if (name.isEmpty())
			errors.rejectValue("name", "required");
		else if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH)
			errors.rejectValue("name", "size");

		if (surname.isEmpty())
			errors.rejectValue("surname", "required");
		else if (surname.length() < MIN_NAME_LENGTH || surname.length() > MAX_NAME_LENGTH)
			errors.rejectValue("surname", "size");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthDate", "required");
		
		if (!errors.hasErrors()) {
			logger.debug("confermato: valori non nulli");
			if (this.hostService.alreadyExists((Host)o)) {
				logger.debug("e' un duplicato");
				errors.reject("host.duplicato");
			}
		}
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Host.class.equals(clazz);
	}
	
}