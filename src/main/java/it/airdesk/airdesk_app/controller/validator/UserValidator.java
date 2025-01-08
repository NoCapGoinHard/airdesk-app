package it.airdesk.airdesk_app.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.airdesk.airdesk_app.model.auth.User;
import it.airdesk.airdesk_app.service.auth.UserService;

@Component
public class UserValidator implements Validator {
	
	final Integer MAX_NAME_LENGTH = 20;
    final Integer MIN_NAME_LENGTH = 2;
    
    @Autowired
	private UserService userService;
    
    private static final Logger logger = LoggerFactory.getLogger(HostValidator.class);

	@Override
	public void validate(Object o, Errors errors) {
		
		User user = (User) o;
		String name = user.getName().trim();
		String surname = user.getSurname().trim();

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
			if (this.userService.alreadyExists((User)o)) {
				logger.debug("e' un duplicato");
				errors.reject("user.duplicato");
			}
		}
	}


	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}
}
